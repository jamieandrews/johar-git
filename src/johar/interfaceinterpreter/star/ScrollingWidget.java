/*
 * Used for creating a scrollable widget
 */

package johar.interfaceinterpreter.star;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Attaches scrollbars to widgets.
 *
 */
public class ScrollingWidget extends JScrollPane {
	private JComponent _widget;
	
	/**
	 * 
	 * @param widget
	 * widget
	 */
	public ScrollingWidget(JComponent widget) {
		setViewportView(widget);
		_widget = widget;
	}
	
	/**
	 * Returns a scrollable Text Widget.
	 * @return
	 * a scrollable text area
	 */
	public JTextArea getTextAreaInstance(){
		if (_widget instanceof JTextArea){
			return (JTextArea) _widget;
		}
		return null;
	}
	
	/**
	 * Returns a scrollable Table Widget.
	 * @return
	 * a scrollable table widget
	 */
	public TableWidget getTableWidgetInstance(){
		if (_widget instanceof TableWidget){
			return (TableWidget) _widget;
		}
		return null;
	}

}
