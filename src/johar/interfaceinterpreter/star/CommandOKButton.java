package johar.interfaceinterpreter.star;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Used for creating the OK button in the Command Dialog.
 */
public class CommandOKButton extends JButton implements ActionListener {
	private CommandController _cc;
	
	/**
	 * Class' constructor
	 * @param cc
	 * the Command Controller object
	 */
	public CommandOKButton(CommandController cc) {
		super();
		_cc = cc;
		addActionListener(this);
		setText("OK");
		setName("OKButton");
	}

	/**
	 * Handles the event raised when the OK button is clicked
	 */
	public void actionPerformed(ActionEvent event) {
		_cc.commandOKButtonClicked();
	}
}
