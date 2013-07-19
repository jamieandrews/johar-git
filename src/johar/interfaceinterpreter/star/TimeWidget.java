/*
 * Creates widget for "timeOfDay" type
 */

package johar.interfaceinterpreter.star;

import java.awt.Component;
import java.text.ParseException;
import java.util.Date;
import javax.swing.JComponent;
import johar.idf.IdfParameter;

/**
 * This is the widget for every timeOfDay parameter.
 *
 */
public class TimeWidget extends ParameterWidget {
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
	public TimeWidget(Object defaultValue, IdfParameter paramObj, CommandController cc) {
		super(defaultValue, paramObj, cc);
		_paramName = paramObj.getParameterName();
	}
	
	/**
	 * Gets the time selected by the user for a repetition.
	 * @param repNumber
	 * repetition number
	 * @return
	 * the selected time
	 */
	public Date getSelectedTime(int repNumber){
		String name = "spn" + _paramName + repNumber;		
		
		for (Component c : getComponents()){
			try {
				if (c.getName().equals(name))
					return getTimeInstance(WidgetAnalyzer.getValue((JComponent) c));
			} catch (ParseException e) {
				MessageDialog.showError("An error occurred while converting time to the required format." +
						" [Error details: " + e.getMessage() + "]");
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
	 * Gets the number of fields for which the user has specified a time.
	 * @return
	 * number of non-empty fields
	 */
	public int getNonEmptyTimeFieldsCount() {
		int numOfFields = getFieldsCount();
		int counter = 0;
		
		for (int i = 0; i < numOfFields; i++) {
			if (getSelectedTime(i) != null && !getSelectedTime(i).equals(""))
				counter++;
		}

		return counter;
	}
	
}
