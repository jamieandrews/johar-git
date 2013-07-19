package johar.interfaceinterpreter.star;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedHashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import johar.gem.GemSetting;
import johar.idf.Idf;
import johar.idf.IdfCommand;
import johar.idf.IdfParameter;

/**
 * Creates parameter widgets for each queryable stage.
 * 
 */
public class StageGUI extends JPanel {

	private GemSetting _gem;
	private CommandController _cc;
	private IdfCommand _currentCommand;
	private int _currentStage;
	private GridBagConstraints constraints;
	private int rowIndex;
	private int colIndex;
	private ParameterWidget paramWidget;
	private LinkedHashMap<String, ParameterWidget> paramObjectsMap;
	private IdfAnalyzer idfAnalyzer;
	
	/**
	 * StageGUI constructor
	 * @param cc
	 * the Command Controller object
	 * @param gem
	 * the GemSetting object
	 * @param idf
	 * the IDF object
	 * @param currentCommand
	 * the current IdfCommand object
	 * @param currentStage
	 * the index or number of the current stage
	 */
	public StageGUI(CommandController cc, GemSetting gem, Idf idf, IdfCommand currentCommand, int currentStage) {
		_cc = cc;
		_currentCommand = currentCommand;
		_currentStage = currentStage;
		_gem = gem;
		paramObjectsMap = new LinkedHashMap<String, ParameterWidget>();  //Stores the parameter widgets
			
		idfAnalyzer = new IdfAnalyzer(idf); /* Provides access to various information about the specified IDF 
		 									* e.g. list of queryable/non-queryable params in a command/stage, 
		 									* list of questions in a command, list of tables, 
		 									* list of browsable tables, etc. */
		try {
			createWidget();
		} catch (Exception e) {
			MessageDialog.showError("An error occurred while performing the requested operation. [Error Details: "
					+ e.getMessage() + "]");
		}
	}
	
	//Creates the stage GUI
	private void createWidget(){
		setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		idfAnalyzer.setCurrentCommand(_currentCommand.getCommandName());	//Sets the current command in the IDF analyzer

		setName("Stage" + _currentStage);
		rowIndex = 0;
		colIndex = 0;
		
		//Create widget for each queryable parameter in the stage
		int numOfParams = _currentCommand.getStage(_currentStage).getNumParameters();
		IdfParameter currentParam;
		
		if (numOfParams > 0){
			for (int i = 0; i < numOfParams; i++){
				currentParam = _currentCommand.getStage(_currentStage).getParameter(i);
				createWidgetParameter(currentParam);
			}			
		}
	}
	
	//Creates the widget for the parameters
	private void createWidgetParameter(IdfParameter currentParam){
		String type = currentParam.getType();
		String paramName = currentParam.getParameterName();
		
		//Gets the default value for the parameter 
		String defaultValueMethod = currentParam.getDefaultValueMethod();
		Object defaultValue = currentParam.getDefaultValue();
		
		if (defaultValueMethod != null  && !defaultValueMethod.equals("")){
			defaultValue = _gem.callDefaultValueMethod(paramName);
		}
		
		//Creates and stores the widgets
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
		else if (type.equals("date"))
			addDateField(defaultValue, currentParam);
		else if (type.equals("tableEntry") && !idfAnalyzer.getTable(currentParam.getSourceTable()).getBrowsable())
			addTableEntryField(defaultValue, currentParam);
		else if (type.equals("file"))
			addFileField(defaultValue, currentParam);

	}
	
	//Creates the Label
	private void initialAction(IdfParameter paramObj){
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex;
		constraints.gridy = colIndex;
		JLabel paramLabel = new JLabel(paramObj.getLabel() + ":");
		add(paramLabel, constraints);
				
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(1,0,0,0);
		constraints.gridx = rowIndex + 1;
		constraints.gridy = colIndex;
	}
	
	//Creates widget for parameter of type "text"
	private void addTextField(Object defaultValue, IdfParameter paramObj){
		initialAction(paramObj);
		
		paramWidget = new TextWidget(defaultValue, paramObj, _cc);
		paramWidget.setName(paramObj.getParameterName());
		
		paramObjectsMap.put(paramObj.getParameterName(), paramWidget);	//Add to Map
		
		add(paramWidget, constraints);
		
		colIndex++;
	}
	
	//Creates widget for parameter of type "int" or "float"
	private void addNumberField(Object defaultValue, IdfParameter paramObj){
		initialAction(paramObj);
		
		paramWidget = new NumberWidget(defaultValue, paramObj, _cc);
		paramWidget.setName(paramObj.getParameterName());
		paramObjectsMap.put(paramObj.getParameterName(), paramWidget);	//Add to Map
		
		add(paramWidget, constraints);
		
		colIndex++;
	}
	
	//Creates widget for parameter of type "boolean"
	private void addBooleanField(Object defaultValue, IdfParameter paramObj){
		initialAction(paramObj);
		
		paramWidget = new BooleanWidget(defaultValue, paramObj, _cc);
		paramWidget.setName(paramObj.getParameterName());
		paramObjectsMap.put(paramObj.getParameterName(), paramWidget);	//Add to Map
		
		add(paramWidget, constraints);
		
		colIndex++;
	}
	
	//Creates widget for parameter of type "choice"
	private void addChoiceField(Object defaultValue, IdfParameter paramObj){
		initialAction(paramObj);
		
		paramWidget = new ChoiceWidget(defaultValue, paramObj, _cc);
		paramWidget.setName(paramObj.getParameterName());
		paramObjectsMap.put(paramObj.getParameterName(), paramWidget);	//Add to Map
		
		add(paramWidget, constraints);
		
		colIndex++;
	}
	
	//Creates widget for parameter of type "timeOfDay"
	private void addTimeField(Object defaultValue, IdfParameter paramObj){
		initialAction(paramObj);
		
		paramWidget = new TimeWidget(defaultValue, paramObj, _cc);
		paramWidget.setName(paramObj.getParameterName());
		paramObjectsMap.put(paramObj.getParameterName(), paramWidget);	//Add to Map
		
		add(paramWidget, constraints);
		
		colIndex++;
	}
	
	//Creates widget for parameter of type "date"
	private void addDateField(Object defaultValue, IdfParameter paramObj){
		initialAction(paramObj);
		
		paramWidget = new DateWidget(defaultValue, paramObj, _cc);
		paramWidget.setName(paramObj.getParameterName());
		paramObjectsMap.put(paramObj.getParameterName(), paramWidget);	 //Add to Map
		
		add(paramWidget, constraints);
		
		colIndex++;		
	}
	
	//Creates widget for parameter of type "file"
	private void addFileField(Object defaultValue, IdfParameter paramObj){
		initialAction(paramObj);
		
		paramWidget = new FileWidget(defaultValue, paramObj, _cc);
		paramWidget.setName(paramObj.getParameterName());
		paramObjectsMap.put(paramObj.getParameterName(), paramWidget);	//Add to Map
		
		add(paramWidget, constraints);
		
		colIndex++;
	}
	
	//Creates widget for parameter of type "tableEntry", whose SourceTable is non-browsable
	private void addTableEntryField(Object defaultValue, IdfParameter paramObj) {
		initialAction(paramObj);
		
		paramWidget = new TableEntryWidget(defaultValue, paramObj, _cc);
		paramWidget.setName(paramObj.getParameterName());
		paramObjectsMap.put(paramObj.getParameterName(), paramWidget);	//Add to Map
		
		add(paramWidget, constraints);
		
		colIndex++;
	}

	/**
	 * Gets all the Parameter Widgets
	 * @return
	 * the map containing parameter widgets
	 */
	public LinkedHashMap<String, ParameterWidget> getParameterWidgets(){
		return paramObjectsMap;
	}
	
}
