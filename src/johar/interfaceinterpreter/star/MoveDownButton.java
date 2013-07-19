package johar.interfaceinterpreter.star;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * Used for creating the Move Down button for a repetition in a parameter widget (in the Command Dialog).
 */
public class MoveDownButton extends JButton implements ActionListener {
	private CommandController _cc;
	private String _paramName;
	private int _repNumber;
	
	/**
	 * Class' constructor
	 * @param cc
	 * the Command Controller object
	 * @param paramName
	 * the parameter name
	 * @param repNumber
	 * repetition number
	 */
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

	/**
	 * Handles the event raised when the Move Down button is clicked.
	 */
	public void actionPerformed(ActionEvent event) {
		_cc.moveDownButtonClicked(_paramName, _repNumber);
	}
	
	/**
	 * Resets the repetition number to which the button is attached.
	 * @param repNumber
	 * repetition number
	 */
	protected void setRepNumber(int repNumber){
		_repNumber = repNumber;
	}
	
	/**
	 * Gets the repetition number to which the button is attached.
	 * @return
	 * repetition number
	 */
	protected int getRepNumber(){
		return _repNumber;
	}
}
