/* ParamValue.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

package johar.gem;

/**
 * One value of a parameter.
 */
public class ParamValue {

    private Object _value;

    public ParamValue(Object value) {
	_value = value;
    }

    public Object getValue() {
	return _value;
    }

}
