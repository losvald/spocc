/*
 * AbstractParsingTableFactory.java
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
import hr.fer.spocc.grammar.cfg.CfgProductionRule;
import hr.fer.spocc.parser.MoveFactory;
import hr.fer.spocc.parser.Parser;
import hr.fer.spocc.parser.ParserSymbols;
import hr.fer.spocc.parser.ParsingTableFactory;

/**
 * @author Leo Osvald
 *
 */
public abstract class AbstractParsingTableFactory 
implements ParsingTableFactory {

	protected static final Symbol<TokenType> EOF
	= ParserSymbols.eof();
	
	protected static final Symbol<TokenType> EPS
	= Symbols.epsilonSymbol();
	
	protected static CfgProductionRule<TokenType> r(
			Variable<TokenType> leftSide, Symbol<TokenType>... rightSide) {
		return new CfgProductionRule<TokenType>(leftSide, rightSide);
	}
	
	protected static Variable<TokenType> v(String name) {
		return new Variable<TokenType>(name);
	}
	
	protected static Terminal<TokenType> t(TokenType value) {
		return new Terminal<TokenType>(value);
	}
	
	protected static void reduce(Parser p, CfgProductionRule<TokenType> rule) {
		MoveFactory.createReduceMove(rule).perform(p);
	}
	
	protected static void shift(Parser p) {
		MoveFactory.createShiftMove().perform(p);
	}
	
	protected static void push(Parser p, int stateId) {
		MoveFactory.createPushMove(stateId).perform(p);
	}
	
	protected static void accept(Parser p){
		
		MoveFactory.acceptMove().perform(p);
	}
	
}
