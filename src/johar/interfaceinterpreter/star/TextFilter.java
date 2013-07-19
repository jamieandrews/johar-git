/*
 * Creating a filter for a "text" widget
 */

package johar.interfaceinterpreter.star;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A filter for a text field which constrains the user to enter text that does not exceed the specified maximum number of characters.
 *
 */
public class TextFilter extends PlainDocument {
	private int _maximumChars;
	
	/**
	 * 
	 * @param maximumChars
	 * maximum number of characters to allow.
	 */
	public TextFilter(int maximumChars) {
		_maximumChars = maximumChars;
	}

	/**
	 * Inserts the specified text, bypassing the DocumentFilter.
	 * @param offset
	 * the offset into the document to insert the content >= 0. All positions that track change at or after the given location will move.
	 * @param length
	 * length of the string to insert
	 * @param input
	 * the string to insert
	 * @param attSet
	 * the attributes to associate with the inserted content. This may be null if there are no attributes.
	 * @throws BadLocationException
	 * thrown whenever the given insert position is not a valid position within the document
	 */
	public void insertString(int offset, int length,
			String input, AttributeSet attSet)	throws BadLocationException {
		
		if ((getLength() + input.length()) <= _maximumChars)
			super.insertString(offset, input, attSet);
		else{
			String newInput = input.substring(0, _maximumChars - getLength());
			super.insertString(offset, newInput, attSet);
		}			
	}
	
	/**
	 * Replaces with specified text, bypassing the DocumentFilter.
	 * @param offset
	 * the offset into the document to insert the content >= 0. All positions that track change at or after the given location will move.
	 * @param length
	 * length of the string to insert
	 * @param input
	 * the string to insert
	 * @param attSet
	 * the attributes to associate with the inserted content. This may be null if there are no attributes.
	 * @throws BadLocationException
	 * thrown whenever the given insert position is not a valid position within the document
	 */
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
