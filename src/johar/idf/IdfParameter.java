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
    private int _maxNumberOfChars;
    private int _minNumberOfChars;
    private int _maxNumberOfLines;
    private int _maxNumberOfReps;
    private int _minNumberOfReps;
    private int _maxIntValue;
    private int _minIntValue;
    private float _maxFloatValue;
    private float _minFloatValue;
    private String _parentParameter;
    private String _parentValue;
    private int _prominence;
    private String _sourceTable;
    private String _briefHelp;
    private String _oneLineHelp;
    private String _multiLineHelp;

    private String _emptyString = "";

    public IdfParameter(Element domElement, ErrorHandler eh) {
	super(domElement, eh, "Parameter/Question");

	_parameterName = domElement.getAttribute("name");
	setElementName(_parameterName);
	String ccConvertedName =
	    TextInputValidator.titleCaseTranslation(_parameterName);

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
		"FileConstraint", 1, 1, 0, 0, _emptyString,
		"Parameter/Question is not of type \"file\".");

	// There can be zero or one RepsModel attributes.
	_repsModel =
	     extractAttrIf(true,
		"RepsModel", 0, 1, 0, 1, _emptyString, null);

	// There can be zero or one Label attributes.
	_label =
	     extractAttrIf(true,
		"Label", 0, 1, 0, 1, ccConvertedName, null);

	// If Type is text,
	// then there can be zero or one MaxNumberOfChars attributes;
	// otherwise there must be no MaxNumberOfChars attributes.
	_maxNumberOfChars =
	    extractAttrIf(_type.equals("text"),
		"MaxNumberOfChars", 0, 1, 0, 0, Integer.MAX_VALUE,
		"Parameter/Question is not of type \"text\".");

	// If Type is text,
	// then there can be zero or one MinNumberOfChars attributes;
	// otherwise there must be no MinNumberOfChars attributes.
	_minNumberOfChars =
	    extractAttrIf(_type.equals("text"),
		"MinNumberOfChars", 0, 1, 0, 0, 0,
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
	    _maxFloatValue = extractAttr("MaxValue", Float.MAX_VALUE);
	    _minFloatValue = extractAttr("MinValue", Float.MIN_VALUE);
	} else if (_type.equals("int")) {
	    _maxIntValue = extractAttr("MaxValue", Integer.MAX_VALUE);
	    _minIntValue = extractAttr("MinValue", Integer.MIN_VALUE);
	}

	// If there is a MaxNumberOfReps or MinNumberOfReps  parameter,
	// then there must be no Optional attribute;
	// otherwise there can be zero or one Optional attributes.
	checkNumInstances(countNumberOf("MaxNumberOfReps") < 1,
	    "Optional", 0, 1, 0, 0, 
	    "Parameter/Question has an explicit MaxNumberOfReps parameter.");
	checkNumInstances(countNumberOf("MinNumberOfReps") < 1,
	    "Optional", 0, 1, 0, 0, 
	    "Parameter/Question has an explicit MinNumberOfReps parameter.");
	boolean optional = extractAttr("Optional", false);
	if (optional) {
	    _maxNumberOfReps = 1;
	    _minNumberOfReps = 0;
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
		"Prominence", 0, 1, 0, 1, 1000, null);

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

	// Release document and error handler for eventual GC
	_domElement = null;
	_eh = null;
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

    public String getFileConstraint() {
	return _fileConstraint;
    }

    public String getRepsModel() {
	return _repsModel;
    }

    public String getLabel() {
	return _label;
    }

    public int getMaxNumberOfChars() {
	return _maxNumberOfChars;
    }

    public int getMaxNumberOfLines() {
	return _maxNumberOfLines;
    }

    public int getMaxNumberOfReps() {
	return _maxNumberOfReps;
    }

    public int getMinNumberOfReps() {
	return _minNumberOfReps;
    }

    public int getMaxIntValue() {
	return _maxIntValue;
    }

    public int getMinIntValue() {
	return _minIntValue;
    }

    public float getMaxFloatValue() {
	return _maxFloatValue;
    }

    public float getMinFloatValue() {
	return _minFloatValue;
    }

    public String getParentParameter() {
	return _parentParameter;
    }

    public String getParentValue() {
	return _parentValue;
    }

    public int getProminence() {
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
	fieldToString("Type", _type);
	fieldToString("Choices", _choices);
	fieldToString("FileConstraint", _fileConstraint);
	fieldToString("RepsModel", _repsModel);
	fieldToString("Label", _label);
	fieldToString("MaxNumberOfChars", Integer.toString(_maxNumberOfChars));
	fieldToString("MaxNumberOfLines", Integer.toString(_maxNumberOfLines));
	fieldToString("MaxNumberOfReps", Integer.toString(_maxNumberOfReps));
	fieldToString("MinNumberOfReps", Integer.toString(_minNumberOfReps));
	fieldToString("MaxIntValue", Integer.toString(_maxIntValue));
	fieldToString("MinIntValue", Integer.toString(_minIntValue));
	fieldToString("MaxFloatValue", Float.toString(_maxFloatValue));
	fieldToString("MinFloatValue", Float.toString(_minFloatValue));
	fieldToString("ParentParameter", _parentParameter);
	fieldToString("ParentValue", _parentValue);
	fieldToString("Prominence", Integer.toString(_prominence));
	fieldToString("SourceTable", _sourceTable);
	fieldToString("BriefHelp", _briefHelp);
	fieldToString("OneLineHelp", _oneLineHelp);
	fieldToString("MultiLineHelp", _multiLineHelp);
    }

    // Standard methods to effect double dispatch.
    public void beforeChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }
    public void afterChildren(VisitorOfIdfElement visitor) {
	visitor.beforeChildren(this, _eh);
    }

}

