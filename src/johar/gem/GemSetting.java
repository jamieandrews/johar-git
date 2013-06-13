/**
 * GemSetting is the interface, extending GemBase, that allows the
 * interface interpreter to set parameters and table attributes,
 * and to call app engine methods.
 * The interface interpreter uses GemFactory to generate a new
 * GemSetting, and uses this GemSetting for all interactions with
 * the app engine.
 * Contrast with Gem, which app engines work with.  Gem clients
 * can set table data, which GemSettings cannot do, but Gems
 * cannot set parameters or some table attributes.
 *
 * @author Jamie Andrews
 * @author Fatima Hussain
 */

package johar.gem;

import johar.idf.*;

public interface GemSetting extends johar.gem.GemBase {

    /**
     * Validates the IDF to make sure that the application engine
     * is as described in the IDF.
     *
     * @throws IdfFormatException if the application engine class
     *  cannot be loaded, if the IDF has declared methods that do not
     *  exist in the application engine class, etc.
     */
    public void validate()
    throws IdfFormatException;

    /**
     * Creates the application engine.
     */
    public void initializeAppEngine();

    /**
     * Calls the ActiveIfMethod of a given command and returns
     * the result, or returns {@code true} if it does not have
     * an ActiveIfMethod.
     * @throws GemException if the command is not declared.
     */
    public boolean methodIsActive(String commandName)
    throws GemException;

    /**
     * Selects the command whose parameters will be loaded and
     * which may eventually be called.
     * @throws GemException if the command is not declared.
     */
    public void selectCurrentCommand(String commandName)
    throws GemException;

    /**
     * Selects the current stage.
     * @throws GemException if the stage does not exist in the current
     *  command, or if the stage number is out of sequence.
     */
    public void selectCurrentStage(int stageNumber)
    throws GemException;

    // ***********************************************************
    // Methods by which interface interp can set command parameters
    // ***********************************************************
    // Generally, all these methods throw GemException if the
    // paramName is not a proper parameter for the selected Command,
    // or is not the correct type for the method.

    /**
     * Clears all parameters in the Gem.  Used to initialize the
     * Gem before loading it with parameter values.
     */
    public void clearParameters()
    throws GemException;

    /**
     * Sets an {@code Object} value as a repetition of a parameter.
     * This should be used for parameters declared in the IDF
     * as of type {@code date}, {@code file}, or {@code timeOfDay}.
     *
     * @param paramName the name of the parameter getting the repetition
     * @param repNumber the sequence number of the repetition
     * @param o the {@code Object} value of the repetition
     * @throws GemException if the parameter does not exist
     */
    public void setParameterValue(String paramName, int repNumber, Object o)
    throws GemException;

    /**
     * Sets a {@code long} value as a repetition of a parameter.
     * This should be used for parameters declared in the IDF
     * as of type {@code int}.  It should also be used for parameters
     * declared in the IDF as of type {@code tableEntry}; when used
     * for these parameters, it indicates the row number of a row
     * that was selected in the table.
     *
     * @param paramName the name of the parameter getting the repetition
     * @param repNumber the sequence number of the repetition
     * @param l the {@code int} value of the repetition
     * @throws GemException if the parameter does not exist
     */
    public void setParameterValue(String paramName, int repNumber, long l)
    throws GemException;

    /**
     * Sets a {@code double} value as a repetition of a parameter.
     * This should be used for parameters declared in the IDF
     * as of type {@code float}.
     *
     * @param paramName the name of the parameter getting the repetition
     * @param repNumber the sequence number of the repetition
     * @param d the {@code double} value of the repetition
     * @throws GemException if the parameter does not exist
     */
    public void setParameterValue(String paramName, int repNumber, double d)
    throws GemException;

    /**
     * Sets a {@code boolean} value as a repetition of a parameter.
     * This should be used for parameters declared in the IDF
     * as of type {@code boolean}.
     *
     * @param paramName the name of the parameter getting the repetition
     * @param repNumber the sequence number of the repetition
     * @param b the {@code boolean} value of the repetition
     * @throws GemException if the parameter does not exist
     */
    public void setParameterValue(String paramName, int repNumber, boolean b)
    throws GemException;

    /**
     * Sets a {@code String} value as a repetition of a parameter.
     * This should be used for parameters declared in the IDF
     * as of type {@code text} or {@code choice}.
     *
     * @param paramName the name of the parameter getting the repetition
     * @param repNumber the sequence number of the repetition
     * @param s the {@code String} value of the repetition
     * @throws GemException if the parameter does not exist
     */
    public void setParameterValue(String paramName, int repNumber, String s)
    throws GemException;

    /**
     * Deletes a repetition of a parameter.
     *
     * @param paramName the name of the parameter whose repetition
     *   is to be deleted
     * @param repNumber the sequence number of the repetition
     *   that is to be deleted
     * @throws GemException if the parameter or repetition does not exist
     */
    public void deleteParameterRepetition(String paramName, int repNumber)
    throws GemException;

    /**
     * Calls the ParameterCheckMethod of the current stage,
     *  if it has one, and otherwise returns {@code null}.
     *
     * @return what the ParameterCheckMethod of the current
     *  stage returns, or {@code null} if it does not have one.
     */
    public String parameterCheck()
    throws GemException;

    /**
     * Calls the AskIfMethod of a Question.
     *
     * @return what the AskIfMethod returns
     * @throws GemException if the question does not exist
     *  in the current command
     */
    public boolean questionShouldBeAsked(String questionName)
    throws GemException;

    /**
     * Calls the DefaultValueMethod of a Parameter or Question.
     *
     * @return what the DefaultValueMethod returns, if it exists
     * @return null, if there is no DefaultValueMethod for this parameter
     * @throws GemException if the parameter or question does not exist
     *  in the current command
     */
    public Object callDefaultValueMethod(String paramOrQuestionName)
    throws GemException;

    /**
     * Calls the CommmandMethod of the current Command.
     *
     * @throws GemException if the CommandMethod throws an exception
     */
    public void callCommandMethod()
    throws GemException;

    /**
     * Calls the QuitAfterIf method, if the selected command has one.
     * Otherwise, returns the value of QuitAfter.
     * @return the result of the QuitAfterIf method call, if the
     *  selected command has one.
     * @return the value of QuitAfter otherwise.
     */
    public boolean quitAfterCurrentCommand()
    throws GemException;

    /**
     * Sets the {@code ShowTextHandler} for the interface
     * interpreter.  This is the object whose {@code showText}
     * method will be called whenever the application engine
     * calls the {@code showText} method in {@code Gem}.
     *
     * @param showTextHandler the {@code ShowTextHandler} for the
     *	application engine
     */
    public void setShowTextHandler(ShowTextHandler showTextHandler);

    /**
     * Gets the {@code ShowTextHandler} previously set for the
     * interface interpreter.
     * @return the {@code ShowTextHandler} previously set for
     *	the application engine, or {@code null} if no
     *	{@code ShowTextHandler} was set
     */
    public ShowTextHandler getShowTextHandler();

}
