/*
 * LR1AutomatonTest.java
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;

/**
 * @author Leo Osvald
 *
 */
@Ignore
public class LR1AutomatonTest {

	Terminal<String> terminal(char c) {
		return new Terminal<String>(String.valueOf(c));
	}

	Variable<String> variable(char c) {
		return new Variable<String>(String.valueOf(c));
	}

	Symbol<String> charToSymbol(char c) {
		if (Character.isUpperCase(c)) {
			return variable(c);
		} else {
			return terminal(c);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	Set<Symbol<String>> createFollowingSet(Character... chars) {
		if (chars == null)
			chars = new Character[]{null};
		Set<Symbol<String>> ret = new HashSet<Symbol<String>>();
		for (Character c : chars) {
			if (c == null)
				ret.add((Symbol)Symbols.eofSymbol());
			else
				ret.add(charToSymbol(c));
		}
		return ret;
	}

	LR1Item<String> item(Variable<String> leftSideSymbol,
			List<Symbol<String>> rightSideSymbols,
			int dotIndex, Set<Symbol<String>> followingTerminals) {
		return new LR1Item<String>(leftSideSymbol, rightSideSymbols, dotIndex, 
				followingTerminals);
	}

	List<Symbol<String>> toRightSide(char... chars) {
		List<Symbol<String>> ret = new ArrayList<Symbol<String>>();
		for (char c : chars) {
			ret.add(charToSymbol(c));
		}
		return ret;
	}
}
