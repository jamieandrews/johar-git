/*
 * Creates the Command Window
 */

package johar.interfaceinterpreter.star;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedHashMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import johar.gem.GemException;
import johar.gem.GemSetting;
import johar.idf.Idf;
import johar.idf.IdfCommand;
import johar.idf.IdfParameter;

public class CommandWidget extends JFrame {
	private Idf _idf;
	private GemSetting _gem;
	private CommandController _cc;
	private IdfCommand currentCommand;
	private String commandLabel;
	private String _commandName;
	private JPanel mainPanel;
	private Container container;
	private GridBagConstraints constraints;
	private int rowIndex;
	private int colIndex;
	private TextWidget paramTextField;
	private NumberWidget paramNumberWidget;	
	private LinkedHashMap<String, Object> paramObjectsMap;
	
	public CommandWidget(CommandController cc, GemSetting gem, Idf idf, String commandName) {
		_idf = idf;
		_cc = cc;
		_commandName = commandName;
		_gem = gem;
		paramObjectsMap = new LinkedHashMap<String, Object>();
	}
	
	private void initialize() {
		rowIndex = 0;
		colIndex = 0;
		mainPanel = new JPanel();
		container = getContentPane();
		container.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		mainPanel.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLocation(300, 30);
		container.add(mainPanel);
	}

	public void createAndDisplayWidget(){
		initialize();
		currentCommand = getIdfCommand(_commandName);
		commandLabel = currentCommand.getLabel();
		int numOfStages = currentCommand.getNumStages();
		boolean isSingleStage;
		
		if (numOfStages == 1){
			_gem.selectCurrentStage(0);
			isSingleStage = true;
			createWidgetForStage(0);
		}
		else{				
			isSingleStage = false;
			createWidgetMultipleStages(numOfStages);
		}
		
		addOKCancelButtons(isSingleStage);
		setVisible(true);
		setTitle(commandLabel);
		pack();		
	}
	
	private void createWidgetMultipleStages(int numOfStages) {
		//for (int i = 0; i < numOfStages; i++){
			createWidgetForStage(0);
		//}
	}

	private void createWidgetForStage(int index){
		int numOfParams = currentCommand.getStage(index).getNumParameters();
		IdfParameter currentParam;
		if (numOfParams > 0){			
			for (int i = 0; i < numOfParams; i++){
				currentParam = currentCommand.getStage(0).getParameter(i);
				createWidgetParameter(currentParam);
			}			
		}		
	}
	
	private void createWidgetParameter(IdfParameter currentParam){
		String type = currentParam.getType();
		String paramName = currentParam.getParameterName();
		String defaultValueMethod = currentParam.getDefaultValueMethod();
		Object defaultValue = currentParam.getDefaultValue();
		
		if (!defaultValueMethod.equals("") && defaultValueMethod != null){
			defaultValue = _gem.callDefaultValueMethod(paramName);
		}
		
		if (type.equals("text"))
			addTextField(defaultValue, currentParam);
		else if (type.equals("int") || type.equals("float"))
			addNumberField(defaultValue, currentParam);
		else if (type.equals("boolean"))
			addBooleanField(defaultValue, currentParam);
		else if (type.equals("choice"))
			addChoiceField(defaultValue, currentParam);
		else if (type.equals("timeOfDay"))
			addTimeField(defaultValue, currentParam);
		else if (type.equals("file"))
			addFileField(defaultValue, currentParam);

	}
	
	private void addTextField(Object defaultValue, IdfParameter paramObj){
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex;
		constraints.gridy = colIndex;
		JLabel paramLabel = new JLabel(paramObj.getLabel() + ":");
		mainPanel.add(paramLabel, constraints);
				
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex + 1;
		constraints.gridy = colIndex;
		paramTextField = new TextWidget(defaultValue, paramObj, _cc);
		paramObjectsMap.put(paramObj.getParameterName(), paramTextField);				//Add to Map
		mainPanel.add(paramTextField, constraints);
		
		colIndex++;
	}
	
	private void addNumberField(Object defaultValue, IdfParameter paramObj){
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex;
		constraints.gridy = colIndex;
		JLabel paramLabel = new JLabel(paramObj.getLabel() + ":");
		mainPanel.add(paramLabel, constraints);
				
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex + 1;
		constraints.gridy = colIndex;
		
		paramNumberWidget = new NumberWidget(defaultValue, paramObj, _cc);
		paramObjectsMap.put(paramObj.getParameterName(), paramNumberWidget);				//Add to Map
		mainPanel.add(paramNumberWidget, constraints);
		
		colIndex++;
	}
	
	private void addBooleanField(Object defaultValue, IdfParameter paramObj){
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex;
		constraints.gridy = colIndex;
		JLabel paramLabel = new JLabel(paramObj.getLabel() + ":");
		mainPanel.add(paramLabel, constraints);
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex + 1;
		constraints.gridy = colIndex;
		BooleanWidget booleanParamField = new BooleanWidget(defaultValue, paramObj, _cc);
		paramObjectsMap.put(paramObj.getParameterName(), booleanParamField);				//Add to Map
		mainPanel.add(booleanParamField, constraints);
		
		colIndex++;
	}
	
	private void addChoiceField(Object defaultValue, IdfParameter paramObj){
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex;
		constraints.gridy = colIndex;
		JLabel paramLabel = new JLabel(paramObj.getLabel() + ":");
		mainPanel.add(paramLabel, constraints);
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex + 1;
		constraints.gridy = colIndex;
		ChoiceWidget choiceParamField = new ChoiceWidget(defaultValue, paramObj, _cc);
		paramObjectsMap.put(paramObj.getParameterName(), choiceParamField);				//Add to Map
		mainPanel.add(choiceParamField, constraints);
		
		colIndex++;
	}
	
	private void addTimeField(Object defaultValue, IdfParameter paramObj){
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex;
		constraints.gridy = colIndex;
		JLabel paramLabel = new JLabel(paramObj.getLabel() + ":");
		mainPanel.add(paramLabel, constraints);
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex + 1;
		constraints.gridy = colIndex;
		TimeWidget timeParamField = new TimeWidget(defaultValue, paramObj, _cc);
		paramObjectsMap.put(paramObj.getParameterName(), timeParamField);				//Add to Map
		mainPanel.add(timeParamField, constraints);
		
		colIndex++;
	}
	
	private void addFileField(Object defaultValue, IdfParameter paramObj){
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex;
		constraints.gridy = colIndex;
		JLabel paramLabel = new JLabel(paramObj.getLabel() + ":");
		mainPanel.add(paramLabel, constraints);
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex + 1;
		constraints.gridy = colIndex;
		FileWidget fileParamField = new FileWidget(defaultValue, paramObj, _cc);
		paramObjectsMap.put(paramObj.getParameterName(), fileParamField);				//Add to Map
		mainPanel.add(fileParamField, constraints);
		
		colIndex++;
	}
	
	private void addOKCancelButtons(boolean isSingleStage){
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(15,0,0,0);
		constraints.gridx = rowIndex;
		constraints.gridy = colIndex;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = GridBagConstraints.WEST;
		CancelButton cancelButton = new CancelButton(_cc);
		cancelButton.setText("Cancel");
		mainPanel.add(cancelButton, constraints);
		
		if (!isSingleStage){
			constraints.anchor = GridBagConstraints.EAST;
			constraints.gridwidth = GridBagConstraints.REMAINDER;
			constraints.insets = new Insets(15,0,0,105);
			//constraints.gridx = rowIndex + 1;
			constraints.weightx = 0.5;
			constraints.gridy = colIndex;
			PreviousButton prevButton = new PreviousButton(_cc);
			prevButton.setText("Previous");
			prevButton.setEnabled(false);
			mainPanel.add(prevButton, constraints);
			
			constraints.insets = new Insets(15,0,0,50);
			constraints.weightx = 0.5;
			constraints.gridy = colIndex;
			NextButton nextButton = new NextButton(_cc);
			nextButton.setText("Next");
			mainPanel.add(nextButton, constraints);
			
			constraints.weightx = 0.5;
		}
		
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(15,0,0,0);
		constraints.gridy = colIndex;
		OKButton okButton = new OKButton(_cc);
		okButton.setText("OK");			
		mainPanel.add(okButton, constraints);

	}	
	
	private IdfCommand getIdfCommand(String commandName) throws GemException {
		int numCommands = _idf.getNumCommands();

		for (int i=0; i<numCommands; i++) {
			IdfCommand idfCommand = _idf.getCommandNumber(i);
			if (commandName.equals(idfCommand.getCommandName())) {
				return idfCommand;
			}
		}
		throw new GemException("Command not declared in IDF: " + commandName);
	}
	
	public LinkedHashMap<String, Object> getParameterObjects(){
		return paramObjectsMap;
	}
}
