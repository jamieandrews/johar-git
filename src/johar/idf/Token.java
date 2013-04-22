/* Token.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */
package johar.idf;

import java.io.*;

public class Token {
    public enum TokenType {
	UNKNOWN_TOKEN,
	ERROR_TOKEN,
	EOF_TOKEN,
	UCIDENT_TOKEN,
	LCIDENT_TOKEN,
	STRING_TOKEN,
	INT_TOKEN,
	LONGTEXT_TOKEN,
	OPENCURLY_TOKEN,
	CLOSECURLY_TOKEN,
	EQUALS_TOKEN
    }
    private TokenType _tokenType;
    private String _stringContents;
    private int _startingLine;
    private int _startingCharacter;
    public Token(TokenType tokenType, String stringContents,
                 int startingLine, int startingCharacter) {
	// System.out.println("New token: " +
	//   "tokenType = " + tokenType +
	//   ", stringContents = '" + stringContents +
	//   "', startingLine = " + startingLine +
	//   ", startingCharacter = " + startingCharacter);
	_tokenType = tokenType;
	_stringContents = stringContents;
	_startingLine = startingLine;
	_startingCharacter = startingCharacter;
    }

    /* tokenType:  TODO
     * @return TODO
     */
    TokenType tokenType() {
	return _tokenType;
    }

    /* printError:  TODO
     * @param s TODO
     * @param err TODO
     */
    void printError(String s, PrintWriter err) {
	err.write(
	    "Error at line " + _startingLine
	    + ", character " + _startingCharacter
	    + ":\n  " + s + "\n");
    }

    /* addAsValue:  TODO
     * @param level TODO
     * @param out TODO
     * @param err TODO
     */
    void addAsValue(int level, PrintWriter out, PrintWriter err) {
	if (_tokenType == TokenType.LONGTEXT_TOKEN) {
	    out.write("\n" + _stringContents);
	    while (level > 0) {
		out.write(" ");
		level--;
	    }
	} else {
	    out.write(_stringContents);
	}
    } /* addAsValue */

    /* openStartTag:  TODO
     * @param level TODO
     * @param out TODO
     * @param err TODO
     */
    void openStartTag(int level, PrintWriter out, PrintWriter err) {
	while (level > 0) {
	    out.write(" ");
	    level--;
	}
	out.write("<" + _stringContents);
    }

    /* addName:  TODO
     * @param out TODO
     * @param err TODO
     */
    void addName(PrintWriter out, PrintWriter err) {
	out.write(" name=\"" + _stringContents + "\"");
    }

    /* closeStartTag:  TODO
     * @param level TODO
     * @param out TODO
     * @param err TODO
     */
    void closeStartTag(int level, PrintWriter out, PrintWriter err) {
	out.write(">");
    }

    /* writeEndTag:  TODO
     * @param level TODO
     * @param out TODO
     * @param err TODO
     */
    void writeEndTag(int level, PrintWriter out, PrintWriter err) {
	while (level > 0) {
	    out.write(" ");
	    level--;
	}
	out.write("</" + _stringContents + ">\n");
    }

    /* closeEmptyElementTag:  TODO
     * @param level TODO
     * @param out TODO
     * @param err TODO
     */
    void closeEmptyElementTag(int level, PrintWriter out, PrintWriter err) {
	out.write("/>\n");
    }

    /* writeSelf:  TODO
     * @param out TODO
     */
    void writeSelf(PrintWriter out) {
	out.write("[[[" + _stringContents + "]]]\n");
    }
}
