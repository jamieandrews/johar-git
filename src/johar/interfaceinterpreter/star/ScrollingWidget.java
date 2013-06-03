/*
 * Used for creating a scrollable widget
 */

package johar.interfaceinterpreter.star;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ScrollingWidget extends JScrollPane {
	private JComponent _widget;
	
	public ScrollingWidget(JComponent widget) {
		setViewportView(widget);
		_widget = widget;
	}
	
	public JTextArea getTextAreaInstance(){
		if (_widget instanceof JTextArea){
			return (JTextArea) _widget;
		}
		return null;
	}

}
