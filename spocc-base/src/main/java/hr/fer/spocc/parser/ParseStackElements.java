/*
 * ParseStackElements.java
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
import hr.fer.spocc.grammar.Symbols;
import hr.fer.spocc.grammar.Terminal;
import hr.fer.spocc.grammar.Variable;

/**
 * @author Leo Osvald
 *
 */
class ParseStackElements {
	
	private ParseStackElements() {
	}
	
	static ParseStackElement fromTerminal(Terminal<TokenType> terminal) {
		return new SymbolParseStackElement(terminal);
	}
	
	static ParseStackElement fromVariable(Variable<TokenType> variable) {
		return new SymbolParseStackElement(variable);
	}
	
	static Symbol<TokenType> asSymbol(ParseStackElement element) {
		if (element instanceof SymbolParseStackElement)
			return ((SymbolParseStackElement) element).getSymbol();
		return ParserSymbols.terminal(
				((ParseTreeNode) element).getToken().getTokenType());
	}
	
	static Variable<TokenType> asVariable(ParseStackElement element) {
		return (Variable<TokenType>) 
		((SymbolParseStackElement) element).getSymbol();
	}
	
	static Terminal<TokenType> asTerminal(ParseStackElement element) {
		return (Terminal<TokenType>) 
		((SymbolParseStackElement) element).getSymbol();
	}
	
	static Symbol<TokenType> asEmptyStackSymbol(ParseStackElement element) {
		return Symbols.epsilonSymbol();
	}
	
//	static final ParseStackElement END_OF_TOKENS
//	= new SymbolParseStackElement(SEQUENCE_TERMINATOR_SYMBOL);
	
	static final ParseStackElement EMPTY_STACK_ELEMENT
	= new ParseStackElement() {
		
		@Override
		public ParseStackElementType getType() {
			return ParseStackElementType.EMPTY_STACK;
		}
		
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (obj == null)
				return false;
			return getClass() == obj.getClass();
		}
		
		public String toString() {
			return "(empty stack)";
		}
	};
	
	
	
}
