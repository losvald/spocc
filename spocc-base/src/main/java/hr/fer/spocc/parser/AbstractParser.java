/*
 * AbstractParser.java
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
package hr.fer.spocc.parser;

import hr.fer.spocc.SymbolTable;
import hr.fer.spocc.TokenType;
import hr.fer.spocc.Tokenizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leo Osvald
 *
 */
abstract class AbstractParser implements Parser {

	private SymbolTable symbolTable;
	private Tokenizer tokenizer;
	private ParsingTable<TokenType> parsingTable;
	
	private File sourceFile;
	
	private final List<SyntaxErrorListener> listeners
	= new ArrayList<SyntaxErrorListener>();
	
	public AbstractParser() {
		this(null, null);
	}
	
	public AbstractParser(Tokenizer tokenizer, 
			ParsingTable<TokenType> parsingTable) {
		setTokenizer(tokenizer);
		setParsingTable(parsingTable);
	}
	
	@Override
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	@Override
	public void setSymbolTable(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	@Override
	public Tokenizer getTokenizer() {
		return tokenizer;
	}

	@Override
	public void setTokenizer(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	@Override
	public ParsingTable<TokenType> getParsingTable() {
		return parsingTable;
	}

	@Override
	public void setParsingTable(ParsingTable<TokenType> parsingTable) {
		this.parsingTable = parsingTable;
	}

	@Override
	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}
	
	@Override
	public File getSourceFile() {
		return sourceFile;
	}

	@Override
	public void reset() {
		getTokenizer().reset();
	}
	
	@Override
	public void addSyntaxErrorListener(SyntaxErrorListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeSyntaxErrorListener(SyntaxErrorListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireSyntaxError(SyntaxError error) {
		for (SyntaxErrorListener l : listeners)
			l.onSyntaxError(error);
	}
	
}
