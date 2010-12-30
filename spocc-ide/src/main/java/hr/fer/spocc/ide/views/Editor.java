/*
 * Editor.java
 *
 * Copyright (C) 2010 Hrvoje Novak <hrvojenova@gmail.com>
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
package hr.fer.spocc.ide.views;

import hr.fer.spocc.CompilationListener;
import hr.fer.spocc.ide.actions.CompileAction;
import hr.fer.spocc.ide.actions.CompileWorker;
import hr.fer.spocc.ide.actions.DrawGraphAction;
import hr.fer.spocc.lexer.LexicalError;
import hr.fer.spocc.lexer.LexicalErrorListener;
import hr.fer.spocc.parser.SyntaxError;
import hr.fer.spocc.parser.SyntaxErrorListener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import com.metaphaseeditor.MetaphaseEditor;

/**
 * @author Hrvoje Novak
 *
 */
public class Editor extends JFrame {

	private static final Editor INSTANCE = new Editor();
	public static final MetaphaseEditor editorPanel = new MetaphaseEditor();
	public static final DefaultTableModel model = new DefaultTableModel();
	public static final JCheckBox symbolTable = new JCheckBox("symbol-table");
	public static final JCheckBox tokenList = new JCheckBox("token-list");
	public static final JCheckBox parseTree = new JCheckBox("parse-tree");
	public static final JCheckBox ignoreLexicalErrors = new JCheckBox("ignore-lexical-errors");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void showGui() {
		addCompileListeners();
		setTitle("SPOCC - Unitled");
		JPanel app = new JPanel(new BorderLayout(5, 5));
		app.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel editor = new JPanel();
		editor.setBorder(BorderFactory.createTitledBorder("Editor"));
		editor.add(editorPanel);
		JPanel actions = new JPanel();
		actions.setBorder(BorderFactory.createTitledBorder("Actions"));
		actions.setLayout(new GridLayout(3, 1, 10, 10));
		JButton compile = new JButton("Compile");
		compile.addMouseListener(new CompileAction());
		JButton drawGraph = new JButton("Draw graph");
		drawGraph.addMouseListener(new DrawGraphAction());
		JPanel options = new JPanel(new GridLayout(4, 1));
		options.setBorder(BorderFactory.createTitledBorder("Compile options"));
		options.add(symbolTable);
		options.add(tokenList);
		options.add(parseTree);
		options.add(ignoreLexicalErrors);
		actions.add(options);
		actions.add(compile);
		actions.add(drawGraph);
		JTable markers = new JTable(model);
		markers.setEnabled(false);
		markers.setDefaultRenderer(Object.class, new MarkersTableRenderer());
		model.addColumn("Line");
		model.addColumn("Type");
		model.addColumn("Description");
		markers.setPreferredScrollableViewportSize(new Dimension(100, 150));
		JScrollPane jsp = new JScrollPane(markers);
		app.add(editor, BorderLayout.WEST);
		app.add(actions, BorderLayout.EAST);
		app.add(jsp, BorderLayout.SOUTH);
		this.getContentPane().add(app);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void addCompileListeners() {
		CompileWorker.c.addCompilationListener(new CompilationListener() {

			@Override
			public void sourceFileCompiled(File sourceFile) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						JOptionPane.showMessageDialog(Editor.getInstance(), "Compiled successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
					}
				});
			}

			@Override
			public void sourceFileCompilationFailed(File sourceFile) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						JOptionPane.showMessageDialog(Editor.getInstance(), "Compilation failed", "Fail", JOptionPane.ERROR_MESSAGE);
					}
				});
			}
		});
		CompileWorker.c.addLexicalErrorListener(new LexicalErrorListener() {

			@Override
			public void onLexicalError(final LexicalError error) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						model.insertRow(model.getRowCount(), new Object[] {error.getLine(), error.getLevel(), error.getMessage()});
					}
				});
			}
		});
		CompileWorker.c.addSyntaxErrorListener(new SyntaxErrorListener() {

			@Override
			public void onSyntaxError(final SyntaxError error) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						model.insertRow(model.getRowCount(), new Object[] {error.getLine(), error.getLevel(), error.getMessage()});
					}
				});
			}
		});
	}
	
	public static Editor getInstance() {
		return INSTANCE;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				getInstance().showGui();
			}
		});
	}
}
