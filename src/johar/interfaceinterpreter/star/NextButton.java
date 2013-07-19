package johar.interfaceinterpreter.star;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Used for creating the Next button in the Command Dialog.
 */
public class NextButton extends JButton implements ActionListener {
	private CommandController _cc;
	
	/**
	 * Class' constructor
	 * @param cc
	 * the Command Controller object
	 */
	public NextButton(CommandController cc) {
		super();
		_cc = cc;
		addActionListener(this);
	}
	
	/**
	 * Handles the event raised when the Next button is clicked.
	 */
	public void actionPerformed(ActionEvent event) {
		_cc.nextButtonClicked();
	}
}
