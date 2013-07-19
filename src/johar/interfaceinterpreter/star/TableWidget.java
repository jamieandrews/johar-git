package johar.interfaceinterpreter.star;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Table Widget in the Table Area or Help Dialog Box.
 *
 */
public class TableWidget extends JTable {
	private String _tableName;
	private CommandController _cc;
	private int _sourceState;
	
	/**
	 * Constructor for the Table Widget in the Table Area.
	 * @param tableName
	 * name of table
	 * @param cc
	 * Command Controller object
	 */
	public TableWidget(String tableName, CommandController cc) {
		_tableName = tableName;
		_cc = cc;
		setName(tableName);
		//setCellSelectionEnabled(false);
		setRowSelectionAllowed(true);
		setShowGrid(true);
		setColumnSelectionAllowed(false);
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setFillsViewportHeight(true);
		getSelectionModel().addListSelectionListener(new TableRowListener());		
	}
	
	/**
	 * Constructor for the Table Widget in the Help Dialog Box.
	 * @param sourceState
	 * Help State [0 - Top Level State, 1 - Command State (Parameters), 2 - Command State (Questions)]
	 * @param cc
	 * Command Controller object
	 */
	public TableWidget(int sourceState, CommandController cc) {
		_sourceState = sourceState;
		_cc = cc;
		setName("help" + sourceState);
		setCellSelectionEnabled(true);
		setRowSelectionAllowed(false);
		setShowGrid(true);
		setColumnSelectionAllowed(false);
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setFillsViewportHeight(true);
		getSelectionModel().addListSelectionListener(new TableRowListenerState());		
	}
	
	//Event Handler for the Table Widget in the Table Area.
	private class TableRowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            _cc.tableRowsSelected(_tableName);
        }
    }
	
	//Event Handler for the Table Widget in the Help Dialog Box.
	private class TableRowListenerState implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            _cc.tableRowClicked(_sourceState);
        }
    }

}
