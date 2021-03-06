package johar.interfaceinterpreter.star;

import java.awt.Component;
import javax.swing.JComponent;

import johar.idf.IdfParameter;

/**
 * This is the widget for every date parameter.
 *
 */
public class DateWidget extends ParameterWidget {
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
	public DateWidget(Object defaultValue, IdfParameter paramObj, CommandController cc) {
		super(defaultValue, paramObj, cc);
		_paramName = paramObj.getParameterName();
	}
	
	/**
	 * Gets the date selected by the user for a repetition.
	 * @param repNumber
	 * repetition number
	 * @return
	 * the selected date
	 */
	public Object getSelectedDate(int repNumber){
		String name = "dte" + _paramName + repNumber;		
		
		for (Component c : getComponents()){
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
	 * Gets the number of fields for which the user has selected a date.
	 * @return
	 * number of non-empty fields
	 */
	public int getNonEmptyDateFieldsCount() {
		int numOfFields = getFieldsCount();
		int counter = 0;
		
		for (int i = 0; i < numOfFields; i++) {
			if (getSelectedDate(i) != null && !getSelectedDate(i).equals(""))
				counter++;
		}

		return counter;
	}
}
