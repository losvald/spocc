/**
 * Metaphase Editor - WYSIWYG HTML Editor Component
 * Copyright (C) 2010  Rudolf Visagie
 * Full text of license can be found in com/metaphaseeditor/LICENSE.txt
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * The author can be contacted at metaphase.editor@gmail.com.
 */

package com.metaphaseeditor;

import com.metaphaseeditor.action.ClearFormattingAction;
import com.metaphaseeditor.action.DecreaseIndentAction;
import com.metaphaseeditor.action.FindReplaceAction;
import com.metaphaseeditor.action.IncreaseIndentAction;
import com.metaphaseeditor.action.InsertHtmlAction;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.swing.JTextComponentSpellChecker;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipInputStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML.Tag;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Hrvoje Novak
 */
public class MetaphaseEditorPanel extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextComponentSpellChecker spellChecker = null;
	private SpellDictionary dictionary = null;
	private JTextArea htmlTextArea;
	private boolean htmlSourceMode = false;
	private Hashtable<Object, Action> editorKitActions;
	private SpellCheckDictionaryVersion spellCheckDictionaryVersion = SpellCheckDictionaryVersion.LIBERAL_US;
	private String customDictionaryFilename = null;
	private File currentFile;
	private String title = "";

	/** Listener for the edits on the current document. */
	protected UndoableEditListener undoHandler = new UndoHandler();

	/** UndoManager that we add edits to. */
	protected UndoManager undo = new UndoManager();

	private UndoAction undoAction = new UndoAction();
	private RedoAction redoAction = new RedoAction();
	private StyledEditorKit.CutAction cutAction = new StyledEditorKit.CutAction();
	private StyledEditorKit.CopyAction copyAction = new StyledEditorKit.CopyAction();
	private StyledEditorKit.PasteAction pasteAction = new StyledEditorKit.PasteAction();
	private FindReplaceAction findReplaceAction;

	private StyledEditorKit editorKit = new StyledEditorKit();

	private JPopupMenu contextMenu;

	private List<ContextMenuListener> contextMenuListeners = new ArrayList<ContextMenuListener>();
	private List<EditorMouseMotionListener> editorMouseMotionListeners = new ArrayList<EditorMouseMotionListener>();

	private enum FontItem {
		FONT("Font", null), ARIAL("Arial", "Arial"), COMIC_SANS_MS(
				"Comic Sans MS", "Comic Sans MS"), COURIER_NEW("Courier New",
				"Courier New"), GEORGIA("Georgia", "Georgia"), LUCINDA_SANS_UNICODE(
						"Lucinda Sans Unicode", "Lucinda Sans Unicode"), TAHOMA(
								"Tahoma", "Tahoma"), TIMES_NEW_ROMAN("Times New Roman",
								"Times New Roman"), TREBUCHET_MS("Trebuchet MS", "Trebuchet MS"), VERDANA(
										"Verdana", "Verdana");

		private String text;
		private String fontName;

		FontItem(String text, String fontName) {
			this.text = text;
			this.fontName = fontName;
		}

		public String getText() {
			return text;
		}

		public String getFontName() {
			return fontName;
		}

		@Override
		public String toString() {
			return text;
		}
	}

	private enum FontSize {
		FONT_SIZE("Size", -1), SIZE8("8", 8), SIZE9("9", 9), SIZE10("10", 10), SIZE11(
				"11", 11), SIZE12("12", 12), SIZE14("14", 14), SIZE18("18", 18), SIZE20(
						"20", 20), SIZE22("22", 22), SIZE24("24", 24), SIZE26("26", 26), SIZE28(
								"28", 28), SIZE36("36", 36), SIZE48("48", 48), SIZE72("72", 72);

		private String text;
		private int size;

		FontSize(String text, int size) {
			this.text = text;
			this.size = size;
		}

		public String getText() {
			return text;
		}

		public int getSize() {
			return size;
		}

		@Override
		public String toString() {
			return text;
		}
	}

	/** Creates new form MetaphaseEditorPanel */
	public MetaphaseEditorPanel() {
		initComponents();

		createEditorKitActionTable();

		htmlTextArea = new JTextArea();
		htmlTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

		htmlTextPane.setContentType("text/html");
		htmlTextPane.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown()) {
					if (e.getKeyCode() == KeyEvent.VK_S && e.isShiftDown()) {
						saveButtonActionPerformed(new ActionEvent(this, 0, ""));
					}
					else if (e.getKeyCode() == KeyEvent.VK_S) {
						if (currentFile != null) {
							try {
								FileOutputStream fo = new FileOutputStream(currentFile);
								fo.write(getDocument().replaceAll("\r\n", "\n").getBytes());
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e2) {
								e2.printStackTrace();
							} catch (Exception e3) {
								e3.printStackTrace();
							}
						}
						else {
							saveButtonActionPerformed(new ActionEvent(this, 0, ""));
						}
					}
				}
			}
		});

		findReplaceAction = new FindReplaceAction("Find/Replace", htmlTextPane);

		cutButton.setAction(cutAction);
		cutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/cut.png")));
		cutButton.setText("");
		cutButton.setToolTipText("Cut");

		copyButton.setAction(copyAction);
		copyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/copy.png")));
		copyButton.setText("");
		copyButton.setToolTipText("Copy");

		pasteButton.setAction(pasteAction);
		pasteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/paste.png")));
		pasteButton.setText("");
		pasteButton.setToolTipText("Paste");

		undoButton.setAction(undoAction);
		undoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/undo.png")));
		undoButton.setText("");
		undoButton.setToolTipText("Undo");

		redoButton.setAction(redoAction);
		redoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/redo.png")));
		redoButton.setText("");
		redoButton.setToolTipText("Redo");

		findButton.setAction(findReplaceAction);
		findButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/find.png")));
		findButton.setText("");
		findButton.setToolTipText("Find");

		replaceButton.setAction(findReplaceAction);
		replaceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/replace.png")));
		replaceButton.setText("");
		replaceButton.setToolTipText("Replace");

		clearFormattingButton.setAction(new ClearFormattingAction(this,
		"Remove Format"));
		clearFormattingButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/removeformat.png")));
		clearFormattingButton.setText("");
		clearFormattingButton.setToolTipText("Remove Format");

		increaseIndentButton.setAction(new IncreaseIndentAction(
				"Increase Indent", this));
		increaseIndentButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/incindent.png")));
		increaseIndentButton.setText("");
		increaseIndentButton.setToolTipText("Increase Indent");

		decreaseIndentButton.setAction(new DecreaseIndentAction(
				"Decrease Indent", this));
		decreaseIndentButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/decindent.png")));
		decreaseIndentButton.setText("");
		decreaseIndentButton.setToolTipText("Decrease Indent");

		leftJustifyButton.setAction(new StyledEditorKit.AlignmentAction(
				"Left Align", StyleConstants.ALIGN_LEFT));
		leftJustifyButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/leftjustify.png")));
		leftJustifyButton.setText("");
		leftJustifyButton.setToolTipText("Left Justify");

		centerJustifyButton.setAction(new StyledEditorKit.AlignmentAction(
				"Center Align", StyleConstants.ALIGN_CENTER));
		centerJustifyButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/centerjustify.png")));
		centerJustifyButton.setText("");
		centerJustifyButton.setToolTipText("Center Justify");

		rightJustifyButton.setAction(new StyledEditorKit.AlignmentAction(
				"Left Align", StyleConstants.ALIGN_RIGHT));
		rightJustifyButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/rightjustify.png")));
		rightJustifyButton.setText("");
		rightJustifyButton.setToolTipText("Right Justify");

		blockJustifyButton.setAction(new StyledEditorKit.AlignmentAction(
				"Justified Align", StyleConstants.ALIGN_JUSTIFIED));
		blockJustifyButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/blockjustify.png")));
		blockJustifyButton.setText("");
		blockJustifyButton.setToolTipText("Block Justify");

		fontComboBox.setRenderer(new FontListCellRenderer());
		fontComboBox.removeAllItems();
		FontItem[] fontItems = FontItem.values();
		for (int i = 0; i < fontItems.length; i++) {
			fontComboBox.addItem(fontItems[i]);
		}

		fontSizeComboBox.setRenderer(new FontSizeListCellRenderer());
		fontSizeComboBox.removeAllItems();
		FontSize[] fontSizes = FontSize.values();
		for (int i = 0; i < fontSizes.length; i++) {
			fontSizeComboBox.addItem(fontSizes[i]);
		}

		setToolbarFocusActionListener(this);

		htmlTextPane.getInputMap().put(KeyStroke.getKeyStroke("control Z"),
		"Undo");
		htmlTextPane.getActionMap().put("Undo", undoAction);

		htmlTextPane.getInputMap().put(KeyStroke.getKeyStroke("control Y"),
		"Redo");
		htmlTextPane.getActionMap().put("Redo", redoAction);

		htmlTextPane.getInputMap().put(KeyStroke.getKeyStroke("control F"),
		"Find");
		htmlTextPane.getActionMap().put("Find", findReplaceAction);

		htmlTextPane.getInputMap().put(KeyStroke.getKeyStroke("control R"),
		"Replace");
		htmlTextPane.getActionMap().put("Replace", findReplaceAction);

		contextMenu = new JPopupMenu();
		JMenuItem cutMenuItem = new JMenuItem();
		cutMenuItem.setAction(cutAction);
		cutMenuItem.setText("Cut");
		cutMenuItem.setMnemonic('C');
		cutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/cut.png")));
		JMenuItem copyMenuItem = new JMenuItem();
		copyMenuItem.setAction(copyAction);
		copyMenuItem.setText("Copy");
		copyMenuItem.setMnemonic('o');
		copyMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/copy.png")));
		JMenuItem pasteMenuItem = new JMenuItem();
		pasteMenuItem.setAction(pasteAction);
		pasteMenuItem.setText("Paste");
		pasteMenuItem.setMnemonic('P');
		pasteMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/paste.png")));
		contextMenu.add(cutMenuItem);
		contextMenu.add(copyMenuItem);
		contextMenu.add(pasteMenuItem);

		htmlTextPane
		.addMouseMotionListener(new DefaultEditorMouseMotionListener());
		htmlTextPane.setEditorKit(editorKit);

		startNewDocument();

		initSpellChecker();
	}

	// The following two methods allow us to find an
	// action provided by the editor kit by its name.
	private void createEditorKitActionTable() {
		editorKitActions = new Hashtable<Object, Action>();
		Action[] actionsArray = editorKit.getActions();
		for (int i = 0; i < actionsArray.length; i++) {
			Action a = actionsArray[i];
			editorKitActions.put(a.getValue(Action.NAME), a);
		}
	}

	protected void resetUndoManager() {
		undo.discardAllEdits();
		undoAction.update();
		redoAction.update();
	}

	public void startNewDocument() {
		Document oldDoc = htmlTextPane.getDocument();
		if (oldDoc != null)
			oldDoc.removeUndoableEditListener(undoHandler);
		htmlDocument = (DefaultStyledDocument) editorKit
		.createDefaultDocument();
		htmlTextPane.setDocument(htmlDocument);
		htmlTextPane.getDocument().addUndoableEditListener(undoHandler);
		resetUndoManager();
		htmlDocument.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
	}

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		htmlDocument = new javax.swing.text.DefaultStyledDocument();
		toolbarPanel = new javax.swing.JPanel();
		editPanel = new javax.swing.JPanel();
		cutButton = new javax.swing.JButton();
		copyButton = new javax.swing.JButton();
		pasteAsTextButton = new javax.swing.JButton();
		pasteButton = new javax.swing.JButton();
		pagePanel = new javax.swing.JPanel();
		saveButton = new javax.swing.JButton();
		newButton = new javax.swing.JButton();
		previewButton = new javax.swing.JButton();
		openButton = new javax.swing.JButton();
		toolsPanel = new javax.swing.JPanel();
		printButton = new javax.swing.JButton();
		spellcheckButton = new javax.swing.JButton();
		undoRedoPanel = new javax.swing.JPanel();
		undoButton = new javax.swing.JButton();
		redoButton = new javax.swing.JButton();
		searchPanel = new javax.swing.JPanel();
		findButton = new javax.swing.JButton();
		replaceButton = new javax.swing.JButton();
		jPanel7 = new javax.swing.JPanel();
		selectAllButton = new javax.swing.JButton();
		clearFormattingButton = new javax.swing.JButton();
		blockPanel = new javax.swing.JPanel();
		decreaseIndentButton = new javax.swing.JButton();
		increaseIndentButton = new javax.swing.JButton();
		justificationPanel = new javax.swing.JPanel();
		leftJustifyButton = new javax.swing.JButton();
		centerJustifyButton = new javax.swing.JButton();
		blockJustifyButton = new javax.swing.JButton();
		rightJustifyButton = new javax.swing.JButton();
		fontComboBox = new javax.swing.JComboBox();
		fontSizeComboBox = new javax.swing.JComboBox();
		colorPanel = new javax.swing.JPanel();
		textColorButton = new javax.swing.JButton();
		backgroundColorButton = new javax.swing.JButton();
		mainScrollPane = new javax.swing.JScrollPane();
		htmlTextPane = new javax.swing.JTextPane();

		editPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		cutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/cut.png"))); // NOI18N
		cutButton.setToolTipText("Cut");

		copyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/copy.png"))); // NOI18N
		copyButton.setToolTipText("Copy");

		pasteAsTextButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/paste_as_text.png"))); // NOI18N
		pasteAsTextButton.setToolTipText("Paste as plain text");
		pasteAsTextButton
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pasteAsTextButtonActionPerformed(evt);
			}
		});

		pasteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/paste.png"))); // NOI18N
		pasteButton.setToolTipText("Paste");

		javax.swing.GroupLayout editPanelLayout = new javax.swing.GroupLayout(
				editPanel);
		editPanel.setLayout(editPanelLayout);
		editPanelLayout
		.setHorizontalGroup(editPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								editPanelLayout
								.createSequentialGroup()
								.addComponent(
										cutButton,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										25,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(
														copyButton,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														25,
														javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		pasteButton,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		25,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						pasteAsTextButton,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						25,
																						javax.swing.GroupLayout.PREFERRED_SIZE)));
		editPanelLayout
		.setVerticalGroup(editPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								editPanelLayout
								.createSequentialGroup()
								.addGroup(
										editPanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														cutButton,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														25,
														javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																copyButton,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																25,
																javax.swing.GroupLayout.PREFERRED_SIZE)
																.addComponent(
																		pasteButton,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		25,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				pasteAsTextButton,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				25,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
																				.addContainerGap()));

		pagePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/save.png"))); // NOI18N
		saveButton.setToolTipText("Save");
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveButtonActionPerformed(evt);
			}
		});

		newButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/newpage.png"))); // NOI18N
		newButton.setToolTipText("New");
		newButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newButtonActionPerformed(evt);
			}
		});

		previewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/preview.png"))); // NOI18N
		previewButton.setToolTipText("Preview");
		previewButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				previewButtonActionPerformed(evt);
			}
		});

		openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/open.png"))); // NOI18N
		openButton.setToolTipText("Open");
		openButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout pagePanelLayout = new javax.swing.GroupLayout(
				pagePanel);
		pagePanel.setLayout(pagePanelLayout);
		pagePanelLayout
		.setHorizontalGroup(pagePanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								pagePanelLayout
								.createSequentialGroup()
								.addComponent(
										openButton,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										25,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(
														saveButton,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														25,
														javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		newButton,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		25,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						previewButton,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						25,
																						javax.swing.GroupLayout.PREFERRED_SIZE)));
		pagePanelLayout.setVerticalGroup(pagePanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(openButton,
						javax.swing.GroupLayout.PREFERRED_SIZE, 25,
						javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(saveButton,
								javax.swing.GroupLayout.PREFERRED_SIZE, 25,
								javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(previewButton,
										javax.swing.GroupLayout.PREFERRED_SIZE, 25,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(newButton,
												javax.swing.GroupLayout.PREFERRED_SIZE, 25,
												javax.swing.GroupLayout.PREFERRED_SIZE));

		toolsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		printButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/print.png"))); // NOI18N
		printButton.setToolTipText("Print");
		printButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				printButtonActionPerformed(evt);
			}
		});

		spellcheckButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/spellcheck.png"))); // NOI18N
		spellcheckButton.setToolTipText("Check Spelling");
		spellcheckButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				spellcheckButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout toolsPanelLayout = new javax.swing.GroupLayout(
				toolsPanel);
		toolsPanel.setLayout(toolsPanelLayout);
		toolsPanelLayout
		.setHorizontalGroup(toolsPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								toolsPanelLayout
								.createSequentialGroup()
								.addComponent(
										printButton,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										25,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(
														spellcheckButton,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														25,
														javax.swing.GroupLayout.PREFERRED_SIZE)));
		toolsPanelLayout.setVerticalGroup(toolsPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(printButton,
						javax.swing.GroupLayout.PREFERRED_SIZE, 25,
						javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(spellcheckButton,
								javax.swing.GroupLayout.PREFERRED_SIZE, 25,
								javax.swing.GroupLayout.PREFERRED_SIZE));

		undoRedoPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		undoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/undo.png"))); // NOI18N
		undoButton.setToolTipText("Undo");

		redoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/redo.png"))); // NOI18N
		redoButton.setToolTipText("Redo");

		javax.swing.GroupLayout undoRedoPanelLayout = new javax.swing.GroupLayout(
				undoRedoPanel);
		undoRedoPanel.setLayout(undoRedoPanelLayout);
		undoRedoPanelLayout
		.setHorizontalGroup(undoRedoPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								undoRedoPanelLayout
								.createSequentialGroup()
								.addComponent(
										undoButton,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										25,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(
														redoButton,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														25,
														javax.swing.GroupLayout.PREFERRED_SIZE)));
		undoRedoPanelLayout.setVerticalGroup(undoRedoPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(undoButton,
						javax.swing.GroupLayout.PREFERRED_SIZE, 25,
						javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(redoButton,
								javax.swing.GroupLayout.PREFERRED_SIZE, 25,
								javax.swing.GroupLayout.PREFERRED_SIZE));

		searchPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		findButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/find.png"))); // NOI18N
		findButton.setToolTipText("Find");

		replaceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/replace.png"))); // NOI18N
		replaceButton.setToolTipText("Replace");

		javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(
				searchPanel);
		searchPanel.setLayout(searchPanelLayout);
		searchPanelLayout
		.setHorizontalGroup(searchPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								searchPanelLayout
								.createSequentialGroup()
								.addComponent(
										findButton,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										25,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(
														replaceButton,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														25,
														javax.swing.GroupLayout.PREFERRED_SIZE)));
		searchPanelLayout.setVerticalGroup(searchPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(findButton,
						javax.swing.GroupLayout.PREFERRED_SIZE, 25,
						javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(replaceButton,
								javax.swing.GroupLayout.PREFERRED_SIZE, 25,
								javax.swing.GroupLayout.PREFERRED_SIZE));

		jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		selectAllButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/selectall.png"))); // NOI18N
		selectAllButton.setToolTipText("Select All");
		selectAllButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				selectAllButtonActionPerformed(evt);
			}
		});

		clearFormattingButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/removeformat.png"))); // NOI18N
		clearFormattingButton.setToolTipText("Remove Format");

		javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(
				jPanel7);
		jPanel7.setLayout(jPanel7Layout);
		jPanel7Layout
		.setHorizontalGroup(jPanel7Layout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel7Layout
								.createSequentialGroup()
								.addComponent(
										selectAllButton,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										25,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(
														clearFormattingButton,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														25,
														javax.swing.GroupLayout.PREFERRED_SIZE)));
		jPanel7Layout.setVerticalGroup(jPanel7Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(selectAllButton,
						javax.swing.GroupLayout.PREFERRED_SIZE, 25,
						javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(clearFormattingButton,
								javax.swing.GroupLayout.PREFERRED_SIZE, 25,
								javax.swing.GroupLayout.PREFERRED_SIZE));

		blockPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		blockPanel.setPreferredSize(new java.awt.Dimension(122, 29));

		decreaseIndentButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/decindent.png"))); // NOI18N
		decreaseIndentButton.setToolTipText("Decrease Indent");

		increaseIndentButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/incindent.png"))); // NOI18N
		increaseIndentButton.setToolTipText("Increase Indent");

		javax.swing.GroupLayout blockPanelLayout = new javax.swing.GroupLayout(
				blockPanel);
		blockPanel.setLayout(blockPanelLayout);
		blockPanelLayout
		.setHorizontalGroup(blockPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								blockPanelLayout
								.createSequentialGroup()
								.addComponent(
										decreaseIndentButton,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										25,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(
														increaseIndentButton,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														25,
														javax.swing.GroupLayout.PREFERRED_SIZE)
														.addGap(64, 64, 64)));
		blockPanelLayout.setVerticalGroup(blockPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(decreaseIndentButton,
						javax.swing.GroupLayout.PREFERRED_SIZE, 25,
						javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(increaseIndentButton,
								javax.swing.GroupLayout.PREFERRED_SIZE, 25,
								javax.swing.GroupLayout.PREFERRED_SIZE));

		justificationPanel.setBorder(javax.swing.BorderFactory
				.createEtchedBorder());
		justificationPanel.setPreferredSize(new java.awt.Dimension(122, 29));

		leftJustifyButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/leftjustify.png"))); // NOI18N
		leftJustifyButton.setToolTipText("Left Justify");

		centerJustifyButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/centerjustify.png"))); // NOI18N
		centerJustifyButton.setToolTipText("Center Justify");

		blockJustifyButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/blockjustify.png"))); // NOI18N
		blockJustifyButton.setToolTipText("Block Justify");

		rightJustifyButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/rightjustify.png"))); // NOI18N
		rightJustifyButton.setToolTipText("Right Justify");

		javax.swing.GroupLayout justificationPanelLayout = new javax.swing.GroupLayout(
				justificationPanel);
		justificationPanel.setLayout(justificationPanelLayout);
		justificationPanelLayout
		.setHorizontalGroup(justificationPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								justificationPanelLayout
								.createSequentialGroup()
								.addComponent(
										leftJustifyButton,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										25,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(
														centerJustifyButton,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														25,
														javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		rightJustifyButton,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		25,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						blockJustifyButton,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						25,
																						javax.swing.GroupLayout.PREFERRED_SIZE)));
		justificationPanelLayout
		.setVerticalGroup(justificationPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								justificationPanelLayout
								.createSequentialGroup()
								.addGroup(
										justificationPanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														leftJustifyButton,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														25,
														javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																centerJustifyButton,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																25,
																javax.swing.GroupLayout.PREFERRED_SIZE)
																.addComponent(
																		rightJustifyButton,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		25,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				blockJustifyButton,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				25,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
																				.addContainerGap()));

		fontComboBox.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "Arial", "Comic Sans MS", "Courier New",
						"Georgia", "Lucinda Sans Unicode", "Tahoma",
						"Times New Roman", "Trebuchet MS", "Verdana" }));
		fontComboBox.setToolTipText("Font");
		fontComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fontComboBoxActionPerformed(evt);
			}
		});

		fontSizeComboBox.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "8", "9", "10", "11", "12", "14", "16", "18",
						"20", "22", "24", "26", "28", "36", "48", "72" }));
		fontSizeComboBox.setToolTipText("Font Size");
		fontSizeComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fontSizeComboBoxActionPerformed(evt);
			}
		});

		colorPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		textColorButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/metaphaseeditor/icons/textcolor.png"))); // NOI18N
		textColorButton.setToolTipText("Text Color");
		textColorButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				textColorButtonActionPerformed(evt);
			}
		});

		backgroundColorButton
		.setIcon(new javax.swing.ImageIcon(getClass().getResource(
		"/com/metaphaseeditor/icons/backgroundcolor.png"))); // NOI18N
		backgroundColorButton.setToolTipText("Background Color");
		backgroundColorButton
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				backgroundColorButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout colorPanelLayout = new javax.swing.GroupLayout(
				colorPanel);
		colorPanel.setLayout(colorPanelLayout);
		colorPanelLayout
		.setHorizontalGroup(colorPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								colorPanelLayout
								.createSequentialGroup()
								.addComponent(
										textColorButton,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										25,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(
														backgroundColorButton,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														25,
														javax.swing.GroupLayout.PREFERRED_SIZE)));
		colorPanelLayout.setVerticalGroup(colorPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(textColorButton,
						javax.swing.GroupLayout.PREFERRED_SIZE, 25,
						javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(backgroundColorButton,
								javax.swing.GroupLayout.PREFERRED_SIZE, 25,
								javax.swing.GroupLayout.PREFERRED_SIZE));

		javax.swing.GroupLayout toolbarPanelLayout = new javax.swing.GroupLayout(
				toolbarPanel);
		toolbarPanel.setLayout(toolbarPanelLayout);
		toolbarPanelLayout
		.setHorizontalGroup(toolbarPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								toolbarPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										toolbarPanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														toolbarPanelLayout
														.createSequentialGroup()
														.addGroup(
																toolbarPanelLayout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(
																				blockPanel,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				125,
																				Short.MAX_VALUE)
																				.addComponent(
																						pagePanel,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addGroup(
																										toolbarPanelLayout
																										.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING,
																												false)
																												.addComponent(
																														justificationPanel,
																														javax.swing.GroupLayout.DEFAULT_SIZE,
																														125,
																														Short.MAX_VALUE)
																														.addComponent(
																																editPanel,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																Short.MAX_VALUE))
																																.addPreferredGap(
																																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																		.addComponent(
																																				toolsPanel,
																																				javax.swing.GroupLayout.PREFERRED_SIZE,
																																				javax.swing.GroupLayout.DEFAULT_SIZE,
																																				javax.swing.GroupLayout.PREFERRED_SIZE)
																																				.addPreferredGap(
																																						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																						.addComponent(
																																								undoRedoPanel,
																																								javax.swing.GroupLayout.PREFERRED_SIZE,
																																								javax.swing.GroupLayout.DEFAULT_SIZE,
																																								javax.swing.GroupLayout.PREFERRED_SIZE)
																																								.addPreferredGap(
																																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																										.addComponent(
																																												searchPanel,
																																												javax.swing.GroupLayout.PREFERRED_SIZE,
																																												javax.swing.GroupLayout.DEFAULT_SIZE,
																																												javax.swing.GroupLayout.PREFERRED_SIZE)
																																												.addGap(7,
																																														7,
																																														7)
																																														.addComponent(
																																																jPanel7,
																																																javax.swing.GroupLayout.PREFERRED_SIZE,
																																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																																javax.swing.GroupLayout.PREFERRED_SIZE)
																																																.addGap(410,
																																																		410,
																																																		410))
																																																		.addGroup(
																																																				toolbarPanelLayout
																																																				.createSequentialGroup()
																																																				.addComponent(
																																																						fontComboBox,
																																																						javax.swing.GroupLayout.PREFERRED_SIZE,
																																																						157,
																																																						javax.swing.GroupLayout.PREFERRED_SIZE)
																																																						.addPreferredGap(
																																																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																								.addComponent(
																																																										fontSizeComboBox,
																																																										javax.swing.GroupLayout.PREFERRED_SIZE,
																																																										115,
																																																										javax.swing.GroupLayout.PREFERRED_SIZE)
																																																										.addGap(10,
																																																												10,
																																																												10)
																																																												.addComponent(
																																																														colorPanel,
																																																														javax.swing.GroupLayout.PREFERRED_SIZE,
																																																														javax.swing.GroupLayout.DEFAULT_SIZE,
																																																														javax.swing.GroupLayout.PREFERRED_SIZE)
																																																														.addContainerGap(
																																																																335,
																																																																Short.MAX_VALUE)))));
		toolbarPanelLayout
		.setVerticalGroup(toolbarPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								toolbarPanelLayout
								.createSequentialGroup()
								.addGroup(
										toolbarPanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addComponent(
														jPanel7,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
														.addComponent(
																searchPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
																.addComponent(
																		undoRedoPanel,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																		.addComponent(
																				toolsPanel,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																				.addComponent(
																						editPanel, 0,
																						29,
																						Short.MAX_VALUE)
																						.addComponent(
																								pagePanel,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										Short.MAX_VALUE)
																										.addGroup(
																												toolbarPanelLayout
																												.createParallelGroup(
																														javax.swing.GroupLayout.Alignment.LEADING)
																														.addComponent(
																																justificationPanel,
																																0, 29,
																																Short.MAX_VALUE)
																																.addComponent(
																																		blockPanel,
																																		javax.swing.GroupLayout.Alignment.TRAILING,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		javax.swing.GroupLayout.DEFAULT_SIZE,
																																		javax.swing.GroupLayout.PREFERRED_SIZE))
																																		.addPreferredGap(
																																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																																				.addGroup(
																																						toolbarPanelLayout
																																						.createParallelGroup(
																																								javax.swing.GroupLayout.Alignment.LEADING)
																																								.addGroup(
																																										toolbarPanelLayout
																																										.createParallelGroup(
																																												javax.swing.GroupLayout.Alignment.BASELINE)
																																												.addComponent(
																																														fontComboBox,
																																														javax.swing.GroupLayout.PREFERRED_SIZE,
																																														27,
																																														javax.swing.GroupLayout.PREFERRED_SIZE)
																																														.addComponent(
																																																fontSizeComboBox,
																																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																																29,
																																																Short.MAX_VALUE))
																																																.addComponent(
																																																		colorPanel,
																																																		javax.swing.GroupLayout.DEFAULT_SIZE,
																																																		javax.swing.GroupLayout.DEFAULT_SIZE,
																																																		Short.MAX_VALUE))
																																																		.addGap(11, 11, 11)));

		htmlTextPane.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				htmlTextPaneMouseClicked(evt);
			}
		});
		htmlTextPane.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				htmlTextPaneKeyPressed(evt);
			}

			public void keyReleased(java.awt.event.KeyEvent evt) {
				htmlTextPaneKeyReleased(evt);
			}

			public void keyTyped(java.awt.event.KeyEvent evt) {
				htmlTextPaneKeyTyped(evt);
			}
		});
		mainScrollPane.setViewportView(htmlTextPane);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(toolbarPanel,
						javax.swing.GroupLayout.Alignment.TRAILING,
						javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(mainScrollPane,
								javax.swing.GroupLayout.DEFAULT_SIZE, 697,
								Short.MAX_VALUE));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
						.addComponent(toolbarPanel,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(mainScrollPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												267, Short.MAX_VALUE)));
	}// </editor-fold>
	//GEN-END:initComponents

	private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
		try {
			htmlTextPane.print();
		} catch (PrinterException e) {
			throw new MetaphaseEditorException(e.getMessage(), e);
		}
	}//GEN-LAST:event_printButtonActionPerformed

	private void htmlTextPaneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_htmlTextPaneKeyPressed
	}//GEN-LAST:event_htmlTextPaneKeyPressed

	private void htmlTextPaneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_htmlTextPaneKeyReleased

	}//GEN-LAST:event_htmlTextPaneKeyReleased

	private void htmlTextPaneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_htmlTextPaneKeyTyped
	}//GEN-LAST:event_htmlTextPaneKeyTyped

	public JTextPane getHtmlTextPane() {
		return htmlTextPane;
	}

	public void setEditorToolTipText(String string) {
		htmlTextPane.setToolTipText(string);
	}

	public void addEditorMouseMotionListener(
			EditorMouseMotionListener editorMouseMotionListener) {
		editorMouseMotionListeners.add(editorMouseMotionListener);
	}

	public void removeEditorMouseMotionListener(
			EditorMouseMotionListener editorMouseMotionListener) {
		editorMouseMotionListeners.remove(editorMouseMotionListener);
	}

	public File getFile() {
		return currentFile;
	}

	public String getTitle() {
		return title;
	}

	public String getDocument() throws Exception {
		return htmlTextPane.getStyledDocument().getText(0,
				htmlTextPane.getStyledDocument().getLength());
	}

	public void setDocument(String value) {
		try {
			StringReader reader = new StringReader(value);
			Document oldDoc = htmlTextPane.getDocument();
			if (oldDoc != null)
				oldDoc.removeUndoableEditListener(undoHandler);
			htmlDocument = (DefaultStyledDocument) editorKit
			.createDefaultDocument();
			editorKit.read(reader, htmlDocument, 0);
			htmlDocument.addUndoableEditListener(undoHandler);
			htmlTextPane.setDocument(htmlDocument);
			resetUndoManager();
		} catch (BadLocationException e) {
			throw new MetaphaseEditorException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MetaphaseEditorException(e.getMessage(), e);
		}
	}

	public JPopupMenu getContextMenu() {
		return contextMenu;
	}

	public void refreshAfterAction() {
		int pos = htmlTextPane.getCaretPosition();
		htmlTextPane.setText(htmlTextPane.getText());
		htmlTextPane.validate();
		try {
			htmlTextPane.setCaretPosition(pos);
		} catch (IllegalArgumentException e) {
			// swallow the exception
			// seems like a bug in the JTextPane component
			// only happens occasionally when pasting text at the end of a document
			System.err.println(e.getMessage());
		}
	}

	private void setToolbarFocusActionListener(JComponent component) {
		Component[] vComponents = component.getComponents();
		for (int i = 0; i < vComponents.length; i++) {
			if (vComponents[i] instanceof JButton) {
				JButton button = (JButton) vComponents[i];
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						htmlTextPane.requestFocus();
					}
				});
			} else if (vComponents[i] instanceof JComboBox) {
				JComboBox comboBox = (JComboBox) vComponents[i];
				comboBox.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent ae) {
						htmlTextPane.requestFocus();
					}
				});
			} else if (vComponents[i] instanceof JPanel) {
				JPanel panel = (JPanel) vComponents[i];
				setToolbarFocusActionListener(panel);
			}
		}
	}

	private void textColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textColorButtonActionPerformed
		Color color = JColorChooser.showDialog(null, "Text Color", null);
		if (color != null) {
			new StyledEditorKit.ForegroundAction("Color", color)
			.actionPerformed(evt);
		}
	}//GEN-LAST:event_textColorButtonActionPerformed

	private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
		htmlTextPane.selectAll();
	}//GEN-LAST:event_aboutButtonActionPerformed

	private void backgroundColorButtonActionPerformed(
			java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backgroundColorButtonActionPerformed
		Color color = JColorChooser.showDialog(null, "Text Color", null);
		if (color != null) {
			new BackgroundColorAction(color).actionPerformed(evt);
		}
	}//GEN-LAST:event_backgroundColorButtonActionPerformed

	private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
		try {
			File current = new File(".");
			JFileChooser chooser = new JFileChooser(current);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			try {
				FileReader f = new FileReader("properties.properties");
				Properties pr = new Properties();
				pr.load(f);
				chooser.setCurrentDirectory(new File (pr.getProperty("path")));
				f.close();
			}
			catch (Exception e) {
			}
			chooser.setFileFilter(new HTMLFileFilter());
			for (;;) {
				int approval = chooser.showSaveDialog(this);
				if (approval == JFileChooser.APPROVE_OPTION) {
					File newFile = chooser.getSelectedFile();
					Properties p = new Properties();
					p.put("path", newFile.getParent());
					FileWriter fp = new FileWriter("properties.properties");
					p.store(fp, "");
					fp.close();
					title = newFile.getAbsolutePath();
					if (newFile.exists()) {
						String message = newFile.getAbsolutePath()
						+ " already exists. \n"
						+ "Do you want to replace it?";
						int option = JOptionPane.showConfirmDialog(this,
								message, "Save",
								JOptionPane.YES_NO_CANCEL_OPTION);
						if (option == JOptionPane.YES_OPTION) {
							File currentFile = newFile;
							FileWriter fw = new FileWriter(currentFile);
							fw.write(htmlTextPane.getText());
							fw.close();
							break;
						} else if (option == JOptionPane.NO_OPTION) {
							continue;
						} else if (option == JOptionPane.CANCEL_OPTION) {
							break;
						}
					} else {
						File currentFile = new File(newFile.getAbsolutePath());
						FileWriter fw = new FileWriter(currentFile);
						fw.write(htmlTextPane.getText());
						fw.close();
						break;
					}
				} else {
					break;
				}
			}

			JFrame j = (JFrame)(SwingUtilities.getRoot(this));
			j.setTitle("SPOCC - " + title);
		} catch (FileNotFoundException e) {
			throw new MetaphaseEditorException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MetaphaseEditorException(e.getMessage(), e);
		}
	}//GEN-LAST:event_saveButtonActionPerformed

	private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
		if (JOptionPane
				.showConfirmDialog(
						this,
						"Are you sure you want to erase all the current content and start a new document?",
						"New", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			startNewDocument();

			if (htmlSourceMode) {
				htmlTextArea.setText(htmlTextPane.getText());
			}
		}
	}//GEN-LAST:event_newButtonActionPerformed

	private void fontComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontComboBoxActionPerformed
		FontItem fontItem = (FontItem) fontComboBox.getSelectedItem();
		if (fontItem != null && fontItem.getFontName() != null) {
			new StyledEditorKit.FontFamilyAction(fontItem.getText(),
					fontItem.getFontName()).actionPerformed(evt);
		}
		if (fontComboBox.getItemCount() > 0) {
			fontComboBox.setSelectedIndex(0);
		}
	}//GEN-LAST:event_fontComboBoxActionPerformed

	private void fontSizeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontSizeComboBoxActionPerformed
		FontSize fontSize = (FontSize) fontSizeComboBox.getSelectedItem();
		if (fontSize != null && fontSize.getSize() != -1) {
			new StyledEditorKit.FontSizeAction(fontSize.getText(),
					fontSize.getSize()).actionPerformed(evt);
		}
		if (fontSizeComboBox.getItemCount() > 0) {
			fontSizeComboBox.setSelectedIndex(0);
		}
	}//GEN-LAST:event_fontSizeComboBoxActionPerformed

	private void previewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewButtonActionPerformed
		try {
			if (htmlSourceMode) {
				htmlTextPane.setText(htmlTextArea.getText());
			}
			File tempFile = File.createTempFile("metaphaseeditorpreview",
			".html");
			tempFile.deleteOnExit();
			FileWriter fw = new FileWriter(tempFile);
			fw.write(htmlTextPane.getText());
			fw.close();

			Desktop.getDesktop().browse(tempFile.toURI());
		} catch (IOException e) {
			throw new MetaphaseEditorException(e.getMessage(), e);
		}
	}//GEN-LAST:event_previewButtonActionPerformed

	private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
		try {
			File current = new File(".");
			JFileChooser chooser = new JFileChooser(current);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			try {
				FileReader f = new FileReader("properties.properties");
				Properties pr = new Properties();
				pr.load(f);
				chooser.setCurrentDirectory(new File (pr.getProperty("path")));
				f.close();
			}
			catch (Exception e) {
			}
			chooser.setFileFilter(new HTMLFileFilter());
			int approval = chooser.showOpenDialog(this);
			if (approval == JFileChooser.APPROVE_OPTION) {
				currentFile = chooser.getSelectedFile();
				Properties p = new Properties();
				p.put("path", currentFile.getParent());
				FileWriter fw = new FileWriter("properties.properties");
				p.store(fw, "");
				fw.close();
				title = currentFile.getAbsolutePath();
				if (currentFile.exists()) {
					FileReader fr = new FileReader(currentFile);
					Document oldDoc = htmlTextPane.getDocument();
					if (oldDoc != null)
						oldDoc.removeUndoableEditListener(undoHandler);
					htmlDocument = (DefaultStyledDocument) editorKit
					.createDefaultDocument();
					editorKit.read(fr, htmlDocument, 0);
					htmlDocument.addUndoableEditListener(undoHandler);
					htmlTextPane.setDocument(htmlDocument);
					resetUndoManager();
					JFrame j = (JFrame)(SwingUtilities.getRoot(this));
					j.setTitle("SPOCC - " + title);
				} else {
					JOptionPane.showMessageDialog(null,
							"The selected file does not exist.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (BadLocationException e) {
			throw new MetaphaseEditorException(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			throw new MetaphaseEditorException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MetaphaseEditorException(e.getMessage(), e);
		}
	}//GEN-LAST:event_openButtonActionPerformed

	private void pasteAsTextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteAsTextButtonActionPerformed
		Clipboard clipboard = getToolkit().getSystemClipboard();
		Transferable transferable = clipboard.getContents(null);
		if (transferable != null) {
			try {
				String plainText = (String) transferable
				.getTransferData(DataFlavor.stringFlavor);
				plainText = plainText.replaceAll("\\r\\n", "<br/>");
				plainText = plainText.replaceAll("\\n", "<br/>");
				plainText = plainText.replaceAll("\\r", "<br/>");
				new InsertHtmlAction(this, "Paste as Text", "<p>" + plainText
						+ "</p>", Tag.P).actionPerformed(null);
			} catch (UnsupportedFlavorException e) {
				throw new MetaphaseEditorException(e.getMessage(), e);
			} catch (IOException e) {
				throw new MetaphaseEditorException(e.getMessage(), e);
			}
		}
	}//GEN-LAST:event_pasteAsTextButtonActionPerformed

	private void htmlTextPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_htmlTextPaneMouseClicked
		if (evt.getButton() == MouseEvent.BUTTON3) {
			for (int i = 0; i < contextMenuListeners.size(); i++) {
				contextMenuListeners.get(i).beforeContextMenuPopup();
			}
			contextMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}//GEN-LAST:event_anchorButtonActionPerformed

	private void spellcheckButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spellcheckButtonActionPerformed
		//JOptionPane.showMessageDialog(null, "The spelling checker functionality is currently unavailable.");        
		Thread thread = new Thread() {
			public void run() {
				try {
					spellChecker.spellCheck(htmlTextPane);
					JOptionPane.showMessageDialog(null,
							"The spelling check is complete.",
							"Check Spelling", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					throw new MetaphaseEditorException(ex.getMessage(), ex);
				}
			}
		};
		thread.start();
	}//GEN-LAST:event_spellcheckButtonActionPerformed

	public void addContextMenuListener(ContextMenuListener contextMenuListener) {
		contextMenuListeners.add(contextMenuListener);
	}

	public void removeContextMenuListener(
			ContextMenuListener contextMenuListener) {
		contextMenuListeners.remove(contextMenuListener);
	}

	public void initSpellChecker() {
		try {
			ZipInputStream zipInputStream = null;
			InputStream inputStream = null;
			if (spellCheckDictionaryVersion == SpellCheckDictionaryVersion.CUSTOM) {
				if (customDictionaryFilename == null) {
					throw new MetaphaseEditorException(
					"The dictionary version has been set to CUSTOM but no custom dictionary file name has been specified.");
				}
				inputStream = new FileInputStream(customDictionaryFilename);
			} else {
				inputStream = this.getClass().getResourceAsStream(
						spellCheckDictionaryVersion.getFilename());
			}
			zipInputStream = new ZipInputStream(inputStream);
			zipInputStream.getNextEntry();
			dictionary = new SpellDictionaryHashMap(new BufferedReader(
					new InputStreamReader(zipInputStream)));
			spellChecker = new JTextComponentSpellChecker(dictionary, null,
			"Check Spelling");
		} catch (FileNotFoundException e) {
			throw new MetaphaseEditorException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MetaphaseEditorException(e.getMessage(), e);
		}
	}

	public void setCustomDictionaryFilename(String customDictionaryFilename) {
		this.customDictionaryFilename = customDictionaryFilename;
	}

	public String getCustomDictionaryFilename() {
		return customDictionaryFilename;
	}

	public void setDictionaryVersion(
			SpellCheckDictionaryVersion spellCheckDictionaryVersion) {
		this.spellCheckDictionaryVersion = spellCheckDictionaryVersion;

		initSpellChecker();
	}

	public SpellCheckDictionaryVersion getDictionaryVersion() {
		return spellCheckDictionaryVersion;
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JButton backgroundColorButton;
	private javax.swing.JButton blockJustifyButton;
	private javax.swing.JPanel blockPanel;
	private javax.swing.JButton centerJustifyButton;
	private javax.swing.JButton clearFormattingButton;
	private javax.swing.JPanel colorPanel;
	private javax.swing.JButton copyButton;
	private javax.swing.JButton cutButton;
	private javax.swing.JButton decreaseIndentButton;
	private javax.swing.JPanel editPanel;
	private javax.swing.JButton findButton;
	private javax.swing.JComboBox fontComboBox;
	private javax.swing.JComboBox fontSizeComboBox;
	private javax.swing.text.DefaultStyledDocument htmlDocument;
	private javax.swing.JTextPane htmlTextPane;
	private javax.swing.JButton increaseIndentButton;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JPanel justificationPanel;
	private javax.swing.JButton leftJustifyButton;
	private javax.swing.JScrollPane mainScrollPane;
	private javax.swing.JButton newButton;
	private javax.swing.JButton openButton;
	private javax.swing.JPanel pagePanel;
	private javax.swing.JButton pasteAsTextButton;
	private javax.swing.JButton pasteButton;
	private javax.swing.JButton previewButton;
	private javax.swing.JButton printButton;
	private javax.swing.JButton redoButton;
	private javax.swing.JButton replaceButton;
	private javax.swing.JButton rightJustifyButton;
	private javax.swing.JButton saveButton;
	private javax.swing.JPanel searchPanel;
	private javax.swing.JButton selectAllButton;
	private javax.swing.JButton spellcheckButton;
	private javax.swing.JButton textColorButton;
	private javax.swing.JPanel toolbarPanel;
	private javax.swing.JPanel toolsPanel;
	private javax.swing.JButton undoButton;
	private javax.swing.JPanel undoRedoPanel;

	// End of variables declaration//GEN-END:variables

	class BackgroundColorAction extends StyledEditorKit.StyledTextAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Color color;

		public BackgroundColorAction(Color color) {
			super(StyleConstants.StrikeThrough.toString());
			this.color = color;
		}

		public void actionPerformed(ActionEvent ae) {
			JEditorPane editor = getEditor(ae);
			if (editor != null) {
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setBackground(sas, color);
				setCharacterAttributes(editor, sas, false);
			}
		}
	}

	class UndoHandler implements UndoableEditListener {
		/**
		 * Messaged when the Document has created an edit, the edit is
		 * added to <code>undo</code>, an instance of UndoManager.
		 */
		public void undoableEditHappened(UndoableEditEvent e) {
			undo.addEdit(e.getEdit());
			undoAction.update();
			redoAction.update();
		}
	}

	class UndoAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent actionEvent) {
			try {
				undo.undo();
			} catch (CannotUndoException e) {
				throw new MetaphaseEditorException(e.getMessage(), e);
			}
			update();
			redoAction.update();
		}

		protected void update() {
			if (undo.canUndo()) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
	}

	class RedoAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RedoAction() {
			super("Redo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent actionEvent) {
			try {
				undo.redo();
			} catch (CannotRedoException e) {
				throw new MetaphaseEditorException(e.getMessage(), e);
			}
			update();
			undoAction.update();
		}

		protected void update() {
			if (undo.canRedo()) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
	}

	class HTMLFileFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File f) {
			return ((f.isDirectory()) || (f.getName().toLowerCase()
					.indexOf(".c") > 0));
		}

		public String getDescription() {
			return "C source code";
		}
	}

	class ParagraphFormatListCellRenderer extends DefaultListCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Component component = super.getListCellRendererComponent(list,
					value, index, isSelected, cellHasFocus);
			if (index == 0) {
				component.setEnabled(false);
			}
			return component;
		}
	}

	class FontListCellRenderer extends DefaultListCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Component component = super.getListCellRendererComponent(list,
					value, index, isSelected, cellHasFocus);
			if (index == 0) {
				component.setEnabled(false);
			}
			FontItem fontItem = (FontItem) value;
			if (fontItem.getFontName() != null) {
				Font currentFont = component.getFont();
				component.setFont(new Font(fontItem.getFontName(), currentFont
						.getStyle(), currentFont.getSize()));
			}
			return component;
		}
	}

	class FontSizeListCellRenderer extends DefaultListCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Component component = super.getListCellRendererComponent(list,
					value, index, isSelected, cellHasFocus);
			if (index == 0) {
				component.setEnabled(false);
			}
			FontSize fontSize = (FontSize) value;
			if (fontSize.getSize() != -1) {
				Font currentFont = component.getFont();
				component.setFont(new Font(currentFont.getName(), currentFont
						.getStyle(), fontSize.getSize()));
			}
			return component;
		}
	}

	class DefaultEditorMouseMotionListener implements MouseMotionListener {

		public void mouseDragged(MouseEvent me) {
			int pos = htmlTextPane.viewToModel(me.getPoint());

			if (pos >= 0) {
				Element element = htmlDocument.getParagraphElement(pos);
				MutableAttributeSet attributes = new SimpleAttributeSet(
						element.getAttributes());

				EditorMouseEvent editorMouseEvent = new EditorMouseEvent();
				editorMouseEvent.setNearestParagraphAttributes(attributes);
				for (int i = 0; i < editorMouseMotionListeners.size(); i++) {
					editorMouseMotionListeners.get(i).mouseDragged(
							editorMouseEvent);
				}
			}
		}

		public void mouseMoved(MouseEvent me) {
			int pos = htmlTextPane.viewToModel(me.getPoint());

			if (pos >= 0) {
				Element element = htmlDocument.getParagraphElement(pos);
				MutableAttributeSet attributes = new SimpleAttributeSet(
						element.getAttributes());

				EditorMouseEvent editorMouseEvent = new EditorMouseEvent();
				editorMouseEvent.setNearestParagraphAttributes(attributes);
				for (int i = 0; i < editorMouseMotionListeners.size(); i++) {
					editorMouseMotionListeners.get(i).mouseMoved(
							editorMouseEvent);
				}
			}
		}
	}
}
