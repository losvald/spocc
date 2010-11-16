/*
 * Lexer.java
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
import hr.fer.spocc.Tokenizer;

import java.io.File;

import org.sglj.util.Pair;

/**
 * Sucelje koje predstavlja leksicki analizator
 * 
 * @author Leo Osvald
 *
 */
public interface Lexer extends Tokenizer {

	/**
	 * Vraca opisnik ovog leksickog analizatora
	 * 
	 * @return opisnik
	 */
	LexerDescriptor getLexerDescriptor();
	
	/**
	 * Zapocinje proces leksicke analize.
	 * @param sourceFile TODO
	 */
	Pair<SymbolTable, TokenList> tokenizeAll(File sourceFile);
	
	/**
	 * Metoda koja se poziva kad se detektira leksicka jedinka koja
	 * se treba odbaciti tj. preskociti.
	 */
	void skip();
	
	/**
	 * Metoda koja se poziva kad je u izvornoj datoteci detektiran
	 * prijelaz u novi red.
	 */
	void newLine();
	
	/**
	 * Metoda koja se poziva kad treba uci u novo stanje.
	 * 
	 * @param newState
	 */
	void enterState(State newState);
	
	/**
	 * Metoda koja se poziva kad je detektirana nova leksicka jedinka
	 * koju cine prvih <tt>n</tt> znakova.<br>
	 * 
	 * Ova naredba je ekvivalentna LEX-ovoj naredbi <code>yyless(n)</code>.
	 * 
	 * @param tokenType tip/naziv leksicke jedinke koja je prepoznata
	 * @param n broj znakova koji se trebaju grupirati u leksicku jedinku
	 */
	void tokenizeFirst(TokenType tokenType, int n);
	
	/**
	 * Motoda koja se poziva kad je detektirana nova leksicka jedinka
	 * koju cine svi skenirani znakovi.<br>
	 * 
	 * Ova naredba je ekvivalentna LEX-ovoj naredbi <code>yyless(0)</code>.
	 * 
	 * @param tokenType tip/naziv leksicke jedinke koja je prepoznata
	 */
	void tokenize(TokenType tokenType);
	
	/**
	 * Vraca broj znakova koji predstavljaju potencijalnu leksicku jedinku.
	 * 
	 * @return broj znakova 
	 */
	int currentTokenSize();
	
	boolean isError();
	
	SymbolTable getSymbolTable();
	
	void addLexicalErrorListener(LexicalErrorListener listener);
	void removeLexicalErrorListener(LexicalErrorListener listener);
	
}
