/*
 * Used for creating the Text Display Area
 */

package johar.interfaceinterpreter.star;

import javax.swing.JTextArea;

public class TextDisplayArea extends JTextArea {

	public TextDisplayArea() {
		setLineWrap(true);
		setWrapStyleWord(true);
	}
	
	public void setContent(String message){
		setText(message);
	}

}
