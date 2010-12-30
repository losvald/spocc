package hr.fer.spocc.ide.actions;

import hr.fer.spocc.ide.views.Editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CompileAction implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		int rowCount = Editor.model.getRowCount();
		if (rowCount != 0) { 
			for (int i = rowCount-1; i >= 0; i--) {
				Editor.model.removeRow(i);
			}
		}
		final CompileWorker compiler = new CompileWorker();
		compiler.execute();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
