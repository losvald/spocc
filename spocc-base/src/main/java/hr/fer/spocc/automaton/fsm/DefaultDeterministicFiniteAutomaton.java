/*
 * DefaultDeterministicFiniteAutomaton.java
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
package hr.fer.spocc.automaton.fsm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Defaultna implementacija sucelja {@link DeterministicFiniteAutomaton}.
 * 
 * @author Leo Osvald
 *
 */
class DefaultDeterministicFiniteAutomaton<T> 
extends	AbstractDeterministicFiniteAutomaton<T> {

	private final Map<State, Map<T, State>> transitions
	= new HashMap<State, Map<T,State>>();
	
	@Override
	public Collection<Transition<T>> getTransitions() {
		List<Transition<T>> list 
		= new ArrayList<Transition<T>>();
		for (Entry<State, Map<T, State>> e : transitions.entrySet()) {
			for (Entry<T, State> e2 : e.getValue().entrySet()) {
				list.add(new Transition<T>(e.getKey(), 
						e2.getValue(), e2.getKey()));
			}
		}
		return Collections.unmodifiableCollection(list);
	}

	@Override
	public State getNextState(T input, State state) {
		Map<T, State> map = transitions.get(state);
		if (map == null || !map.containsKey(input))
			return null;
		return map.get(input);
	}

	@Override
	protected void addTransition(State from, State to, T input) {
		Map<T, State> transitionMap = transitions.get(from);
		if (transitionMap == null) {
			transitionMap = new HashMap<T, State>();
			transitions.put(from, transitionMap);
		}
		
		transitionMap.put(input, to);
	}

	@Override
	protected void clearTransitions() {
		transitions.clear();
	}

}
