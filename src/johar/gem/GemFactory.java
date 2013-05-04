/* GemFactory.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

package johar.gem;

import johar.gem.GemFullImplementation;
import johar.idf.Idf;

/**
 * A Factory (GoF) object for generating {@code GemSetting}s.
 *
 * @author Jamie Andrews
 */
public class GemFactory {
    /* newGemSetting:  TODO
     * @return TODO
     */
    public static johar.gem.GemSetting newGemSetting(
	    Idf idf, ShowTextHandler showTextHandler) {
	return new johar.gem.GemFullImplementation(idf, showTextHandler);
    }
}
