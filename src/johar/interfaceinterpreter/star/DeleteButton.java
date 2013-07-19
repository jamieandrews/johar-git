package johar.interfaceinterpreter.star;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Used for creating the Delete button for a repetition in a parameter widget (in the Command Dialog).
 */
public class DeleteButton extends JButton implements ActionListener {
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
	public DeleteButton(CommandController cc, String paramName, int repNumber) {
		super();
		_cc = cc;
		_paramName = paramName;
		_repNumber = repNumber;
		setBorder(null);
		setPreferredSize(new Dimension(26, 26));
		setIcon(new ImageIcon(getClass().getResource("delete.png")));
        setToolTipText("Delete");
		addActionListener(this);
	}

	/**
	 * Handles the event raised when the Delete button is clicked.
	 */
	public void actionPerformed(ActionEvent event) {
		_cc.deleteButtonClicked(_paramName, _repNumber);
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
