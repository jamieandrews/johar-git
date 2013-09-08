package johar.interfaceinterpreter.star;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerDateModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import org.jdesktop.swingx.JXDatePicker;

import johar.idf.IdfParameter;

/**
 * This class creates widget for each queryable parameter. This class is inherited by 
 * the BooleanWidget, ChoiceWidget, DateWidget, FileWidget, NumberWidget, TableEntryWidget, 
 * TextWidget, and TimeWidget classes. 
 *
 */
public class ParameterWidget extends JPanel {
	private long _minNumOfReps;
	private long _maxNumOfReps;
	private String _paramName;
	private String _paramType;
	private String _repModel;
	private MoveUpButton upButton;
	private MoveDownButton downButton;
	private DeleteButton delButton;
	private CommandController _cc;
	private GridBagConstraints constraints;
	private GridBagLayout layout;
	private AddMoreButton moreButton;
	private List<JComponent> fieldList;
	private Object _defaultValue;
	private JFileChooser fileChooser;
	private JTextField fileNameField;
	private JButton browseButton;
	private JRadioButton yesButton;
	private JRadioButton noButton;
	private ButtonGroup buttonsGroup;
	private String _fieldPrefix1;
	private String _fieldPrefix2;
	private IdfParameter _paramObj;
	private JTextField currentTextField;
	private JTextArea currentTextField2;
	private ScrollingWidget scrollableTextArea;
	private JFormattedTextField currentNumberField;
	private JComboBox currentChoiceField;
	private SpinnerDateModel currentDateModel;
	private JXDatePicker currentDateUI;
	private JSpinner currentSpinner;
	private long _maxNumOfChars;
	private long _maxNumOfLines;
	private JComboBox currentTableEntryField;
	private TreeMap<Integer, List<Object>> locationMap;
	
	/**
	 * The class' constructor
	 * @param defaultValue
	 * default value of the parameter
	 * @param paramObj
	 * IdfParameter object of the parameter
	 * @param cc
	 * the Command Controller object
	 */
	public ParameterWidget(Object defaultValue, IdfParameter paramObj, CommandController cc) {
		_minNumOfReps = paramObj.getMinNumberOfReps();
		_maxNumOfReps = paramObj.getMaxNumberOfReps();			
		_defaultValue = defaultValue;
		_paramName = paramObj.getParameterName();
		_repModel = paramObj.getRepsModel();
		_paramType = paramObj.getType();
		_paramObj = paramObj;
		_cc = cc;
		
		try {
			createWidget();
		} catch (Exception e) {
			 MessageDialog.showError("An error occurred while performing the requested operation. [Error Details: "
						+ e.getMessage() + "]");
		}
	}
	
	//Create the widget based on the minimum number of repetitions
	private void createWidget() {
		layout = new GridBagLayout();
		
		setLayout(layout);
		if (_maxNumOfReps > 1)
			setBorder(BorderFactory.createEtchedBorder());
		
		int xPosition = 0;
		constraints = new GridBagConstraints();
		constraints.gridx = xPosition;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;		
		constraints.gridy = 0;
		constraints.insets = new Insets(5,0,0,0);
		
		createField(0, true, "");
		if (fieldList.size() == 1){
			addToPanel(fieldList.get(0), constraints);
		}
		else{
			if (fieldList.size() > 1){
				addToPanel(fieldList.get(0), constraints);
				for (int k = 1; k < fieldList.size(); k++){	
					xPosition = xPosition + k;
					constraints.gridx = xPosition;
					if (_paramType.equals("boolean"))
						constraints.insets = new Insets(5,10,0,5);
					else
						constraints.insets = new Insets(5,0,0,5);
					addToPanel(fieldList.get(k), constraints);
				}
			}
		}	
		
		if (_maxNumOfReps > 1 && _repModel.equals("sequence")){			
			upButton = new MoveUpButton(_cc, _paramName, 0);
			downButton = new MoveDownButton(_cc, _paramName, 0);
			upButton.setName("btnUp" + _paramName + 0);
			downButton.setName("btnDown" + _paramName + 0);
			
			xPosition++;
			constraints.gridx = xPosition;
			constraints.insets = new Insets(5,0,0,0);	
			addToPanel(upButton, constraints);
			
			xPosition++;
			constraints.gridx = xPosition;
			constraints.insets = new Insets(5,0,0,0);
			upButton.setEnabled(false);
			downButton.setEnabled(false);
			addToPanel(downButton, constraints);			
		}
			
		if (_minNumOfReps != _maxNumOfReps){
			delButton = new DeleteButton(_cc, _paramName, 0);
			delButton.setName("btnDel" + _paramName + 0);
						
			xPosition++;
			constraints.gridx = xPosition;
			constraints.insets = new Insets(5,0,0,0);
			
			addToPanel(delButton, constraints);
		}
		
		if (_minNumOfReps > 1){	
			setEnabledDownButton("btnDown" + _paramName + 0, true);
			for (int i = 1; i < _minNumOfReps; i++){
				xPosition = 0;
				constraints.gridx = xPosition;
				constraints.gridy = i;
				constraints.insets = new Insets(5,0,0,0);
				
				createField(i, true, "");
				if (fieldList.size() == 1){
					addToPanel(fieldList.get(0), constraints);
				}
				else{
					if (fieldList.size() > 1){
						addToPanel(fieldList.get(0), constraints);
						for (int k = 1; k < fieldList.size(); k++){	
							xPosition = xPosition + k;
							constraints.gridx = xPosition;
							if (_paramType.equals("boolean"))
								constraints.insets = new Insets(5,10,0,5);
							else
								constraints.insets = new Insets(5,0,0,5);
							addToPanel(fieldList.get(k), constraints);
						}
					}
				}
				
				constraints.weightx = 0.0;
				
				if (_repModel.equals("sequence")){
					upButton = new MoveUpButton(_cc, _paramName, i);
					downButton = new MoveDownButton(_cc, _paramName, i);					
					
					upButton.setName("btnUp" + _paramName + i);
					downButton.setName("btnDown" + _paramName + i);
						
					if (i == _minNumOfReps-1){
						downButton.setEnabled(false);
					}
						
					xPosition++;
					constraints.gridx = xPosition;
					constraints.insets = new Insets(5,0,0,0);	
					addToPanel(upButton, constraints);
						
					xPosition++;
					constraints.gridx = xPosition;
					constraints.insets = new Insets(5,0,0,0);
					addToPanel(downButton, constraints);
				}
				
				if (_minNumOfReps != _maxNumOfReps) {
					delButton = new DeleteButton(_cc, _paramName, i);
					delButton.setName("btnDel" + _paramName + i);

					xPosition++;
					constraints.gridx = xPosition;
					constraints.insets = new Insets(5, 0, 0, 0);

					addToPanel(delButton, constraints);
				}
			}
		}
		
		if (_minNumOfReps < _maxNumOfReps){
			moreButton = new AddMoreButton(_cc, _paramName);
			moreButton.setName("btnMore" + _paramName);
			constraints.gridx = 0;
			constraints.gridy = (int) _maxNumOfReps;
			constraints.insets = new Insets(5,0,0,0);
			addToPanel(moreButton, constraints);			
		}
		
		if (getFieldsCount() == _minNumOfReps){
			setEnabledDeleteButton(false);
		}
		
		repaint();
	}

	//This method carry out the main task of creating the required widget
	private void createField(int repNumber, boolean dvFlag, Object value) {
		fieldList = new ArrayList<JComponent>();
		
		if (_paramType.equals("file")){
			fieldList = setupFileField(repNumber, dvFlag, value.toString());
			_fieldPrefix1 = "txt";
			_fieldPrefix2 = "btnBrowse";
		}
		else if (_paramType.equals("boolean")){
			fieldList = setupBooleanField(repNumber, dvFlag);
			_fieldPrefix1 = "rdoYes";
			_fieldPrefix2 = "rdoNo";
		}
		else if (_paramType.equals("text")){
			fieldList = setupTextField(repNumber, dvFlag, value.toString());
			_fieldPrefix1 = "txt";
			_fieldPrefix2 = "";
		}
		else if (_paramType.equals("int") || _paramType.equals("float") ){
			fieldList = setupNumberField(repNumber, dvFlag, value.toString());
			_fieldPrefix1 = "txt";
			_fieldPrefix2 = "";
		}
		else if (_paramType.equals("choice")){
			fieldList = setupChoiceField(repNumber, dvFlag, value);
			_fieldPrefix1 = "cbo";
			_fieldPrefix2 = "";
		}
		
		else if (_paramType.equals("timeOfDay")){
			fieldList = setupTimeField(repNumber, dvFlag, value);
			_fieldPrefix1 = "spn";
			_fieldPrefix2 = "";
		}
		
		else if (_paramType.equals("date")){
			fieldList = setupDateField(repNumber, dvFlag, value);
			_fieldPrefix1 = "dte";
			_fieldPrefix2 = "";
		}
		
		else if (_paramType.equals("tableEntry")){
			fieldList = setupTableEntryField(repNumber, dvFlag, value);
			_fieldPrefix1 = "tbe";
			_fieldPrefix2 = "";
		}
	}
	
	private void addToPanel(JComponent comp, GridBagConstraints cons, int position){
		if (_paramType.equals("text")){
			if (comp instanceof JTextField || comp instanceof ScrollingWidget){
				if (_maxNumOfLines == 1)
					add(currentTextField, cons, position);
				else
					add(scrollableTextArea, cons, position);
			}
			else
				add(comp, cons, position);			
		}
		else
			add(comp, cons, position);
	}
	
	//Adds the created field to the widget's panel
	private void addToPanel(JComponent comp, GridBagConstraints cons){
		if (_paramType.equals("text")){
			if (comp instanceof JTextField || comp instanceof ScrollingWidget){
				if (_maxNumOfLines == 1)
					add(currentTextField, cons);
				else
					add(scrollableTextArea, cons);
			}
			else
				add(comp, cons);
		}
		else
			add(comp, cons);
	}	

	/**
	 * When the Add Another button is clicked, a new field for the repetition is added.
	 * @param repNumber
	 * repetition number
	 */
	public void addNewField(int repNumber){
		int position = getComponentCount();
		int xPosition = 0;
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;				
		constraints.anchor = GridBagConstraints.WEST;
		constraints.weightx = 1.0;
		constraints.gridx = xPosition;
		constraints.gridy = repNumber;
		constraints.insets = new Insets(5,0,0,0);		
		
		createField(repNumber, true, "");
		if (fieldList.size() == 1){
			addToPanel(fieldList.get(0), constraints, position - 1);
		}
		else{
			if (fieldList.size() > 1){
				int position2 = position - 1;
				addToPanel(fieldList.get(0), constraints, position2);				
				for (int k = 1; k < fieldList.size(); k++){	
					xPosition = xPosition + k;
					constraints.gridx = xPosition;
					if (_paramType.equals("boolean"))
						constraints.insets = new Insets(5,10,0,5);
					else
						constraints.insets = new Insets(5,0,0,5);
					addToPanel(fieldList.get(k), constraints, position2 + k);
				}
			}
		}
		
		if (_repModel.equals("sequence")){
			upButton = new MoveUpButton(_cc, _paramName, repNumber);
			downButton = new MoveDownButton(_cc, _paramName, repNumber);					
			
			upButton.setName("btnUp" + _paramName + repNumber);
			downButton.setName("btnDown" + _paramName + repNumber);

			downButton.setEnabled(false);
			
			setEnabledDownButton("btnDown" + _paramName + (repNumber - 1), true);
			
			if (getFieldsCount() == 1)
				upButton.setEnabled(false);
			
			xPosition++;
			constraints.gridx = xPosition;
			constraints.insets = new Insets(5,0,0,0);	
			addToPanel(upButton, constraints, position + (xPosition - 1));
			
			xPosition++;
			constraints.gridx = xPosition;
			constraints.insets = new Insets(5,0,0,0);
			addToPanel(downButton, constraints, position + (xPosition - 1));
		}
		
		if ((repNumber + 1) >= _minNumOfReps){
			delButton = new DeleteButton(_cc, _paramName, repNumber);
			delButton.setName("btnDel" + _paramName + repNumber);
			
			xPosition++;
			constraints.gridx = xPosition;
			constraints.insets = new Insets(5,0,0,0);
			
			if (_repModel.equals("sequence"))
				addToPanel(delButton, constraints, position + (xPosition - 1));
			else
				addToPanel(delButton, constraints, position + (xPosition - 3));
		}
		
		if ((repNumber + 1) == _maxNumOfReps){
			removeAddMoreButton("btnMore" + _paramName);
		}
		
		if (getFieldsCount() > _minNumOfReps){
			setEnabledDeleteButton(true);
		}
		
		revalidate();
		repaint();
	}
	
	/**
	 * This method creates a field for the specified repetition and then inserts it at a particular position in the panel.
	 * @param repNumber
	 * repetition number
	 * @param position
	 * position (base index of 0) to insert field in the parameter widget 
	 * @param initValue
	 * initial value to display in the field
	 * @param enableDelete
	 * "true" means the field should have the delete button enabled, while "false" means otherwise
	 */
	public void insertField(int repNumber, int position, Object initValue, boolean enableDelete){
		int xPosition = 0;
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;			
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = xPosition;
		constraints.weightx = 1.0;
		constraints.gridy = repNumber;
		constraints.insets = new Insets(5,0,0,0);		
		
		createField(repNumber, false, initValue);
		if (fieldList.size() == 1){
			addToPanel(fieldList.get(0), constraints, position);
		}
		else{
			if (fieldList.size() > 1){
				int position2 = position;
				addToPanel(fieldList.get(0), constraints, position2);				
				for (int k = 1; k < fieldList.size(); k++){		
					xPosition = xPosition + k;
					constraints.gridx = xPosition;
					if (_paramType.equals("boolean"))
						constraints.insets = new Insets(5,10,0,5);
					else
						constraints.insets = new Insets(5,0,0,5);
					addToPanel(fieldList.get(k), constraints, position2 + k);
				}
			}
		}		
		
		if (_repModel.equals("sequence")){
			upButton = new MoveUpButton(_cc, _paramName, repNumber);
			downButton = new MoveDownButton(_cc, _paramName, repNumber);					
			
			upButton.setName("btnUp" + _paramName + repNumber);
			downButton.setName("btnDown" + _paramName + repNumber);
			
			if ((repNumber + 1) == getFieldsCount() && getFieldsCount() == _minNumOfReps)
				downButton.setEnabled(false);
			else {
				if (getFieldsCount() > _minNumOfReps){
					if ((repNumber + 1) == getFieldsCount()){
						downButton.setEnabled(false);
						setEnabledDownButton("btnDown" + _paramName + (repNumber - 1), true);
					}					
				}
				else if ((repNumber + 1) == _maxNumOfReps){
					downButton.setEnabled(false);
					setEnabledDownButton("btnDown" + _paramName + (repNumber - 1), true);
				}
			}				
			
			if (repNumber == 0)
				upButton.setEnabled(false);
						
			xPosition++;
			constraints.gridx = xPosition;
			constraints.insets = new Insets(5,0,0,0);	
			addToPanel(upButton, constraints, position + xPosition);
			
			xPosition++;
			constraints.gridx = xPosition;
			constraints.insets = new Insets(5,0,0,0);
			addToPanel(downButton, constraints, position + xPosition);
		}
		
		if (_minNumOfReps != _maxNumOfReps){
			delButton = new DeleteButton(_cc, _paramName, repNumber);
			delButton.setName("btnDel" + _paramName + repNumber);
			xPosition++;
			constraints.gridx = xPosition;
			constraints.insets = new Insets(5,0,0,0);
			addToPanel(delButton, constraints, position + xPosition);		
			
			setEnabledDeleteButton("btnDel" + _paramName + repNumber, enableDelete);
		}		
		
	}
	
	//Removes the Add Another button
	private void removeAddMoreButton(String name){
		for (Component c : getComponents()){
			if (c.getName().equals(name)) {
				remove(c);
				break;
			}
		}
	}
	
	//Enable/Disable the Move Down button
	private void setEnabledDownButton(String name, boolean b){
		for (Component c : getComponents()){
			if (c.getName().equals(name)) {
				((MoveDownButton) c).setEnabled(b);
				break;
			}
		}
	}
	
	//Enable/Disable the Move Up button
	private void setEnabledUpButton(String name, boolean b){
		for (Component c : getComponents()){
			if (c.getName().equals(name)) {
				((MoveUpButton) c).setEnabled(b);
				break;
			}
		}
	}
	
	//Enable/Disable the Delete button
	private void setEnabledDeleteButton(boolean b){
		for (Component c : getComponents()){
			if (c instanceof DeleteButton) {
				((DeleteButton) c).setEnabled(b);
			}
		}
	}
	
	//Enable/Disable the Delete button
	private void setEnabledDeleteButton(String name, boolean b){
		for (Component c : getComponents()){
			if (c.getName().equals(name)) {
				((DeleteButton) c).setEnabled(b);
			}
		}
	}
	
	//Create field for a repetition of parameter of type "file"
	private List<JComponent> setupFileField(int repNumber, boolean dvFlag, String value){
		List<JComponent> fieldList = new ArrayList<JComponent>();
		
		fileNameField = new JTextField();
		fileNameField.setPreferredSize(new Dimension(300, 27));
		fileNameField.setEditable(true);	
		fileNameField.setName("txt" + _paramName + repNumber);		
		
		if (dvFlag){
			if (_defaultValue != null && !_defaultValue.toString().trim().equals(""))
				fileNameField.setText(_defaultValue.toString());
		}
		else{
			if (value == null)
				value = "";
			
			fileNameField.setText(value);
		}
		
		browseButton = new JButton("Browse...");
		browseButton.setPreferredSize(new Dimension(90, 27));
		browseButton.setName("btnBrowse" + _paramName + repNumber);
		browseButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION){
					String btnName = ((JButton) e.getSource()).getName();
					String fileFieldName = "txt" + btnName.substring(9);
					JTextField tf = (JTextField) getComponent(fileFieldName);
					tf.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
			
		});
		
		fieldList.add(fileNameField);
		fieldList.add(browseButton);
		
		return fieldList;
	}
	
	//Create field for a repetition of parameter of type "boolean"
	private List<JComponent> setupBooleanField(int repNumber, boolean dvFlag){	
		List<JComponent> fieldList = new ArrayList<JComponent>();
		yesButton = new JRadioButton("Yes");
		yesButton.setName("rdoYes" + _paramName + repNumber);
		noButton = new JRadioButton("No");
		noButton.setName("rdoNo" + _paramName + repNumber);
		buttonsGroup = new ButtonGroup();
		buttonsGroup.add(yesButton);
		buttonsGroup.add(noButton);
		
		if (dvFlag){
			if (_defaultValue != null && !_defaultValue.toString().trim().equals("")){
				if (_defaultValue.toString().toLowerCase().equals("yes") || _defaultValue.toString().toLowerCase().equals("true"))
					yesButton.setSelected(true);
				else if (_defaultValue.toString().toLowerCase().equals("no") || _defaultValue.toString().toLowerCase().equals("false"))
					noButton.setSelected(true);
			}
		}
		
		fieldList.add(yesButton);
		fieldList.add(noButton);
		
		return fieldList;
	}
	
	//Create field for a repetition of parameter of type "text"
	private List<JComponent> setupTextField(int repNumber, boolean dvFlag, String value) {	
		List<JComponent> fieldList = new ArrayList<JComponent>();
		int width;
		_maxNumOfChars = _paramObj.getMaxNumberOfChars();
		_maxNumOfLines = (_paramObj.getMaxNumberOfLines() == Long.MAX_VALUE) ? 1 : _paramObj.getMaxNumberOfLines();
		
		if (_maxNumOfLines == 1){
			currentTextField = new JTextField();
			currentTextField.setName("txt" + _paramName + repNumber);					
			
			if (_maxNumOfChars < Long.MAX_VALUE){
				currentTextField.setDocument(new TextFilter((int) _maxNumOfChars));	
				width = (int)_maxNumOfChars * 9;
				if (width > 400)
					width = 400;
				currentTextField.setPreferredSize(new Dimension(width, 28));
			}
			else
				currentTextField.setPreferredSize(new Dimension(400, 28));
		}
		else{
			currentTextField2 = new JTextArea();
			currentTextField2.setRows((int) _maxNumOfLines);
			
			currentTextField2.setDocument(new PlainDocument(){
					public void insertString(int offset, int length,
							String input, AttributeSet attSet)	throws BadLocationException {	
						
						int numOfLines = currentTextField2.getLineCount();						
						if (numOfLines <= _maxNumOfLines){
							if ((getLength() + input.length()) <= _maxNumOfChars)
								super.insertString(offset, input, attSet);
							else{
								String newInput = input.substring(0, (int) _maxNumOfChars - getLength());
								super.insertString(offset, newInput, attSet);
							}	
						}								
					}
					
					public void replace(int offset, int length,
							String input, AttributeSet attSet)	throws BadLocationException {

						int numOfLines = currentTextField2.getLineCount();						
						if (numOfLines <= _maxNumOfLines){
							if ((getLength() + input.length() - length) <= _maxNumOfChars)
								super.replace(offset, length, input, attSet);
							else{
								String newInput = input.substring(0, (int) _maxNumOfChars - getLength());
								super.insertString(offset, newInput, attSet);
							}
						}
					}
				});	
			
			scrollableTextArea = new ScrollingWidget(currentTextField2);
			scrollableTextArea.setName("txt" + _paramName + repNumber);
			scrollableTextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollableTextArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			int height = (30 * (int) _maxNumOfLines) - (int) (_maxNumOfLines * 2) ;
			if (height > 200)
				height = 200;
			scrollableTextArea.setPreferredSize(new Dimension(400, height));
		}
		
		if (dvFlag){
			if (_defaultValue != null && !_defaultValue.toString().trim().equals("")){
				if (_maxNumOfLines == 1)
					currentTextField.setText(_defaultValue.toString());
				else
					currentTextField2.append(_defaultValue.toString());				
			}
		}
		else{
			if (value != null && !value.trim().equals("")){
				if (_maxNumOfLines == 1)
					currentTextField.setText(value);
				else{
					currentTextField2.append(value);	
				}								
			}
		}
		
		if (_maxNumOfLines == 1)
			fieldList.add(currentTextField);
		else{
			fieldList.add(scrollableTextArea);	
		}
	
		return fieldList;		
	}
	
	//Create field for a repetition of parameter of type "int" or "float"
	private List<JComponent> setupNumberField(int repNumber, boolean dvFlag, String value) {
		List<JComponent> fieldList = new ArrayList<JComponent>();		
		currentNumberField = new JFormattedTextField();
		currentNumberField.setName("txt" + _paramName + repNumber);
		
		int width;
		if (_paramType.equals("int")){
			if (_paramObj.getMaxIntValue() == Long.MAX_VALUE){
				width = 250;
			}
			else
				width = 100;
		}
		else{
			if (_paramObj.getMaxFloatValue() == Double.MAX_VALUE){
				width = 250;
			}
			else
				width = 200;
		}

		currentNumberField.setPreferredSize(new Dimension(width, 28));
		DocumentFilter docFilter = null;
		if (_paramType.equals("int"))
			docFilter = new NumberFilter(true);
		else
			docFilter = new NumberFilter(false);
		
		((AbstractDocument) currentNumberField.getDocument()).setDocumentFilter(docFilter);

		if (dvFlag){
			if (_defaultValue != null && !_defaultValue.toString().trim().equals("")){
				double defaultValFloat;
				long defaultValInt;
				if (_defaultValue.toString().contains(".")){
					defaultValFloat = Double.parseDouble(_defaultValue.toString());
					currentNumberField.setText(String.valueOf(defaultValFloat));
				}
				else if (!_defaultValue.toString().contains(".")){
					defaultValInt = Long.parseLong(_defaultValue.toString());
					currentNumberField.setText(String.valueOf(defaultValInt));
				}		
			}
		}
		else{
			currentNumberField.setText(value);
		}
		
		fieldList.add(currentNumberField);
		
		return fieldList;
	}
	
	//Creates field for a repetition of parameter of type "choice"
	private List<JComponent> setupChoiceField(int repNumber, boolean dvFlag, Object value) {
		List<JComponent> fieldList = new ArrayList<JComponent>();
		currentChoiceField = new JComboBox();
		currentChoiceField.setName("cbo" + _paramName + repNumber);
		String[] choiceArray = _paramObj.getChoices().split("\\|");
		
		for (String choice : choiceArray){
			currentChoiceField.addItem(choice.trim());
		}

		if (dvFlag){
			if (_defaultValue != null && !_defaultValue.toString().trim().equals("")){
				currentChoiceField.setSelectedItem(_defaultValue);
			}
		}
		else{
			currentChoiceField.setSelectedItem(value);
		}
		
		fieldList.add(currentChoiceField);
		
		return fieldList;
	}
	
	//Creates field for a repetition of parameter of type "tableEntry", with a non-browsable source table
	private List<JComponent> setupTableEntryField(int repNumber, boolean dvFlag, Object value) {
		List<JComponent> fieldList = new ArrayList<JComponent>();
		List<String> dataSet = _cc.getTableData(_paramObj.getSourceTable());
		
		NonBrowsableTableModel tableEntryModel = new NonBrowsableTableModel(dataSet);
		currentTableEntryField = new JComboBox(tableEntryModel);
		currentTableEntryField.setName("tbe" + _paramName + repNumber);
		
		if (dvFlag){
			if (_defaultValue != null && !_defaultValue.toString().trim().equals("")){
				currentTableEntryField.setSelectedItem(_defaultValue);
			}
		}
		else{
			currentTableEntryField.setSelectedItem(value);
		}
		
		fieldList.add(currentTableEntryField);
		
		return fieldList;
	}
	
	//Creates field for a repetition of parameter of type "timeOfDay"
	private List<JComponent> setupTimeField(int repNumber, boolean dvFlag, Object value) {
		List<JComponent> fieldList = new ArrayList<JComponent>();		
		currentDateModel = new SpinnerDateModel();
		currentDateModel.setCalendarField(Calendar.MINUTE);

		currentSpinner= new JSpinner();
		currentSpinner.setName("spn" + _paramName + repNumber);
		currentSpinner.setModel(currentDateModel);
		currentSpinner.setPreferredSize(new Dimension(100, 27));
		currentSpinner.setEditor(new JSpinner.DateEditor(currentSpinner, "h:mm a"));
		
		if (dvFlag){
			if (_defaultValue != null && !_defaultValue.toString().trim().equals("")){
				try {
					currentSpinner.setValue(new SimpleDateFormat("h:mm a").parse(_defaultValue.toString().trim()));
				} catch (ParseException e) {
					MessageDialog.showError("An error occurred while performing the requested operation. [Error Details: "
							+ e.getMessage() + "]");
				}
			}
		}
		else{
			if (value != null && !value.toString().trim().equals("")){
				try {
					Object time = formatTime(value);
					currentSpinner.setValue(new SimpleDateFormat("h:mm a").parse(time.toString()));
				} catch (ParseException e) {
					MessageDialog.showError("An error occurred while performing the requested operation. [Error Details: "
							+ e.getMessage() + "]");
				}
			}
		}	
		
		fieldList.add(currentSpinner);
		
		return fieldList;
	}
	
	//Creates field for a repetition of parameter of type "date"
	private List<JComponent> setupDateField(int repNumber, boolean dvFlag, Object value) {
		List<JComponent> fieldList = new ArrayList<JComponent>();		
		String name = "dte" + _paramName + repNumber;
		
		currentDateUI = new JXDatePicker();
		currentDateUI.setName(name);
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		JButton dateButton = (JButton) currentDateUI.getComponent(1);
		dateButton.setIcon(new ImageIcon(getClass().getResource("dateIcon.png")));
		dateButton.setPreferredSize(new Dimension(30, currentDateUI.getPreferredSize().height));
		dateButton.setToolTipText("Select date");
		currentDateUI.setFormats(df);
		
		if (dvFlag){
			if (_defaultValue != null && !_defaultValue.toString().trim().equals("")){
				try {
					currentDateUI.setDate(df.parse(_defaultValue.toString().trim()));
				} catch (Exception e) {
					MessageDialog.showError("An error occurred while performing the requested operation. [Error Details: "
							+ e.getMessage() + "]");
				}
			}
		}
		else{
			if (value != null && !value.toString().trim().equals("")){
				try {
					currentDateUI.setDate((Date) value);
				} catch (Exception e) {
					MessageDialog.showError("An error occurred while performing the requested operation. [Error Details: "
							+ e.getMessage() + "]");
				}
			}
		}	
		
		fieldList.add(currentDateUI);
		
		return fieldList;
	}
	
	//Rearranges fields
	private void redistributeFields(int repNumber, int startIndex, GridBagConstraints cons, GridBagConstraints cons2){
		Component[] c = getComponents();
		int counter = repNumber - 1;

		GridBagConstraints constraint = new GridBagConstraints();
		int xPosition = 0;
		
		for (int i = startIndex; i < c.length; i++){
			if (c[i].getName().contains(_fieldPrefix1)) {
				counter++;
				xPosition = 0;
				constraint.gridx = xPosition;
				constraint.gridy = counter;
				constraint.weightx = cons.weightx;
				constraint.fill = cons.fill;
				constraint.anchor = cons.anchor;
				constraint.insets = cons.insets;
				layout.setConstraints(c[i], constraint);
			} else if (c[i].getName().contains(_fieldPrefix2)
					&& !_fieldPrefix2.equals("")) {
				xPosition++;
				constraint.gridx = xPosition;
				constraint.gridy = counter;
				constraint.fill = cons2.fill;
				constraint.anchor = cons2.anchor;
				constraint.insets = cons2.insets;
				layout.setConstraints(c[i], constraint);
			} else if (c[i].getName().contains("btnUp")) {
				xPosition++;
				constraint.gridx = xPosition;
				constraint.gridy = counter;
				constraint.fill = cons.fill;
				constraint.anchor = cons.anchor;
				constraint.insets = cons.insets;
				layout.setConstraints(c[i], constraint);
			} else if (c[i].getName().contains("btnDown")) {
				xPosition++;
				constraint.gridx = xPosition;
				constraint.gridy = counter;
				constraint.fill = cons.fill;
				constraint.anchor = cons.anchor;
				constraint.insets = cons.insets;
				layout.setConstraints(c[i], constraint);
			} else if (c[i].getName().contains("btnDel")) {
				xPosition++;
				constraint.gridx = xPosition;
				constraint.gridy = counter;
				constraint.fill = cons.fill;
				constraint.anchor = cons.anchor;
				constraint.insets = cons.insets;
				layout.setConstraints(c[i], constraint);
			}
		}
		
		revalidate();
	}
	
	/**
	 * Deletes the field for the specified repetition when the Delete button is clicked.
	 * @param repNumber
	 * repetition number
	 */
	public void deleteField(int repNumber) {
		String name1 = "btnDel" + _paramName + repNumber;
		String name2 = _fieldPrefix1 + _paramName + repNumber;
		String name3 = "btnUp" + _paramName + repNumber;
		String name4 = "btnDown" + _paramName + repNumber;
		String name5 = _fieldPrefix2 + _paramName + repNumber;
		int index = getComponentIndex(name2);
		GridBagConstraints cons = null;
		GridBagConstraints cons2 = null;
		
		for (Component c : getComponents()){
			if (c.getName().equals(name1)) {
				remove(c);
			} 
			else if (c.getName().equals(name2)) {
				cons = layout.getConstraints(c);
				remove(c);
			} 
			else if (c.getName().equals(name3)) {
				remove(c);
			} 
			else if (c.getName().equals(name4)) {
				remove(c);
			} 
			else if (c.getName().equals(name5) && !_fieldPrefix2.equals("")) {
				cons2 = layout.getConstraints(c);
				remove(c);
			}
		}
		
		renameFields(repNumber, index);		
		redistributeFields(repNumber, index, cons, cons2);
		
		if (repNumber == getFieldsCount()){
			setEnabledDownButton("btnDown" + _paramName + (repNumber - 1), false);
		}	
		
		if (repNumber == 0){
			setEnabledUpButton("btnUp" + _paramName + repNumber, false);
		}
		
		if (getFieldsCount() + 1 == _maxNumOfReps){			
			addMoreButton();
		}
		
		if (getFieldsCount() == _minNumOfReps){
			setEnabledDeleteButton(false);
		}
		
		revalidate();
		repaint();
	}
	
	//Renames fields 
	private void renameFields(int repNumber, int startIndex){
		Component[] c = getComponents();
		int counter = repNumber - 1;
		for (int i = startIndex; i < c.length; i++){
			if (c[i].getName().contains(_fieldPrefix1)) {
				counter++;
				c[i].setName(_fieldPrefix1 + _paramName + counter);
			} 
			else if (c[i].getName().contains(_fieldPrefix2)
					&& !_fieldPrefix2.equals("")) {
				c[i].setName(_fieldPrefix2 + _paramName + counter);
			} 
			else if (c[i].getName().contains("btnUp")) {
				c[i].setName("btnUp" + _paramName + counter);
				((MoveUpButton) c[i]).setRepNumber(counter);
			} 
			else if (c[i].getName().contains("btnDown")) {
				c[i].setName("btnDown" + _paramName + counter);
				((MoveDownButton) c[i]).setRepNumber(counter);
			} 
			else if (c[i].getName().contains("btnDel")) {
				c[i].setName("btnDel" + _paramName + counter);
				((DeleteButton) c[i]).setRepNumber(counter);
			}
		}
	}
	
	//Creates the Add Another button
	private void addMoreButton(){
		moreButton = new AddMoreButton(_cc, _paramName);
		moreButton.setName("btnMore" + _paramName);
		constraints.gridx = 0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridy = getFieldsCount() + 1;
		constraints.insets = new Insets(5,0,0,0);
		add(moreButton, constraints);
	}
	
	/**
	 * Gets the number of fields (in the parameter widget) currently visible to the user
	 * @return
	 * number of fields
	 */
	public int getFieldsCount(){
		int counter = 0;
		for (Component c : getComponents()){
			if (c.getName().contains(_fieldPrefix1)) {
				counter = counter + 1;
			}
		}
		return counter;
	}
	
	//Gets the index of a component (button or field)
	private int getComponentIndex(String name){
		Component[] c = getComponents();
		for (int i = 0; i < c.length; i++){
			if (c[i].getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}
	
	//Gets a component (button or field)
	private JComponent getComponent(String name){		
		for (Component c : getComponents()){
			if (c.getName().equals(name)) {
				return (JComponent) c;
			}
		}
		return null;
	}
	
	//Deletes a component (button or field)
	private void deleteComponent(String name) {
		for (Component c : getComponents()){
			if (c.getName().equals(name)) {
				remove(c);
				break;
			}
		}
	}
	
	//Called by the BooleanWidget class to set the selected option (Yes or No)
	protected void setSelectedButton(Object valueYes, Object valueNo){
		if (valueYes != null && valueNo != null) {
			boolean yesValue = (Boolean) valueYes;
			boolean noValue = (Boolean) valueNo;
			if (yesValue)
				yesButton.setSelected(true);
			else if (noValue)
				noButton.setSelected(true);
		}
	}
	
	//Returns information about the fields to be swapped when the Move Down button is clicked
	private TreeMap<Integer,List<Object>> getMoveDownFieldsInfo(int repNumber){
		TreeMap<Integer,List<Object>> locationMap = new TreeMap<Integer,List<Object>>();	
		List<Object> compData = new ArrayList<Object>();
		
		String name = _fieldPrefix1 + _paramName + repNumber;
		String name1 = _fieldPrefix1 + _paramName + (repNumber + 1);
		String name2 = "btnUp" + _paramName + repNumber;
		String name3 = "btnUp" + _paramName + (repNumber + 1);
		String name4 = "btnDown" + _paramName + repNumber;
		String name5 = "btnDown" + _paramName + (repNumber + 1);
		String name6 = "btnDel" + _paramName + repNumber;
		String name7 = "btnDel" + _paramName + (repNumber + 1);
		String name8 = _fieldPrefix2 + _paramName + repNumber;
		String name9 = _fieldPrefix2 + _paramName + (repNumber + 1);
		
		Component[] c = getComponents();
		for (int i = 0; i < c.length; i++){
			if (c[i].getName().equals(name)) {
				compData.add(WidgetAnalyzer.getValue((JComponent) c[i]));
				compData.add(String.valueOf(i));
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name8)) {
				compData.add(WidgetAnalyzer.getValue((JComponent) c[i]));
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name2)) {
				compData.add(c[i].getName());
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name4)) {
				compData.add(c[i].getName());

				if (_minNumOfReps == _maxNumOfReps)
					compData.add(false);

				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name6)) {
				compData.add(c[i].isEnabled());
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name1)) {
				locationMap.put(repNumber, compData);
				compData = new ArrayList<Object>();

				compData.add(WidgetAnalyzer.getValue((JComponent) c[i]));
				compData.add(String.valueOf(i));
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name9)) {
				compData.add(WidgetAnalyzer.getValue((JComponent) c[i]));
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name3)) {
				compData.add(c[i].getName());
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name5)) {
				compData.add(c[i].getName());

				if (_minNumOfReps == _maxNumOfReps)
					compData.add(false);

				deleteComponent(c[i].getName());
				locationMap.put(repNumber + 1, compData);
			} else if (c[i].getName().equals(name7)) {
				compData.add(c[i].isEnabled());
				deleteComponent(c[i].getName());
				locationMap.put(repNumber + 1, compData);
				break;
			}		
		}
		
		revalidate();
		return locationMap;
	}
	
	//Returns information about the fields to be swapped when the Move Up button is clicked
	private TreeMap<Integer,List<Object>> getMoveUpFieldsInfo(int repNumber){
		TreeMap<Integer,List<Object>> locationMap = new TreeMap<Integer,List<Object>>();	
		List<Object> compData = new ArrayList<Object>();
				
		String name = _fieldPrefix1 + _paramName + repNumber;
		String name1 = _fieldPrefix1 + _paramName + (repNumber - 1);
		String name2 = "btnUp" + _paramName + repNumber;
		String name3 = "btnUp" + _paramName + (repNumber - 1);
		String name4 = "btnDown" + _paramName + repNumber;
		String name5 = "btnDown" + _paramName + (repNumber - 1);
		String name6 = "btnDel" + _paramName + repNumber;
		String name7 = "btnDel" + _paramName + (repNumber - 1);
		String name8 = _fieldPrefix2 + _paramName + repNumber;
		String name9 = _fieldPrefix2 + _paramName + (repNumber - 1);
		
		Component[] c = getComponents();
		for (int i = 0; i < c.length; i++){
			if (c[i].getName().equals(name1)) {
				compData.add(WidgetAnalyzer.getValue((JComponent) c[i]));
				compData.add(String.valueOf(i));
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name9)) {
				compData.add(WidgetAnalyzer.getValue((JComponent) c[i]));
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name3)) {
				compData.add(c[i].getName());
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name5)) {
				compData.add(c[i].getName());

				if (_minNumOfReps == _maxNumOfReps)
					compData.add(false);

				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name7)) {
				compData.add(c[i].isEnabled());
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name)) {
				locationMap.put(repNumber - 1, compData);
				compData = new ArrayList<Object>();

				compData.add(WidgetAnalyzer.getValue((JComponent) c[i]));
				compData.add(String.valueOf(i));
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name8)) {
				compData.add(WidgetAnalyzer.getValue((JComponent) c[i]));
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name2)) {
				compData.add(c[i].getName());
				deleteComponent(c[i].getName());
			} else if (c[i].getName().equals(name4)) {
				compData.add(c[i].getName());

				if (_minNumOfReps == _maxNumOfReps)
					compData.add(false);

				deleteComponent(c[i].getName());
				locationMap.put(repNumber, compData);
			} else if (c[i].getName().equals(name6)) {
				compData.add(c[i].isEnabled());
				deleteComponent(c[i].getName());
				locationMap.put(repNumber, compData);
				break;
			}	
		}
		
		revalidate();
		
		return locationMap;
	}
	
	/**
	 * Converts time to a particular format
	 * @param value
	 * value to convert
	 * @return
	 * time object
	 */
	protected Object formatTime(Object value){
		Date time = (Date) value;
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		return new SimpleDateFormat("h:mm a").format(cal.getTime());
	}
	
	/**
	 * Gets time instance
	 * @param value
	 * value to convert
	 * @return
	 * time object
	 * @throws ParseException 
	 * exception is thrown if the specified time could not be parsed
	 */
	protected Date getTimeInstance(Object value) throws ParseException{
		Object formattedTime = formatTime(value);
		return new SimpleDateFormat("h:mm a").parse(formattedTime.toString());
	}
	
	/**
	 * Gets the current parameter
	 * @return
	 * the parameter object
	 */
	protected IdfParameter getParamObject(){
		return _paramObj;
	}	
	
	/**
	 * Move the specified parameter repetition up.
	 * @param repNumber
	 * repetition number
	 */
	protected void performMoveUpAction(int repNumber){
		List<Integer> keys = new ArrayList<Integer>();
		
		locationMap = getMoveUpFieldsInfo(repNumber);   //Gets information about the fields to be swapped 
		 												//during the move up action
		
		for (int key : locationMap.keySet()){
			keys.add(key);
		}
		
		if (_paramObj.getType().equals("boolean")){
			insertField(keys.get(1)-1, Integer.parseInt(locationMap.get(keys.get(0)).get(1).toString()), null, 
					(Boolean) locationMap.get(keys.get(0)).get(5));
			setSelectedButton(locationMap.get(keys.get(1)).get(0), locationMap.get(keys.get(1)).get(2));				
			 
			insertField(keys.get(0)+1, Integer.parseInt(locationMap.get(keys.get(1)).get(1).toString()), null, 
					(Boolean) locationMap.get(keys.get(1)).get(5));
			setSelectedButton(locationMap.get(keys.get(0)).get(0), locationMap.get(keys.get(0)).get(2));
		}
		else if (_paramObj.getType().equals("file")){	
			insertField(keys.get(1)-1, Integer.parseInt(locationMap.get(keys.get(0)).get(1).toString()), 
					locationMap.get(keys.get(1)).get(0), (Boolean) locationMap.get(keys.get(0)).get(5));
			 
			insertField(keys.get(0)+1, Integer.parseInt(locationMap.get(keys.get(1)).get(1).toString()), 
					locationMap.get(keys.get(0)).get(0), (Boolean) locationMap.get(keys.get(1)).get(5));
		}
		else if (_paramObj.getType().equals("int") || _paramObj.getType().equals("float") 
				|| _paramObj.getType().equals("text") || _paramObj.getType().equals("choice") 
				|| _paramObj.getType().equals("timeOfDay") || _paramObj.getType().equals("date") 
				|| _paramObj.getType().equals("tableEntry"))
		{
			insertField(keys.get(1)-1, Integer.parseInt(locationMap.get(keys.get(0)).get(1).toString()),
					locationMap.get(keys.get(1)).get(0), (Boolean) locationMap.get(keys.get(0)).get(4));	
			 
			insertField(keys.get(0)+1, Integer.parseInt(locationMap.get(keys.get(1)).get(1).toString()),
					locationMap.get(keys.get(0)).get(0), (Boolean) locationMap.get(keys.get(1)).get(4));
		}
		
		revalidate();
	}
	
	/**
	 * Moves the specified parameter repetition down when the Move Down button is clicked.
	 * @param repNumber
	 * repetition number
	 */
	protected void performMoveDownAction(int repNumber){
		List<Integer> keys = new ArrayList<Integer>();
		
		locationMap = getMoveDownFieldsInfo(repNumber);	 //Gets information about the fields to be swapped 
														 //during the move down action
		
		for (int key : locationMap.keySet()){
			keys.add(key);
		}

		if (_paramObj.getType().equals("boolean")){
			insertField(keys.get(1)-1, Integer.parseInt(locationMap.get(keys.get(0)).get(1).toString()), null, 
					(Boolean) locationMap.get(keys.get(0)).get(5));
			setSelectedButton(locationMap.get(keys.get(1)).get(0), locationMap.get(keys.get(1)).get(2));				
			 
			insertField(keys.get(0)+1, Integer.parseInt(locationMap.get(keys.get(1)).get(1).toString()), null, 
					(Boolean) locationMap.get(keys.get(1)).get(5));
			setSelectedButton(locationMap.get(keys.get(0)).get(0), locationMap.get(keys.get(0)).get(2));
		}
		else if (_paramObj.getType().equals("file")){	
			insertField(keys.get(1)-1, Integer.parseInt(locationMap.get(keys.get(0)).get(1).toString()), 
					locationMap.get(keys.get(1)).get(0), (Boolean) locationMap.get(keys.get(0)).get(5));		
			 
			insertField(keys.get(0)+1, Integer.parseInt(locationMap.get(keys.get(1)).get(1).toString()), 
					locationMap.get(keys.get(0)).get(0), (Boolean) locationMap.get(keys.get(1)).get(5));
		}
		else if (_paramObj.getType().equals("int") || _paramObj.getType().equals("float") 
				|| _paramObj.getType().equals("text") || _paramObj.getType().equals("choice") 
				|| _paramObj.getType().equals("timeOfDay") || _paramObj.getType().equals("date") 
				|| _paramObj.getType().equals("tableEntry"))
		{
			insertField(keys.get(1)-1, Integer.parseInt(locationMap.get(keys.get(0)).get(1).toString()), 
					locationMap.get(keys.get(1)).get(0), (Boolean) locationMap.get(keys.get(0)).get(4));	
			 
			insertField(keys.get(0)+1, Integer.parseInt(locationMap.get(keys.get(1)).get(1).toString()), 
					locationMap.get(keys.get(0)).get(0), (Boolean) locationMap.get(keys.get(1)).get(4));
		}	
		
		revalidate();
	}
	
}
