/** ErrorHandler.java
 * @author Jamie Andrews
 */

package johar.idf;

class ErrorHandler {
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

}
