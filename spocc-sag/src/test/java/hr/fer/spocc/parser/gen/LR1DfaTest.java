/*
 * LR1DfaTest.java
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

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;

/**
  * @author Leo Osvald
  *
  */
public abstract class LR1DfaTest extends LR1AutomatonTest {

	LR1Dfa lr1dfa;
	
	@Before
	public void before() {
		// TODO
		loadDfa();
		lr1dfa.reset();
	}
	
	abstract void loadDfa();
	
	void checkContains(String sequence, LR1Item<String>... items) {
		for (int i = 0; i < sequence.length(); ++i) {
			Symbol<String> symbol = charToSymbol(sequence.charAt(i));
			lr1dfa.process(symbol);
		}
		Set<LR1Item<String>> expected = new HashSet<LR1Item<String>>();
		for (LR1Item<String> item : items)
			expected.add(item);
		if (lr1dfa.getCurrentState() != null)
		Assert.assertEquals(expected, lr1dfa.getAssociated(
				lr1dfa.getCurrentState().getId()));
		else if (items.length != 0) Assert.fail();
	}
}
