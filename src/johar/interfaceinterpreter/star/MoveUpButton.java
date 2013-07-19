package johar.interfaceinterpreter.star;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * Used for creating the Move Up button for a repetition in a parameter widget (in the Command Dialog).
 */
public class MoveUpButton extends JButton implements ActionListener {
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
	public MoveUpButton(CommandController cc, String paramName, int repNumber) {
		super();
		_cc = cc;
		_paramName = paramName;
		_repNumber = repNumber;
		setBorder(null);
		setPreferredSize(new Dimension(26, 26));
		setIcon(new ImageIcon(getClass().getResource("moveUp.png")));		
        	setToolTipText("Move up");
		addActionListener(this);
	}

	/**
	 * Handles the event raised when the Move Up button is clicked.
	 */
	public void actionPerformed(ActionEvent event) {
		_cc.moveUpButtonClicked(_paramName, _repNumber);
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
