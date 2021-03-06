/* IdfParameter.java
 * This source file is part of the Johar project.
 * @author Jamie Andrews
 */

package johar.idf;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import johar.utilities.TextInputValidator;

public class IdfParameter extends IdfElement {
    // The attributes of Parameter (or Question)
    private String _parameterName;
    private String _type;
    private String _choices;
    private String _defaultValue;
    private String _defaultValueMethod;
    private String _fileConstraint;
    private String _repsModel;
    private String _label;
    private long _maxNumberOfChars;
    private long _minNumberOfChars;
    private long _maxNumberOfLines;
    private long _maxNumberOfReps;
    private long _minNumberOfReps;
    private long _maxIntValue;
    private long _minIntValue;
    private double _maxFloatValue;
    private double _minFloatValue;
    private String _parentParameter;
    private String _parentValue;
    private long _prominence;
    private String _sourceTable;
    private String _briefHelp;
    private String _oneLineHelp;
    private String _multiLineHelp;

    private String _emptyString = "";

    public IdfParameter(Element domElement, ErrorHandler eh) {
	super(domElement, eh, "Parameter/Question");

	try {
	    extractParameters();
	} catch (IdfFormatException e) {
	    if (_parameterName != null) {
		eh.error("Parameter/Question " + _parameterName +
		    ", " + e.getMessage());
	    } else {
		eh.error("Parameter/Question format error");
	    }
	}
    }

    private void extractParameters() throws IdfFormatException {

	// Extract parameter name and do camel-case conversion
	_parameterName = _domElement.getAttribute("name");
	setElementName(_parameterName);
	String ccConvertedName =
	    TextInputValidator.titleCaseTranslation(_parameterName);

	// Extract Type first, since this determines many other things.
	// There must be exactly one Type attribute
	_type =
	    extractAttrIf(true,
		"Type", 1, 1, 1, 1, "boolean", null);

	// If Type is choice,
	// then there must be exactly one Choices attribute;
	// otherwise there must be no Choices attributes.
	_choices =
	    extractAttrIf(_type.equals("choice"),
		"Choices", 1, 1, 0, 0, _emptyString,
		"Parameter/Question is not of type \"choice\".");

	// There can be zero or one DefaultValue attributes.
	_defaultValue =
	     extractAttrIf(true,
		"DefaultValue", 0, 1, 0, 1, _emptyString, null);

	// If there is no DefaultValue attribute,
	// there can be zero or one DefaultValueMethod attributes;
	// otherwise there must be no DefaultValueMethod attributes.
	_defaultValueMethod =
	    extractAttrIf(countNumberOf("DefaultValue") < 1,
		"DefaultValueMethod", 0, 1, 0, 0, _emptyString,
		"Parameter/Question has a DefaultValue attribute.");

	// If Type is file,
	// then there can be zero or one FileConstraint attributes;
	// otherwise there must be no FileConstraint attributes.
	_fileConstraint =
	    extractAttrIf(_type.equals("file"),
		"FileConstraint", 0, 1, 0, 0, "none",
		"Parameter/Question is not of type \"file\".");

	// There can be zero or one Label attributes.
	_label =
	     extractAttrIf(true,
		"Label", 0, 1, 0, 1, ccConvertedName, null);

	// If Type is text,
	// then there can be zero or one MaxNumberOfChars attributes;
	// otherwise there must be no MaxNumberOfChars attributes.
	_maxNumberOfChars =
	    extractAttrIf(_type.equals("text"),
		"MaxNumberOfChars", 0, 1, 0, 0, Long.MAX_VALUE,
		"Parameter/Question is not of type \"text\".");

	// If Type is text,
	// then there can be zero or one MinNumberOfChars attributes;
	// otherwise there must be no MinNumberOfChars attributes.
	_minNumberOfChars =
	    extractAttrIf(_type.equals("text"),
		"MinNumberOfChars", 0, 1, 0, 0, 0,
		"Parameter/Question is not of type \"text\".");

	// If Type is text,
	// then there can be zero or one MaxNumberOfLines attributes;
	// otherwise there must be no MaxNumberOfLines attributes.
	_maxNumberOfLines =
	    extractAttrIf(_type.equals("text"),
		"MaxNumberOfLines", 0, 1, 0, 0, Long.MAX_VALUE,
		"Parameter/Question is not of type \"text\".");

	// There can be zero or one MaxNumberOfReps attributes.
	_maxNumberOfReps =
	    extractAttrIf(true,
		"MaxNumberOfReps", 0, 1, 0, 1, 1, null);

	// There can be zero or one MinNumberOfReps attributes.
	_minNumberOfReps =
	    extractAttrIf(true,
		"MinNumberOfReps", 0, 1, 0, 1, 1, null);

	// If Type is int or float,
	// then there can be zero or one MinValue attributes;
	// otherwise there must be no MinValue attributes.
	checkNumInstances(_type.equals("int") || _type.equals("float"),
	    "MinValue", 0, 1, 0, 0, 
	    "Parameter/Question is not of type \"int\" or \"float\".");

	// If Type is int or float,
	// then there can be zero or one MaxValue attributes;
	// otherwise there must be no MaxValue attributes.
	checkNumInstances(_type.equals("int") || _type.equals("float"),
	    "MaxValue", 0, 1, 0, 0, 
	    "Parameter/Question is not of type \"int\" or \"float\".");

	if (_type.equals("float")) {
	    _maxFloatValue = extractAttr("MaxValue", Double.MAX_VALUE);
	    _minFloatValue = extractAttr("MinValue", Double.MIN_VALUE);
	    _maxIntValue = Long.MAX_VALUE;
	    _minIntValue = Long.MIN_VALUE;
	} else if (_type.equals("int")) {
	    _maxIntValue = extractAttr("MaxValue", Long.MAX_VALUE);
	    _minIntValue = extractAttr("MinValue", Long.MIN_VALUE);
	    _maxFloatValue = Double.MAX_VALUE;
	    _minFloatValue = Double.MIN_VALUE;
	}

	// There can be zero or one ParentParameter attributes.
	_parentParameter =
	    extractAttrIf(true,
		"ParentParameter", 0, 1, 0, 1, _emptyString, null);

	// If there is a ParentParameter attribute,
	// then there must be exactly one ParentValue attribute;
	// otherwise there must be no ParentValue attributes.
	_parentValue =
	    extractAttrIf(countNumberOf("ParentParameter") > 0,
		"ParentValue", 1, 1, 0, 0, _emptyString,
		"Parameter/Question has no ParentParameter attribute.");

	// There can be zero or one Prominence attributes.
	_prominence =
	    extractAttrIf(true,
		"Prominence", 0, 1, 0, 1, 2000, null);

	// There can be zero or one RepsModel attributes.
	_repsModel =
	     extractAttrIf(true,
		"RepsModel", 0, 1, 0, 1, "set", null);

	// If Type is tableEntry,
	// then there must be exactly one SourceTable attribute;
	// otherwise there must be no SourceTable attributes.
	_sourceTable =
	    extractAttrIf(_type.equals("tableEntry"),
		"SourceTable", 1, 1, 0, 0, _emptyString,
		"Parameter/Question is not of type TableEntry.");

	// There can be zero or one BriefHelp attributes.
	_briefHelp =
	    extractAttrIf(true,
		"BriefHelp", 0, 1, 0, 1, _label, null);

	// There can be zero or one OneLineHelp attributes.
	_oneLineHelp =
	    extractAttrIf(true,
		"OneLineHelp", 0, 1, 0, 1, _briefHelp, null);

	// There can be zero or one MultiLineHelp attributes.
	_multiLineHelp =
	    extractAttrIf(true,
		"MultiLineHelp", 0, 1, 0, 1, _oneLineHelp, null);
    }

    // Getters.

    public String getParameterName() {
	return _parameterName;
    }

    public String getType() {
	return _type;
    }

    public String getChoices() {
	return _choices;
    }

    public String getDefaultValue() {
	return _defaultValue;
    }

    public String getDefaultValueMethod() {
	return _defaultValueMethod;
    }

    public String getFileConstraint() {
	return _fileConstraint;
    }

    public String getRepsModel() {
	return _repsModel;
    }

    public String getLabel() {
	return _label;
    }

    public long getMaxNumberOfChars() {
	return _maxNumberOfChars;
    }

    public long getMaxNumberOfLines() {
	return _maxNumberOfLines;
    }

    public long getMaxNumberOfReps() {
	return _maxNumberOfReps;
    }

    public long getMinNumberOfChars() {
	return _minNumberOfChars;
    }

    public long getMinNumberOfReps() {
	return _minNumberOfReps;
    }

    public long getMaxIntValue() {
	return _maxIntValue;
    }

    public long getMinIntValue() {
	return _minIntValue;
    }

    public double getMaxFloatValue() {
	return _maxFloatValue;
    }

    public double getMinFloatValue() {
	return _minFloatValue;
    }

    public String getParentParameter() {
	return _parentParameter;
    }

    public String getParentValue() {
	return _parentValue;
    }

    public long getProminence() {
	return _prominence;
    }

    public String getSourceTable() {
	return _sourceTable;
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

    public int indentLevel() {
	return 6;
    }

    public void contentsToString() {
	fieldToString("ParameterName", _parameterName);
	// Type gets prominent place because helps the user interpret
	// the output.
	fieldToString("Type", _type);

	fieldToString("BriefHelp", _briefHelp);
	fieldToString("Choices", _choices);
	fieldToString("DefaultValue", _defaultValue);
	fieldToString("DefaultValueMethod", _defaultValueMethod);
	fieldToString("FileConstraint", _fileConstraint);
	fieldToString("Label", _label);
	fieldToString("MaxNumberOfChars", Long.toString(_maxNumberOfChars));
	fieldToString("MaxNumberOfLines", Long.toString(_maxNumberOfLines));
	fieldToString("MaxNumberOfReps", Long.toString(_maxNumberOfReps));
	// MaxValue analyzed above into two values, int and float
	fieldToString("MaxIntValue", Long.toString(_maxIntValue));
	fieldToString("MaxFloatValue", Double.toString(_maxFloatValue));
	fieldToString("MinNumberOfChars", Long.toString(_minNumberOfChars));
	fieldToString("MinNumberOfReps", Long.toString(_minNumberOfReps));
	// MinValue analyzed above into two values, int and float
	fieldToString("MinIntValue", Long.toString(_minIntValue));
	fieldToString("MinFloatValue", Double.toString(_minFloatValue));
	fieldToString("MultiLineHelp", _multiLineHelp);
	fieldToString("OneLineHelp", _oneLineHelp);
	fieldToString("ParentParameter", _parentParameter);
	fieldToString("ParentValue", _parentValue);
	fieldToString("Prominence", Long.toString(_prominence));
	fieldToString("RepsModel", _repsModel);
	fieldToString("SourceTable", _sourceTable);
    }

    // Standard methods to effect double dispatch.
    public void beforeChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }
    public void afterChildren(VisitorOfIdfElement visitor) {
	visitor.afterChildren(this, _eh);
    }

}

