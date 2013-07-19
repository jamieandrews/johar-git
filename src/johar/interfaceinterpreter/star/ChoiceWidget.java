/*
 * Creates widget for "choice" type
 */

package johar.interfaceinterpreter.star;

import java.awt.Component;
import javax.swing.JComponent;
import johar.idf.IdfParameter;

/**
 * This is the widget for every choice parameter.
 *
 */
public class ChoiceWidget extends ParameterWidget {
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
	public ChoiceWidget(Object defaultValue, IdfParameter paramObj, CommandController cc) {
		super(defaultValue, paramObj, cc);
		_paramName = paramObj.getParameterName();
	}
		
	/**
	 * Gets the choice selected by the user for a repetition.
	 * @param repNumber
	 * repetition number
	 * @return
	 * the selected choice
	 */
	public Object getSelectedChoice(int repNumber){
		String name = "cbo" + _paramName + repNumber;
		
		for (Component c : getComponents()) {
			if (c.getName().equals(name))
				return WidgetAnalyzer.getValue((JComponent) c);
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
	 * Gets the number of fields for which the user has selected a choice.
	 * @return
	 * number of non-empty fields
	 */
	public int getNonEmptyChoiceFieldsCount() {
		int numOfFields = getFieldsCount();
		int counter = 0;
		
		for (int i = 0; i < numOfFields; i++) {
			if (getSelectedChoice(i) != null && !getSelectedChoice(i).toString().trim().equals(""))
				counter++;
		}

		return counter;
	}
	
}
