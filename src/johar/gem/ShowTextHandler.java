/* ShowTextHandler.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

package johar.gem;

/**
 * The interface implemented by any object designed to handle
 * {@code showText} calls from the application engine.
 * Generally a {@code ShowTextHandler} will be an object
 * closely associated with the application engine, or might
 * even be the application engine itself.
 * This class is an instance of the Command pattern.
 */
public interface ShowTextHandler {
    public static final int DEBUG_LEVEL = 0;
    public static final int STATUS_LEVEL = 1000;
    public static final int RESULT_LEVEL = 2000;
    public static final int HIGHPRIO_LEVEL = 3000;
    /**
     * Show text with a given priority level.
     * If this {@code ShowTextHandler} has been set as the
     * {@code ShowTextHandler} in the {@code Gem}, then
     * whenever the application engine calls the {@code showText}
     * method in {@code Gem}, this method will be called.
     *
     * @param text the text to display
     * @param priorityLevel the priority level of the message
     * @see Gem
     */
    public void showText(String text, int priorityLevel);
}
