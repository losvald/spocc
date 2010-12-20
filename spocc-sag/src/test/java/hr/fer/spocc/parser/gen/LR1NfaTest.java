/*
 * LR1NfaTest.java
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

import hr.fer.spocc.automaton.fsm.FiniteStateMachine.Transition;
import hr.fer.spocc.automaton.fsm.State;
import hr.fer.spocc.grammar.Symbol;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.junit.Assert;
import org.junit.Before;

/**
  * @author Leo Osvald
  *
  */
public abstract class LR1NfaTest extends LR1AutomatonTest {

	LR1Nfa lr1nfa;
	
	@Before
	public void before() {
		// TODO
		loadNfa();
	}
	
	abstract void loadNfa();
	
	void checkContains(String sequence, LR1Item<String>... items) {
		lr1nfa.reset();
		System.out.println("Current states: "+lr1nfa.getCurrentStates());
		for (int i = 0; i < sequence.length(); ++i) {
			Symbol<String> symbol = charToSymbol(sequence.charAt(i));
			lr1nfa.process(symbol);
		}
		Set<LR1Item<String>> expected = new HashSet<LR1Item<String>>();
		for (LR1Item<String> item : items)
			expected.add(item);
		Set<LR1Item<String>> actual = new HashSet<LR1Item<String>>();
		for (State state : lr1nfa.getCurrentStates()) {
			actual.add(lr1nfa.getAssociated(state.getId()));
		}
		Assert.assertEquals(expected, actual);
	}
	
	protected void checkTransitions(
			Transition<Symbol<String>>... expectedTransitions) {
		Validate.isTrue(expectedTransitions.length > 0,
		"Moras dat bar jedan prijelaz, inace nema smisla");
		Collection<Transition<Symbol<String>>> transitions = lr1nfa.getTransitions();
		for (Transition<Symbol<String>> transition : expectedTransitions) {
			Assert.assertTrue(transitions.contains(transition));
		}
	}
	
	protected Transition<Symbol<String>> createTransition(
			int fromId, int toId, Character symbol) {
		return new Transition<Symbol<String>>(
				lr1nfa.getState(fromId), lr1nfa.getState(toId), 
				symbol != null ? charToSymbol(symbol) : null);
	}
	
}
