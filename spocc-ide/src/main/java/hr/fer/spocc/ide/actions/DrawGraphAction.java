package hr.fer.spocc.ide.actions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DrawGraphAction implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		final GraphWorker graph = new GraphWorker();
		graph.execute();
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
