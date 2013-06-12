/* GemFullImplementation.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

package johar.gem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;
import johar.gem.GemImplementation;
import johar.gem.GemSetting;
import johar.idf.*;

public class GemFullImplementation
extends johar.gem.GemImplementation
implements johar.gem.GemSetting {

    protected Class<?> _appEngineClass;
    protected Object _appEngineObject;
    protected IdfCommand _currentCommand;
    protected int _maxSelectableStageNumber;
    protected IdfStage _currentStage;

    public GemFullImplementation(Idf idf, ShowTextHandler showTextHandler) {
	super(idf, showTextHandler);
	_appEngineClass = null;
	_appEngineObject = null;
	_currentCommand = null;
	_maxSelectableStageNumber = 0;
	_currentStage = null;
    }

    public void validate()
    throws IdfFormatException {
	String appEngineClassName = _idf.getApplicationEngine();

	// Get the class and create an instance of it.
	// If there are any problems, complain.
	try {
	    _appEngineClass = Class.forName(appEngineClassName);
	    _appEngineObject = _appEngineClass.newInstance();
	} catch (Exception e) {
	    throw new IdfFormatException(
		"Could not create app engine instance: " +
		e.getMessage()
	    );
	}
	
	// Now validate that all the mentioned methods are there.
	MethodValidator methodValidator =
	    new MethodValidator(_appEngineClass);
	_idf.acceptVisitor(methodValidator);
	String errorMessage = methodValidator.getErrorMessage();
	if (!errorMessage.equals("")) {
	    throw new IdfFormatException(
		"Not all methods mentioned in IDF " +
		"are declared properly in app engine:\n" +
		errorMessage
	    );
	}
    }

    private static class MethodValidator extends VisitorOfIdfElement {
	private Class<?> _appEngineClass;
	private StringBuffer _sb; // will collect error messages

	public MethodValidator(Class<?> appEngineClass) {
	    _appEngineClass = appEngineClass;
	    _sb = new StringBuffer();
	}

	public void beforeChildren(Idf idf, ErrorHandler eh) {
	    checkMethod(idf.getInitializationMethod(), void.class);
	}

	public void beforeChildren(IdfCommand idfCommand, ErrorHandler eh) {
	    String methodName;

	    // check ActiveIfMethod
	    methodName = idfCommand.getActiveIfMethod();
	    if (!methodName.equals("")) {
		checkMethod(methodName, boolean.class);
	    }

	    // check CommandMethod
	    methodName = idfCommand.getCommandMethod();
	    checkMethod(methodName, void.class);

	    // check QuitAfterIfMethod
	    methodName = idfCommand.getQuitAfterIfMethod();
	    if (!methodName.equals("")) {
		checkMethod(methodName, boolean.class);
	    }
	}

	public void beforeChildren(IdfParameter idfParameter, ErrorHandler eh) {
	    String methodName;

	    // check DefaultValueMethod
	    methodName = idfParameter.getDefaultValueMethod();
	    if (!methodName.equals("")) {
		checkMethod(methodName, java.lang.Object.class);
	    }
	}

	public void beforeChildren(IdfQuestion idfQuestion, ErrorHandler eh) {
	    String methodName;

	    // check DefaultValueMethod
	    methodName = idfQuestion.getDefaultValueMethod();
	    if (!methodName.equals("")) {
		checkMethod(methodName, java.lang.Object.class);
	    }

	    // check AskIfMethod
	    methodName = idfQuestion.getAskIfMethod();
	    checkMethod(methodName, boolean.class);
	}

	public void beforeChildren(IdfStage idfStage, ErrorHandler eh) {
	    String methodName;

	    // check ParameterCheckMethod
	    methodName = idfStage.getParameterCheckMethod();
	    if (!methodName.equals("")) {
		checkMethod(methodName, java.lang.String.class);
	    }
	}

	private void checkMethod(String methodName, Class<?> returnValueClass) {
	    Method m = null;
	    try {
		m = _appEngineClass.getMethod(methodName, johar.gem.Gem.class);
	    } catch (Exception e) {
		_sb.append(
		    "Method " + methodName +
		    " either does not exist, or does not take one parameter" +
		    " of type Gem\n"
		);
	    }
	    if (m != null) {
		Class<?> returnType = m.getReturnType();
		if (!returnValueClass.isAssignableFrom(returnType)) {
		    _sb.append(
			"Return type of method " + methodName +
			" is incorrect\n"
		    );
		}
	    }
	}

	public String getErrorMessage() {
	    return _sb.toString();
	}
    }

    private static class TableAdder extends VisitorOfIdfElement {
	private Map<String, TableStructure> _mapStringTableStructure;

	public TableAdder(Map<String,TableStructure> mapStringTableStructure) {
	    _mapStringTableStructure = mapStringTableStructure;
	}

	public void beforeChildren(IdfTable t, ErrorHandler eh) {
	    String name = t.getName();
	    String defaultHeading = t.getDefaultHeading();
	    boolean isBrowsable = t.getBrowsable();

	    _mapStringTableStructure.put(name,
		new TableStructure(defaultHeading, isBrowsable));
	}
    }

    public void initializeAppEngine() {
	// Set up each individual table
	TableAdder tableAdder = new TableAdder(_mapStringTableStructure);
	_idf.acceptVisitor(tableAdder);

	// Call initialization method
	String initializationMethodName = _idf.getInitializationMethod();
	Object o = callAppEngineMethod(initializationMethodName);
	// Don't need the return value.
    }

    private Object callAppEngineMethod(String methodName) {
	Object returnValue = null;
	try {
	    Method m = _appEngineClass.getMethod(methodName, johar.gem.Gem.class);
	    returnValue = m.invoke(_appEngineObject, this);
	} catch (Exception e) {
	    // Problem!! The app engine method has thrown an exception.
	    // Show a high-priority text message to the user
	    _showTextHandler.showText(
		"Application Engine Error:  method " + methodName +
		" threw exception.\nException message: " +
		e.getMessage(),
		3000
	    );
	}

	return returnValue;
    }

    public boolean methodIsActive(String commandName)
    throws GemException {
	boolean returnValue;

	String activeIfMethodName =
	    getIdfCommand(commandName).getActiveIfMethod();

	if (activeIfMethodName.equals("")) {
	    // it's always active
	    returnValue = true;
	} else {
	    // Should be able to cast to boolean, since we have
	    // already checked that the command has the correct
	    // return type
	    returnValue =
		((Boolean) callAppEngineMethod(activeIfMethodName))
		.booleanValue();
	}

	return returnValue;
    }

    private IdfCommand getIdfCommand(String commandName)
    throws GemException {
	int numCommands = _idf.getNumCommands();

	for (int i=0; i<numCommands; i++) {
	    // See if this command is the one we're looking for
	    IdfCommand idfCommand = _idf.getCommandNumber(i);
	    if (commandName.equals(idfCommand.getCommandName())) {
		return idfCommand;
	    }
	}

	// If we have gone through the commands without finding it
	throw new GemException("Command not declared in IDF: " + commandName);
    }

    public void selectCurrentCommand(String commandName)
    throws GemException {
	_currentCommand = null;
	_currentCommand = getIdfCommand(commandName);
	_maxSelectableStageNumber = 0;
	_currentStage = null;
    }

    public void selectCurrentStage(int stageNumber)
    throws GemException {
	if (stageNumber >= _currentCommand.getNumStages()) {
	    throw new GemException("Not that many stages in current command");
	} else if (stageNumber > _maxSelectableStageNumber) {
	    throw new GemException("Stage number out of sequence");
	} else if (stageNumber == _maxSelectableStageNumber) {
	    _maxSelectableStageNumber++;
	}
	_currentStage = _currentCommand.getStage(stageNumber);
    }

    public void clearParameters()
    throws GemException {
	_mapStringParamValuesInfo =
	    new java.util.HashMap<String, ParamValuesInfo>();
    }

    // Also has the effect of throwing an exception if param does not exist.
    private IdfParameter getIdfParameter(String paramName)
    throws GemException {
	int numStages = _currentCommand.getNumStages();

	for (int i=0; i<numStages; i++) {
	    IdfStage stage = _currentCommand.getStage(i);

	    // Look through the parameters for this stage
	    int k = stage.getNumParameters();
	    for (int j=0; j<k; j++) {
		IdfParameter parameter = stage.getParameter(j);
		if (paramName.equals(parameter.getParameterName())) {
		    return parameter;
		}
	    }
	}

	int k = _currentCommand.getNumQuestions();
	for (int j=0; j<k; j++) {
	    IdfQuestion question = _currentCommand.getQuestion(j);
	    if (paramName.equals(question.getParameterName())) {
		return question;
	    }
	}

	// If we have got here, we must not have found it
	throw new GemException(
	    "Parameter " + paramName + " not found in current command");
    }

    private String getParameterType(String paramName)
    throws GemException {
	IdfParameter parameter = getIdfParameter(paramName);
	return parameter.getType();
    }

    private ParamValuesInfo getParamValuesInfo(String paramName,
	String[] acceptableParamTypes, String valueTypeName)
    throws GemException {
	ParamValuesInfo pvi = null;

	String paramType = getParameterType(paramName);
	for (int i=0; i<acceptableParamTypes.length; i++) {
	    if (paramType.equals(acceptableParamTypes[i])) {
		pvi = _mapStringParamValuesInfo.get(paramName);
		if (pvi==null) {
		    pvi = new ParamValuesInfo();
		    _mapStringParamValuesInfo.put(paramName, pvi);
		}
		return pvi;
	    }
	}

	// If here, it's not of the right type
	throw new GemException(
	    "setParameterValue: parameter " + paramName +
	    " cannot take " + valueTypeName + " as value"
	);
    }

    public void setParameterValue(String paramName, int repNumber, Object o)
    throws GemException {
	ParamValuesInfo pvi = getParamValuesInfo(
	    paramName, new String[] {"date","file","timeOfDay"}, "Object");
	pvi.setParameterValue(repNumber, o);
    }

    public void setParameterValue(String paramName, int repNumber, long l)
    throws GemException {
	ParamValuesInfo pvi = getParamValuesInfo(
	    paramName, new String[] {"int"}, "long");
	pvi.setParameterValue(repNumber, l);
    }

    public void setParameterValue(String paramName, int repNumber, double d)
    throws GemException {
	ParamValuesInfo pvi = getParamValuesInfo(
	    paramName, new String[] {"float"}, "double");
	pvi.setParameterValue(repNumber, d);
    }

    public void setParameterValue(String paramName, int repNumber, boolean b)
    throws GemException {
	ParamValuesInfo pvi = getParamValuesInfo(
	    paramName, new String[] {"boolean"}, "boolean");
	pvi.setParameterValue(repNumber, b);
    }

    public void setParameterValue(String paramName, int repNumber, String s)
    throws GemException {
	ParamValuesInfo pvi = getParamValuesInfo(
	    paramName, new String[] {"choice","text"}, "String");
	pvi.setParameterValue(repNumber, s);
    }

    public boolean parameterCheckSucceeds()
    throws GemException {
	boolean returnValue;

	String parameterCheckMethodName =
	    _currentStage.getParameterCheckMethod();

	if (parameterCheckMethodName.equals("")) {
	    returnValue = true;
	} else {
	    returnValue =
		((Boolean) callAppEngineMethod(parameterCheckMethodName))
		.booleanValue();
	}

	return returnValue;
    }

    public boolean questionShouldBeAsked(String questionName)
    throws GemException {
	IdfQuestion idfQuestion =
	    (IdfQuestion) getIdfParameter(questionName);
	String askIfMethodName = idfQuestion.getAskIfMethod();

	boolean returnValue =
	    ((Boolean) callAppEngineMethod(askIfMethodName))
	    .booleanValue();

	return returnValue;
    }

    public Object callDefaultValueMethod(String paramOrQuestionName)
    throws GemException {
	Object returnValue;

	IdfParameter idfParameter =
	    (IdfQuestion) getIdfParameter(paramOrQuestionName);
	String defaultValueMethodName = idfParameter.getDefaultValueMethod();

	if (defaultValueMethodName.equals("")) {
	    returnValue = null;
	} else {
	    returnValue = callAppEngineMethod(defaultValueMethodName);
	}

	return returnValue;
    }

    public void callCommandMethod()
    throws GemException {
	String commandMethodName = _currentCommand.getCommandMethod();
	callAppEngineMethod(commandMethodName);
    }

    public boolean quitAfterCurrentCommand()
    throws GemException {
	String quitAfterIfMethodName = _currentCommand.getQuitAfterIfMethod();
	if (quitAfterIfMethodName.equals("")) {
	    return _currentCommand.getQuitAfter();
	}

	boolean returnValue =
	    ((Boolean) callAppEngineMethod(quitAfterIfMethodName))
	    .booleanValue();

	return returnValue;
    }

    public void setShowTextHandler(ShowTextHandler showTextHandler) {
	_showTextHandler = showTextHandler;
    }

    public ShowTextHandler getShowTextHandler() {
	return _showTextHandler;
    }

}

