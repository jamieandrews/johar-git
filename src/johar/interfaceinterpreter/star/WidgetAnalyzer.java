package johar.interfaceinterpreter.star;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

/**
 * Searches for widgets in containers; It also retrieves values in widgets.
 *
 */
public class WidgetAnalyzer {
	
	/**
	 * Searches for a widget in the specified JComponent container and returns its index if found.
	 * @param container
	 * container being searched
	 * @param name
	 * name of the widget to find
	 * @return
	 * index of the widget (-1 if not found)
	 */
	protected static int getWidgetIndex(JComponent container, String name){
		Component[] widgets = container.getComponents();
		for (int i = 0; i < widgets.length; i++){
			try {
				if (widgets[i].getName().equals(name)){
					return i;
				}
			} catch (Exception e) {}
		}
		return -1;
	}
	
	/**
	 *  Searches for a widget in the specified Frame and returns its index if found.
	 * @param frame
	 * frame being searched
	 * @param name
	 * name of the widget to find
	 * @return
	 * index of the widget (-1 if not found)
	 */
	protected static int getWidgetIndexInFrame(JFrame frame, String name){
		Component[] widgets = ((JComponent) frame.getContentPane()).getComponents();

		for (int i = 0; i < widgets.length; i++){
			try {
				if (widgets[i].getName().equals(name)){
					return i;
				}
			} catch (Exception e) {}
		}
		return -1;
	}
	
	/**
	 * Searches for a widget in the specified JComponent container and returns the widget if found.
	 * @param container
	 * container being searched
	 * @param name
	 * name of the widget to find
	 * @return
	 * the widget (null if not found)
	 */
	protected static JComponent getWidget(JComponent container, String name){
		Component[] widgets = container.getComponents();
		for (int i = 0; i < widgets.length; i++){
			try {
				if (widgets[i].getName().equals(name)){
					return (JComponent) widgets[i];
				}
			} catch (Exception e) {}
		}
		return null;
	}
	
	/**
	 * Searches for a widget in the specified JFrame and returns the widget if found.
	 * @param frame
	 * frame being searched
	 * @param name
	 * name of the widget to find
	 * @return
	 * the widget (null if not found)
	 */
	protected static JComponent getWidgetInFrame(JFrame frame, String name){
		JComponent panel = (JComponent) frame.getContentPane().getComponent(0);
		for (Component widget : panel.getComponents()){
			try {
				if (widget.getName().equals(name)){
					return (JComponent) widget;
				}
			} catch (Exception e) {}
		}
		return null;
	}	
	
	/**
	 * Gets the value in the specified widget.
	 * @param widget
	 * the specified widget
	 * @return
	 * the value in the widget
	 */
	protected static Object getValue(JComponent widget){
		if (widget instanceof JTextField){
			return ((JTextField) widget).getText().trim();
		}
		else if (widget instanceof ScrollingWidget){
			ScrollingWidget sw = (ScrollingWidget) widget;
			return sw.getTextAreaInstance().getText().trim();
		}
		else if (widget instanceof JRadioButton){
			return ((JRadioButton) widget).isSelected();
		}
		else if (widget instanceof JFormattedTextField){
			return ((JFormattedTextField) widget).getText();
		}
		else if (widget instanceof JComboBox){
			return ((JComboBox) widget).getSelectedItem();
		}
		else if (widget instanceof JSpinner){
			return ((JSpinner) widget).getValue();
		}
		else if (widget instanceof JXDatePicker){
			return ((JXDatePicker) widget).getDate();
		}
		return null;
	}
	
	/**
	 * Gets the index of a menu item in the specified menu.
	 * @param menu
	 * menu being searched
	 * @param name
	 * name of the menu item to find
	 * @return
	 * index of the menu item (-1 if not found)
	 */
	protected static int getMenuItemIndex(JMenu menu, String name){
		Component[] c = menu.getMenuComponents();
		for (int i = 0; i < c.length; i++){
			try {
				if (c[i].getName().equals(name)){
					return i;
				}
			} catch (Exception e) {}
		}
		return -1;
	}

	/**
	 * Gets the index of a selected value in a widget.
	 * @param widget
	 * the specified widget
	 * @return
	 * the index of the selected value
	 */
	protected static int getValueIndex(JComponent widget) {
		if (widget instanceof JComboBox){
			return ((JComboBox) widget).getSelectedIndex();
		}
		
		return -1;
	}

}
