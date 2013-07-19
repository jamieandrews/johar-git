/*
 * Used for creating the Status Bar
 */

package johar.interfaceinterpreter.star;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * The Status Bar in the Star Window.
 *
 */
public class StatusBar extends JLabel {
	
	public StatusBar(){
		setName("status");
		setStatusText("Welcome.");
		//setBorder(BorderFactory.createEtchedBorder());
	}
	
	/**
	 * Sets the specified text in the Status Bar.
	 * @param statusText
	 * text to display
	 */
	public void setStatusText(String statusText){
		setText(statusText);
	}
}
