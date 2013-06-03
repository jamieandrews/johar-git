/*
 * Used for creating the Move down button (in the Command Window)
 */

package johar.interfaceinterpreter.star;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class MoveDownButton extends JButton implements ActionListener {
	private CommandController _cc;
	private String _paramName;
	private int _repNumber;
	
	public MoveDownButton(CommandController cc, String paramName, int repNumber) {
		super();
		_cc = cc;
		_paramName = paramName;
		_repNumber = repNumber;
		setBorder(null);
		setPreferredSize(new Dimension(26, 26));
		setIcon(new ImageIcon(getClass().getResource("moveDown.png")));
        	setToolTipText("Move down");
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		_cc.moveDownButtonClicked(_paramName, _repNumber);
	}
	
	public void setRepNumber(int repNumber){
		_repNumber = repNumber;
	}
}
