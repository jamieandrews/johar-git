package johar.interfaceinterpreter.star;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Used for creating the Cancel button in the Command Dialog.
 */
public class CommandCancelButton extends JButton implements ActionListener {
	private CommandController _cc;
	
	/**
	 * Class' constructor
	 * @param cc
	 * the Command Controller object
	 */
	public CommandCancelButton(CommandController cc) {
		super();
		_cc = cc;
		addActionListener(this);
		setText("Cancel");
		setName("CancelButton");
	}
	
	/**
	 * Handles the event raised when the Cancel button is clicked
	 */
	public void actionPerformed(ActionEvent event) {
		_cc.commandCancelButtonClicked();
	}

}
