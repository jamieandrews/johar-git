/* IdfQuestion.java
 * This source file is part of the Johar project.
 * @author Jamie Andrews
 */

package johar.idf;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import johar.utilities.TextInputValidator;

public class IdfQuestion extends IdfParameter {
    // The additional attributes of Question
    private String _askIfMethod;

    public IdfQuestion(Element domElement, ErrorHandler eh) {
	// There are attributes that cannot appear in Questions.
	// However, the XSD should have made sure that they already
	// are not appearing in the Question.  Thus it is safe to
	// call the superclass constructor to extract all other
	// attributes.
	super(domElement, eh);

	// Restore doc and error handler (nulled by superclass ctor)
	_domElement = domElement;
	_eh = eh;

	// There must be exactly one AskIfMethod attribute
	_askIfMethod =
	    extractAttrIf(true,
		"AskIfMethod", 1, 1, 1, 1, "", null);
    }

    public void contentsToString() {
	super.contentsToString();
	fieldToString("AskIfMethod", _askIfMethod);
    }

    // Standard methods to effect double dispatch.
    public void beforeChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }
    public void afterChildren(VisitorOfIdfElement visitor) {
	visitor.afterChildren(this, _eh);
    }

}

