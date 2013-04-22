/* Tokenizer.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */
package johar.idf;

import java.io.*;

public class Tokenizer {
    Token lastTokenParsed;
    char lastCharRead;
    char lastCharGotten;
    boolean peekedAtToken;
    boolean peekedAtChar;
    int lineNumber;
    int charNumber;
    boolean EOF;
    Reader _in;
    PrintWriter _err;
    public Tokenizer(Reader in, PrintWriter err) {
	lastTokenParsed = null;
	peekedAtToken = false;
	peekedAtChar = false;
	lineNumber = 1;
	charNumber = 0;
	EOF = false;
	_in = in;
	_err = err;
    }

    /* getNextToken:  TODO
     * @return TODO
     */
    public Token getNextToken() {
	// cout << "getNextToken()" << endl;
	peekNextToken();
	peekedAtToken = false;
	return lastTokenParsed;
    }

    /* peekNextToken:  TODO
     * @return TODO
     */
    public Token peekNextToken() {
	// cout << "peekNextToken()" << endl;
	if (!peekedAtToken) {
	    lastTokenParsed = parseToken();
	}
	peekedAtToken = true;
	return lastTokenParsed;
    }

    /* getNextChar:  TODO
     * @return TODO
     */
    public char getNextChar() {
	peekNextChar();
	if (lastCharGotten == '\n') {
	    lineNumber++;
	    charNumber = 0;
	}
	charNumber++;
	lastCharGotten = lastCharRead;
	peekedAtChar = false;
	return lastCharGotten;
    } /* getNextChar */

    /* peekNextChar:  TODO
     * @return TODO
     */
    public char peekNextChar() {
	if (!peekedAtChar) {
	    int code;
	    try {
		code = _in.read();
	    } catch (Exception e) {
		code = -1;
	    }
	    if (code < 0) {
		EOF = true;
		lastCharRead = (char) 0;
	    } else {
		lastCharRead = (char) code;
	    }
	}
	peekedAtChar = true;
	return lastCharRead;
    } /* peekNextChar */

    /* parseToken:  TODO
     * @return TODO
     */
    public Token parseToken() {
	char c;

	eatWhitespace();
	c = getNextChar();
	if (EOF) {
	    return new Token(Token.TokenType.EOF_TOKEN,
	                     "$EOF$",
	                     lineNumber,
	                     charNumber);
	} else if (c == '{') {
	    char c2 = peekNextChar();
	    if (c2 == '{') {
		return parseRestOfLongText(lineNumber, charNumber);
	    } else {
		return new Token(Token.TokenType.OPENCURLY_TOKEN,
		                 "{",
		                 lineNumber,
		                 charNumber);
	    }
	} else if ( Character.isLetter(c) ) {
	    return parseRestOfIdent(c, lineNumber, charNumber);
	} else if ( Character.isDigit(c) || (c == '-') ) {
	    return parseRestOfNumber(c, lineNumber, charNumber);
	} else if (c == '"') {
	    return parseRestOfString(lineNumber, charNumber);
	} else if (c == '}') {
	    return new Token(Token.TokenType.CLOSECURLY_TOKEN,
	                     "}",
	                     lineNumber,
	                     charNumber);
	} else if (c == '=') {
	    return new Token(Token.TokenType.EQUALS_TOKEN,
	                     "=",
	                     lineNumber,
	                     charNumber);
	} else {
	    _err.write("Error: Illegal character at line " + lineNumber +
	               ", character " + charNumber + "\n");
	    return new Token(Token.TokenType.ERROR_TOKEN,
	                     "",
	                     lineNumber,
	                     charNumber);
	}
    } /* parseToken */

    /* eatWhitespace:  TODO
     * 
     */
    public void eatWhitespace() {
	while ( Character.isWhitespace( peekNextChar() ) ) {
	    getNextChar(); // throw away char
	}
    }

    /* eatBlanks:  TODO
     * 
     */
    public void eatBlanks() {
	while (peekNextChar() == ' '
	      || peekNextChar() == '\t') {
	    getNextChar(); // throw away char
	}
    }

    /* parseRestOfLongText:  TODO
     * @param startLine TODO
     * @param startChar TODO
     * @return TODO
     */
    public Token parseRestOfLongText(int startLine, int startChar) {
	// Precondition: peeked char is "{".
	char c;
	StringBuffer startingBlanks = new StringBuffer();
	StringBuffer contents = new StringBuffer();

	getNextChar(); // throw away the "{"
	eatBlanks();
	if (peekNextChar() == '\r') { // Bill's character
	    getNextChar(); // throw it away
	}
	if (peekNextChar() != '\n') {
	    _err.write(
	        "Error on line " + startLine +
	        ", character " + startChar +
	        ":\n  Long text opener '{{' must be followed by only blank, tab, and end of line\n");
	    return new Token(Token.TokenType.ERROR_TOKEN,
	                     "",
	                     startLine,
	                     startChar);
	} else {
	    getNextChar();
	}
	// OK, rest of opening line discarded;
	// now starting to parse actual content
	while (true) {
	    // Get another line, whether it's the ending line or another line
	    // of content
	    startingBlanks = new StringBuffer();
	    c = getNextChar();
	    while (c == ' ') {
		startingBlanks.append(c);
		c = getNextChar();
	    }
	    if (EOF) { // Oops, long text not terminated until EOF
		_err.write("Error on line " + startLine +
		           ", character " + startChar +
		           ": Unterminated long text\n");
		return new Token(Token.TokenType.ERROR_TOKEN,
		                 "",
		                 startLine,
		                 startChar);
	    }
	    if ( (c == '}') && (peekNextChar() == '}') ) { // end of whole
                                                           // long text
		getNextChar(); // throw away final '}'
		return new Token(Token.TokenType.LONGTEXT_TOKEN,
		                 contents.toString(),
		                 startLine,
		                 startChar);
	    }
	    // else, regular line; go on
	    contents.append( startingBlanks.toString() );
	    expandingAppend(contents, c);
	    while (c != '\n') {
		// Get rest of line
		c = getNextChar();
		if (EOF) { // Again with the unterminated
		    _err.write("Error on line " + startLine +
		               ", character " + startChar +
		               ": Unterminated long text\n");
		    return new Token(Token.TokenType.ERROR_TOKEN,
		                     "",
		                     startLine,
		                     startChar);
		}
		expandingAppend(contents, c);
	    }
	    // OK, now there's another line in the contents.
	    // We'll continue on our merry way until we get the "}}", or EOF
	}
    } /* parseRestOfLongText */

    // Append token, possibly taking account of XML expansion
    private void expandingAppend(StringBuffer sb, char c) {
	switch (c) {
	case '&':
	    sb.append("&amp;");
	    break;

	case '<':
	    sb.append("&lt;");
	    break;

	case '>':
	    sb.append("&gt;");
	    break;

	case '\'':
	    sb.append("&apos;");
	    break;

	case '"':
	    sb.append("&quot;");
	    break;

	default:
	    sb.append(c);
	} /* switch */
    } /* expandingAppend */

    /* parseRestOfIdent:  TODO
     * @param firstChar TODO
     * @param startLine TODO
     * @param startChar TODO
     * @return TODO
     */
    private Token parseRestOfIdent(char firstChar,
                                   int startLine,
                                   int startChar) {
	StringBuffer contents = new StringBuffer();

	contents.append(firstChar);
	char c = peekNextChar();
	while ( Character.isLetterOrDigit(c) || (c == '_') ) {
	    getNextChar();
	    contents.append(c);
	    c = peekNextChar();
	}
	// OK, done
	if ( Character.isUpperCase(firstChar) ) {
	    return new Token(Token.TokenType.UCIDENT_TOKEN, contents.toString(
	                         ), startLine, startChar);
	} else {
	    return new Token(Token.TokenType.LCIDENT_TOKEN, contents.toString(
	                         ), startLine, startChar);
	}
    } /* parseRestOfIdent */

    /* parseRestOfNumber:  TODO
     * @param firstChar TODO
     * @param startLine TODO
     * @param startChar TODO
     * @return TODO
     */
    private Token parseRestOfNumber(char firstChar,
                                    int startLine,
                                    int startChar) {
	StringBuffer contents = new StringBuffer();

	contents.append(firstChar);
	char c = peekNextChar();
	if (c == '-') {
	    getNextChar();
	    contents.append(c);
	    c = peekNextChar();
	}
	while ( Character.isDigit(c) ) {
	    getNextChar();
	    contents.append(c);
	    c = peekNextChar();
	}
	if (c == '.') {
	    getNextChar();
	    contents.append(c);
	    c = peekNextChar();
	    if ( !Character.isDigit(c) ) {
		_err.write("Error at line " + lineNumber +
		           ", character " + charNumber +
		           ": expecting decimal digits\n");
		return new Token(Token.TokenType.ERROR_TOKEN,
		                 "",
		                 lineNumber,
		                 charNumber);
	    }
	    while ( Character.isDigit(c) ) {
		getNextChar();
		contents.append(c);
		c = peekNextChar();
	    }
	    if ( (c == 'e') || (c == 'E') ) {
		getNextChar();
		contents.append(c);
		c = peekNextChar();
		if ( !Character.isDigit(c) ) {
		    _err.write("Error at line " + lineNumber +
		               ", character " + charNumber +
		               ": expecting exponent\n");
		    return new Token(Token.TokenType.ERROR_TOKEN,
		                     "",
		                     lineNumber,
		                     charNumber);
		}
		while ( Character.isDigit(c) ) {
		    getNextChar();
		    contents.append(c);
		    c = peekNextChar();
		}
	    }
	}
	// OK, done
	return new Token(Token.TokenType.INT_TOKEN,
	                 contents.toString(), startLine, startChar);
    } /* parseRestOfNumber */

    /* parseRestOfString:  TODO
     * @param startLine TODO
     * @param startChar TODO
     * @return TODO
     */
    private Token parseRestOfString(int startLine, int startChar) {
	StringBuffer contents = new StringBuffer();
	char c = peekNextChar();

	while (c != '"') {
	    getNextChar();
	    if ( EOF || (c == '\r') || (c == '\n') ) {
		_err.write("Error at line " + lineNumber +
		           ", character " + charNumber +
		           ": unterminated string\n");
		return new Token(Token.TokenType.ERROR_TOKEN,
		                 "",
		                 lineNumber,
		                 charNumber);
	    }
	    if (c == '\\') { // literal next character (note, also lit next in
                             // C++!)
		c = getNextChar();
	    }
	    expandingAppend(contents, c);
	    c = peekNextChar();
	}
	// throw away closing quote
	getNextChar();
	// OK, done
	return new Token(Token.TokenType.STRING_TOKEN,
	                 contents.toString(), startLine, startChar);
    } /* parseRestOfString */
}
