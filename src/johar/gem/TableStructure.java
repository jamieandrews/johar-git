/* TableStructure.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

package johar.gem;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*
 * A class to store the structure of
 * a table
 */
public class TableStructure {
    private List<String> _table;
    private String _tableHeading;
    private boolean _isBrowsable;
    private boolean _isShown;
    private boolean _isUpdated;

    public TableStructure(String initialHeading, boolean isBrowsable) {
	_table = new ArrayList<String>();
        _tableHeading = initialHeading;
	_isBrowsable = isBrowsable;
	_isShown = false;
	_isUpdated = false;
    }

    public String getTableHeading() {
	return _tableHeading;
    }

    public void setTableHeading(String s) {
	_tableHeading = s;
    }

    public String getRowText(int rowNumber) {
	return _table.get(rowNumber);
    }

    public boolean tableIsShown() {
	return _isShown;
    }

    public void showTable() {
	if (_isBrowsable) {
	    _isShown = true;
	}
    }

    public void hideTable() {
	_isShown = false;
    }

    public boolean tableIsUpdated() {
	return _isUpdated;
    }

    public void resetIsUpdated() {
	_isUpdated = false;
    }

    public void clearTable() {
	_table = new ArrayList<String>();
    }

    public void fillRow(int rowNumber, String text) {
	while (_table.size() <= rowNumber) {
	    _table.add("");
	}
	_table.set(rowNumber, text);
	_isUpdated = true;
    }

    public boolean rowIsFilled(int rowNumber) {
	if (_table.size() > rowNumber)
	    return true;
	else
	    return false;
    }

}
