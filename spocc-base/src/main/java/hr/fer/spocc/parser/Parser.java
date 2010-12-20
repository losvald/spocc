/*
 * Parser.java
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
import hr.fer.spocc.grammar.cfg.CfgProductionRule;

import java.io.File;

/**
 * @author Leo Osvald
 *
 */
public interface Parser {
	void setSymbolTable(SymbolTable symbolTable);
	SymbolTable getSymbolTable();
	void setTokenizer(Tokenizer tokenizer);
	Tokenizer getTokenizer();
	ParsingTable<TokenType> getParsingTable();
	void setParsingTable(ParsingTable<TokenType> parsingTable);
	void setSourceFile(File sourceFile);
	File getSourceFile();
	void reset();
	ParseTree getParseTree();
	void reduce(CfgProductionRule<TokenType> rule);
	void push(int stateId);
	void shift();
	void accept();
	void error();
	boolean parse();
	void addSyntaxErrorListener(SyntaxErrorListener listener);
	void removeSyntaxErrorListener(SyntaxErrorListener listener);
}
