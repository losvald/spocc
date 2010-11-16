/*
 * SymbolTableRow.java
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

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * Redak tablice leksickih jedinki.
 * Dva retka su jednaka ako su im imena (tipove) leksicke jedinke
 * jednaki i imaju isti leksem (izvorni tekst).
 * 
 * @author Leo Osvald
 *
 */
public class SymbolTableRow implements Comparable<SymbolTableRow> {
	
	private final TokenType tokenType;
	private final String lexeme;
	
	public SymbolTableRow(TokenType tokenType, String lexeme) {
		this.tokenType = tokenType;
		this.lexeme = lexeme;
	}
	
	public TokenType getTokenType() {
		return tokenType;
	}

	public String getLexeme() {
		return lexeme;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lexeme == null) ? 0 : lexeme.hashCode());
		result = prime * result
				+ ((tokenType == null) ? 0 : tokenType.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SymbolTableRow other = (SymbolTableRow) obj;
		if (lexeme == null) {
			if (other.lexeme != null)
				return false;
		} else if (!lexeme.equals(other.lexeme))
			return false;
		if (tokenType == null) {
			if (other.tokenType != null)
				return false;
		} else if (!tokenType.equals(other.tokenType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SymbolTableRow [tokenType=" + tokenType + ", lexeme=" + lexeme
				+ "]\n";
	}

	@Override
	public int compareTo(SymbolTableRow o) {
		return new CompareToBuilder()
		.append(getTokenType(), o.getTokenType())
		.append(getLexeme(), o.getLexeme())
		.toComparison();
	}
	
	
	
}
