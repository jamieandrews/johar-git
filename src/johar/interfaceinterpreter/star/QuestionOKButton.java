package johar.interfaceinterpreter.star;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * The OK button on the Question Dialog Box.
 *
 */
public class QuestionOKButton extends JButton implements ActionListener {
	private CommandController _cc;
	private String _questionName;
	
	/**
	 * 
	 * @param cc
	 * Command Controller object
	 * @param questionName
	 * question name
	 */
	public QuestionOKButton(CommandController cc, String questionName) {
		super();
		_cc = cc;
		_questionName = questionName;
		addActionListener(this);
		setText("OK");
		setName("ok");
	}

	/**
	 * Handles event raised when the button is clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		_cc.questionOKButtonClicked(_questionName);		
	}

}
