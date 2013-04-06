/* IdfCommand.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

package johar.idf;

import java.util.Vector;

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
    private Vector<IdfStage> _stageVector;
    private int _numStages;

    // The attributes of Command
    private String _commandName;
    private String _activeIfMethod;
    private String _label;
    private String _briefHelp;
    private String _oneLineHelp;
    private String _multiLineHelp;
    private int _prominence;
    private boolean _quitAfter;
    private String _quitAfterIfMethod;
    public static final int BRIEF_HELP_LENGTH = 30;
    public static final int ONELINE_HELP_LENGTH = 80;

    public IdfCommand(Element domElement, ErrorHandler eh) {
	super(domElement, eh, "Command");
	_stageVector = new Vector<IdfStage>();
	_numStages = 0;

	_commandName = domElement.getAttribute("name");
	setElementName(_commandName);
	String ccConvertedName =
	    TextInputValidator.titleCaseTranslation(_commandName);

	// Confirm counts of attributes
	complainIfMoreThanOne("ActiveIfMethod");
	complainIfMoreThanOne("Label");
	complainIfMoreThanOne("BriefHelp");
	complainIfMoreThanOne("OneLineHelp");
	complainIfMoreThanOne("MultiLineHelp");
	complainIfMoreThanOne("Prominence");
	complainIfMoreThanOne("QuitAfter");

	// Extract attributes
	_activeIfMethod = extractAttr("ActiveIfMethod", "");
	_label = extractAttr("Label", ccConvertedName);
	_briefHelp = extractAttr("BriefHelp", _label);
	_oneLineHelp = extractAttr("OneLineHelp", _briefHelp);
	_multiLineHelp = extractAttr("MultiLineHelp", _oneLineHelp);
	_prominence = extractAttr("Prominence", 1000);
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

	// Release document and error handler for eventual GC
	_domElement = null;
	_eh = null;
    }

    public void processStages() {
	NodeList stageList = _domElement.getElementsByTagName("Stage");
	int n = stageList.getLength();
	for (int i=0; i<n; i++) {
	    Element e = (Element) stageList.item(i);
	    IdfStage stage = new IdfStage(e, _eh, _label);
	    _stageVector.add(stage);
	    _numStages += 1;
	}
    }

    public void processContentsWithoutStages() {
	IdfStage stage = new IdfStage(_domElement, _eh, _label);
	_stageVector.add(stage);
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
	fieldToString("Label", _label);
	fieldToString("BriefHelp", _briefHelp);
	fieldToString("OneLineHelp", _oneLineHelp);
	fieldToString("MultiLineHelp", _multiLineHelp);
	fieldToString("Prominence", new Integer(_prominence).toString());
	fieldToString("QuitAfter", new Boolean(_quitAfter).toString());
	fieldToString("QuitAfterIfMethod", _quitAfterIfMethod);
	elementVectorToString(_stageVector);
    }

    // Getters.

    public int getNumStages() {
	return _numStages;
    }

    public IdfStage getStageNumber(int i) {
	return _stageVector.elementAt(i);
    }

    public String getCommandName() {
	return _commandName;
    }

    public String getActiveIfMethod() {
	return _activeIfMethod;
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

    public int getProminence() {
	return _prominence;
    }

    public boolean getQuitAfter() {
	return _quitAfter;
    }

    public String getQuitAfterIfMethod() {
	return _quitAfterIfMethod;
    }

    public void passVisitorToChildren(VisitorOfIdfElement visitor) {
	int j = _stageVector.size();
	for (int i=0; i<j; i++) {
	    _stageVector.elementAt(i).acceptVisitor(visitor);
	}
    }

    // Standard methods to effect double dispatch.
    public void beforeChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }
    public void afterChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }

}

