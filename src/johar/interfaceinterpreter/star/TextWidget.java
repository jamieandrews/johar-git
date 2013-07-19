/*
 * Creates widget for "text" type
 */

package johar.interfaceinterpreter.star;

import java.awt.Component;
import javax.swing.JComponent;
import johar.idf.IdfParameter;

/**
 * This is the widget for every text parameter.
 *
 */
public class TextWidget extends ParameterWidget {
	private String _paramName;
	
	/**
	 * The class' constructor
	 * @param defaultValue
	 * default value of the parameter
	 * @param paramObj
	 * IdfParameter object of the parameter
	 * @param cc
	 * the Command Controller object
	 */
	public TextWidget(Object defaultValue, IdfParameter paramObj, CommandController cc) {		
		super(defaultValue, paramObj, cc);
		_paramName = paramObj.getParameterName();
	}
		
	/**
	 * Gets the text provided by the user for a repetition.
	 * @param repNumber
	 * repetition number
	 * @return
	 * the specified text
	 */
	public String getValue(int repNumber){
		String name = "txt" + _paramName + repNumber;
		
		for (Component c : getComponents()){
			if (c.getName().equals(name)){					
				return WidgetAnalyzer.getValue((JComponent) c).toString();
			}
		}
		return null;
	}	

	/**
	 * Move the specified parameter repetition up.
	 * @param repNumber
	 * repetition number
	 */
	public void moveUpFields(int repNumber){
		performMoveUpAction(repNumber);
	}
	
	/**
	 * Moves the specified parameter repetition down when the Move Down button is clicked.
	 * @param repNumber
	 * repetition number
	 */
	public void moveDownFields(int repNumber){
		performMoveDownAction(repNumber);
	}
	
	/**
	 * Gets the number of fields for which the user has provided a text.
	 * @return
	 * number of non-empty fields
	 */
	public int getNonEmptyTextFieldsCount() {
		int numOfFields = getFieldsCount();
		int counter = 0;
		
		for (int i = 0; i < numOfFields; i++) {
			if (getValue(i) != null && !getValue(i).toString().trim().equals(""))
				counter++;
		}

		return counter;
	}
	
}
