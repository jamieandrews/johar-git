/* XMLErrorHandler.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */
package johar.idf;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
/*
 * A class to handle the errors while validating
 * an XML document against an XML Schema.
 */
public class XMLErrorHandler extends DefaultHandler {
    public boolean error = false;
    public XMLErrorHandler() {
    }
    /* error:  TODO
     * @param e TODO
     */
    public void error(SAXParseException e) {
	error = true;
	System.out.println( "Parsing error:  " + e.getMessage() );
    }

    /* warning:  TODO
     * @param e TODO
     */
    public void warning(SAXParseException e) {
	System.out.println( "Parsing problem:  " + e.getMessage() );
    }

    /* fatalError:  TODO
     * @param e TODO
     */
    public void fatalError(SAXParseException e) {
	error = true;
	System.out.println( "Parsing error:  " + e.getMessage() );
	System.out.println("Cannot continue.");
	System.exit(1);
    }
}
