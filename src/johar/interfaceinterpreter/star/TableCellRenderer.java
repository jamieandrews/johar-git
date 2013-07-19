package johar.interfaceinterpreter.star;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Cell Renderer for all Help tables.
 *
 */
public class TableCellRenderer extends DefaultTableCellRenderer implements MouseMotionListener {

	/**
	 * Returns the rendered cell.
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                              int rowNumber, int columnNumber) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowNumber, columnNumber);

		table.addMouseMotionListener(this);
		
		//Sets the colour of the texts in the first column to blue.
		if (columnNumber == 0) 
			comp.setForeground(Color.blue);		
		else
			comp.setForeground(Color.black);
		
		return comp;
	}

	/**
	 * Handles event raised when the mouse is dragged.
	 */
	public void mouseDragged(MouseEvent e) {}

	/**
	 * Handles event raised when the mouse is moved.
	 */
	public void mouseMoved(MouseEvent e) {
		//Use a hand cursor on the first column
		JTable table = (JTable) e.getComponent();
		if (table.columnAtPoint(e.getPoint()) == 0)
			table.setCursor(new Cursor(Cursor.HAND_CURSOR));
		else
			table.setCursor(null);
	}


}
