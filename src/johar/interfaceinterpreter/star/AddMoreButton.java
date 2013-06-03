/*
 * Used for creating the Add another button (in the Command Window)
 */

package johar.interfaceinterpreter.star;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class AddMoreButton extends JButton implements ActionListener {
	private CommandController _cc;
	private String _paramName;

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

	public void actionPerformed(ActionEvent event) {
		_cc.addMoreButtonClicked(_paramName);
	}
}
