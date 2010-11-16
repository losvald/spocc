/*
 * SymbolTable.java
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sglj.util.Pair;

/**
 * Tablica simbola.
 * 
 * @author Leo Osvald
 *
 */
public class SymbolTable {
	
	private final Map<Key, SymbolTableRow> rows 
	= new HashMap<Key, SymbolTableRow>();
	
	public Key addRow(SymbolTableRow row) {
		Key key = createKey(row);
		if (getRow(key) == null)
			rows.put(key, row);
		return key;
	}
	
	public boolean containsRow(SymbolTableRow row) {
		return rows.containsKey(createKey(row));
	}
	
	public SymbolTableRow getRow(Key key) {
		return rows.get(key);
	}
	
	public int getRowCount() {
		return rows.size();
	}

	/**
	 * Vraca kolekciju redova tablice simbola.
	 * 
	 * @return neizmjenjiva kolekcija
	 */
	public Collection<SymbolTableRow> getRows() {
		return Collections.unmodifiableCollection(rows.values());
	}
	
	@Override
	public int hashCode() {
		return 31 + ((rows == null) ? 0 : rows.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SymbolTable))
			return false;
		SymbolTable other = (SymbolTable) obj;
		if (rows == null) {
			if (other.rows != null)
				return false;
		} else if (!rows.equals(other.rows))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return rows.toString();
	}
	
	private Key createKey(SymbolTableRow row) {
		return new Key(row.getTokenType(), row.getLexeme());
	}
	
	
	public static class Key extends Pair<TokenType, String> {

		public Key(TokenType first, String second) {
			super(first, second);
		}
		
	}
	
}
