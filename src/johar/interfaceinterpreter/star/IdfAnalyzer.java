package johar.interfaceinterpreter.star;

import java.util.ArrayList;
import java.util.List;

import johar.gem.GemException;
import johar.idf.Idf;
import johar.idf.IdfCommand;
import johar.idf.IdfCommandGroup;
import johar.idf.IdfParameter;
import johar.idf.IdfQuestion;
import johar.idf.IdfStage;
import johar.idf.IdfTable;

/**
 * Provides access to various information about the specified IDF (e.g list of queryable/non-queryable params in a command/stage, 
 * list of questions in a command, list of tables, list of browsable tables, etc.).
 * 
 */
public class IdfAnalyzer {
	private Idf _idf;
	private IdfCommand currentCommand;
	
	public IdfAnalyzer(Idf idf) {
		_idf = idf;
	}
	
	public void setCurrentCommand(String commandName){
		currentCommand = getIdfCommand(commandName);
	}
	
	public IdfCommand getCurrentIdfCommand() throws GemException {
		return currentCommand;
	}
	
	public IdfCommand getIdfCommand(String commandName) throws GemException {
		int numCommands = _idf.getNumCommands();

		for (int i=0; i<numCommands; i++) {
			IdfCommand idfCommand = _idf.getCommandNumber(i);
			if (commandName.equals(idfCommand.getCommandName())) {
				return idfCommand;
			}
		}
		throw new GemException("Command not declared in IDF: " + commandName);
	}

	public List<IdfCommand> getAllCommands() throws GemException {
		List<IdfCommand> commandList = new ArrayList<IdfCommand>();
		int numCommands = _idf.getNumCommands();

		for (int i=0; i<numCommands; i++) {
			IdfCommand idfCommand = _idf.getCommandNumber(i);			
			commandList.add(idfCommand);
		}

		return commandList;
	}
	
	public IdfCommand getIdfCommandByLabel(String label) throws GemException {		
		int numCommands = _idf.getNumCommands();

		for (int i=0; i<numCommands; i++) {
			IdfCommand idfCommand = _idf.getCommandNumber(i);			
			if (idfCommand.getLabel().equals(label))
				return idfCommand;
		}

		return null;
	}
	
	public IdfCommandGroup getCommandGroup(String commandName) throws GemException {		
		int numCommandGroups = _idf.getNumCommandGroups();

		for (int i = 0; i < numCommandGroups; i++) {
			IdfCommandGroup idfCommandGrp = _idf.getCommandGroupNumber(i);	
			for (int j = 0; j < idfCommandGrp.getNumMembers(); j++){
				if (idfCommandGrp.getMemberNumber(j).equals(commandName)){
					return idfCommandGrp;
				}
			}			
		}

		return null;
	}
	
    public IdfParameter getIdfParameter(String paramName) throws GemException {
    	int numStages = currentCommand.getNumStages();

    	for (int i=0; i<numStages; i++) {
    		IdfStage stage = currentCommand.getStage(i);

    		int k = stage.getNumParameters();
    		for (int j=0; j<k; j++) {
    			IdfParameter parameter = stage.getParameter(j);
    			if (paramName.equals(parameter.getParameterName())) {
    				return parameter;
    			}
    		}
    	}

    	return null;
    }
    
    public IdfParameter getIdfParameter(String paramName, int stageNumber) throws GemException {
		IdfStage stage = currentCommand.getStage(stageNumber);

		int k = stage.getNumParameters();
		for (int j = 0; j < k; j++) {
			IdfParameter parameter = stage.getParameter(j);
			if (paramName.equals(parameter.getParameterName())) {
				return parameter;
			}
		}

		return null;
    }
    
    public IdfParameter getIdfParameterByLabel(IdfCommand command, String paramLabel) throws GemException {
    	int numStages = command.getNumStages();

    	for (int i=0; i<numStages; i++) {
    		IdfStage stage = command.getStage(i);

    		int k = stage.getNumParameters();
    		for (int j=0; j<k; j++) {
    			IdfParameter parameter = stage.getParameter(j);
    			if (paramLabel.equals(parameter.getLabel())) {
    				return parameter;
    			}
    		}
    	}

    	return null;
    }
    
    public IdfQuestion getIdfQuestion(String questionName) throws GemException {    	
    	int k = currentCommand.getNumQuestions();
    	for (int j=0; j<k; j++) {
    		IdfQuestion question = currentCommand.getQuestion(j);
    		if (questionName.equals(question.getParameterName())) {
    			return question;
    		}
    	}

    	throw new GemException("Question " + questionName + " not found in current command");
    }
    
    public IdfQuestion getIdfQuestionByLabel(IdfCommand command, String questionLabel) throws GemException {    	
    	int k = command.getNumQuestions();
    	for (int j = 0; j < k; j++) {
    		IdfQuestion question = command.getQuestion(j);
    		if (questionLabel.equals(question.getLabel())) {
    			return question;
    		}
    	}

    	return null;
    }
    
    public int getQuestionNumber(String questionName) throws GemException {    	
    	int k = currentCommand.getNumQuestions();
    	for (int j=0; j<k; j++) {
    		IdfQuestion question = currentCommand.getQuestion(j);
    		if (questionName.equals(question.getParameterName())) {
    			return j;
    		}
    	}

    	throw new GemException("Question " + questionName + " not found in current command");
    }
    
    public IdfQuestion getIdfQuestion(int questionNumber) throws GemException { 
    	return currentCommand.getQuestion(questionNumber);
    }

    public List<IdfParameter> getParametersInCommand(boolean includeQuestions) {
    	List<IdfParameter> paramList = new ArrayList<IdfParameter>();
    	int numStages = currentCommand.getNumStages();

    	for (int i=0; i<numStages; i++) {
    		IdfStage stage = currentCommand.getStage(i);

    		int k = stage.getNumParameters();
    		for (int j=0; j<k; j++) {
    			IdfParameter parameter = stage.getParameter(j);
    			paramList.add(parameter);
    		}
    	}

    	if (includeQuestions){
    		int k = currentCommand.getNumQuestions();
	    	for (int j=0; j<k; j++) {
	    		IdfQuestion question = currentCommand.getQuestion(j);
	    		paramList.add(question);
	    	}
    	}    	

    	return paramList;
    }
    
    public List<IdfParameter> getParametersInStage(int stageNumber) {
    	List<IdfParameter> paramList = new ArrayList<IdfParameter>();
    	
		IdfStage stage = currentCommand.getStage(stageNumber);

		int k = stage.getNumParameters();
		for (int j = 0; j < k; j++) {
			IdfParameter parameter = stage.getParameter(j);
			paramList.add(parameter);
		}
    	
    	return paramList;
    }
    
    public List<IdfQuestion> getQuestionsInCommand() {
    	List<IdfQuestion> questionList = new ArrayList<IdfQuestion>();
    	
    	int k = currentCommand.getNumQuestions();
	    for (int j=0; j<k; j++) {
	    	IdfQuestion question = currentCommand.getQuestion(j);
	    	questionList.add(question);
	    }
	    
    	return questionList;
    }
    
    public IdfTable getTable(String tableName) throws GemException {
		int numTables = _idf.getNumTables();

		for (int i=0; i<numTables; i++) {
			IdfTable idfTable = _idf.getTableNumber(i);
			if (idfTable.getTableName().equals(tableName)) {
				return idfTable;
			}
		}
		throw new GemException("Table not declared in IDF: " + tableName);
	}  
    
    public List<IdfTable> getTables() {
    	List<IdfTable> tableList = new ArrayList<IdfTable>();
		int numTables = _idf.getNumTables();

		for (int i=0; i<numTables; i++) {
			IdfTable idfTable = _idf.getTableNumber(i);
			tableList.add(idfTable);
		}

		return tableList;
	}
    
    public List<IdfTable> getBrowsableTables() {
    	List<IdfTable> tableList = new ArrayList<IdfTable>();
		int numTables = _idf.getNumTables();

		for (int i=0; i<numTables; i++) {
			IdfTable idfTable = _idf.getTableNumber(i);
			if (idfTable.getBrowsable())
				tableList.add(idfTable);
		}

		return tableList;
	}
    
    public boolean hasQueryableParams() {
    	boolean output = false;
    	int numStages = currentCommand.getNumStages();

    	for (int i=0; i<numStages; i++) {
    		IdfStage stage = currentCommand.getStage(i);

    		int k = stage.getNumParameters();
    		for (int j=0; j<k; j++) {
    			IdfParameter parameter = stage.getParameter(j);
    			if (parameter.getType().equals("tableEntry")){
    				if (!(getTable(parameter.getSourceTable()).getBrowsable())){
    					output = true;
    					break;
    				}
    			}
    			else {
    				output = true;
					break;
    			}
    		}
    		
    		if (output)
    			break;
    	}
    	
    	return output;
    }
    
    public int getNumOfQueryableStages() {
    	int output = 0;
    	int numStages = currentCommand.getNumStages();

    	for (int i=0; i<numStages; i++) {
    		IdfStage stage = currentCommand.getStage(i);

    		int k = stage.getNumParameters();
    		for (int j=0; j<k; j++) {
    			IdfParameter parameter = stage.getParameter(j);
    			if (parameter.getType().equals("tableEntry")){
    				if (!(getTable(parameter.getSourceTable()).getBrowsable())){
    					output++;
    					break;
    				}
    			}
    			else {
    				output++;
					break;
    			}
    		}
    	}
    	
    	return output;
    }
    
    public int getNumOfQueryableStages(IdfCommand command) {
    	int output = 0;
    	int numStages = command.getNumStages();

    	for (int i=0; i<numStages; i++) {
    		IdfStage stage = command.getStage(i);

    		int k = stage.getNumParameters();
    		for (int j=0; j<k; j++) {
    			IdfParameter parameter = stage.getParameter(j);
    			if (parameter.getType().equals("tableEntry")){
    				if (!(getTable(parameter.getSourceTable()).getBrowsable())){
    					output++;
    					break;
    				}
    			}
    			else {
    				output++;
					break;
    			}
    		}
    	}
    	
    	return output;
    }
    
    public int getNumOfQueryableStagesBefore(IdfCommand command, int stageNumber) {
    	int output = 0;

    	for (int i=0; i<stageNumber; i++) {
    		IdfStage stage = command.getStage(i);

    		int k = stage.getNumParameters();
    		for (int j=0; j<k; j++) {
    			IdfParameter parameter = stage.getParameter(j);
    			if (parameter.getType().equals("tableEntry")){
    				if (!(getTable(parameter.getSourceTable()).getBrowsable())){
    					output++;
    					break;
    				}
    			}
    			else {
    				output++;
					break;
    			}
    		}
    	}
    	
    	return output;
    }
    
    public int getNumOfQueryableStagesAfter(IdfCommand command, int stageNumber) {
    	int output = 0;
    	int numStages = command.getNumStages();
    	
    	for (int i=stageNumber+1; i < numStages; i++) {
    		IdfStage stage = command.getStage(i);

    		int k = stage.getNumParameters();
    		for (int j=0; j<k; j++) {
    			IdfParameter parameter = stage.getParameter(j);
    			if (parameter.getType().equals("tableEntry")){
    				if (!(getTable(parameter.getSourceTable()).getBrowsable())){
    					output++;
    					break;
    				}
    			}
    			else {
    				output++;
					break;
    			}
    		}
    	}
    	
    	return output;
    }

	public boolean hasQueryableParams(int currentStage) {
		boolean output = false;
    	
		IdfStage stage = currentCommand.getStage(currentStage);

    	int k = stage.getNumParameters();
    	for (int j=0; j<k; j++) {
    		IdfParameter parameter = stage.getParameter(j);
    		if (parameter.getType().equals("tableEntry")){
    			if (!(getTable(parameter.getSourceTable()).getBrowsable())){
    				output = true;
    				break;
    			}
    		}
    		else {
    			output = true;
				break;
    		}
    	}
    	
    	return output;
	}

	public List<IdfParameter> getQueryableParameters(int currentStage) {
    	List<IdfParameter> paramList = new ArrayList<IdfParameter>();

    	IdfStage stage = currentCommand.getStage(currentStage);

    	int k = stage.getNumParameters();
    	for (int j=0; j<k; j++) {
    		IdfParameter parameter = stage.getParameter(j);
    		if (parameter.getType().equals("tableEntry")){
    			if (!(getTable(parameter.getSourceTable()).getBrowsable())){
        			paramList.add(parameter);  				
    			}
    		}
    		else {
    			paramList.add(parameter);
    		}
    	}

    	return paramList;
    }
	
	public IdfCommand getCommand() {		
		return currentCommand;
	}
	
	public int numberOfStages(){
		return currentCommand.getNumStages();
	}

}
