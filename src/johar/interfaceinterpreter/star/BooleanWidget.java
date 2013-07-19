package johar.interfaceinterpreter.star;

import java.awt.Component;

import javax.swing.JComponent;
import johar.idf.IdfParameter;

/**
 * This is the widget for every boolean parameter.
 *
 */
public class BooleanWidget extends ParameterWidget {
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
	public BooleanWidget(Object defaultValue, IdfParameter paramObj, CommandController cc) {
		super(defaultValue, paramObj, cc);
		_paramName = paramObj.getParameterName();
	}
	
	/**
	 * Gets the option selected by the user for a repetition.
	 * @param repNumber
	 * repetition number
	 * @return
	 * the selected option (yes or no)
	 */
	public String getSelectedOption(int repNumber){
		String nameYes = "rdoYes" + _paramName + repNumber;
		String nameNo = "rdoNo" + _paramName + repNumber;
		boolean isSelected;
		
		for (Component c : getComponents()){
			if (c.getName().equals(nameYes)) {
				isSelected = (Boolean) WidgetAnalyzer.getValue((JComponent) c);
				if (isSelected) {
					return "Yes";
				}
			} else if (c.getName().equals(nameNo)) {
				isSelected = (Boolean) WidgetAnalyzer.getValue((JComponent) c);
				if (isSelected) {
					return "No";
				}
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
	 * Gets the number of fields for which the user has selected an option.
	 * @return
	 * number of non-empty fields
	 */
	public int getNonEmptyBooleanFieldsCount() {
		int numOfFields = getFieldsCount();
		int counter = 0;
		
		for (int i = 0; i < numOfFields; i++) {
			if (getSelectedOption(i) != null)
				counter++;
		}

		return counter;
	}	
}
