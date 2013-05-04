/* IdfStage.java
 * This source file is part of the Johar project.
 * @author Jamie Andrews
 */

package johar.idf;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import johar.utilities.TextInputValidator;

public class IdfStage extends IdfElement {
    // The attributes of Stage (or Command, if only one stage)
    private String _parameterCheckMethod;
    private List<IdfParameter> _parameterList;

    public IdfStage(Element domElement, ErrorHandler eh, String commandLabel) {
	super(domElement, eh, "Stage");

	NodeList nodeList;
	int n;

	_parameterList = new ArrayList<IdfParameter>();

	// Confirm counts of attributes
	complainIfMoreThanOne("ParameterCheckMethod");

	_parameterCheckMethod = extractAttr("ParameterCheckMethod", "");

	nodeList = domElement.getElementsByTagName("Parameter");
	n = nodeList.getLength();
	for (int i=0; i<n; i++) {
	    Element e = (Element) nodeList.item(i);
	    IdfParameter p = new IdfParameter(e, _eh);
	    _parameterList.add(p);
	}
    }

    // Getters.

    public String getParameterCheckMethod() {
	return _parameterCheckMethod;
    }

    public int getNumParameters() {
	return _parameterList.size();
    }

    public IdfParameter getParameter(int i) {
	return _parameterList.get(i);
    }

    // For displaying.

    public int indentLevel() {
	return 4;
    }

    public void contentsToString() {
	fieldToString("ParameterCheckMethod", _parameterCheckMethod);
	elementListToString(_parameterList);
    }

    public void passVisitorToChildren(VisitorOfIdfElement visitor) {
	int i, j;

	j = _parameterList.size();
	for (i=0; i<j; i++) {
	    _parameterList.get(i).acceptVisitor(visitor);
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

