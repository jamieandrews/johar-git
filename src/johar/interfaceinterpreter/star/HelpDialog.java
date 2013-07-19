package johar.interfaceinterpreter.star;

import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import johar.idf.Idf;
import johar.idf.IdfCommand;
import johar.idf.IdfParameter;
import johar.idf.IdfQuestion;

/**
 * This class creates a Help Box with three states: the Top-Level state, 
 * the Command state, and the Parameter/Question state. 
 */
public class HelpDialog extends JFrame {
	private Container container;
	private Idf _idf;
	private CommandController _cc;
	private IdfAnalyzer idfAnalyzer;
	private HelpContents contents;
	private JTextArea multiLineBox;
	private ScrollingWidget multiLineScroll;
	private JPanel titlePanel;
	private JSplitPane splitter;
	
	/**
	 * The class' constructor
	 * @param idf
	 * the IDF object
	 * @param cc
	 * the Command Controller object
	 */
	public HelpDialog(Idf idf, CommandController cc) {
		_idf = idf;
		_cc = cc;		
		idfAnalyzer = new IdfAnalyzer(idf); /* Provides access to various information about the specified IDF 
		 									* e.g. list of queryable/non-queryable params in a command/stage, 
		 									* list of questions in a command, list of tables, 
		 									* list of browsable tables, etc. */
		contents = new HelpContents(_idf, _cc);	 //Get the tables
		
		try {
			initDialog();
		} catch (Exception e) {
			MessageDialog.showError("An error occurred while performing the requested operation. [Error Details: "
					+ e.getMessage() + "]");
		}
	}
	
	//Creates and shows the Dialog Box
	private void initDialog(){
		container = getContentPane();
		container.setLayout(new BorderLayout());
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLocation(300, 30);
		setTitle("Help");
		setName("help");		
		setSize(new Dimension(700, 600));
		showTopLevelState();
		setVisible(true);
	}
	
	//Sets the title of all the three states
	private void setTitle(int index, String title){		
		int compIndex = getCompIndex("Title" + index);
		if (compIndex >= 0){
			JPanel titlePan = (JPanel) container.getComponent(compIndex);
			((JLabel) titlePan.getComponent(0)).setText(title);
		}
		else {
			titlePanel = new JPanel();
			titlePanel.setLayout(new BorderLayout());
			JLabel titleLabel = new JLabel(title);
			titlePanel.setName("Title" + index);
			titlePanel.setBackground(new Color(235, 235, 235));
			titlePanel.add(titleLabel, BorderLayout.NORTH);
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
			titleLabel.setFont(new Font(null,Font.BOLD,13));		
			container.add(titlePanel, BorderLayout.NORTH);
		}
	}
	
	//Creates the scrollable text area, and sets its content to the MultiLineHelp
	private void setMultiLineText(String name, String text, String textHeader){
		int compIndex = getCompIndex(name);
		if (compIndex >= 0){
			JPanel titlePan = (JPanel) container.getComponent(compIndex);
			if (WidgetAnalyzer.getWidgetIndex(titlePan, "multiLineBox") == -1){
				multiLineBox = new JTextArea();
				multiLineBox.setText(text);
				multiLineBox.setEditable(false);
				multiLineBox.setWrapStyleWord(true);
				multiLineBox.setLineWrap(true);
				multiLineScroll = new ScrollingWidget(multiLineBox);
				multiLineScroll.setName("multiLineBox");
				
				if (name.equals("Title1")){
					titlePan.setPreferredSize(new Dimension(700, 200));
					titlePan.add(multiLineScroll);
				}
				else{
					JLabel titleLabel = new JLabel(textHeader);
					multiLineScroll.setPreferredSize(new Dimension(700, getPreferredSize().height - 460));
					titlePan.add(titleLabel, BorderLayout.CENTER);	
					titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
					titleLabel.setFont(new Font(null,Font.ITALIC,13));
					titlePan.add(multiLineScroll, BorderLayout.SOUTH);
				}	
			}
			else
				((ScrollingWidget) titlePan.getComponent(1)).getTextAreaInstance().setText(text);
		}
	}
		
	/**
	 * Shows the Top Level State
	 */
	public void showTopLevelState(){
		setTitle(0, "Commands");
		
		if (getCompIndex("topLevelHelp") == -1)
			container.add(contents.getTopLevelStateTable());
		
		if (!(getCompIndex("buttonsPanel") >= 0)) 
			addButtons();
		
		setCompVisible("topLevelHelp", true);
		deleteComponent("paramsTables");
		setTitleVisible("Title0", true);
		deleteComponent("Title1");
		deleteComponent("Title2");
		setButtonVisible("back", false);	
		
		validate();
		repaint();
	}
	
	/**
	 * Shows the Command State for the selected command
	 * @param command
	 * command selected
	 */
	public void showCommandState(IdfCommand command){
		String commandGrp = "";
		try {
			commandGrp = idfAnalyzer.getCommandGroup(command.getCommandName()).getLabel();
		} catch (Exception e) {}
		
		setTitle(1, commandGrp + " \u2192 " + command.getLabel());
		setMultiLineText("Title1", command.getMultiLineHelp(), "");
		
		JPanel questionPanel = new JPanel();
		questionPanel.setLayout(new BorderLayout());
		JLabel titleLabel = new JLabel("Questions that might be asked:");
		titleLabel.setFont(new Font(null,Font.PLAIN,14));
		questionPanel.setBackground(new Color(240, 240, 240));
		questionPanel.setName("questionsHelp");
		questionPanel.add(titleLabel, BorderLayout.NORTH);
		questionPanel.add(contents.getCommandStateTableQues(command));
		
		if (getCompIndex("paramsTables") == -1){
			splitter = new JSplitPane();
			splitter.setDividerSize(5);
			splitter.setOneTouchExpandable(true);
			splitter.setOrientation(JSplitPane.VERTICAL_SPLIT);
			
			splitter.setDividerLocation(250);
			splitter.setName("paramsTables");
			splitter.setLeftComponent(contents.getCommandStateTableParams(command));
			splitter.setRightComponent(questionPanel);
			container.add(splitter);
		}		
		
		deleteComponent("topLevelHelp");
		setCompVisible("paramsTables", true);
		deleteComponent("Title0");
		setTitleVisible("Title1", true);
		deleteComponent("Title2");
		setButtonVisible("back", true);
				
		validate();
		repaint();
	}
	
	/**
	 * Shows the Parameter State for the selected parameter
	 * @param command
	 * current command
	 * @param param
	 * selected parameter
	 * @param isQuestion
	 * true, if param is a Question; false, if param is a Parameter
	 */
	public void showParameterState(IdfCommand command, IdfParameter param, boolean isQuestion){
		String commandGrp = "";
		
		commandGrp = idfAnalyzer.getCommandGroup(command.getCommandName()).getLabel();
				
		setTitle(2, commandGrp + " \u2192 " + command.getLabel());
		
		if (!isQuestion)
			setMultiLineText("Title2", param.getMultiLineHelp(), "Parameter: " + param.getLabel());
		else
			setMultiLineText("Title2", param.getMultiLineHelp(), "Question: " + param.getLabel());
		
		deleteComponent("topLevelHelp");
		deleteComponent("paramsTables");
		deleteComponent("Title0");
		deleteComponent("Title1");
		setTitleVisible("Title2", true);
		setButtonVisible("back", true);
		
		validate();
		repaint();
	}
	
	/**
	 * Gets the table of commands displayed in the Top Level State
	 * @return
	 * the table object
	 */
	public TableWidget getTopLevelTable(){
		int compIndex = getCompIndex("topLevelHelp");
		if (compIndex >= 0){
			return ((ScrollingWidget) container.getComponent(compIndex)).getTableWidgetInstance();
		}
		return null;
	}
	
	/**
	 * Gets the table of parameters displayed in the Command State
	 * @return
	 * the table object
	 */
	public TableWidget getParamsTable(){
		int compIndex = getCompIndex(splitter, "paramsHelp");
		if (compIndex >= 0){
			return ((ScrollingWidget) splitter.getComponent(compIndex)).getTableWidgetInstance();
		}
		return null;
	}
	
	/**
	 * Gets the table of questions displayed in the Command State
	 * @return
	 * the table object
	 */
	public TableWidget getQuestionsTable(){
		int compIndex = getCompIndex(splitter, "questionsHelp");
		if (compIndex >= 0){
			JPanel quesPanel= (JPanel) splitter.getComponent(compIndex);
			return ((ScrollingWidget) quesPanel.getComponent(1)).getTableWidgetInstance();
		}
		return null;
	}
	
	/**
	 * Gets the selected row index from the commands table in the Top Level State
	 * @return
	 * the index of the selected row
	 */
	public int getTopLevelTableSelectedRow(){
		TableWidget table = getTopLevelTable();
		if (table != null)
			return table.getSelectedRow();
		else
			return -1;
	}	
	
	/**
	 * Gets the selected row index from the parameters table in the Command State
	 * @return
	 * the index of the selected row
	 */
	public int getParamsTableSelectedRow(){
		TableWidget table = getParamsTable();
		if (table != null)
			return table.getSelectedRow();
		else
			return -1;
	}
	
	/**
	 * Gets the selected row index from the questions table in the Command State
	 * @return
	 * the index of the selected row
	 */
	public int getQuestionsTableSelectedRow(){
		TableWidget table = getQuestionsTable();
		if (table != null)
			return table.getSelectedRow();
		else
			return -1;
	}

	//Deletes the specified component from the main container
	private void deleteComponent(String name){
		int compIndex = getCompIndex(name);
		if (compIndex >= 0){
			container.remove(compIndex);
		}		
	}
	
	//Makes the specified component to be visible or not visible
	private void setCompVisible(String name, boolean isVisible){
		int compIndex = getCompIndex(name);
		if (compIndex >= 0){
			container.getComponent(compIndex).setVisible(isVisible);
		}		
	}
	
	//Makes the specified button to be visible or not visible
	private void setButtonVisible(String name, boolean isVisible){
		int compIndex = getCompIndex("buttonsPanel");
		if (compIndex >= 0){
			((JPanel) container.getComponent(compIndex)).getComponent(0).setVisible(isVisible);
		}		
	}
	
	//Makes the title visible or not visible
	private void setTitleVisible(String name, boolean isVisible){
		int compIndex = getCompIndex(name);
		if (compIndex >= 0){
			((JPanel) container.getComponent(compIndex)).setVisible(isVisible);
		}		
	}
	
	//Get the index of the specified component in the main container
	private int getCompIndex(String name){
		return WidgetAnalyzer.getWidgetIndex((JComponent) container, name);
	}
	
	//Get the index of the specified component in a particular container
	private int getCompIndex(JComponent comp, String name){
		return WidgetAnalyzer.getWidgetIndex(comp, name);
	}
	
	//Adds buttons to the main container
	private void addButtons(){
		JPanel buttonsPanel = new JPanel();
		
		buttonsPanel.setBorder(new EmptyBorder(10, 10, 5, 10));
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		buttonsPanel.setName("buttonsPanel");
				
		HelpBackButton backButton = new HelpBackButton(_cc);
		backButton.setVisible(false);
		buttonsPanel.add(backButton);		
		
		HelpOKButton okButton = new HelpOKButton(_cc);
		
		buttonsPanel.add(Box.createHorizontalGlue());
		buttonsPanel.add(okButton);		
		container.add(buttonsPanel, BorderLayout.SOUTH);
		
	}
}
