/** ErrorHandler.java
 * @author Jamie Andrews
 */

package johar.idf;

import org.xml.sax.SAXParseException;

public class ErrorHandler implements org.xml.sax.ErrorHandler {
    StringBuffer _sb;
    boolean _errorsExist;

    public ErrorHandler() {
	_sb = new StringBuffer();
	_errorsExist = false;
    }

    public void error(String errorMessage) {
	_errorsExist = true;
	_sb.append(errorMessage);
	_sb.append("\n");
    }

    public boolean errorsExist() {
	return _errorsExist;
    }

    public String getErrorMessages() {
	return _sb.toString();
    }


    // For org.xml.sax.ErrorHandler
    public void error(SAXParseException exception) {
	_errorsExist = true;
	_sb.append("XML parsing recoverable error:  ");
	_sb.append(exception.getMessage());
	_sb.append("\n");
    }

    public void fatalError(SAXParseException exception) {
	_errorsExist = true;
	_sb.append("XML parsing fatal error:  ");
	_sb.append(exception.getMessage());
	_sb.append("\n");
    }

    public void warning(SAXParseException exception) {
	_errorsExist = true;
	_sb.append("XML parsing warning:  ");
	_sb.append(exception.getMessage());
	_sb.append("\n");
    }

}
