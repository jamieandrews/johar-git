/** IdfCommandGroup.java
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


public class IdfCommandGroup extends IdfElement {
    private Vector<String> _memberVector;
    private String _commandGroupName;
    private String _mnemonic;

    public IdfCommandGroup(Element domElement, ErrorHandler eh) {
	super(domElement, eh, "CommandGroup");
	_memberVector = new Vector<String>();

	_commandGroupName = domElement.getAttribute("name");
	setElementName(_commandGroupName);
	String ccConvertedName =
	    TextInputValidator.titleCaseTranslation(_commandGroupName);

	complainIfMoreThanOne("Mnemonic");
	_mnemonic = extractAttr("Mnemonic", "");

	NodeList memberList = _domElement.getElementsByTagName("Member");
	int n = memberList.getLength();
	for (int i=0; i<n; i++) {
	    Element e = (Element) memberList.item(i);
	    String member =
		e.getChildNodes().item(0).getNodeValue().trim();
	    _memberVector.add(member);
	}

	// Release document and error handler for eventual GC
	_domElement = null;
	_eh = null;
    }

    // Constructor for when no explicit command groups are given.
    public IdfCommandGroup(Vector<IdfCommand> allCommands) {
	super(null, null, "CommandGroup");
	_memberVector = new Vector<String>();
	_commandGroupName = "Commands";
	setElementName(_commandGroupName);
	String ccConvertedName =
	    TextInputValidator.titleCaseTranslation(_commandGroupName);
	for (IdfCommand c : allCommands) {
	    String member = c.getCommandName();
	    _memberVector.add(member);
	}
	_mnemonic = "C";
    }

    // For toString.
    public int indentLevel() {
	return 2;
    }

    // For toString.  Overrides superclass method.
    public void contentsToString() {
	for (int i=0; i<_memberVector.size(); i++) {
	    fieldToString("Member", _memberVector.elementAt(i));
	}
	fieldToString("Mnemonic", _mnemonic);
    }

    // Getters.

    public int getNumMembers() {
	return _memberVector.size();
    }

    public String getMemberNumber(int i) {
	return _memberVector.elementAt(i);
    }

    public String getMnemonic() {
	return _mnemonic;
    }

    // Standard methods to effect double dispatch.
    public void beforeChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }
    public void afterChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }

}

