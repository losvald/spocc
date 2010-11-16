/*
 * AbstractLexer.java
 *
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
package hr.fer.spocc.lexer;

import hr.fer.spocc.SymbolTable;
import hr.fer.spocc.TokenType;
import hr.fer.spocc.util.FileContentGrabber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.sglj.util.Pair;


/**
 * Apstraktna implementacija sucelja {@link Lexer}.
 * 
 * @author Leo Osvald
 *
 */
public abstract class AbstractLexer implements Lexer {
	
	private final LexerDescriptor lexerDescriptor;
	
	private final FileContentGrabber sourceReader;
	
	private final List<LexicalErrorListener> listeners
	= new ArrayList<LexicalErrorListener>();
	
	protected int from;
	protected int to;
	protected int lineNumber;
	
	/**
	 * Izgradjuje leksicki analizator na temelju opisa. Opis obicno generira
	 * neki generator leksickog analizatora.
	 * 
	 * @param descriptor
	 */
	public AbstractLexer(LexerDescriptor descriptor) {
		this.lexerDescriptor = descriptor;
		this.sourceReader = createSourceReader();
	}
	
	@Override
	public void skip() {
		tokenize(null);
	}
	
	@Override
	public void tokenize(TokenType tokenType) {
		tokenizeFirst(tokenType, currentTokenSize());
	}
	
	@Override
	public int currentTokenSize() {
		return to - from;
	}
	
	@Override
	public LexerDescriptor getLexerDescriptor() {
		return lexerDescriptor;
	}
	
	@Override
	public Pair<SymbolTable, TokenList> tokenizeAll(File sourceFile) {
		Validate.isTrue(sourceFile.exists(),
				"Source file \"" + sourceFile + "\" does not exist");
		Validate.isTrue(sourceFile.canRead(),
				"Source file \"" + sourceFile + "\" must be readable");
		
		lineNumber = 1;
		try {
			getSourceReader().open(sourceFile);
			return tokenizeAll();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		} finally {
			try {
				getSourceReader().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
//		BufferedInputStream bis = null;
//		try {
//			bis = new BufferedInputStream(new FileInputStream(sourceFile));
//			return tokenizeAll(bis);
//		} finally {
//			if (bis != null) {
//				bis.close();
//			}
//		}
	}
	
	@Override
	public void newLine() {
		++lineNumber;
	}
	
	@Override
	public void addLexicalErrorListener(LexicalErrorListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeLexicalErrorListener(LexicalErrorListener listener) {
		listeners.remove(listener);
	}
	
	protected abstract Pair<SymbolTable, TokenList> tokenizeAll() 
	throws IOException;
	
	protected abstract FileContentGrabber createSourceReader();
	
	protected FileContentGrabber getSourceReader() {
		return this.sourceReader;
	}
	
	/**
	 * Cita znakove u intervalu [from, to) te vraca taj podstring.
	 * 
	 * @param from indeks od (ukljucivo)
	 * @param to indeks do (iskljucivo)
	 * @return podstring [from, to), ili kraci ako je detektiran zadnji znak
	 */
	protected String formToken(int from, int to) {
		StringBuffer sb = new StringBuffer(to - from);
		for (int i = from; i < to; ++i) {
			Character c = getSourceReader().getCharAt(i);
			if (c == null)
				break;
			sb.append(c);
		}
		return sb.toString();
	}

	protected void fireLexicalError(LexicalError error) {
		for (LexicalErrorListener l : listeners)
			l.onLexicalError(error);
	}
	
}
