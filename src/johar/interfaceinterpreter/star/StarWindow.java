package johar.interfaceinterpreter.star;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSplitPane;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.plaf.ColorUIResource;
import javax.swing.WindowConstants;

/**
 * The Main Window or Frame of the Star Interface Interpreter.
 */

public class StarWindow extends JFrame implements ComponentListener {
    private JMenuBar mainMenu;
    private JPanel mainPanel;
    private JSplitPane splitter;
    private JPanel statusPanel;
    private GroupLayout statusPanelLayout;
    private String horizontalLine;
   
    public StarWindow() {
		initialize();
	}
	
	private void initialize() {
        try {
			mainPanel = new JPanel();
			splitter = new JSplitPane();
			statusPanel = new JPanel();
			mainMenu = new JMenuBar();			

			UIManager.put("Table.gridColor", new ColorUIResource(Color.gray));
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			mainMenu.setName("starMenu");
			splitter.setName("splitter");
			splitter.setDividerLocation(450);			
			splitter.setOneTouchExpandable(true);
			splitter.addComponentListener(this);
						
			statusPanelLayout = new GroupLayout(statusPanel);
			statusPanel.setLayout(statusPanelLayout);
			
			GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
			mainPanel.setLayout(mainPanelLayout);
			mainPanelLayout.setHorizontalGroup(
			    mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			    .addComponent(splitter, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
			    .addComponent(statusPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			);
			mainPanelLayout.setVerticalGroup(
			    mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			    .addGroup(mainPanelLayout.createSequentialGroup()
			        .addComponent(splitter, GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addComponent(statusPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
			);
			
			getContentPane().add(mainPanel, BorderLayout.CENTER);

			setJMenuBar(mainMenu);
			initHorizontalLine();
			
			pack();
		} catch (Exception e) {
			MessageDialog.showError("An error occurred while performing the requested operation. [Error Details: "
					+ e.getMessage() + "]");
		}
    }
	
	/**
	 * Adds a menu (i.e. the Command Group menu) to the menu bar
	 * @param cmdMenu
	 * the menu to add
	 */
	public void addMenu(CommandMenu cmdMenu){
		mainMenu.add(cmdMenu);
	}
	
	/**
	 * Adds the specified command menu item to the parent menu
	 * @param cmdItem
	 * the command menu item to add
	 * @param parentMenuName
	 * the name of the parent menu (i.e. the Command Group)
	 */
	public void addMenuItem(CommandMenuItem cmdItem, String parentMenuName){
		int numOfMenus = mainMenu.getMenuCount();
		for (int i = 0; i < numOfMenus; i++) {
			if (mainMenu.getMenu(i).getName().equals(parentMenuName)) {
				mainMenu.getMenu(i).add(cmdItem);
				break;
			}
		}
	}
	
	/**
	 * Adds the Text Display Area to the main panel
	 * @param scrollingTextArea
	 * the Text Display Area to add
	 */
	public void setTextArea(ScrollingWidget scrollingTextArea) {
		scrollingTextArea.getVerticalScrollBar().addComponentListener(this);
		splitter.setLeftComponent(scrollingTextArea);
	}
	
	/**
	 * Adds the Table Area to the main panel
	 * @param tableArea
	 * the Table Area to add
	 */
	public void setTableArea(TableArea tableArea){
		splitter.setRightComponent(tableArea);
	}
	
	private void initHorizontalLine(){
		String operatingSys = System.getProperty("os.name").toLowerCase();
		String line1 = "-----------------------------------------------------";
		String line2 = "-----------------------------------------------------------------------------------------------------------";    
			
		if (operatingSys.indexOf("mac") >= 0){
			horizontalLine = line1;
			splitter.setDividerSize(5);
		}
		else{
			horizontalLine = line2;
			splitter.setDividerSize(7);
		}
	}
	
	private TableArea getTableArea(){
		Object splitterObj = WidgetAnalyzer.getWidgetInFrame(this, "splitter");
		
		if (splitterObj != null){	
			TableArea tableArea = (TableArea) WidgetAnalyzer.getWidget((JSplitPane) splitterObj, "tableDisplayArea");
			return tableArea;
		}

		return null;
	}
	
	/**
	 * Gets the Text Display Area widget
	 * @return
	 * the Text Display Area widget
	 */
	public TextDisplayArea getTextDisplayArea(){
		Object splitterObj = WidgetAnalyzer.getWidgetInFrame(this, "splitter");
		
		if (splitterObj != null){		
			ScrollingWidget scrollingTextArea = (ScrollingWidget) WidgetAnalyzer.getWidget((JSplitPane) splitterObj, "textDisplayArea");
			return (TextDisplayArea) scrollingTextArea.getTextAreaInstance();
		}
		
		return null;
	}
	
	/**
	 * Gets a table widget located in the Table Area
	 * @param tableName
	 * name of the required table
	 * @return
	 * the required table widget
	 */
	public TableWidget getTableWidget(String tableName){
		Object tableObj = WidgetAnalyzer.getWidget(getTableArea(), tableName);
		
		if (tableObj == null){
			return null;
		}
		
		ScrollingWidget tableScroller = (ScrollingWidget) tableObj;
		return tableScroller.getTableWidgetInstance();
	}
	
	/**
	 * Adds the status bar to the main panel 
	 * @param statusBar
	 * status bar widget
	 */
	public void setStatusBar(StatusBar statusBar){
		statusPanelLayout.setHorizontalGroup(
			    statusPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			    .addComponent(statusBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			);
			statusPanelLayout.setVerticalGroup(
			    statusPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			    .addComponent(statusBar, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
			);
	}
	
	/**
	 * Sets specified message in the Status bar
	 * @param message
	 * message or text
	 */
	public void setStatusMessage(String message) {
		StatusBar statusBar = (StatusBar) getStatusBar("status");
		if (statusBar != null) {
			statusBar.setStatusText(message);
		}
	}
		
	/**
	 * Gets status bar widget
	 * @param name
	 * 
	 * @return
	 */
	private Component getStatusBar(String name){
		Component[] widgets = statusPanel.getComponents();
		for (Component widget : widgets){
			if (widget.getName().equals(name)){
				return widget;
			}
		}
		return null;
	}
	
	/**
	 * Refreshes the tables and updates the Table Area
	 * @param tableName
	 * table name
	 * @param isShown
	 * true or false (i.e. it determines whether a table should be shown or not)
	 */
	public void updateTableArea(String tableName, boolean isShown){
		TableArea tableArea = getTableArea();
		if (isShown){			
			if (!tableArea.searchForTable(tableName)){
				tableArea.addTable(tableName);
			}
			else{				
				tableArea.refreshTable(tableName);
			}
		}
		else{
			tableArea.deleteTab(tableName);			
		}
		tableArea.revalidateTopTable();
	}
	
	/**
	 * Enables or disables a menu item
	 * @param name
	 * name of the menu item
	 * @param enable
	 * true or false
	 */
	public void setMenuItemEnabled(String name, boolean enable){
		int numOfMenus = mainMenu.getMenuCount();

		for (int i = 0; i < numOfMenus; i++) {
			int index = WidgetAnalyzer.getMenuItemIndex(mainMenu.getMenu(i),
					name);
			if (index > -1) {
				mainMenu.getMenu(i).getMenuComponent(index).setEnabled(enable);
				break;
			}
		}
	}
	
	/**
	 * Appends a horizontal line to the bottom of the Text Display Area
	 */
	public void appendHorizontalLineToTextArea() {
		TextDisplayArea textArea = getTextDisplayArea();		
		textArea.append(horizontalLine);
		textArea.append("\n");
	}
	
	/**
	 * Checks if horizontal line is the last string displayed in the Text Area.
	 * @return
	 * true if horizontal line is the last string displayed; false, if otherwise.
	 */
	protected boolean horizontalLineIsLastText(){
		TextDisplayArea textArea = getTextDisplayArea();
		String[] linesOfText = textArea.getText().split("\\n");
		
		if (linesOfText[linesOfText.length - 1].equals(horizontalLine))
			return true;
		else
			return false;		
	}
	
	

	/**
	 * Called when a component is resized
	 * @param e
	 * Component Event object
	 */
	public void componentResized(ComponentEvent e) {
		if (!(splitter.getDividerLocation() > 450))
			splitter.setDividerLocation(450);
	}

	/**
	 * Called when a component is hidden
	 * @param e
	 * Component Event object
	 */
	public void componentHidden(ComponentEvent e) {
		if (e.getComponent() instanceof JScrollBar)
			splitter.setDividerLocation(450);
	}
	
	/**
	 * Called when a component is moved
	 * @param e
	 * Component Event object
	 */
	public void componentMoved(ComponentEvent e) {}
	
	/**
	 * Called when a component is shown
	 * @param e
	 * Component Event object
	 */
	public void componentShown(ComponentEvent e) {
		if (e.getComponent() instanceof JScrollBar)
			splitter.setDividerLocation(470);
	}

}
