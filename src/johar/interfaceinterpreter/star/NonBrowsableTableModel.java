package johar.interfaceinterpreter.star;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * Model for Non-Browsable Tables.
 *
 */
public class NonBrowsableTableModel extends AbstractListModel implements ComboBoxModel {
	private List<String> _dataSet;
	private String selectedValue;
	
	/**
	 * 
	 * @param dataSet
	 * data to load into the Combo Box
	 */
	public NonBrowsableTableModel(List<String> dataSet) {
		_dataSet = new ArrayList<String>();		
		_dataSet = dataSet;
	}

	/**
	 * Get value at a position
	 */
	public String getElementAt(int index) {
		return _dataSet.get(index);
	}

	/**
	 * Get the size of the dataset
	 */
	public int getSize() {
		return _dataSet.size();
	}

	/**
	 * Get the selected value
	 */
	public String getSelectedItem() {
		return selectedValue;
	}

	/**
	 * Set the selected value
	 */
	public void setSelectedItem(Object value) {
		if (value != null)
			selectedValue = value.toString();
		else
			selectedValue = "";
	}
}
