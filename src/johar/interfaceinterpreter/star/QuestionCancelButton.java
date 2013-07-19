package johar.interfaceinterpreter.star;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * The OK button on the Question Dialog Box.
 *
 */
public class QuestionCancelButton extends JButton implements ActionListener {
	private CommandController _cc;
	private String _questionName;
	
	/**
	 * 
	 * @param cc
	 * Command Controller object
	 * @param questionName
	 * question name
	 */
	public QuestionCancelButton(CommandController cc, String questionName) {
		super();
		_cc = cc;
		_questionName = questionName;
		addActionListener(this);
		setText("Cancel");
		setName("cancel");
	}

	/**
	 * Handles event raised when the button is clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		_cc.questionCancelButtonClicked(_questionName);		
	}

}
