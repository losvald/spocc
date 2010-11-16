/*
 * CfgProductionRule.java
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
package hr.fer.spocc.grammar.cfg;

import hr.fer.spocc.grammar.ProductionRule;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.SymbolType;
import hr.fer.spocc.grammar.Variable;

import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * Kontekstno neovisna gramatika (Context-Free Grammar).
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public class CfgProductionRule<T> extends ProductionRule<T> {

	public CfgProductionRule(Variable<T> leftSideSymbol,
			Symbol<T>... rightSideSymbols) {
		super(leftSideSymbol, rightSideSymbols);
		Validate.isTrue(getLeftSide().size() == 1,
				"Left side must have exactly one symbol");
		Validate.isTrue(getLeftSideSymbol().getSymbolType()
				== SymbolType.VARIABLE,
				"Left side must be a variable");
	}
	
	public CfgProductionRule(Symbol<T> leftSideSymbol, 
			List<Symbol<T>> rightSideSymbols) {
		super(leftSideSymbol, rightSideSymbols);
	}
	
	@Override
	public int getLeftSideSize() {
		return 1;
	}
	
	public Variable<T> getLeftSideSymbol() {
		return (Variable<T>) getLeftSideSymbol(0);
	}
	
	public boolean isEpsilon() {
		return getRightSide().isEmpty();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getLeftSideSymbol().toString());
		sb.append(" ::= ");
		if (isEpsilon())
			sb.append('$');
		else {
			for (Symbol<T> symbol : getRightSide()) 
				sb.append(symbol);
//			for (int i = 0; i < getRightSideSize(); ++i) {
//				if (i > 0 )
//					sb.append(' ');
//				sb.append(getRightSideSymbol(i).toString());
//			}
		}
		return sb.toString();
	}

}
