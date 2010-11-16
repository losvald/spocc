/*
 * Token.java
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


/**
 * Redak tablice uniformnih znakova. Redak predstavlja uredjenu trojku
 * (uniformni_znak, redni_broj_linije, referenca_na_tablicu_znakova).<br> 
 * Napomena: Ovdje je uniformni znak poistovjecen s tipom leksicke jedinke
 * ({@link TokenType}).
 * <br>
 * Ova klasa je immutable.
 * 
 * @author Leo Osvald
 *
 */
public final class Token {
	private final TokenType tokenType;
	private final int lineNumber;
	private final SymbolTableRow symbolTableRow;

	public Token(TokenType tokenType, int lineNumber,
			SymbolTableRow symbolTableRow) {
		this.tokenType = tokenType;
		this.lineNumber = lineNumber;
		this.symbolTableRow = symbolTableRow;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public SymbolTableRow getSymbolTableRow() {
		return symbolTableRow;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lineNumber;
		result = prime * result
				+ ((symbolTableRow == null) ? 0 : symbolTableRow.hashCode());
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
		Token other = (Token) obj;
		if (lineNumber != other.lineNumber)
			return false;
		if (symbolTableRow == null) {
			if (other.symbolTableRow != null)
				return false;
		} else if (!symbolTableRow.equals(other.symbolTableRow))
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
		return "TokenListElement [tokenType=" + tokenType + ", lineNumber="
				+ lineNumber + ", symbolTableRow=" + symbolTableRow + "]";
	}
	
	
	
}