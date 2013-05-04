/* ParamValuesInfo.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

package johar.gem;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about all the (repetitions of) parameter values
 * of a given {@code Parameter}.
 */
public class ParamValuesInfo {
    private java.util.List<ParamValue> _paramValueList;
    private int _repCount = 0;
    private int _currentRep = 0;

    public ParamValuesInfo() {
	_paramValueList = new ArrayList<ParamValue>();
    }

    public int getParameterRepCount() {
	return _paramValueList.size();
    }

    public void setParameterValue(int repNumber, Object paramValue) {
	while (_paramValueList.size() <= repNumber) {
	    _paramValueList.add(null);
	}
	_paramValueList.set(repNumber, new ParamValue(paramValue));
    }

    public Object getParameterRep(int repNumber) {
	return _paramValueList.get(repNumber).getValue();
    }

}
