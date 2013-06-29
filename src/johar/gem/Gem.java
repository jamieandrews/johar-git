/**
 * Gem is the interface, extending GemBase, that allows the
 * application engine to set table data and output to the user.
 * <p> The application engine methods all take a single parameter of
 * type Gem.
 * <p> Contrast with GemSetting, which interface interpreters work
 * with.  GemSetting clients can set parameters and some table
 * attributes, which Gems cannot do, but GemSettings cannot set
 * table data or application-logic-specific outputs.
 *
 * @author Jamie Andrews
 * @author Fatima Hussain
 */

package johar.gem;

public interface Gem extends johar.gem.GemBase {
    /**
     * Clears a table; that is, makes it have zero filled rows.
     *
     * @param tableName the name of the table to clear
     * @throws GemException if the named table does not exist
     */
    public void clearTable(String tableName) throws GemException;

    /**
     * Sets a heading for a table.  This text is shown at the
     * top of the table.  Before the first call to this method
     * for a given table name, the heading is the default heading.
     *
     * @param tableName	the name of the table to set the heading for
     * @param heading the new heading
     * @throws GemException if the named table does not exist
     */
    public void setTableHeading(String tableName, String heading)
    throws GemException;

    /**
     * Sets column names for a table.  This text is shown at the
     * top of each column, under the heading.  Before the first call
     * to this method for a given table name, the column names are
     * the default column names from the IDF.
     * <p> If the text contains unescaped "bar" characters ("|"), then
     * the IntI may show the parts of the text between the bars as
     * names for the separate columns.
     *
     * @param tableName	the name of the table to set the heading for
     * @param columnNames the new column names
     * @throws GemException if the named table does not exist
     */
    public void setColumnNames(String tableName, String columnNames)
    throws GemException;

    /**
     * Fills a row of a table with a text string.
     * <p> If the highest row filled before the call is row r, and
     * rowNumber is greater than r+1, then all of the rows between r+1
     * and rowNumber-1 will be filled with the empty text.
     * <p> If the text contains unescaped "bar" characters ("|"), then
     * the IntI may show the parts of the text between the bars as
     * separate "cells" within the row.
     *
     * @param tableName the name of the table whose row will be filled
     * @param rowNumber the number of the row to be filled (starting at 0)
     * @param text the text for the row
     * @throws GemException if the named table does not exist,
     *	or if {@code rowNumber} is less than 0
     */
    public void fillRow(String tableName, int rowNumber, String text)
    throws GemException;

    /**
     * Shows a table.
     * <p> If the table is browsable, then the user will be able to browse the
     * table until the next time hideTable is called on the table.
     * By default, every table is shown.
     *
     * @param tableName the name of the table to show
     * @throws GemException if the named table does not exist
     */
    public void showTable(String tableName)
    throws GemException;

    /**
     * Hides a table.
     * <p> The user will not be able to browse the  table until the next time
     * showTable is called on the table.
     * By default, every table is shown.
     *
     * @param tableName the name of the table to hide
     * @throws GemException if the named table does not exist
     */
    public void hideTable(String tableName)
    throws GemException;

    /**
     * Sets the table which is "on top".
     * <p> An application engine may place a table "on top" in order to
     * give the user easier access to it.  An IntI may use the
     * information that the top table was set by the application
     * engine in order to present it first to the user.
     *
     * @param tableName the name of table which is to be put on top
     * @throws GemException if the named table does not exist
     */
    public void setTopTable(String tableName)
    throws GemException;

    /**
     * Shows text to user with given priority level.
     * @param text the text to show
     * @param priorityLevel the priority of the message.
     * <ul>
     * <li> Level 3000 and above:  High-priority message.  User should be
     *   asked to confirm receipt of the message, unless they have
     *   explicitly turned off such a feature.  Appropriate for
     *   error messages.
     * <li> Level 2000-2999:  Result message.  Appropriate for displaying
     *   the main data that the user has asked the application to
     *   provide, other than data appearing in tables.
     * <li> Level 1000-1999:  Status message.  Appropriate for displaying
     *   short messages indicating the completion of an operation
     *   and/or simple success and failure information.
     * <li> Level 0-999:  Verbose/debug message.  Appropriate for
     *   information that would only be displayed if the user asked
     *   to see details of computation.
     * </ul>
     * Different interface interpreters can choose different
     * ways of displaying the message based on priority.  For
     * instance, a GUI might choose to display a message with
     * priority level 3000 and above as a popup, a message with
     * priority level 2000-2999 in the main app window, and a
     * message with priority 1000-1999 in a status line at the
     * bottom of the screen.
     */
    public void showText(String text, int priorityLevel);

}
