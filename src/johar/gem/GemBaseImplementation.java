/* GemBaseImplementation.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

package johar.gem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import johar.gem.GemBase;
import johar.idf.Idf;

public class GemBaseImplementation implements johar.gem.GemBase {
    protected Idf _idf;
    // protected Class _appEngineClass;
    // protected Object _appEngineObject;
    // protected ShowTextHandler _showTextHandler;
    protected Map<String, ParamValuesInfo> _mapStringParamValuesInfo;
    protected Map<String, TableStructure> _mapStringTableStructure;
    protected String _topTable;

    public GemBaseImplementation(Idf idf) {
	_idf = idf;
	// _appEngineClass = null;
	// _appEngineObject = null;
	// _showTextHandler = null;
	_mapStringParamValuesInfo = new HashMap<String, ParamValuesInfo>();
	_mapStringTableStructure = new HashMap<String, TableStructure>();
	_topTable = null;
    }

    // See GemBase.java for descriptions of public functions.

    protected ParamValuesInfo getPVI(String paramName)
    throws GemException {
	ParamValuesInfo pvi = _mapStringParamValuesInfo.get(paramName);

	if (pvi == null)
	    throw new GemException(
		"Parameter " + paramName +
		" does not appear in current command"
	    );

	return pvi;
    }

    public int getParameterRepCount(String paramName)
    throws GemException {
	return getPVI(paramName).getParameterRepCount();
    }

    public Object getParameter(String paramName, int repNumber)
    throws GemException {
	Object o = getPVI(paramName).getParameterRep(repNumber);
	return o;
    }

    public Object getParameter(String paramName)
    throws GemException {
	return getParameter(paramName, 0);
    }

    public long getIntParameter(String paramName, int repNumber)
    throws GemException {
	Object o = getPVI(paramName).getParameterRep(repNumber);
	try {
	    long value = ((Long) o).longValue();
	    return value;
	} catch (ClassCastException e) {
	    throw new GemException(
		"Parameter is not of type 'int' or 'tableEntry'");
	}
    }

    public long getIntParameter(String paramName)
    throws GemException {
	return getIntParameter(paramName, 0);
    }

    public double getFloatParameter(String paramName, int repNumber)
    throws GemException {
	Object o = getPVI(paramName).getParameterRep(repNumber);
	try {
	    double value = ((Double) o).doubleValue();
	    return value;
	} catch (ClassCastException e) {
	    throw new GemException("Parameter is not of type 'float'");
	}
    }

    public double getFloatParameter(String paramName)
    throws GemException {
	return getFloatParameter(paramName, 0);
    }

    public boolean getBooleanParameter(String paramName, int repNumber)
    throws GemException {
	Object o = getPVI(paramName).getParameterRep(repNumber);
	try {
	    boolean value = ((Boolean) o).booleanValue();
	    return value;
	} catch (ClassCastException e) {
	    throw new GemException("Parameter is not of type 'boolean'");
	}
    }

    public boolean getBooleanParameter(String paramName)
    throws GemException {
	return getBooleanParameter(paramName, 0);
    }

    public String getStringParameter(String paramName, int repNumber)
    throws GemException {
	Object o = getPVI(paramName).getParameterRep(repNumber);
	try {
	    // This case is slightly different, because String is
	    // already a reference type
	    String value = (String) o;
	    return value;
	} catch (ClassCastException e) {
	    throw new GemException(
		"Parameter is not of type 'text' or 'choices'");
	}
    }

    public String getStringParameter(String paramName)
    throws GemException {
	return getStringParameter(paramName, 0);
    }

    protected TableStructure getTableStructure(String tableName)
    throws GemException {
	TableStructure ts = _mapStringTableStructure.get(tableName);

	if (ts == null)
	    throw new GemException("Table " + tableName + " does not exist");

	return ts;
    }

    public boolean tableIsShown(String tableName)
    throws GemException {
	return getTableStructure(tableName).tableIsShown();
    }

    public String getTableHeading(String tableName)
    throws GemException {
	return getTableStructure(tableName).getTableHeading();
    }

    public String getColumnNames(String tableName)
    throws GemException {
	return getTableStructure(tableName).getColumnNames();
    }

    public boolean rowIsFilled(String tableName, int rowNumber)
    throws GemException {
	return getTableStructure(tableName).rowIsFilled(rowNumber);
    }

    public String getRowText(String tableName, int rowNumber)
    throws GemException {
	return getTableStructure(tableName).getRowText(rowNumber);
    }

    public boolean tableIsUpdated(String tableName)
    throws GemException {
	return getTableStructure(tableName).tableIsUpdated();
    }

    public String getTopTable()
    throws GemException {
	return _topTable;
    }

}
