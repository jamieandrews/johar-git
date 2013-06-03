/*
 * Used for creating the Status Bar
 */

package johar.interfaceinterpreter.star;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class StatusBar extends JLabel {
	private String _statusText;
	public StatusBar(String statusText){
		_statusText = statusText;
		setStatusText(_statusText);
		setBorder(BorderFactory.createEtchedBorder());
	}
	
	public StatusBar(){		
		setBorder(BorderFactory.createEtchedBorder());
	}
	
	public void setStatusText(String statusText){
		try {
			setText(statusText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
