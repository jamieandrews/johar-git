/*
 * Creates filters for "int" and "float" types
 */

package johar.interfaceinterpreter.star;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumberFilter extends DocumentFilter {
	private boolean filterFlag;
	private boolean _isInteger;
	
	public NumberFilter(boolean isInteger) {
		filterFlag = true;
		_isInteger = isInteger;
	}
	
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
		
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
		String input, AttributeSet attSet) throws BadLocationException {
		
		if (_isInteger){
			if (filterFlag){
				if (offset == 0)
					fb.insertString(offset, input.replaceAll("[^0-9-]", ""), attSet);
				else
					fb.insertString(offset, input.replaceAll("[^0-9]", ""), attSet);
			}
			else{
				fb.insertString(offset, input.replaceAll("^[0-9]", ""), attSet);
			}	
		}
		else{
			if (filterFlag){
				if (offset == 0)
					fb.insertString(offset, input.replaceAll("[^0-9.-]", ""), attSet);
				else
					fb.insertString(offset, input.replaceAll("[^0-9.]", ""), attSet);
			}
			else{
				fb.insertString(offset, input.replaceAll("^[0-9]", ""), attSet);
			}	
		}			 
	}
	
	public void useDefaultFilter(boolean flag){
		filterFlag = flag;
	}
	
}
