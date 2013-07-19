package johar.interfaceinterpreter.star;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Used for creating the Add Another button in a parameter widget (in the Command Dialog).
 */
public class AddMoreButton extends JButton implements ActionListener {
	private CommandController _cc;
	private String _paramName;

	/**
	 * Class' constructor
	 * @param cc
	 * the Command Controller object
	 * @param paramName
	 * the parameter name
	 */
	public AddMoreButton(CommandController cc, String paramName) {
		super();
		_cc = cc;
		_paramName = paramName;
		setBorder(null);
		setPreferredSize(new Dimension(27, 26));
		setIcon(new ImageIcon(getClass().getResource("addMore.png")));
        setToolTipText("Add another");
		addActionListener(this);
	}

	/**
	 * Handles the event raised when the Add Another button is clicked
	 */
	public void actionPerformed(ActionEvent event) {
		_cc.addMoreButtonClicked(_paramName);
	}
}
