/*
 * Used for creating the Text Display Area
 */

package johar.interfaceinterpreter.star;

import javax.swing.JTextArea;

/**
 * Text Display Area of the Star Window.
 *
 */
public class TextDisplayArea extends JTextArea {
	
	public TextDisplayArea() {
		setLineWrap(true);
		setWrapStyleWord(true);
		setEditable(false);
	}
	
	/**
	 * Sets the specified text in the Text Display Area.
	 * @param message
	 * text to display
	 */
	public void setContent(String message){		
		append(message);
		append("\n");
	}

}
;