/* IdfTable.java
 * This source file is part of the Johar project.
 * @author Jamie Andrews
 */

package johar.idf;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import johar.utilities.TextInputValidator;

public class IdfTable extends IdfElement {
    // The attributes of Table
    private String  _tableName;
    private boolean _browsable;
    private String  _defaultHeading;
    private String  _label;

    public IdfTable(Element domElement, ErrorHandler eh) {
	super(domElement, eh, "Table");

	NodeList nodeList;
	int n;

	_tableName = domElement.getAttribute("name");
	setElementName(_tableName);
	String ccConvertedName =
	    TextInputValidator.titleCaseTranslation(_tableName);

	// Confirm counts of attributes
	complainIfMoreThanOne("Browsable");
	complainIfMoreThanOne("DefaultHeading");
	complainIfMoreThanOne("Label");

	_browsable = extractAttr("Browsable", true);
	_defaultHeading = extractAttr("DefaultHeading", ccConvertedName);
	_label = extractAttr("Label", ccConvertedName);
    }

    // Getters.

    public String getName() {
	return _tableName;
    }

    public boolean getBrowsable() {
	return _browsable;
    }

    public String getDefaultHeading() {
	return _defaultHeading;
    }

    // For displaying.

    public int indentLevel() {
	return 2;
    }

    public void contentsToString() {
	fieldToString("Browsable", (_browsable ? "true" : "false"));
	fieldToString("DefaultHeading", _defaultHeading);
	fieldToString("Label", _label);
    }

    // Standard methods to effect double dispatch.
    public void beforeChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }
    public void afterChildren(VisitorOfIdfElement visitor) {
	visitor.afterChildren(this, _eh);
    }

}

