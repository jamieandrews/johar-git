/* IdfCommand.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

package johar.idf;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import johar.utilities.TextInputValidator;

/**
 * A class which stores the information about a command. An IDF consists of
 *many
 * commands, and the commands are selected by the user, through an interface
 * interprester. The interface interpreter then requests the application
 *engine
 * to execute a particular command.
 *
 * @author Fatima Hussain
 * @author Jamie Andrews
 * @version v1
 */
public class IdfCommand extends IdfElement {
    // The command stages (there is at least one)
    private List<IdfStage> _stageList;
    private List<IdfQuestion> _questionList;
    private int _numStages;

    // The attributes of Command
    private String _commandName;
    private String _activeIfMethod;
    private String _commandMethod;
    private String _label;
    private String _briefHelp;
    private String _oneLineHelp;
    private String _multiLineHelp;
    private long _prominence;
    private boolean _quitAfter;
    private String _quitAfterIfMethod;
    public static final long BRIEF_HELP_LENGTH = 30;
    public static final long ONELINE_HELP_LENGTH = 80;

    public IdfCommand(Element domElement, ErrorHandler eh) {
	super(domElement, eh, "Command");
	_stageList = new ArrayList<IdfStage>();
	_numStages = 0;
	_questionList = new ArrayList<IdfQuestion>();

	_commandName = domElement.getAttribute("name");
	setElementName(_commandName);
	String ccConvertedName =
	    TextInputValidator.titleCaseTranslation(_commandName);

	// Confirm counts of attributes
	complainIfMoreThanOne("ActiveIfMethod");
	complainIfMoreThanOne("BriefHelp");
	complainIfMoreThanOne("CommandMethod");
	complainIfMoreThanOne("Label");
	complainIfMoreThanOne("MultiLineHelp");
	complainIfMoreThanOne("OneLineHelp");
	complainIfMoreThanOne("Prominence");
	complainIfMoreThanOne("QuitAfter");

	// Extract help attributes in order
	_label = extractAttr("Label", ccConvertedName);
	_briefHelp = extractAttr("BriefHelp", _label);
	_oneLineHelp = extractAttr("OneLineHelp", _briefHelp);
	_multiLineHelp = extractAttr("MultiLineHelp", _oneLineHelp);

	// Extract other attributes
	_activeIfMethod = extractAttr("ActiveIfMethod", "");
	_commandMethod = extractAttr("CommandMethod", _commandName);
	try {
	    _prominence = extractAttr("Prominence", 2000);
	} catch (IdfFormatException e) {
	    _eh.error("Command " + _commandName +
		": value of Prominence must be an integer"
	    );
	}
	_quitAfter = extractAttr("QuitAfter", false);

	// QuitAfterIfMethod only if QuitAfter not defined
	_quitAfterIfMethod =
	    extractAttrIf(countNumberOf("QuitAfter") < 1,
		"QuitAfterIfMethod", 0, 1, 0, 0, "",
		"Command has QuitAfter attribute.");

	// Either process stages, or else process contents without
	int numStages = countNumberOf("Stage");
	if (numStages > 0) {
	    processStages();
	} else {
	    processContentsWithoutStages();
	}

	// Process questions
	NodeList questionList = domElement.getElementsByTagName("Question");
	int n = questionList.getLength();
	for (int i=0; i<n; i++) {
	    Element e = (Element) questionList.item(i);
	    IdfQuestion q = new IdfQuestion(e, _eh);
	    _questionList.add(q);
	}
    }

    public void processStages() {
	NodeList stageList = _domElement.getElementsByTagName("Stage");
	int n = stageList.getLength();
	for (int i=0; i<n; i++) {
	    Element e = (Element) stageList.item(i);
	    IdfStage stage = new IdfStage(e, _eh, _label);
	    _stageList.add(stage);
	    _numStages += 1;
	}
    }

    public void processContentsWithoutStages() {
	IdfStage stage = new IdfStage(_domElement, _eh, _label);
	_stageList.add(stage);
	_numStages = 1;
    }

    // For toString.  Overrides superclass method.
    public int indentLevel() {
	return 2;
    }

    // For toString.  Overrides superclass method.
    public void contentsToString() {
	fieldToString("CommandName", _commandName);
	fieldToString("ActiveIfMethod", _activeIfMethod);
	fieldToString("BriefHelp", _briefHelp);
	fieldToString("CommandMethod", _commandMethod);
	fieldToString("Label", _label);
	fieldToString("MultiLineHelp", _multiLineHelp);
	fieldToString("OneLineHelp", _oneLineHelp);
	fieldToString("Prominence", new Long(_prominence).toString());
	fieldToString("QuitAfter", new Boolean(_quitAfter).toString());
	fieldToString("QuitAfterIfMethod", _quitAfterIfMethod);
	elementListToString(_stageList);
	elementListToString(_questionList);
    }

    // Getters.

    public int getNumStages() {
	return _numStages;
    }

    public IdfStage getStage(int i) {
	return _stageList.get(i);
    }

    public int getNumQuestions() {
	return _questionList.size();
    }

    public IdfQuestion getQuestion(int i) {
	return _questionList.get(i);
    }

    public String getCommandName() {
	return _commandName;
    }

    public String getActiveIfMethod() {
	return _activeIfMethod;
    }

    public String getCommandMethod() {
	return _commandMethod;
    }

    public String getLabel() {
	return _label;
    }

    public String getBriefHelp() {
	return _briefHelp;
    }

    public String getOneLineHelp() {
	return _oneLineHelp;
    }

    public String getMultiLineHelp() {
	return _multiLineHelp;
    }

    public long getProminence() {
	return _prominence;
    }

    public boolean getQuitAfter() {
	return _quitAfter;
    }

    public String getQuitAfterIfMethod() {
	return _quitAfterIfMethod;
    }

    public void passVisitorToChildren(VisitorOfIdfElement visitor) {
	int i, j;

	j = _stageList.size();
	for (i=0; i<j; i++) {
	    _stageList.get(i).acceptVisitor(visitor);
	}

	j = _questionList.size();
	for (i=0; i<j; i++) {
	    _questionList.get(i).acceptVisitor(visitor);
	}
    }

    // Standard methods to effect double dispatch.
    public void beforeChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }
    public void afterChildren(VisitorOfIdfElement visitor) {
	visitor.afterChildren(this, _eh);
    }

}

