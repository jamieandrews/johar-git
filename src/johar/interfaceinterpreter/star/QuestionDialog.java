/*
 * Creates the Command Window
 */

package johar.interfaceinterpreter.star;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.awt.Dialog;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import johar.idf.Idf;
import johar.idf.IdfQuestion;

/**
 * The Question Dialog Box for each question attribute in the IDF.
 *
 */
public class QuestionDialog extends JDialog implements WindowListener {
	private IdfQuestion _questionObj;
	private CommandController _cc;
	private JPanel mainPanel;
	private Container container;
	private GridBagConstraints constraints;
	private ParameterWidget paramWidget;
	private Object _defaultValue;
	private List<ParameterWidget> paramWidgetList;
	private IdfAnalyzer idfAnalyzer;
	
	/**
	 * 
	 * @param defaultValue
	 * default value of the Question attribute
	 * @param cc
	 * the Command Controller object
	 * @param questionObj
	 * IdfQuestion object
	 */
	public QuestionDialog(Object defaultValue, CommandController cc, Idf idf, IdfQuestion questionObj) {
		_questionObj = questionObj;
		_cc = cc;
		_defaultValue = defaultValue;
		paramWidgetList = new ArrayList<ParameterWidget>();		
		idfAnalyzer = new IdfAnalyzer(idf); /* Provides access to various information about the specified IDF 
											* e.g. list of queryable/non-queryable params in a command/stage, 
											* list of questions in a command, list of tables, 
											* list of browsable tables, etc. */
	}
	
	//Initialize the GUI components
	private void initialize() {
		mainPanel = new JPanel();
		container = getContentPane();
		container.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		mainPanel.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();

		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocation(300, 90);
		setResizable(false);

		container.add(mainPanel);
		addWindowListener(this);
	}

	/**
	 * Creates and displays the dialog box.
	 */
	public void showDialog(){
		try {
			initialize();
			_questionObj.getLabel();
			
			createWidgetQuestion();
			addOKCancelButtons();		
			setTitle("Question");
			pack();
			setVisible(true);
		} catch (Exception e) {
			MessageDialog.showError("An error occurred while performing the requested operation. [Error Details: "
					+ e.getMessage() + "]");
		}
	}	
	
	//Creates the widget for the question
	private void createWidgetQuestion(){		
		constraints.gridx = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridy = 0;
		
		//Creates a Label for the Question Dialog
		JLabel questionLabel = new JLabel();
		
		if (_questionObj.getLabel().length() > 90)
			questionLabel.setText("<html><body><p style='width: 400px'>" + _questionObj.getLabel());
		else
			questionLabel.setText(_questionObj.getLabel());
		
		mainPanel.add(questionLabel, constraints);
		
		//Creates and stores the widgets
		String type = _questionObj.getType();		
		if (type.equals("text"))
			addTextField();
		else if (type.equals("int") || type.equals("float"))
			addNumberField();
		else if (type.equals("boolean"))
			addBooleanField();
		else if (type.equals("choice"))
			addChoiceField();
		else if (type.equals("timeOfDay"))
			addTimeField();
		else if (type.equals("date"))
			addDateField();
		else if (type.equals("file"))
			addFileField();
		else if (type.equals("tableEntry") && !idfAnalyzer.getTable(_questionObj.getSourceTable()).getBrowsable())
			addTableEntryField();

	}	

	//The position of the question widget
	private void initPosition(){
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy = 1;
		constraints.insets = new Insets(5,0,0,0);
	}
	
	//Creates widget for question of type "text"
	private void addTextField(){
		constraints.gridy = 1;
		constraints.insets = new Insets(5,0,0,0);
		
		paramWidget = new TextWidget(_defaultValue, _questionObj, _cc);
		paramWidgetList.add(paramWidget);				//Add to Map
		mainPanel.add(paramWidget, constraints);		
	}
	
	//Creates widget for question of type "int" or "float"
	private void addNumberField(){
		initPosition();
		
		paramWidget = new NumberWidget(_defaultValue, _questionObj, _cc);
		paramWidgetList.add(paramWidget);			//Add to Map
		mainPanel.add(paramWidget, constraints);
	}
	
	//Creates widget for question of type "boolean"
	private void addBooleanField(){
		initPosition();
		
		paramWidget = new BooleanWidget(_defaultValue, _questionObj, _cc);
		paramWidgetList.add(paramWidget);			//Add to Map
		mainPanel.add(paramWidget, constraints);
	}
	
	//Creates widget for question of type "choice"
	private void addChoiceField(){		
		initPosition();
		
		paramWidget = new ChoiceWidget(_defaultValue, _questionObj, _cc);
		paramWidgetList.add(paramWidget);			//Add to Map
		mainPanel.add(paramWidget, constraints);
	}
	
	//Creates widget for question of type "timeOfDay"
	private void addTimeField(){
		initPosition();
		
		paramWidget = new TimeWidget(_defaultValue, _questionObj, _cc);
		paramWidgetList.add(paramWidget);			//Add to Map
		mainPanel.add(paramWidget, constraints);
	}
	
	//Creates widget for question of type "date"
	private void addDateField(){
		initPosition();
		
		paramWidget = new DateWidget(_defaultValue, _questionObj, _cc);
		paramWidgetList.add(paramWidget);			//Add to Map
		mainPanel.add(paramWidget, constraints);
	}
	
	//Creates widget for question of type "file"
	private void addFileField(){		
		initPosition();
		
		paramWidget = new FileWidget(_defaultValue, _questionObj, _cc);
		paramWidgetList.add(paramWidget);				//Add to Map
		mainPanel.add(paramWidget, constraints);
	}
	
	//Creates widget for question of type "tableEntry", whose SourceTable is non-browsable
	private void addTableEntryField() {
		initPosition();
		
		paramWidget = new TableEntryWidget(_defaultValue, _questionObj, _cc);
		paramWidgetList.add(paramWidget);				//Add to Map
		mainPanel.add(paramWidget, constraints);
	}
	
	//Creates the buttons
	private void addOKCancelButtons(){
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(15,0,0,0);
		constraints.gridy = 2;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = GridBagConstraints.WEST;
		
		QuestionCancelButton cancelButton = new QuestionCancelButton(_cc, _questionObj.getParameterName());
		mainPanel.add(cancelButton, constraints);

		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridy = 2;
		
		QuestionOKButton okButton = new QuestionOKButton(_cc, _questionObj.getParameterName());		
		mainPanel.add(okButton, constraints);
	}	
	
	/**
	 * Gets the Question Widget
	 * @return
	 * the map containing parameter widget
	 */
	public List<ParameterWidget> getQuestionWidget(){
		return paramWidgetList;
	}

	public void windowClosed(WindowEvent e) {
		dispose();		
	}

	public void windowActivated(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {}	
	public void windowDeactivated(WindowEvent e) {}	
	public void windowDeiconified(WindowEvent e) {}	
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}

