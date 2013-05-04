/* GemImplementation.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

package johar.gem;

import java.util.TreeMap;

import johar.gem.Gem;
import johar.gem.GemBaseImplementation;
import johar.idf.*;

public class GemImplementation
extends johar.gem.GemBaseImplementation
implements johar.gem.Gem {

    protected ShowTextHandler _showTextHandler;

    public GemImplementation(Idf idf, ShowTextHandler showTextHandler) {
	super(idf);
	_showTextHandler = showTextHandler;
    }

    // See Gem.java for documentation about the methods.

    public void clearTable(String tableName)
    throws GemException {
	getTableStructure(tableName).clearTable();
    }

    public void setTableHeading(String tableName, String heading)
    throws GemException {
	getTableStructure(tableName).setTableHeading(heading);
    }

    public void fillRow(String tableName, int rowNumber, String text)
    throws GemException {
	getTableStructure(tableName).fillRow(rowNumber, text);
    }

    public void showTable(String tableName)
    throws GemException {
	getTableStructure(tableName).showTable();
    }

    public void hideTable(String tableName)
    throws GemException {
	getTableStructure(tableName).hideTable();
    }

    public void setTopTable(String tableName)
    throws GemException {
	_topTable = "";
	getTableStructure(tableName); // throws exception if not there
	_topTable = tableName;
    }

    public void showText(String text, int priorityLevel) {
	_showTextHandler.showText(text, priorityLevel);
    }

}
