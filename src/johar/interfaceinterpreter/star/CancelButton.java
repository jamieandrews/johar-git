/*
 * Used for creating the Cancel button (in the Command Window)
 */

package johar.interfaceinterpreter.star;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class CancelButton extends JButton implements ActionListener {
	private CommandController _cc;
	
	public CancelButton(CommandController cc) {
		super();
		_cc = cc;
		addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event) {
		_cc.cancelButtonClicked();
	}

}
