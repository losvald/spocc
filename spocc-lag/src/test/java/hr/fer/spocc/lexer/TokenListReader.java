/*
 * TokenListReader.java
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
import hr.fer.spocc.SymbolTable.Key;
import hr.fer.spocc.util.AbstractEntityReader;

import java.util.Map;
import java.util.Scanner;

import org.sglj.util.Pair;

/**
 * @author Leo Osvald
 *
 */
public class TokenListReader extends AbstractEntityReader<TokenList> {

	private final SymbolTable symbolTable;
	private final Map<Integer, SymbolTable.Key> orderMap;
	
	// disable instantiation
	TokenListReader(Pair<SymbolTable, Map<Integer, Key>> symbolTable) {
		this.symbolTable = symbolTable.first();
		this.orderMap = symbolTable.second();
	}
	
	@Override
	protected TokenList read(Scanner sc) {
		TokenList tl = new TokenList();
		while (sc.hasNext()) {
			TokenType tokenType = LexerBaseTest.getTokenType(sc.next());
			int lineNumber = sc.nextInt();
			int index = sc.nextInt();
			tl.add(tokenType, lineNumber, 
					symbolTable.getRow(orderMap.get(index)));
		}
		return tl;
	}

}
