/*
 * Used for creating the Menu items
 */

package johar.interfaceinterpreter.star;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class CommandMenuItem extends JMenuItem implements ActionListener {
	private String _commandName;
	private CommandController _cc;
	public CommandMenuItem(String commandName, CommandController cc){
		super();
		_cc = cc;
		_commandName = commandName;
		addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event){
		_cc.commandMenuItemClicked(_commandName);
	}
}
