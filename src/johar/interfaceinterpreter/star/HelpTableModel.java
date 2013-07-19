package johar.interfaceinterpreter.star;

import java.util.List;
import java.util.TreeMap;

/**
 * Model for Help Tables.
 *
 */
public class HelpTableModel extends BrowsableTableModel {

	/**
	 * The Table Model's constructor.
	 * @param columnList
	 * list of columns
	 * @param rowsMap
	 * rows to load into the table
	 */
	public HelpTableModel(List<String> columnList, TreeMap<Integer,List<Object>> rowsMap) {
		super(columnList, rowsMap);
	}
	
}
