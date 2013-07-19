package johar.interfaceinterpreter.star;

import java.util.List;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

/**
 * Model for Browsable Tables.
 *
 */
public class BrowsableTableModel extends AbstractTableModel {

	private List<String> _columnList;
	private TreeMap<Integer,List<Object>> _rowsMap;
	
	/**
	 * The Table Model's constructor.
	 * @param columnList
	 * list of columns
	 * @param rowsMap
	 * rows to load into the table
	 */
	public BrowsableTableModel(List<String> columnList, TreeMap<Integer,List<Object>> rowsMap) {
		_columnList = columnList;
		_rowsMap = rowsMap;
	}
	
	/**
	 * Refreshes data in the table
	 * @param columnList
	 * list of columns
	 * @param rowsMap
	 * rows to load into the table
	 */
	public void refreshTable(List<String> columnList, TreeMap<Integer,List<Object>> rowsMap) {
		_columnList = columnList;
		_rowsMap = rowsMap;
	}

	/**
	 * Gets the number of columns in the table.
	 */
	public int getColumnCount() {
		return _columnList.size();
	}

	/**
	 * Gets the number of rows in the table.
	 */
	public int getRowCount() {
		return _rowsMap.size();
	}

	/**
	 * Gets the value in a cell.
	 */
	public Object getValueAt(int rowNumber, int columnNumber) {
		return _rowsMap.get(rowNumber).get(columnNumber);
	}
	
	/**
	 * Gets the name of a column.
	 */
	public String getColumnName(int columnNumber){
		return _columnList.get(columnNumber);
	}
	
	/**
	 * Gets the values in a particular row.
	 * @param rowNumber
	 * row number
	 * @return
	 * list of values in the row
	 */
	public List<Object> getRow(int rowNumber){
		return _rowsMap.get(rowNumber);
	}
	
	/**
	 * Returns Object.class of the specified column
	 */
	public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }
	
	/**
	 * Checks if a cell is editable.
	 */
	public boolean isCellEditable(int rowNumber, int columnNumber) {
		return false;
	}

}
