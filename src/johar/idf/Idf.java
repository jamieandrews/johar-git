/* Idf.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

package johar.idf;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.validation.Schema;

import johar.utilities.TextInputValidator;

/**
 * @author Fatima Hussain
 * @author Jamie Andrews
 * @version 2.0
 */
public class Idf extends IdfElement {
    private List<IdfCommand> _commandList;
    private List<IdfTable> _tableList;
    private List<IdfCommandGroup> _commandGroupList;

    private String _application;
    private String _applicationEngine;
    private String _idfVersion;
    private String _initializationMethod;

    private static String _lastErrorMessages = "";

    public Idf(Element domElement, ErrorHandler eh) {
	super(domElement, eh, "JoharIdf");

	NodeList nodeList;
	int n;

	_commandList = new ArrayList<IdfCommand>();
	_tableList = new ArrayList<IdfTable>();
	_commandGroupList = new ArrayList<IdfCommandGroup>();

	_application = domElement.getAttribute("name");
	setElementName(_application);
	String ccConvertedName =
	    TextInputValidator.titleCaseTranslation(_application);

	// Application (exactly 1)
	_application = 
	    extractAttrIf(true,
		"Application", 1, 1, 1, 1, "", "");

	// ApplicationEngine (0 or 1)
	// Default value is application name
	complainIfMoreThanOne("ApplicationEngine");
	_applicationEngine = extractAttr("ApplicationEngine", _application);

	// Command (0 or more)
	nodeList = _domElement.getElementsByTagName("Command");
	n = nodeList.getLength();
	for (int i=0; i<n; i++) {
	    Element e = (Element) nodeList.item(i);
	    IdfCommand c = new IdfCommand(e, _eh);
	    _commandList.add(c);
	}

	// CommandGroup (0 or more)
	nodeList = _domElement.getElementsByTagName("CommandGroup");
	n = nodeList.getLength();
	for (int i=0; i<n; i++) {
	    Element e = (Element) nodeList.item(i);
	    IdfCommandGroup cg = new IdfCommandGroup(e, _eh);
	    _commandGroupList.add(cg);
	}
	// If there are no explicit CommandGroups, put everything into one.
	if (n == 0) {
	    IdfCommandGroup cg = new IdfCommandGroup(_commandList);
	    _commandGroupList.add(cg);
	}

	// IdfVersion (exactly 1)
	_idfVersion =
	    extractAttrIf(true,
		"IdfVersion", 1, 1, 1, 1, "", "");

	// InitializationMethod (0 or 1)
	// Default value is "applicationEngineInitialize"
	complainIfMoreThanOne("InitializationMethod");
	_initializationMethod =
	    extractAttr("InitializationMethod",
		"applicationEngineInitialize");

	// Table (0 or more)
	nodeList = _domElement.getElementsByTagName("Table");
	n = nodeList.getLength();
	for (int i=0; i<n; i++) {
	    Element e = (Element) nodeList.item(i);
	    IdfTable t = new IdfTable(e, _eh);
	    _tableList.add(t);
	}

	runVisitors();
    }

    public void runVisitors() {
	acceptVisitor(new Visitors.CommandNamesUnique());
	acceptVisitor(new Visitors.HelpMessageConstraints());
	acceptVisitor(new Visitors.ParameterNamesUniqueInCommand());
	acceptVisitor(new Visitors.MinRepsLeqMaxReps());
	acceptVisitor(new Visitors.MinValueLeqMaxValue());
	acceptVisitor(new Visitors.MinCharsLeqMaxChars());
    }

    /**
     * Return an InputStream containing an XML file.
     * @parameter filename The name of an .idf or .xml file.
     * @returns An InputStream containing just the file, if it is
     *   already an XML file.
     * @returns An InputStream containing the IDF translated to XML,
     *   if filename is an IDF file.
     */
    private static InputStream fileToXmlInputStream(String filename)
	    throws IOException {
	InputStream returnValue = null;

	if (filename.endsWith(".xml")) {
	    // It's already an XML file

	    File f = new File(filename);
	    returnValue = new FileInputStream(f);

	} else if (filename.endsWith(".idf")) {
	    // Set up "stdin" Reader for idf2xml
	    File f = new File(filename);
	    FileInputStream fis = new FileInputStream(f);
	    Reader in = new InputStreamReader(fis);

	    // Set up "stdout" PrintWriter
	    ByteArrayOutputStream baosOut = new ByteArrayOutputStream();
	    PrintWriter out = new PrintWriter(baosOut);

	    // Set up "stderr" PrintWriter
	    ByteArrayOutputStream baosErr = new ByteArrayOutputStream();
	    PrintWriter err = new PrintWriter(baosErr);

	    // Convert IDF to XML
	    Idf2xml idf2xml = new Idf2xml(in, out, err);
	    idf2xml.convert();

	    // Check for errors
	    byte[] errByteArray = baosErr.toByteArray();
	    if (errByteArray.length > 0) {
	       // Can't continue
	       throw new IOException(new String(errByteArray));
	    }

	    // Get XML file as byte array
	    byte[] xmlByteArray = baosOut.toByteArray();

	    returnValue = new ByteArrayInputStream(xmlByteArray);
	} else {
	    throw new IOException("File " + filename + " is neither an .xml nor an .idf file");
	}

	return returnValue;
    }

    public static Idf idfFromFile(String filename)
	    throws IdfFormatException {
	try {
	    Schema joharSchema = JoharXsd.joharXsdSchema();
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setSchema(joharSchema);

	    DocumentBuilder docBuilder = dbf.newDocumentBuilder();
	    ErrorHandler eh = new ErrorHandler();
	    docBuilder.setErrorHandler(eh);
	    InputStream is = fileToXmlInputStream(filename);
	    Document doc = docBuilder.parse(is);

	    doc.getDocumentElement().normalize();
	    Element e = doc.getDocumentElement();
	    Idf newIdf = new Idf(e, eh);
	    _lastErrorMessages = eh.getErrorMessages();
	    if (eh.errorsExist()) {
		throw new IdfFormatException(_lastErrorMessages);
	    } else {
		return newIdf;
	    }
	} catch (SAXParseException e) {
	    // e.printStackTrace();
	    throw new IdfFormatException(
		"* XML parse error:  line " + e.getLineNumber() +
		", uri " + e.getSystemId() +
		": " +
		e.getMessage()
	    );
	} catch (SAXException e) {
	    Exception x = e.getException();
	    if (x == null) {
		x = e;
	    }
	    // ( (x == null) ? e : x ).printStackTrace();
	    throw new IdfFormatException(
		"* SAX Exception: " +
		x.getMessage()
	    );
	} catch (IOException e) {
	    // e.printStackTrace();
	    // System.out.println("Input problem with file " + filename +
		//  ": " + e.getMessage());
	    throw new IdfFormatException(
		"* Input problem, file " + filename +
		": " +
		e.getMessage()
	    );
	} catch (Throwable t) {
	    // t.printStackTrace();
	    throw new IdfFormatException(
		"* Exception/Error thrown: " +
		t.getMessage()
	    );
	}
    }

    public static String getErrorMsgs() {
	return _lastErrorMessages;
    }

    // For toString.  Overrides superclass method.
    public int indentLevel() {
	return 0;
    }

    // For toString.  Overrides superclass method.
    public void contentsToString() {
	fieldToString("Application", _application);
	fieldToString("ApplicationEngine", _applicationEngine);
	fieldToString("IdfVersion", _idfVersion);
	fieldToString("InitializationMethod", _initializationMethod);
	elementListToString(_commandList);
	elementListToString(_tableList);
	elementListToString(_commandGroupList);
    }

    // Getters.

    public String getApplication() {
	return _application;
    }

    public String getApplicationEngine() {
	return _applicationEngine;
    }

    public String getIdfVersion() {
	return _idfVersion;
    }

    public String getInitializationMethod() {
	return _initializationMethod;
    }

    public int getNumCommands() {
	return _commandList.size();
    }

    public IdfCommand getCommandNumber(int i) {
	return _commandList.get(i);
    }

    public int getNumTables() {
	return _tableList.size();
    }

    public IdfTable getTableNumber(int i) {
	return _tableList.get(i);
    }

    public int getNumCommandGroups() {
	return _commandGroupList.size();
    }

    public IdfCommandGroup getCommandGroupNumber(int i) {
	return _commandGroupList.get(i);
    }

    public static void main(String[] args) {
	if (args.length < 1) {
	    System.out.println("To test:  java johar.idf.Idf inputfile");
	} else {
	    Idf idf = null;
	    try {
		idf = idfFromFile(args[0]);
	    } catch (IdfFormatException e) {
		System.out.println(e.getMessage());
	    }
	    if (idf == null) {
		System.out.println("Errors in IDF file.");
	    } else {
		System.out.println(idf.toString());
	    }
	}
    }

    public void passVisitorToChildren(VisitorOfIdfElement visitor) {
	int i, j;

	j = _commandList.size();
	for (i=0; i<j; i++) {
	    _commandList.get(i).acceptVisitor(visitor);
	}

	j = _tableList.size();
	for (i=0; i<j; i++) {
	    _tableList.get(i).acceptVisitor(visitor);
	}

	j = _commandGroupList.size();
	for (i=0; i<j; i++) {
	    _commandGroupList.get(i).acceptVisitor(visitor);
	}
    }

    // Standard methods to effect double dispatch.
    public void beforeChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }
    public void afterChildren(VisitorOfIdfElement visitor) {
	visitor.afterChildren(this, _eh);
    }

}

