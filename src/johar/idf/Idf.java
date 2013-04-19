/* Idf.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

package johar.idf;

import java.io.File;
import java.util.Vector;

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
    private Vector<IdfCommand> _commandVector;
    private Vector<IdfTable> _tableVector;
    private Vector<IdfCommandGroup> _commandGroupVector;

    private String _application;
    private String _applicationEngine;
    private String _idfVersion;
    private String _initializationMethod;

    public Idf(Element domElement, ErrorHandler eh) {
	super(domElement, eh, "JoharIdf");

	NodeList nodeList;
	int n;

	_commandVector = new Vector<IdfCommand>();
	_tableVector = new Vector<IdfTable>();
	_commandGroupVector = new Vector<IdfCommandGroup>();

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
	    _commandVector.add(c);
	}

	// CommandGroup (0 or more)
	nodeList = _domElement.getElementsByTagName("CommandGroup");
	n = nodeList.getLength();
	for (int i=0; i<n; i++) {
	    Element e = (Element) nodeList.item(i);
	    IdfCommandGroup cg = new IdfCommandGroup(e, _eh);
	    _commandGroupVector.add(cg);
	}
	// If there are no explicit CommandGroups, put everything into one.
	if (n == 0) {
	    IdfCommandGroup cg = new IdfCommandGroup(_commandVector);
	    _commandGroupVector.add(cg);
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
	    _tableVector.add(t);
	}

	runVisitors();

	// Release document and error handler for eventual GC
	_domElement = null;
	_eh = null;
    }

    public void runVisitors() {
	acceptVisitor(new Visitors.CommandNamesUnique());
    }

    public static Idf idfFromFile(String filename) {
	try {
	    Schema joharSchema = JoharXsd.joharXsdSchema();
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setSchema(joharSchema);

	    // Should put in here something to check whether the
	    // file is .idf or .xml.  If .idf, should convert to XML
	    // InputStream using idf2xml and parse that instead. 
	    // DocumentBuilder.parse() does also take an InputStream as
	    // parameter.
	    DocumentBuilder docBuilder = dbf.newDocumentBuilder();
	    Document doc = docBuilder.parse( new File(filename) );
	    doc.getDocumentElement().normalize();
	    Element e = doc.getDocumentElement();
	    ErrorHandler eh = new ErrorHandler();
	    Idf newIdf = new Idf(e, eh);
	    if (eh.errorsExist()) {
		System.out.println(eh.getErrorMessages());
		return null;
	    } else {
		return newIdf;
	    }
	} catch (SAXParseException err) {
	    System.out.println(
		"* XML parse error:  line " + err.getLineNumber() +
		", uri " + err.getSystemId() +
		":"
	    );
	    System.out.println("  " + err.getMessage() );
	} catch (SAXException e) {
	    Exception x = e.getException();
	    ( (x == null) ? e : x ).printStackTrace();
	} catch (Throwable t) {
	    t.printStackTrace();
	}

	return null;
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
	elementVectorToString(_commandVector);
	elementVectorToString(_tableVector);
	elementVectorToString(_commandGroupVector);
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
	return _commandVector.size();
    }

    public IdfCommand getCommandNumber(int i) {
	return _commandVector.elementAt(i);
    }

    public int getNumTables() {
	return _tableVector.size();
    }

    public IdfTable getTableNumber(int i) {
	return _tableVector.elementAt(i);
    }

    public int getNumCommandGroups() {
	return _commandGroupVector.size();
    }

    public IdfCommandGroup getCommandGroupNumber(int i) {
	return _commandGroupVector.elementAt(i);
    }

    public static void main(String[] args) {
	if (args.length < 1) {
	    System.out.println("To test:  java johar.idf.Idf inputfile");
	} else {
	    Idf idf = idfFromFile(args[0]);
	    if (idf == null) {
		System.out.println("Errors in IDF file.");
	    } else {
		System.out.println(idf.toString());
	    }
	}
    }

    public void passVisitorToChildren(VisitorOfIdfElement visitor) {
	int i, j;

	j = _commandVector.size();
	for (i=0; i<j; i++) {
	    _commandVector.elementAt(i).acceptVisitor(visitor);
	}

	j = _tableVector.size();
	for (i=0; i<j; i++) {
	    _tableVector.elementAt(i).acceptVisitor(visitor);
	}

	j = _commandGroupVector.size();
	for (i=0; i<j; i++) {
	    _commandGroupVector.elementAt(i).acceptVisitor(visitor);
	}
    }

    // Standard methods to effect double dispatch.
    public void beforeChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }
    public void afterChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }

}

