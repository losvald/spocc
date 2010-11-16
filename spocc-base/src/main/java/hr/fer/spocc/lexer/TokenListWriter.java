/*
 * TokenListWriter.java
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

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import hr.fer.spocc.SymbolTable;
import hr.fer.spocc.SymbolTableRow;
import hr.fer.spocc.Token;
import hr.fer.spocc.util.AbstractEntityWriter;

/**
 * Ispisivac tablice uniformnih znakova.
 * 
 * @author Leo Osvald
 *
 */
public class TokenListWriter extends AbstractEntityWriter<TokenList> {

	final SymbolTableRow[] symbolTableRows;
	
	public TokenListWriter(SymbolTable symbolTable) {
		Collection<SymbolTableRow> c = symbolTable.getRows();
		symbolTableRows = new SymbolTableRow[c.size()];
		int ind = 0;
		for (SymbolTableRow row : c) {
			symbolTableRows[ind++] = row;
		}
		Arrays.sort(symbolTableRows);
	}
	
	@Override
	public boolean print(TokenList entity, PrintStream printStream) {
		int ind = 0;
		for (Token e : entity.getAll()) {
			printStream.printf("%d %s %d\n", ind++, 
					e.getTokenType().toString(),
					Arrays.binarySearch(symbolTableRows, e.getSymbolTableRow()));
		}
		return true;
	}

}
