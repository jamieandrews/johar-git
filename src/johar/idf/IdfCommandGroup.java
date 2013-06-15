/** IdfCommandGroup.java
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


public class IdfCommandGroup extends IdfElement {
    private List<String> _memberList;
    private String _commandGroupName;
    private String _label;

    public IdfCommandGroup(Element domElement, ErrorHandler eh) {
	super(domElement, eh, "CommandGroup");
	_memberList = new ArrayList<String>();

	_commandGroupName = domElement.getAttribute("name");
	setElementName(_commandGroupName);
	String ccConvertedName =
	    TextInputValidator.titleCaseTranslation(_commandGroupName);

	complainIfMoreThanOne("Label");
	_label = extractAttr("Label", ccConvertedName);

	checkNumInstances(true, "Member",
	    1, Integer.MAX_VALUE,
	    1, Integer.MAX_VALUE, "");

	NodeList memberList = _domElement.getElementsByTagName("Member");
	int n = memberList.getLength();
	//if (n == 0){
	//    _eh.error("CommandGroup " + _commandGroupName +
	//	": must have at least one Member");
	//}
	for (int i=0; i<n; i++) {
	    Element e = (Element) memberList.item(i);
	    String member =
		e.getChildNodes().item(0).getNodeValue().trim();
	    _memberList.add(member);
	}
    }

    // Constructor for when no explicit command groups are given.
    public IdfCommandGroup(List<IdfCommand> allCommands) {
	super(null, null, "CommandGroup");
	_memberList = new ArrayList<String>();
	_commandGroupName = "Commands";
	setElementName(_commandGroupName);
	String ccConvertedName =
	    TextInputValidator.titleCaseTranslation(_commandGroupName);
	for (IdfCommand c : allCommands) {
	    String member = c.getCommandName();
	    _memberList.add(member);
	}
	_label = "Commands";
    }

    // For toString.
    public int indentLevel() {
	return 2;
    }

    // For toString.  Overrides superclass method.
    public void contentsToString() {
	fieldToString("Label", _label);
	for (int i=0; i<_memberList.size(); i++) {
	    fieldToString("Member", _memberList.get(i));
	}
    }

    // Getters.

    public String getCommandGroupName() {
	return _commandGroupName;
    }

    public int getNumMembers() {
	return _memberList.size();
    }

    public String getMemberNumber(int i) {
	return _memberList.get(i);
    }

    public String getLabel() {
	return _label;
    }

    // Standard methods to effect double dispatch.
    public void beforeChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }
    public void afterChildren(VisitorOfIdfElement visitor) {
	visitor.afterChildren(this, _eh);
    }

}

