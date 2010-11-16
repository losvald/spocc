/*
 * TokenList.java
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

import hr.fer.spocc.SymbolTableRow;
import hr.fer.spocc.Token;
import hr.fer.spocc.TokenType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tablica uniformnih znakova. Sastoji se od uredjenih trojki
 * (uniformni_znak, redni_broj_linije, referenca_na_tablicu_znakova).
 * 
 * @author Leo Osvald
 *
 */
public class TokenList {

	private final List<Token> rows 
	= new ArrayList<Token>();
	
	public void add(TokenType tokenType, int lineNumber, 
			SymbolTableRow symbolTableRow) {
		rows.add(new Token(tokenType, lineNumber, symbolTableRow));
	}
	
	public Token get(int index) {
		return rows.get(index);
	}
	
	public List<Token> getAll() {
		return Collections.unmodifiableList(rows);
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
		if (!(obj instanceof TokenList))
			return false;
		TokenList other = (TokenList) obj;
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
	
}
