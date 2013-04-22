/* Idf2xml.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */
package johar.idf;

import java.io.*;

public class Idf2xml {
    boolean lastValueHadChildren;
    Reader _in;
    PrintWriter _out;
    PrintWriter _err;
    // Copies value from input to output.
    // Level = number of spaces to indent.
    // Sets eof to true if end-of-file reached.
    private boolean transduceValue(Tokenizer t, int level) {
	Token token;

	lastValueHadChildren = false;
	boolean eof = false;
	token = t.getNextToken();
	if (token.tokenType() == Token.TokenType.EOF_TOKEN) {
	    token.printError("Expecting value, got end-of-file", _err);
	    eof = true;
	} else if ( (token.tokenType() == Token.TokenType.UCIDENT_TOKEN)
	          || (token.tokenType() == Token.TokenType.LCIDENT_TOKEN)
	          || (token.tokenType() == Token.TokenType.STRING_TOKEN)
	          || (token.tokenType() == Token.TokenType.INT_TOKEN)
	          || (token.tokenType() == Token.TokenType.LONGTEXT_TOKEN)
	            ) {
	    token.addAsValue(level, _out, _err);
	} else if (token.tokenType() == Token.TokenType.OPENCURLY_TOKEN) {
	    token = t.peekNextToken();
	    _out.write("\n");
	    while ( !eof &&
	           (token.tokenType() !=
	            Token.TokenType.CLOSECURLY_TOKEN) ) {
		transduceAVP(t, level + 2);
		if (eof) {
		    token.printError(
		        "Expecting close curly brace, got end-of-file",
		        _err);
		} else {
		    token = t.peekNextToken();
		}
	    }
	    // At this point, token points to an un-gotten CLOSECURLY_TOKEN
	    // or EOF is true
	    if (!eof) {
		token = t.getNextToken();
	    }
	    lastValueHadChildren = true;
	} else {
	    // Value has to start with one of the above
	    token.printError("Expecting value, got something else", _err);
	}
	return eof;
    } /* transduceValue */

    // Copies one attribute-value pair from input to output.
    // Level = number of spaces to indent.
    // Sets eof to true if end-of-file reached.
    // OK if eof before attribute-value pair reached.
    private boolean transduceAVP(Tokenizer t, int level) {
	Token token;
	Token nextToken;
	boolean eof = false;

	token = t.getNextToken();
	if (token.tokenType() == Token.TokenType.EOF_TOKEN) {
	    return true;
	} else if (token.tokenType() == Token.TokenType.UCIDENT_TOKEN) {
	    // Tag
	    token.openStartTag(level, _out, _err);
	    nextToken = t.peekNextToken();
	    if (nextToken.tokenType() == Token.TokenType.LCIDENT_TOKEN) {
		// "name" for tag
		nextToken = t.getNextToken();
		nextToken.addName(_out, _err);
		nextToken = t.peekNextToken();
	    }
	    if (nextToken.tokenType() == Token.TokenType.EQUALS_TOKEN) {
		// Has value (possibly in addition to name)
		nextToken = t.getNextToken();
		token.closeStartTag(level, _out, _err);
		eof = transduceValue(t, level);
		if (lastValueHadChildren) {
		    token.writeEndTag(level, _out, _err);
		} else {
		    token.writeEndTag(0, _out, _err);
		}
	    } else {
		// Has no value
		token.closeEmptyElementTag(level, _out, _err);
	    }
	} else {
	    token.printError("Expecting upper-case identifier; ignoring",
	                     _err);
	}
	return eof;
    } /* transduceAVP */

    /* main:  Converts .idf file on standard input to .xml file
     *        on standard output.
     * @param args Command-line arguments (ignored).
     */
    public static void main(String[] args) {
	Reader in = new InputStreamReader(System.in);
	PrintWriter out = new PrintWriter(System.out);
	PrintWriter err = new PrintWriter(System.err);
	Idf2xml tehInstance = new Idf2xml(in, out, err);

	tehInstance.convert();
    }

    /* Constructor takes a Reader and two PrintWriters
     * to allow for maximum flexibility in how it is called
     * (with a String, using a file, etc.)
     */
    public Idf2xml(Reader in, PrintWriter out, PrintWriter err) {
	_in = in;
	_out = out;
	_err = err;
    }

    /* convert:  Converts an .idf file input on _in to an .xml file
     * output on _out.  Errors are written on _err.
     */
    public void convert() {
	Tokenizer t = new Tokenizer(_in, _err);
	boolean eof = false;

	_out.write(
	    "<Johar\n" +
	    "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
	    "    xsi:noNamespaceSchemaLocation=\"johar.xsd\">\n\n"
	    );
	eof = transduceAVP(t, 0);
	while (!eof) {
	    eof = transduceAVP(t, 0);
	}
	_out.write("</Johar>\n");
	_out.flush();
	_err.flush();
    } /* convert */
}
