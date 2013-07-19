/*
 * Used for creating the Menu items
 */

package johar.interfaceinterpreter.star;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

/**
 * A Menu Item in the Star GUI.
 * 
 */
public class CommandMenuItem extends JMenuItem implements ActionListener {
	private String _commandName;
	private CommandController _cc;
	
	/**
	 * The class' constructor.
	 * @param commandName
	 * command name
	 * @param cc
	 * Command Controller object
	 * @param isStarMenuItem
	 * Determines whether the menu item is for the "Star" menu or for CommandGroup menu.<br />
	 * - true, if it is for "Star" menu; false, if otherwise.
	 */
	public CommandMenuItem(String commandName, CommandController cc, boolean isStarMenuItem){
		super();
		_cc = cc;
		_commandName = commandName;
		setName(commandName);
		
		if (isStarMenuItem)
			addActionListener(new StarMenuListener());
		else
			addActionListener(this);
	}
	
	/**
	 * Handles event raised when the menu item is clicked.
	 */
	public void actionPerformed(ActionEvent event){
		_cc.commandMenuItemClicked(_commandName);
	}
	
	//Handler for the "Star" menu item events.
	private class StarMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent event){
			_cc.commandMenuItemClickedStar(_commandName);
		}
    }
}
