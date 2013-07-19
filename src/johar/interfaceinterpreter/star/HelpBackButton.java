package johar.interfaceinterpreter.star;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * The Back button on the Help Dialog Box.
 *
 */
public class HelpBackButton extends JButton implements ActionListener {
	private CommandController _cc;
	
	/**
	 * 
	 * @param cc
	 * Command Controller object
	 */
	public HelpBackButton(CommandController cc) {
		super();
		_cc = cc;
		addActionListener(this);
		setText("Back");
		setName("back");		
	}

	/**
	 * Handles event raised when the button is clicked.
	 */
	public void actionPerformed(ActionEvent event) {
		_cc.helpBackButtonClicked();
	}
}
