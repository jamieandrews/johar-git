package johar.interfaceinterpreter.star;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.table.JTableHeader;

import johar.idf.Idf;
import johar.idf.IdfCommand;
import johar.idf.IdfParameter;
import johar.idf.IdfQuestion;

/**
 * Creates the contents of the Help Dialog Box.
 *
 */
public class HelpContents {
	private IdfAnalyzer idfAnalyzer;
	private CommandController _cc;
	private List<String> columnList;
	private TreeMap<Integer, List<Object>> rowsMap;
	private ScrollingWidget scrollingTable;
	private HelpTableModel tableModel;
	private TableWidget table;

	/**
	 * 
	 * @param idf
	 * IDF object
	 * @param cc
	 * Command Controller object
	 */
	public HelpContents(Idf idf, CommandController cc) {
		idfAnalyzer = new IdfAnalyzer(idf);
		_cc = cc;
	}

	/**
	 * Creates and returns a scrollable table containing Commands' help information for the Top Level State.
	 * @return
	 * the scrollable Commands table for the Top Level State
	 */
	protected ScrollingWidget getTopLevelStateTable() {
		loadTopLevelStateData();
		tableModel = new HelpTableModel(columnList, rowsMap);
		table = new TableWidget(0, _cc);
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setMinWidth(200);
		table.getColumnModel().getColumn(0).setMaxWidth(300);
		table.setDefaultRenderer(Object.class, new TableCellRenderer());
		table.setTableHeader(new JTableHeader());
		scrollingTable = new ScrollingWidget(table);
		scrollingTable.setName("topLevelHelp");

		return scrollingTable;
	}

	/**
	 * Creates and returns a scrollable table containing Parameters' help information for the Command State.
	 * @return
	 * the scrollable Parameters table for the Command State
	 */
	protected ScrollingWidget getCommandStateTableParams(IdfCommand command) {
		loadCommandStateData(command);

		tableModel = new HelpTableModel(columnList, rowsMap);
		table = new TableWidget(1, _cc);
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setMinWidth(200);
		table.getColumnModel().getColumn(0).setMaxWidth(300);
		table.setDefaultRenderer(Object.class, new TableCellRenderer());
		table.setTableHeader(new JTableHeader());
		scrollingTable = new ScrollingWidget(table);
		scrollingTable.setName("paramsHelp");

		return scrollingTable;
	}

	/**
	 * Creates and returns a scrollable table containing Questions' help information for the Command State.
	 * @return
	 * the scrollable Questions table for the Command State
	 */
	protected ScrollingWidget getCommandStateTableQues(IdfCommand command) {
		loadCommandStateData2(command);

	    tableModel = new HelpTableModel(columnList, rowsMap);
		table = new TableWidget(2, _cc);
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setMinWidth(200);
		table.getColumnModel().getColumn(0).setMaxWidth(300);
		table.setDefaultRenderer(Object.class, new TableCellRenderer());
		table.setTableHeader(new JTableHeader());
		scrollingTable = new ScrollingWidget(table);
		scrollingTable.setName("questionsHelp");

		return scrollingTable;
	}

	//Loads Commands' help information into a table
	private void loadTopLevelStateData() {
		columnList = new ArrayList<String>();
		rowsMap = new TreeMap<Integer, List<Object>>();
		List<Object> rowList;
		int rowCounter = 0;
		
		columnList.add("");
		columnList.add("");

		for (IdfCommand command : idfAnalyzer.getAllCommands()) {
			rowList = new ArrayList<Object>();
			rowList.add(command.getLabel());
			rowList.add(command.getOneLineHelp());
			rowsMap.put(rowCounter, rowList);
			rowCounter++;
		}
	}

	//Loads Parameters' help information into a table
	private void loadCommandStateData(IdfCommand command) {
		columnList = new ArrayList<String>();
		rowsMap = new TreeMap<Integer, List<Object>>();
		List<Object> rowList;
		int rowCounter = 0;

		columnList.add("");
		columnList.add("");

		idfAnalyzer.setCurrentCommand(command.getCommandName());
		for (IdfParameter param : idfAnalyzer.getParametersInCommand(false)) {
			rowList = new ArrayList<Object>();
			rowList.add(param.getLabel());
			rowList.add(param.getOneLineHelp());
			rowsMap.put(rowCounter, rowList);
			rowCounter++;
		}
	}

	//Loads Questions' help information into a table
	private void loadCommandStateData2(IdfCommand command) {
		columnList = new ArrayList<String>();
		rowsMap = new TreeMap<Integer, List<Object>>();
		List<Object> rowList;
		int rowCounter = 0;
		
		columnList.add("");
		columnList.add("");

		idfAnalyzer.setCurrentCommand(command.getCommandName());
		for (IdfQuestion param : idfAnalyzer.getQuestionsInCommand()) {
			rowList = new ArrayList<Object>();
			rowList.add(param.getLabel());
			rowList.add(param.getOneLineHelp());
			rowsMap.put(rowCounter, rowList);
			rowCounter++;
		}
	}

}
