/*
 * Used for creating the Menus
 */

package johar.interfaceinterpreter.star;

import javax.swing.JMenu;

/**
 * A Menu in the Star GUI. A Menu corresponds to a particular CommandGroup in the IDF.
 *
 */
public class CommandMenu extends JMenu {

	/**
	 * The class' constructor.
	 * @param name
	 * name of the Menu. It is used to identify the menu during processing.
	 * @param text
	 * Menu text shown to users
	 */
	public CommandMenu(String name, String text) {
		setName(name);
		setText(text);
	}
}
