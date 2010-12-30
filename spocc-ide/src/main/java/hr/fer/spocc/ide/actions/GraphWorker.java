/*
 * GraphWorker.java
 *
 * Copyright (C) 2010 Hrvoje Novak <hrvojenova@gmail.com>
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 *
 * This file is part of SPoCC.
 *
 * SPoCC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SPoCC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SPoCC. If not, see <http://www.gnu.org/licenses/>.
 */
package hr.fer.spocc.ide.actions;

import hr.fer.spocc.CompilerFactory;
import hr.fer.spocc.export.DotExporter;
import hr.fer.spocc.ide.views.Editor;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import org.sglj.swing.dialog.MyDialog;
import org.sglj.swing.util.ScreenUtils;

/**
 * @author Leo Osvald
 * @author Hrvoje Novak
 *
 */
public class GraphWorker extends SwingWorker<Boolean, Void> {

	private static final String SEPARATOR = System.getProperty("file.separator"); 
	
	@Override
	protected Boolean doInBackground() throws Exception {
		if (!CompilerFactory.getInstance().isCompiledSuccessfully()) {
			JOptionPane.showMessageDialog(Editor.getInstance(), 
					"The program has to be compiled first",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		try {
			System.out.println("Parse tree: " + CompilerFactory
					.getInstance().getParseTree());
			String fileName = System.getProperty("java.io.tmpdir") + SEPARATOR + "stablo.gv";
			System.err.println("Drawing graph to: "+fileName);
			DotExporter.exportToFile(new File(fileName),
					CompilerFactory.getInstance().getParseTree());
			String iconFilename =  System.getProperty("java.io.tmpdir") + SEPARATOR + "stablo.png";
			String command = "dot -Tpng "+fileName + " > "+iconFilename;
			System.err.println("Executing: "+ command);
			try {
				Process p;
				if (System.getProperty("os.name").indexOf("Windows") > -1)
					p = Runtime.getRuntime().exec(new String[]{"cmd","/C",command});
				else
					p = Runtime.getRuntime().exec(new String[]{"sh","-c",command});
				p.waitFor();
				ImageIcon icon = new ImageIcon(iconFilename);
				JLabel imageLabel = new JLabel(icon);
				JScrollPane jsp = new JScrollPane(imageLabel);
				
				final MyDialog dialog = new MyDialog(Editor.getInstance()) {
					
					@Override
					protected void onClose() {
						dispose();
					}
					
					@Override
					protected void handleOption(Object option) {
						System.out.println("User option: " + option + "(instance of Integer?"
								+ (option instanceof Integer) + ")");
						//ako je zatvoren dijalog, nista
						if(option == null) return ;
						//inace, pogledaj sta je kliknuto
						if(option.equals(JOptionPane.CLOSED_OPTION)) {
							onClose();
						}
					}
				};
				
				dialog.setLayout(new BorderLayout());
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setModalityType(ModalityType.MODELESS);
				dialog.setTitle("Parse tree - " + iconFilename);
				dialog.getContentPane().add(jsp, BorderLayout.CENTER);
				dialog.getContentPane().add(new JLabel("Graph written to: " + fileName), 
						BorderLayout.SOUTH);
				dialog.setMinimumSize(ScreenUtils.getValidDimension(
						new Dimension(1024, 768)));
				dialog.setVisible(true);
				dialog.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
							dialog.dispose();
					}
				});
				
				return true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				JOptionPane.showMessageDialog(Editor.getInstance(), "Error", 
						"Parse tree written to: "+fileName
						+ "\nFailed to execute command: \"" + command + "\"", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(Editor.getInstance(), null, 
					"Error displaying parse tree", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

}
