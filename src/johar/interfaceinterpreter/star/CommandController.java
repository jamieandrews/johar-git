/*
 * The Command Controller
 */

package johar.interfaceinterpreter.star;

import java.util.LinkedHashMap;

import johar.gem.GemException;
import johar.gem.GemSetting;
import johar.idf.Idf;
import johar.idf.IdfCommand;
import johar.idf.IdfParameter;
import johar.idf.IdfQuestion;
import johar.idf.IdfStage;

public class CommandController implements CommandControllerBase {
	private GemSetting _gem;
	private Idf _idf;
	private IdfCommand currentCommand;	
	private CommandWidget commandGUI;
	private String _commandName;
	private LinkedHashMap<String, Object> paramObjectsMap;
	private Star _starGUI;
	
	public CommandController(GemSetting gem, Idf idf, Star starGUI){
		_gem = gem;
		_idf = idf;
		_starGUI = starGUI;
	}
	
	public void commandMenuItemClicked(String commandName){
		_commandName = commandName;
		currentCommand = getIdfCommand(_commandName);
		_gem.selectCurrentCommand(_commandName);
		commandGUI = new CommandWidget(this, _gem, _idf, _commandName);
		commandGUI.createAndDisplayWidget();
	}
	
	public void previousButtonClicked(){
		//Pending
	}
	
	public void nextButtonClicked(){
		//Pending
	}
	
	public void okButtonClicked(){
		paramObjectsMap = commandGUI.getParameterObjects();
		String paramType = "";
		String paramLabel = "";	
		IdfParameter parameter;
		try {
			for (String param : paramObjectsMap.keySet()){	
				paramType = getParameterType(param);
				parameter = getIdfParameter(param);
				paramLabel = parameter.getLabel();
				
				if (paramType.equals("text")){
					TextWidget paramWidget = (TextWidget) paramObjectsMap.get(param);
					String paramValue;
					for (int i = 0; i < paramWidget.getTextFieldsCount(); i++){
						paramValue = paramWidget.getValue(i);
						if (paramValue != null && !paramValue.equals("")){
							_gem.setParameterValue(param, i, paramValue);
						}
						else{
							throw new GemException("MESSAGE: Please enter any missing information " + "for \"" + paramLabel + "\"");
						}
					}
				}
				else if (paramType.equals("int")){
					NumberWidget paramWidget = (NumberWidget) paramObjectsMap.get(param);
					Object paramValue;
					long inputValue;
					for (int i = 0; i < paramWidget.getNumFieldsCount(); i++){
						paramValue = paramWidget.getValue(i);
						if (paramValue != null && !paramValue.equals("")){
							inputValue = Long.parseLong(paramValue.toString());
							if (inputValue >= parameter.getMinIntValue() && inputValue <= parameter.getMaxIntValue()){
								_gem.setParameterValue(param, i, inputValue);
							}
							else
								throw new GemException("MESSAGE: Your value for " + paramLabel + " (" + inputValue + ") must be between " + parameter.getMinIntValue() + " and " + parameter.getMaxIntValue() + ".");							
							//System.out.println(param + i + " => " + Long.parseLong(paramValue.toString()));
						}
						else{
							throw new GemException("MESSAGE: Please specify any missing numeric value " + "for \"" + paramLabel + "\"");
						}							
					}
				}
				else if (paramType.equals("float")){
					NumberWidget paramWidget = (NumberWidget) paramObjectsMap.get(param);
					Object paramValue;
					double inputValue;
					for (int i = 0; i < paramWidget.getNumFieldsCount(); i++){
						paramValue = paramWidget.getValue(i);
						if (paramValue != null && !paramValue.equals("")){
							inputValue = Double.parseDouble(paramValue.toString());
							if (inputValue >= parameter.getMinFloatValue() && inputValue <= parameter.getMaxFloatValue()){
								_gem.setParameterValue(param, i, inputValue);
							}
							else
								throw new GemException("MESSAGE: Your value for " + paramLabel + " (" + inputValue + ") must be between " + parameter.getMinFloatValue() + " and " + parameter.getMaxFloatValue() + ".");							
							//System.out.println(param + i + " => " + Double.parseDouble(paramValue.toString()));
						}
						else{
							throw new GemException("MESSAGE: Please specify any missing numeric value " + "for \"" + paramLabel + "\"");
						}							
					}
				}
				else if (paramType.equals("boolean")){
					BooleanWidget paramWidget = (BooleanWidget) paramObjectsMap.get(param);
					String paramValue;
					for (int i = 0; i < paramWidget.getBooleanFieldsCount(); i++){
						paramValue = paramWidget.getSelectedOption(i);
						if (paramValue != null && !paramValue.equals("")){
							if (paramValue.equals("Yes"))
								_gem.setParameterValue(param, i, true);
							else if (paramValue.equals("No"))
								_gem.setParameterValue(param, i, false);
						}
						else{
							throw new GemException("MESSAGE: Please select any missing option " + "for \"" + paramLabel + "\"");
						}
					}		
				}
				else if (paramType.equals("choice")){
					ChoiceWidget paramWidget = (ChoiceWidget) paramObjectsMap.get(param);
					Object paramValue;
					for (int i = 0; i < paramWidget.getChoiceFieldsCount(); i++){
						paramValue = paramWidget.getSelectedChoice(i);
						if (paramValue != null && !paramValue.equals("")){
							_gem.setParameterValue(param, i, paramValue.toString());
						}
						else{
							throw new GemException("MESSAGE: Please select any missing option " + "for \"" + paramLabel + "\"");
						}
					}
				}
				else if (paramType.equals("timeOfDay")){
					TimeWidget paramWidget = (TimeWidget) paramObjectsMap.get(param);
					Object paramValue;
					for (int i = 0; i < paramWidget.getTimeFieldsCount(); i++){
						paramValue = paramWidget.getSelectedTime(i);
						if (paramValue != null && !paramValue.equals("")){
							_gem.setParameterValue(param, i, paramValue);
						}
						else{
							throw new GemException("MESSAGE: Please specify any missing time " + "for \"" + paramLabel + "\"");
						}
					}
				}
				else if (paramType.equals("file")){
					FileWidget paramWidget = (FileWidget) paramObjectsMap.get(param);
					Object paramValue;
					for (int i = 0; i < paramWidget.getFileFieldsCount(); i++){
						paramValue = paramWidget.getSelectedFilePath(i);
						if (paramValue != null && !paramValue.equals("")){
							_gem.setParameterValue(param, i, paramValue);
						}
						else{
							throw new GemException("MESSAGE: Please select the missing file(s) " + "for \"" + paramLabel + "\"");
						}
					}
				}
			}
			
			//Call CommandMethod if parameter check returns true
			//if (_gem.parameterCheckSucceeds()){
				_gem.callCommandMethod();
			//}
			
			commandGUI.setVisible(false);
			commandGUI.dispose();
		} catch (GemException e) {
			_starGUI.showText(e.getMessage(), 2000);
		}catch (NumberFormatException e) {
			_starGUI.showText("MESSAGE: Please enter valid number(s).", 2000);
		}
	}
	
	public void cancelButtonClicked(){
		_gem.clearParameters();
		commandGUI.setVisible(false);
		commandGUI.dispose();
	}
	
	public void addMoreButtonClicked(String paramName){		
		IdfParameter parameter = getIdfParameter(paramName);
		String paramType = parameter.getType();
		
		if (paramType.equals("text")){
			paramObjectsMap = commandGUI.getParameterObjects();
			TextWidget paramWidget = (TextWidget) paramObjectsMap.get(paramName);
			int textFieldCount = paramWidget.getTextFieldsCount();
			paramWidget.addTextField(textFieldCount);
		}
		else if (paramType.equals("int")){
			paramObjectsMap = commandGUI.getParameterObjects();
			NumberWidget paramWidget = (NumberWidget) paramObjectsMap.get(paramName);
			int numberFieldCount = paramWidget.getNumFieldsCount();
			paramWidget.addNumberField(numberFieldCount);
		}
		else if (paramType.equals("float")){
			paramObjectsMap = commandGUI.getParameterObjects();
			NumberWidget paramWidget = (NumberWidget) paramObjectsMap.get(paramName);
			int numberFieldCount = paramWidget.getNumFieldsCount();
			paramWidget.addNumberField(numberFieldCount);
		}
		else if (paramType.equals("boolean")){
			paramObjectsMap = commandGUI.getParameterObjects();
			BooleanWidget paramWidget = (BooleanWidget) paramObjectsMap.get(paramName);
			int booleanFieldCount = paramWidget.getBooleanFieldsCount();
			paramWidget.addBooleanField(booleanFieldCount);
		}
		else if (paramType.equals("choice")){
			paramObjectsMap = commandGUI.getParameterObjects();
			ChoiceWidget paramWidget = (ChoiceWidget) paramObjectsMap.get(paramName);
			int choiceFieldCount = paramWidget.getChoiceFieldsCount();
			paramWidget.addChoiceField(choiceFieldCount);
		}
		else if (paramType.equals("timeOfDay")){
			paramObjectsMap = commandGUI.getParameterObjects();
			TimeWidget paramWidget = (TimeWidget) paramObjectsMap.get(paramName);
			int timeFieldCount = paramWidget.getTimeFieldsCount();
			paramWidget.insertTimeField(timeFieldCount);
		}
		else if (paramType.equals("file")){
			paramObjectsMap = commandGUI.getParameterObjects();
			FileWidget paramWidget = (FileWidget) paramObjectsMap.get(paramName);
			int fileFieldCount = paramWidget.getFileFieldsCount();
			paramWidget.addFileField(fileFieldCount);
		}
		commandGUI.pack();
	}
	
	public void moveUpButtonClicked(String paramName, int repNumber){		
		IdfParameter parameter = getIdfParameter(paramName);
		String paramType = parameter.getType();
		
		if (paramType.equals("text")){
			paramObjectsMap = commandGUI.getParameterObjects();
			TextWidget paramWidget = (TextWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveUpFields(repNumber);
		}
		else if (paramType.equals("int")){
			paramObjectsMap = commandGUI.getParameterObjects();
			NumberWidget paramWidget = (NumberWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveUpFields(repNumber);
		}
		else if (paramType.equals("float")){
			paramObjectsMap = commandGUI.getParameterObjects();
			NumberWidget paramWidget = (NumberWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveUpFields(repNumber);
		}
		else if (paramType.equals("boolean")){
			paramObjectsMap = commandGUI.getParameterObjects();
			BooleanWidget paramWidget = (BooleanWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveUpFields(repNumber);
		}
		else if (paramType.equals("choice")){
			paramObjectsMap = commandGUI.getParameterObjects();
			ChoiceWidget paramWidget = (ChoiceWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveUpFields(repNumber);
		}
		else if (paramType.equals("timeOfDay")){
			paramObjectsMap = commandGUI.getParameterObjects();
			TimeWidget paramWidget = (TimeWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveUpFields(repNumber);
		}
		else if (paramType.equals("file")){
			paramObjectsMap = commandGUI.getParameterObjects();
			FileWidget paramWidget = (FileWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveUpFields(repNumber);
		}
		commandGUI.pack();
	}
	
	public void moveDownButtonClicked(String paramName, int repNumber){
		IdfParameter parameter = getIdfParameter(paramName);
		String paramType = parameter.getType();
		
		if (paramType.equals("text")){
			paramObjectsMap = commandGUI.getParameterObjects();
			TextWidget paramWidget = (TextWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveDownFields(repNumber);
		}
		else if (paramType.equals("int")){
			paramObjectsMap = commandGUI.getParameterObjects();
			NumberWidget paramWidget = (NumberWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveDownFields(repNumber);
		}
		else if (paramType.equals("float")){
			paramObjectsMap = commandGUI.getParameterObjects();
			NumberWidget paramWidget = (NumberWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveDownFields(repNumber);
		}
		else if (paramType.equals("boolean")){
			paramObjectsMap = commandGUI.getParameterObjects();
			BooleanWidget paramWidget = (BooleanWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveDownFields(repNumber);
		}
		else if (paramType.equals("choice")){
			paramObjectsMap = commandGUI.getParameterObjects();
			ChoiceWidget paramWidget = (ChoiceWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveDownFields(repNumber);
		}
		else if (paramType.equals("timeOfDay")){
			paramObjectsMap = commandGUI.getParameterObjects();
			TimeWidget paramWidget = (TimeWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveDownFields(repNumber);
		}
		else if (paramType.equals("file")){
			paramObjectsMap = commandGUI.getParameterObjects();
			FileWidget paramWidget = (FileWidget) paramObjectsMap.get(paramName);				
			paramWidget.moveDownFields(repNumber);
		}
		commandGUI.pack();
	}
	
	public void deleteButtonClicked(String paramName, int repNumber){		
		IdfParameter parameter = getIdfParameter(paramName);
		String paramType = parameter.getType();
		
		if (paramType.equals("text")){
			paramObjectsMap = commandGUI.getParameterObjects();
			TextWidget paramWidget = (TextWidget) paramObjectsMap.get(paramName);				
			paramWidget.deleteTextField(repNumber);
		}
		else if (paramType.equals("int")){
			paramObjectsMap = commandGUI.getParameterObjects();
			NumberWidget paramWidget = (NumberWidget) paramObjectsMap.get(paramName);				
			paramWidget.deleteNumberField(repNumber);
		}
		else if (paramType.equals("float")){
			paramObjectsMap = commandGUI.getParameterObjects();
			NumberWidget paramWidget = (NumberWidget) paramObjectsMap.get(paramName);				
			paramWidget.deleteNumberField(repNumber);
		}
		else if (paramType.equals("boolean")){
			paramObjectsMap = commandGUI.getParameterObjects();
			BooleanWidget paramWidget = (BooleanWidget) paramObjectsMap.get(paramName);				
			paramWidget.deleteBooleanField(repNumber);
		}
		else if (paramType.equals("choice")){
			paramObjectsMap = commandGUI.getParameterObjects();
			ChoiceWidget paramWidget = (ChoiceWidget) paramObjectsMap.get(paramName);				
			paramWidget.deleteChoiceField(repNumber);
		}
		else if (paramType.equals("timeOfDay")){
			paramObjectsMap = commandGUI.getParameterObjects();
			TimeWidget paramWidget = (TimeWidget) paramObjectsMap.get(paramName);				
			paramWidget.deleteTimeField(repNumber);
		}
		else if (paramType.equals("file")){
			paramObjectsMap = commandGUI.getParameterObjects();
			FileWidget paramWidget = (FileWidget) paramObjectsMap.get(paramName);				
			paramWidget.deleteFileField(repNumber);
		}
		commandGUI.pack();
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
	
	// Also has the effect of throwing an exception if param does not exist.
    private IdfParameter getIdfParameter(String paramName) throws GemException {
    	int numStages = currentCommand.getNumStages();

    	for (int i=0; i<numStages; i++) {
    		IdfStage stage = currentCommand.getStage(i);

    		// Look through the parameters for this stage
    		int k = stage.getNumParameters();
    		for (int j=0; j<k; j++) {
    			IdfParameter parameter = stage.getParameter(j);
    			if (paramName.equals(parameter.getParameterName())) {
    				return parameter;
    			}
    		}
    	}

    	int k = currentCommand.getNumQuestions();
    	for (int j=0; j<k; j++) {
    		IdfQuestion question = currentCommand.getQuestion(j);
    		if (paramName.equals(question.getParameterName())) {
    			return question;
    		}
    	}

    	// If we have got here, we must not have found it
    	throw new GemException(
    			"Parameter " + paramName + " not found in current command");
    }

    private String getParameterType(String paramName) throws GemException {
    	IdfParameter parameter = getIdfParameter(paramName);
    	return parameter.getType();
    }
}
