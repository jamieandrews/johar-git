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
	super(domElement, eh);

	// Restore doc and error handler
	_domElement = domElement;
	_eh = eh;

	// There must be exactly one AskIfMethod attribute
	_askIfMethod =
	    extractAttrIf(true,
		"AskIfMethod", 1, 1, 1, 1, "", null);

	// Release document and error handler for eventual GC
	_domElement = null;
	_eh = null;
    }

    public void contentsToString() {
	super.contentsToString();
	fieldToString("AskIfMethod", _askIfMethod);
    }

}

