/*
 * Creates filters for "int" and "float" types
 */

package johar.interfaceinterpreter.star;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Makes a text field to accept only numeric values - integers and float-point numbers. 
 *
 */
public class NumberFilter extends DocumentFilter {
	private boolean filterFlag;
	private boolean _isInteger;
	
	/**
	 * 
	 * @param isInteger
	 * true, if only integers should be allowed; false, if both integers and floating-point numbers should be accepted.
	 */
	public NumberFilter(boolean isInteger) {
		filterFlag = true;
		_isInteger = isInteger;
	}
	
	/**
	 * Inserts the specified text, bypassing the DocumentFilter.
	 * @param fb
	 * DocumentFilter.FilterBypass object
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
	public void insertString(DocumentFilter.FilterBypass fb, int offset, int length,
		String input, AttributeSet attSet)	throws BadLocationException {
		
		if (_isInteger){
			if (filterFlag){
				if (offset == 0)
					fb.insertString(offset, input.replaceAll("[^0-9-]", ""), attSet);
				else
					fb.insertString(offset, input.replaceAll("[^0-9]", ""), attSet);
			}
			else{
				fb.insertString(offset, input.replaceAll("[^0-9]", ""), attSet);
			}
		}
		else {
			if (filterFlag){
				if (offset == 0)
					fb.insertString(offset, input.replaceAll("[^0-9.-]", ""), attSet);
				else
					fb.insertString(offset, input.replaceAll("[^0-9.]", ""), attSet);
			}
			else{
				fb.insertString(offset, input.replaceAll("[^0-9]", ""), attSet);
			}
		}	
	}
		
	/**
	 * Replaces with specified text, bypassing the DocumentFilter.
	 * @param fb
	 * DocumentFilter.FilterBypass object
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
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
		String input, AttributeSet attSet) throws BadLocationException {
		
		if (_isInteger){
			if (filterFlag){
				if (offset == 0)
					fb.replace(offset, length, input.replaceAll("[^0-9-]", ""), attSet);
				else
					fb.replace(offset, length, input.replaceAll("[^0-9]", ""), attSet);
			}
			else{
				fb.replace(offset, length, input.replaceAll("^[0-9]", ""), attSet);
			}	
		}
		else{
			if (filterFlag){
				if (offset == 0)
					fb.replace(offset, length, input.replaceAll("[^0-9.-]", ""), attSet);
				else
					fb.replace(offset, length, input.replaceAll("[^0-9.]", ""), attSet);
			}
			else{
				fb.replace(offset, length, input.replaceAll("^[0-9]", ""), attSet);
			}	
		}			 
	}
	
	/**
	 * Specifies whether to use the default filter.
	 * @param flag
	 */
	public void useDefaultFilter(boolean flag){
		filterFlag = flag;
	}
	
}
