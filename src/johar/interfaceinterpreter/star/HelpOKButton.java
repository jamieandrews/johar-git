package johar.interfaceinterpreter.star;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * The OK button on the Help Dialog Box.
 *
 */
public class HelpOKButton extends JButton implements ActionListener {
	private CommandController _cc;
	
	/**
	 * 
	 * @param cc
	 * Command Controller object
	 */
	public HelpOKButton(CommandController cc) {
		super();
		_cc = cc;
		addActionListener(this);
		setText("OK");
		setName("ok");
	}

	/**
	 * Handles event raised when the button is clicked.
	 */
	public void actionPerformed(ActionEvent event) {
		_cc.helpOKButtonClicked();		
	}
}
