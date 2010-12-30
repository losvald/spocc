/*
 * CompileWorker.java
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

import java.io.File;
import java.io.FileOutputStream;

import hr.fer.spocc.Compiler;
import hr.fer.spocc.CompilerFactory;
import hr.fer.spocc.ide.views.Editor;

import javax.swing.SwingWorker;

/**
 * @author Hrvoje Novak
 * @author Leo Osvald
 *
 */
public class CompileWorker extends SwingWorker<Boolean, Void> {

	public static final Compiler c = CompilerFactory.getInstance();

	public CompileWorker() {
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		String source = Editor.editorPanel.getDocument();
		File f = File.createTempFile("compile", "ppj");
		FileOutputStream fo = new FileOutputStream(f);
		fo.write(source.getBytes());
		fo.close();
		c.clearCompileFlags(Compiler.PRINT_PARSE_TREE);
		c.clearCompileFlags(Compiler.PRINT_SYMBOL_TABLE);
		c.clearCompileFlags(Compiler.PRINT_TOKEN_LIST);
		c.clearCompileFlags(Compiler.IGNORE_LEXICAL_ERRORS);
		String title = Editor.editorPanel.getTitle();
		if (Editor.symbolTable.isSelected()) {
			c.setCompileFlags(Compiler.PRINT_SYMBOL_TABLE);
			c.setSymbolTableOutputFile(new File(title.substring(0, title.lastIndexOf(System.getProperty("file.separator"))) + "source.st"));
		}
		if (Editor.tokenList.isSelected()) {
			c.setCompileFlags(Compiler.PRINT_TOKEN_LIST);
			c.setTokenListOutputFile(new File(title.substring(0, title.lastIndexOf(System.getProperty("file.separator"))) + "source.tl"));
		}
		if (Editor.parseTree.isSelected()) {
			c.setCompileFlags(Compiler.PRINT_PARSE_TREE);
			c.setParseTreeOutputFile(new File(title.substring(0, title.lastIndexOf(System.getProperty("file.separator"))) + "source.pt"));
		}
		if (Editor.ignoreLexicalErrors.isSelected()) c.setCompileFlags(Compiler.IGNORE_LEXICAL_ERRORS);
		c.compile(f);
		f.deleteOnExit();
		return true;
	}

}
