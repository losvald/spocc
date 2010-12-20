/*
 * SymbolParseStackElement.java
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

import hr.fer.spocc.TokenType;
import hr.fer.spocc.grammar.Symbol;

/**
 * @author Leo Osvald
 *
 */
public class SymbolParseStackElement implements ParseStackElement {

	private final Symbol<TokenType> symbol;
	
	public SymbolParseStackElement(Symbol<TokenType> symbol) {
		this.symbol = symbol;
	}

	@Override
	public ParseStackElementType getType() {
		switch (symbol.getSymbolType()) {
		case TERMINAL:
			return ParseStackElementType.TERMINAL;
		case VARIABLE:
			return ParseStackElementType.VARIABLE;
		default:
			return ParseStackElementType.EMPTY_STACK;
		}
	}
	
	public Symbol<TokenType> getSymbol() {
		return symbol;
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return symbol.equals(((SymbolParseStackElement) obj).symbol);
	}
	
	@Override
	public String toString() {
		return symbol.toString();
	}

}
