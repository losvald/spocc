/*
 * SymbolTableReader.java
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
import hr.fer.spocc.SymbolTable.Key;
import hr.fer.spocc.SymbolTableRow;
import hr.fer.spocc.TokenType;
import hr.fer.spocc.util.AbstractEntityReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.sglj.util.Pair;

/**
 * @author Leo Osvald
 *
 */
public class SymbolTableReader extends AbstractEntityReader<
Pair<SymbolTable, Map<Integer, Key>>> {

	private static final SymbolTableReader INSTANCE = new SymbolTableReader();
	
	private SymbolTableReader() {
	}
	
	@Override
	protected Pair<SymbolTable, Map<Integer, Key>> read(Scanner sc) {
		SymbolTable st = new SymbolTable();
		Map<Integer, Key> map = new HashMap<Integer, SymbolTable.Key>();
		int index = 0;
		while (sc.hasNextInt()) {
			sc.nextInt();
			String tokenName = sc.next();
			TokenType tokenType = LexerBaseTest.getTokenType(tokenName);
			String lexeme = sc.next();
			st.addRow(new SymbolTableRow(tokenType, lexeme));
			map.put(index++, new SymbolTable.Key(tokenType, lexeme));
		}
		return new Pair<SymbolTable, Map<Integer,Key>>(st, map);
	}

	public static SymbolTableReader getInstance() {
		return INSTANCE;
	}

}
