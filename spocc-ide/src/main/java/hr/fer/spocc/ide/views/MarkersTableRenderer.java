package hr.fer.spocc.ide.views;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MarkersTableRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int VALIDATION_COLUMN = 1;

	public MarkersTableRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp = super.getTableCellRendererComponent(table,  value, isSelected, hasFocus, row, column);

		String s =  table.getModel().getValueAt(row, VALIDATION_COLUMN ).toString();

		if(s.equalsIgnoreCase("error")) {
			comp.setForeground(Color.red);
		}
		else if (s.equalsIgnoreCase("warning")) {
			comp.setForeground(Color.yellow);
		}
		else {
			comp.setForeground(null);
		}
		return comp;
	}
}
