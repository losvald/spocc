/*
 * SymbolTableWriter.java
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
package hr.fer.spocc;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import hr.fer.spocc.util.AbstractEntityWriter;

/**
 * Ispisuje tablicu znakova u sljedecem formatu:
 * <pre>
 * 0 DESNA_ZAGRADA )
 * 1 LIJEVA_ZAGRADA (
 * 2 OP_MINUS -
 * 3 OPERAND 076
 * 4 OPERAND 0x12
 * 5 OPERAND 3 
 * 6 UMINUS -
 * </pre>
 * Redoslijed je odredjen metodom {@link SymbolTableRow#compareTo(SymbolTableRow)},
 * pa tako i indeksi.
 * 
 * @author Leo Osvald
 *
 */
public class SymbolTableWriter extends AbstractEntityWriter<SymbolTable> {

	private static final SymbolTableWriter INSTANCE = new SymbolTableWriter();
	
	@Override
	public boolean print(SymbolTable entity, PrintStream printStream) {
		Collection<SymbolTableRow> c = entity.getRows();
		SymbolTableRow[] rows = new SymbolTableRow[c.size()];
		int ind = 0;
		for (SymbolTableRow row : c) {
			rows[ind++] = row;
		}
		Arrays.sort(rows);
		ind = 0;
		for (SymbolTableRow row : rows) {
			printStream.printf("%d %s %s\n", ind++, 
					row.getTokenType().toString(),
					row.getLexeme());
		}
		return true;
	}

	public static SymbolTableWriter getInstance() {
		return INSTANCE;
	}

}
