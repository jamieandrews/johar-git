/*
 * Creating a filter for a "text" widget
 */

package johar.interfaceinterpreter.star;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class TextFilter extends PlainDocument {
	private int _maximumChars;
	
	public TextFilter(int maximumChars) {
		_maximumChars = maximumChars;
	}

	public void insertString(int offset, int length,
			String input, AttributeSet attSet)	throws BadLocationException {
		
		if ((getLength() + input.length()) <= _maximumChars)
			super.insertString(offset, input, attSet);
		else{
			String newInput = input.substring(0, _maximumChars - getLength());
			super.insertString(offset, newInput, attSet);
		}			
	}
	
	public void replace(int offset, int length,
			String input, AttributeSet attSet)	throws BadLocationException {
		
		if ((getLength() + input.length() - length) <= _maximumChars)
			super.replace(offset, length, input, attSet);
		else{
			String newInput = input.substring(0, _maximumChars - getLength());
			super.insertString(offset, newInput, attSet);
		}
	}
}
