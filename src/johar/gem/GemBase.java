/**
 * The base functionality of the Gem, containing all the methods
 * that both application engines and interface interpreters need
 * to call.
 *
 * @author Jamie Andrews
 * @author Fatima Hussain
 */

package johar.gem;

import java.util.List;

public interface GemBase {
    /**
     * Gets the number of repetitions of a parameter currently in the Gem.
     * The value returned is greater than or equal to zero.
     *
     * @param paramName the name of the parameter wanted
     * @return the number of repetitions of the parameter by this
     *	name currently in the Gem
     * @throws GemException if no parameter with this name currently
     *	exists in the Gem
     */
    public int getParameterRepCount(String paramName)
    throws GemException;

    /**
     * Gets the Object value for a command parameter repetition.
     * Suitable for parameters of type {@code date}, {@code file},
     * and {@code timeOfDay}.
     *
     * @param paramName the name of the parameter wanted
     * @param repNumber the parameter repetition wanted (starting at 0)
     * @return the Object value of the repNumber-th repetition of the
     *	parameter with this name
     * @throws GemException if there are fewer than repNumber repetitions
     *  of the parameter with this name currently in the Gem, or
     *  if no parameter with this name currently exists in the Gem, or
     *	if the parameter with this name is not
     *	of type {@code date}, {@code file}, or {@code timeOfDay}
     */
    public Object getParameter(String paramName, int repNumber)
    throws GemException;

    /**
     * Gets the Object value for a command parameter repetition 0.
     * <p> Equivalent to getParameter(paramName, 0).
     */
    public Object getParameter(String paramName)
    throws GemException;

    /**
     * Gets the integer value for a command parameter repetition.
     * <p> Can be used only with parameters declared to be of type
     * {@code int} or {@code tableEntry} in the IDF.
     *
     * @param paramName the name of the parameter wanted
     * @param repNumber the parameter repetition wanted (starting at 0)
     * @return the integer value of the repNumber-th repetition of the
     *	parameter with this name
     * @throws GemException if there are fewer than repNumber repetitions
     *  of the parameter with this name currently in the Gem, or
     *  if no parameter with this name currently exists in the Gem, or
     *	if the parameter with this name is not
     *	of type {@code int} or {@code tableEntry}
     */
    public long getIntParameter(String paramName, int repNumber)
    throws GemException;

    /**
     * Gets the integer value for a command parameter repetition.
     * <p> Equivalent to getIntParameter(paramName, 0).
     */
    public long getIntParameter(String paramName)
    throws GemException;

    /**
     * Gets the floating-point value for a command parameter repetition.
     * <p> Can be used only with parameters declared to be of type
     * {@code float} in the IDF.
     *
     * @param paramName the name of the parameter wanted
     * @param repNumber the parameter repetition wanted (starting at 0)
     * @return the floating-point value of the repNumber-th repetition of
     *	the parameter with this name
     * @throws GemException if there are fewer than repNumber repetitions
     *  of the parameter with this name currently in the Gem, or
     *  if no parameter with this name currently exists in the Gem, or
     *	if the parameter with this name is not
     *	of type {@code float}
     */
    public double getFloatParameter(String paramName, int repNumber)
    throws GemException;

    /**
     * Gets the floating-point value for a command parameter repetition.
     * <p> Equivalent to getIntParameter(paramName, 0).
     */
    public double getFloatParameter(String paramName)
    throws GemException;

    /**
     * Gets the boolean value for a command parameter repetition.
     * <p> Can be used only with parameters declared to be of type
     * {@code boolean} in the IDF.
     *
     * @param paramName the name of the parameter wanted
     * @param repNumber the parameter repetition wanted (starting at 0)
     * @return the boolean value of the repNumber-th repetition of
     *	the parameter with this name
     * @throws GemException if there are fewer than repNumber repetitions
     *  of the parameter with this name currently in the Gem, or
     *  if no parameter with this name currently exists in the Gem, or
     *	if the parameter with this name is not
     *	of type {@code boolean}
     */
    public boolean getBooleanParameter(String paramName, int repNumber)
    throws GemException;

    /**
     * Gets the boolean value for a command parameter repetition.
     * <p> Equivalent to getIntParameter(paramName, 0).
     */
    public boolean getBooleanParameter(String paramName)
    throws GemException;

    /**
     * Gets the string value for a command parameter repetition.
     * <p> Can be used only with parameters declared to be of type
     * {@code text} or {@code choice} in the IDF.
     *
     * @param paramName the name of the parameter wanted
     * @param repNumber the parameter repetition wanted (starting at 0)
     * @return the string value of the repNumber-th repetition of
     *	the parameter with this name
     * @throws GemException if there are fewer than repNumber repetitions
     *  of the parameter with this name currently in the Gem, or
     *  if no parameter with this name currently exists in the Gem, or
     *	if the parameter with this name is not
     *	of type {@code text} or {@code choice}
     */
    public String getStringParameter(String paramName, int repNumber)
    throws GemException;

    /**
     * Gets the string value for a command parameter repetition.
     * <p> Equivalent to getIntParameter(paramName, 0).
     */
    public String getStringParameter(String paramName)
    throws GemException;

    // ***********************************************************
    // Methods by which tables can be accessed
    // ***********************************************************
    // Generally, all these methods throw GemException if the
    // tableName given does not exist in the IDF.
    // Associated with a table is the notion of a "filled" and
    // "unfilled" row.  The first N rows of a table are filled
    // and the rest are unfilled (N can be 0).  There cannot be
    // an unfilled row followed by a filled row.  The user will
    // see only the filled rows of the table.

    /**
     * Checks whether the table is shown.
     *
     * @param tableName the name of the table to check
     * @return true if the table is shown, false if it is hidden
     * @throws GemException if the named table does not exist
     */
    public boolean tableIsShown(String tableName)
    throws GemException;

    /**
     * Gets the current heading for a table.
     *
     * @param tableName the name of the table to get the heading for
     * @return the heading of the named table
     * @throws GemException if the named table does not exist
     */
    public String getTableHeading(String tableName)
    throws GemException;

    /**
     * Gets the current column names for a table.
     *
     * @param tableName the name of the table to get the heading for
     * @return the column names of the named table
     * @throws GemException if the named table does not exist
     */
    public String getColumnNames(String tableName)
    throws GemException;

    /**
     * Tells whether a row of a table is filled.
     *
     * @param tableName the name of the table
     * @param rowNumber the number of the row wanted (starting at 0)
     * @return true iff the given row is filled
     * @throws GemException if no table with this name currently
     *	exists in the Gem
     */
    public boolean rowIsFilled(String tableName, int rowNumber)
    throws GemException;

    /**
     * Gets the text supplied for a row in a table.
     * The text was supplied by the application
     * engine.  The pieces of text that are to go in each column
     * in the row are separated by bars ("|").
     *
     * @param tableName the name of the table
     * @param rowNumber the number of the row wanted
     * @throws GemException if no table with this name currently
     *	exists in the Gem, or if the row is not filled
     */
    public String getRowText(String tableName, int rowNumber)
    throws GemException;

    /**
     * Tells whether the application engine has made
     * updates in the table since the last call of the command
     * method of a command.
     *
     * @return true if the table is updated
     * @throws GemException if no table with this name currently
     *	exists in the Gem
     */
    public boolean tableIsUpdated(String tableName)
    throws GemException;

    /**
     * Gets the name of the table which has been set as the
     * "top table" by the application engine.
     *
     * @return name of the table on top, if one has been set
     * @return {@code null} if no table was set to be the top
     *  table since the last call of the command method of a command
     */
    public String getTopTable()
    throws GemException;

}
