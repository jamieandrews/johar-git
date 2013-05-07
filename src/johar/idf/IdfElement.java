/* IdfElement.java
 * This source file is part of the Johar project.
 *
 * @author Jamie Andrews
 */

package johar.idf;
import java.io.File;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import johar.utilities.TextInputValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class IdfElement {

    protected Element _domElement;
    protected ErrorHandler _eh;
    protected String _elementKind;
    protected String _elementName;

    public IdfElement(Element domElement, ErrorHandler eh, String elementKind) {
	_domElement = domElement;
	_eh = eh;
	_elementKind = elementKind;
	_elementName = "";
    }

    public void setElementName(String elementName) {
	_elementName = " " + elementName;
    }

    // getElementsByTagName returns all elements at any depth in the
    // document.  We must instead return just the children with the
    // appropriate tag names.  Hence the helper class IdfNodeList and
    // this method.
    protected NodeList getChildrenByTagName(String tagName) {
        NodeList nodeList = _domElement.getChildNodes();
        int length = nodeList.getLength();

        IdfNodeList children = new IdfNodeList();

        for (int i=0; i<length; i++) {
            if (nodeList.item(i).getNodeName().equals(tagName)) {
                children.add(nodeList.item(i));
            }
        }

	return children;
    }

    protected void complainIfMoreThanOne(String tagName) {
	NodeList nodeList = getChildrenByTagName(tagName);
	if (nodeList.getLength() > 1) {
	    _eh.error(
		_elementKind + _elementName +
		" must have no more than one " + tagName + " attribute."
	    );
	}
    }

    protected void complainIfMoreThanZero(String tagName,
	     String reason) {
	NodeList nodeList = getChildrenByTagName(tagName);
	if (nodeList.getLength() > 1) {
	    _eh.error(
		_elementKind + _elementName +
		" must not have a " + tagName + " attribute.\n" +
		reason
	    );
	}
    }

    protected int countNumberOf(String tagName) {
	NodeList nodeList = getChildrenByTagName(tagName);
	return nodeList.getLength();
    }

    protected String getContentsFirstNode(NodeList nodeList) {
	Element attr = (Element) nodeList.item(0);
	return attr.getChildNodes().item(0).getNodeValue().trim();
    }

    protected String extractAttr(String attrName, String defaultValue) {
	NodeList nodeList = getChildrenByTagName(attrName);
	if (nodeList.getLength() == 0) {
	    return defaultValue;
	} else {
	    String contents = getContentsFirstNode(nodeList);
	    return contents;
	}
    }

    protected long extractAttr(String attrName, long defaultValue)
	    throws IdfFormatException {
	NodeList nodeList = getChildrenByTagName(attrName);
	if (nodeList.getLength() == 0) {
	    return defaultValue;
	} else {
	    String contents = getContentsFirstNode(nodeList);
	    if (contents.equals("unlim")) {
		return Long.MAX_VALUE;
	    } else {
		try {
		    return Long.parseLong(contents);
		} catch (NumberFormatException e) {
		    throw new IdfFormatException(
			"attribute " + attrName +
			": value should be an integer"
		    );
		}
	    }
	}
    }

    protected double extractAttr(String attrName, double defaultValue)
	    throws IdfFormatException {
	NodeList nodeList = getChildrenByTagName(attrName);
	if (nodeList.getLength() == 0) {
	    return defaultValue;
	} else {
	    String contents = getContentsFirstNode(nodeList);
	    if (contents.equals("unlim")) {
		return Double.MAX_VALUE;
	    } else {
		try {
		    return Double.parseDouble(contents);
		} catch (NumberFormatException e) {
		    throw new IdfFormatException(
			"attribute " + attrName +
			": value should be a floating-point number"
		    );
		}
	    }
	}
    }

    protected boolean extractAttr(String attrName, boolean defaultValue) {
	NodeList nodeList = getChildrenByTagName(attrName);
	if (nodeList.getLength() == 0) {
	    return defaultValue;
	} else {
	    String contents = getContentsFirstNode(nodeList);
	    if (contents.equals("yes") || contents.equals("true") ||
		contents.equals("Yes") || contents.equals("True") ||
		contents.equals("YES") || contents.equals("TRUE")) {
		return true;
	    } else {
		return false;
	    }
	}
    }

    /** Complain if number of instances is too low or high.
      * @param cond The condition to evaluate.
      * @param attrName The name of the attribute to check.
      * @param minIfTrue The minimum number of instances of the attribute
      *		to insist on if cond is true.
      * @param maxIfTrue The minimum number of instances of the attribute
      *		to insist on if cond is true.
      * @param minIfFalse The minimum number of instances of the attribute
      *		to insist on if cond is false.
      * @param maxIfFalse The minimum number of instances of the attribute
      *		to insist on if cond is false.
      * @param reasonForComplaint The reason to be appended to the
      *		error message if cond is false.
      */
    protected void checkNumInstances(boolean cond, String attrName,
	    int minIfTrue, int maxIfTrue, int minIfFalse, int maxIfFalse, 
	    String reasonForComplaint) {
	NodeList nodeList = getChildrenByTagName(attrName);
	int count = nodeList.getLength();
	if (cond) {
	    if ((count != minIfTrue) && (minIfTrue == maxIfTrue)) {
		_eh.error(
		    _elementKind + _elementName +
		    " must have exactly " + minIfTrue +
		    " " + attrName + " attribute(s)."
		);
	    } else if (count < minIfTrue) {
		_eh.error(
		    _elementKind + _elementName +
		    " must have at least " + minIfTrue +
		    " " + attrName + " attribute(s)."
		);
	    } else if (count > maxIfTrue) {
		_eh.error(
		    _elementKind + _elementName +
		    " must have no more than " + maxIfTrue +
		    " " + attrName + " attribute(s)."
		);
	    }
	} else { // condition is not true
	    if ((count != minIfFalse) && (minIfFalse == maxIfFalse)) {
		_eh.error(
		    _elementKind + _elementName +
		    " must have exactly " + minIfFalse +
		    " " + attrName + " attribute(s).\n" +
		    reasonForComplaint
		);
	    } else if (count < minIfFalse) {
		_eh.error(
		    _elementKind + _elementName +
		    " must have at least " + minIfFalse +
		    " " + attrName + " attribute(s).\n" +
		    reasonForComplaint
		);
	    } else if (count > maxIfFalse) {
		_eh.error(
		    _elementKind + _elementName +
		    " must have no more than " + maxIfTrue +
		    " " + attrName + " attribute(s).\n" +
		    reasonForComplaint
		);
	    }
	}
    }

    /** Conditionally return extracted String value for attribute.
      * Complain if number of instances is too low or high.
      * @param cond       As in checkNumInstances.
      * @param attrName   The attribute to extract.
      * @param minIfTrue  As in checkNumInstances.
      * @param maxIfTrue  As in checkNumInstances.
      * @param minIfFalse As in checkNumInstances.
      * @param maxIfFalse As in checkNumInstances.
      * @param defaultValue The value to return if there are no instances
      *		of the attribute.
      * @param reasonForComplaint As in checkNumInstances.
      * @return extracted string, if instances exist.
      * @return default value, if no instances exist.
      */
    protected String extractAttrIf(boolean cond, String attrName,
	    int minIfTrue, int maxIfTrue, int minIfFalse, int maxIfFalse, 
	    String defaultValue, String reasonForComplaint) {
	checkNumInstances(cond, attrName, minIfTrue, maxIfTrue,
	    minIfFalse, maxIfFalse, reasonForComplaint);
	return extractAttr(attrName, defaultValue);
    }

    /** Similar to the other extractAttrIf methods, but
      * with int.
      */
    protected long extractAttrIf(boolean cond, String attrName,
	    int minIfTrue, int maxIfTrue, int minIfFalse, int maxIfFalse, 
	    long defaultValue, String reasonForComplaint)
	    throws IdfFormatException {
	checkNumInstances(cond, attrName, minIfTrue, maxIfTrue,
	    minIfFalse, maxIfFalse, reasonForComplaint);
	return extractAttr(attrName, defaultValue);
    }

    /** Similar to the other extractAttrIf methods, but
      * with double.
      */
    protected double extractAttrIf(boolean cond, String attrName,
	    int minIfTrue, int maxIfTrue, int minIfFalse, int maxIfFalse, 
	    double defaultValue, String reasonForComplaint)
	    throws IdfFormatException {
	checkNumInstances(cond, attrName, minIfTrue, maxIfTrue,
	    minIfFalse, maxIfFalse, reasonForComplaint);
	return extractAttr(attrName, defaultValue);
    }

    /** Similar to the other extractAttrIf methods, but
      * with boolean.
      */
    protected boolean extractAttrIf(boolean cond, String attrName,
	    int minIfTrue, int maxIfTrue, int minIfFalse, int maxIfFalse, 
	    boolean defaultValue, String reasonForComplaint) {
	checkNumInstances(cond, attrName, minIfTrue, maxIfTrue,
	    minIfFalse, maxIfFalse, reasonForComplaint);
	return extractAttr(attrName, defaultValue);
    }

    protected void stringLengthNoCr(String s, String name, long maxLength) {
	if (s.length() > maxLength) {
	    _eh.error(
		"Every " + name +
		" must be no more than " + maxLength +
		" characters in length."
	    );
	}
	if (s.contains("\n")) {
	    _eh.error(
		"Every " + name +
		" must consist of only one line."
	    );
	}
    }

    /*** toString and related ***/

    private StringBuffer _toStringSb;
    private String _indentString;

    public String toString() {
	_toStringSb = new StringBuffer();
	_indentString = "";

	for (int i=0; i<indentLevel(); i++) {
	    _indentString += " ";
	}

	_toStringSb.append(_indentString);
	_toStringSb.append(_elementKind);
	_toStringSb.append(_elementName);
	_toStringSb.append(" {\n");
	contentsToString();
	_toStringSb.append(_indentString);
	_toStringSb.append("}\n");

	return _toStringSb.toString();
    }

    protected void fieldToString(String name, String value) {
	_toStringSb.append(_indentString);
	_toStringSb.append("  ");
	_toStringSb.append(name);
	_toStringSb.append(" = ");
	_toStringSb.append(value);
	_toStringSb.append("\n");
    }

    protected void elementListToString(List<? extends IdfElement> v) {
	int n = v.size();
	for (int i=0; i<n; i++) {
	    _toStringSb.append(v.get(i).toString());
	}
    }

    // Subclasses will override.
    public void contentsToString() {
    }

    // Subclasses will override.
    public int indentLevel() {
	return 0;
    }

    /*** Visitor and related ***/

    // Visitor pattern.
    public void acceptVisitor(VisitorOfIdfElement visitor) {
	beforeChildren(visitor);
	passVisitorToChildren(visitor);
	afterChildren(visitor);
    }

    // Subclasses may override.
    public void passVisitorToChildren(VisitorOfIdfElement visitor) {
    }

    // Subclasses must override to effect double dispatch.
    public void beforeChildren(VisitorOfIdfElement visitor) {
    }

    // Subclasses must override to effect double dispatch.
    public void afterChildren(VisitorOfIdfElement visitor) {
    }

}
