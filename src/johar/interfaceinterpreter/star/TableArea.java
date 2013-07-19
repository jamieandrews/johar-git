package johar.interfaceinterpreter.star;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JTabbedPane;
import johar.gem.GemSetting;
import johar.idf.Idf;
import johar.idf.IdfTable;

/**
 * The Table Area in the Star Window.
 *
 */
public class TableArea extends JTabbedPane {
	private Idf _idf;
	private GemSetting _gem;
	private CommandController _cc;
	private IdfAnalyzer _idfAnalyzer;
	private List<String> columnList;
	private TreeMap<Integer, List<Object>> rowsMap;
	private BrowsableTableModel tableModel;
	private TableWidget table;
	private ScrollingWidget tableScroller;
	
	/**
	 * 
	 * @param idf
	 * IDF object
	 * @param gem
	 * GemSetting object
	 * @param cc
	 * Command Controller object
	 */
	public TableArea(Idf idf, GemSetting gem, CommandController cc) {
		_idf = idf;
		_gem = gem;
		_cc = cc;
		createTables();
	}	
	
	//Creates tables
	private void createTables(){
		_idfAnalyzer = new IdfAnalyzer(_idf);
		
		List<IdfTable> tableList = _idfAnalyzer.getBrowsableTables();
		String tableName;
		
		for (IdfTable table : tableList){
			tableName = table.getTableName();
			
			if (_gem.tableIsShown(tableName)){
				addTable(tableName);
			}
		}
	}
	
	//Loads data into a table
	private void loadTableData(String tableName){
		columnList = new ArrayList<String>();
		rowsMap = new TreeMap<Integer, List<Object>>();
		List<Object> rowList;
		
		//Get columns
		String[] cols = _gem.getColumnNames(tableName).split("\\|");
		for (String col : cols){
			columnList.add(col);
		}
		
		//Fill rows
		boolean rowFilled = true;
		int rowNum = 0;
		String rowText = "";
		Object[] cellContent = null;
		while (rowFilled){
			if (_gem.rowIsFilled(tableName, rowNum)){
				rowList = new ArrayList<Object>();
				rowText = _gem.getRowText(tableName, rowNum);
				cellContent = rowText.split("\\|");
				
				for (int i = 0; i < cellContent.length; i++){
					rowList.add(cellContent[i]);
				}
				rowsMap.put(rowNum, rowList);
				rowNum++;
			}
			else{
				rowFilled = false;
			}			
		}
	}
	
	/**
	 * Adds the specified table to the Table Area.
	 * @param tableName
	 * name of the table to add
	 */
	public void addTable(String tableName){
		loadTableData(tableName);
		
		tableModel = new BrowsableTableModel(columnList, rowsMap);
		table = new TableWidget(tableName, _cc);
		table.setModel(tableModel);		
		tableScroller = new ScrollingWidget(table);
		tableScroller.setName(tableName);
		
		addTab(_gem.getTableHeading(tableName), tableScroller);
		makeTableVisible(tableName);
		
		revalidateTopTable();
		repaint();
	}
	
	/**
	 * Makes the specified table visible in the Table Area.
	 * @param tableName
	 * name of table
	 */
	public void makeTableVisible(String tableName){		
		int index = WidgetAnalyzer.getWidgetIndex(this, tableName);
		if (index >= 0){
			setSelectedIndex(index);
		}
	}
	
	/**
	 * Determines and sets the top table in the Table Area.
	 */
	public void revalidateTopTable(){
		if (searchForTable(_gem.getTopTable())){
			makeTableVisible(_gem.getTopTable());
		}
	}
	
	/**
	 * Searches for a table in the Table Area.
	 * @param tableName
	 * name of the table to find
	 * @return
	 * true, if found; false, if not found
	 */
	protected boolean searchForTable(String tableName){
		int index = WidgetAnalyzer.getWidgetIndex(this, tableName);
		if (index >= 0){
			return true;
		}
		return false;
	}
	
	/**
	 * Removes a table from the Table Area.
	 * @param tableName
	 * name of the table to remove.
	 */
	protected void deleteTab(String tableName){
		int index = WidgetAnalyzer.getWidgetIndex(this, tableName);
		if (index >= 0){
			remove(index);
		}
		repaint();
	}
	
	/**
	 * Refreshes the specified table.
	 * @param tableName
	 * name of the table to refresh.
	 */
	public void refreshTable(String tableName){
		int index = WidgetAnalyzer.getWidgetIndex(this, tableName);
		if (index >= 0){
			if (_gem.tableIsUpdated(tableName)){
				loadTableData(tableName);
				tableScroller = (ScrollingWidget) getComponentAt(index);
				table = (TableWidget) tableScroller.getTableWidgetInstance();
				tableModel = (BrowsableTableModel) table.getModel();
				tableModel.refreshTable(columnList, rowsMap);
			}	
			table.clearSelection();
			setTitleAt(index, _gem.getTableHeading(tableName));
		}
		repaint();
	}

}
