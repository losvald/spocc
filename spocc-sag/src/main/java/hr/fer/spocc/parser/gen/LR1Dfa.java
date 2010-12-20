/*
 * LR1Dfa.java
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

import hr.fer.spocc.automaton.fsm.DeterministicFiniteAutomaton;
import hr.fer.spocc.automaton.fsm.StateAssociatedDfa;
import hr.fer.spocc.grammar.Symbol;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Leo Osvald
 *
 */
public class LR1Dfa extends StateAssociatedDfa<Symbol<String>, Set<LR1Item<String>>> {

	private final Map<Integer, Set<LR1Item<String>>> map
	= new HashMap<Integer, Set<LR1Item<String>>>();
	
	public LR1Dfa(DeterministicFiniteAutomaton<Symbol<String>> dfa) {
		super(dfa);
	}
	
	@Override
	public void associate(int stateId, Set<LR1Item<String>> value) {
		map.put(stateId, value);
	}

	@Override
	public Set<LR1Item<String>> getAssociated(int stateId) {
		return map.get(stateId);
	}

}
