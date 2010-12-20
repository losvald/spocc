/*
 * GrammarIOUtils.java
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
package hr.fer.spocc.parser.gen;

import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.Symbols;
import hr.fer.spocc.grammar.Terminal;
import hr.fer.spocc.grammar.Variable;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

/**
 * @author Leo Osvald
 *
 */
public class GrammarIOUtils {
	private GrammarIOUtils() {
	}
	
	public static CfgProductionRule<String> toRule(String bnfRule) {
		String[] sides = bnfRule.split("=>");
		Validate.isTrue(sides.length == 2);
		String[] rightSideSymbolStr = sides[1].split("\\s+");
		ArrayList<Symbol<String>> symbols = new ArrayList<Symbol<String>>();
		for (int i = 0; i < rightSideSymbolStr.length; ++i) {
			if (!rightSideSymbolStr[i].trim().isEmpty())
				symbols.add(toSymbol(rightSideSymbolStr[i]));
		}
		return new CfgProductionRule<String>(
				toVariable(sides[0].trim()),
				symbols);
	}
	
	public static Variable<String> toVariable(String bnfVariable) {
		Validate.notNull(bnfVariable);
		Validate.isTrue(surroundedByAngleBrackets(bnfVariable));
		return new Variable<String>(trimAngleBrackets(bnfVariable));
	}
	
	public static Terminal<String> toTerminal(String bnfTerminal) {
		Validate.notNull(bnfTerminal);
		return new Terminal<String>(bnfTerminal);
	}
	
	public static Symbol<String> toSymbol(String bnfSymbol) {
		if (bnfSymbol.equals("_")) {
			return (Symbol) Symbols.eofSymbol();
		}
		if (surroundedByAngleBrackets(bnfSymbol))
			return toVariable(bnfSymbol);
		return toTerminal(bnfSymbol);
	}
	
	private static boolean surroundedByAngleBrackets(String s) {
		if (s.isEmpty())
			return false;
		return s.startsWith("<") && s.endsWith(">");
	}
	
	private static String trimAngleBrackets(String s) {
		Validate.isTrue(s.length() >= 2);
		return s.substring(1, s.length() - 1);
	}
}
