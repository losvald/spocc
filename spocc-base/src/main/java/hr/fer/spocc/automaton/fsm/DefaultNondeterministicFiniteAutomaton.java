/*
 * DefaultNondeterministicFiniteAutomaton.java
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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.sglj.util.HashMultiMap;
import org.sglj.util.MultiMap;
import org.sglj.util.SetUtils;

/**
 * Defaultna implementacija nedeterministickog automata.
 * Implementacija je prilicno efikasna, osim za slucaj kad se
 * metoda {@link #isAccept()} poziva vise puta zaredom (jer se rezultat
 * ne sprema).
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
class DefaultNondeterministicFiniteAutomaton<T> extends
		AbstractNondeterministicFiniteAutomaton<T> {

	private final Map<State, MultiMap<T, State>> transitions
	= new HashMap<State, MultiMap<T,State>>();
	
	private final Set<State> currentStates = new HashSet<State>();
	
	private final Set<State> unmodifiableCurrentStates =
		Collections.unmodifiableSet(currentStates);
	
	@Override
	public void reset() {
		currentStates.clear();
		currentStates.add(getStartState());
		currentStates.addAll(powerSet(currentStates));
	}

	@Override
	public boolean isAccept() {
		// TODO implementirati u O(1)
		for (State s : powerSet(getCurrentStates())) {
			if (s.isAccept())
				return true;
		}
		return false;
	}

	@Override
	public Set<State> getCurrentStates() {
		return unmodifiableCurrentStates;
	}
	
	/**
	 * Ova implementacija koristi BFS algoritam pretrazivanja i vremenska
	 * slozenost je linearna.
	 */
	@Override
	public Set<State> powerSet(Set<State> states) {
		Set<State> visited = new HashSet<State>();
		Queue<State> queue = new ArrayDeque<State>();
		queue.addAll(states);
		visited.addAll(states);
		while (!queue.isEmpty()) {
			State u = queue.remove();
			for (State next : getNextStates(null, u)) {
				if (!visited.contains(next)) {
					visited.add(next);
					queue.add(next);
				}
			}
		}
		return visited;
	}
	
	@Override
	public Collection<Transition<T>> getTransitions() {
		List<Transition<T>> list 
		= new ArrayList<Transition<T>>();
		for (Entry<State, MultiMap<T, State>> e : transitions.entrySet()) {
			for (Entry<T, State> e2 : e.getValue().entrySet()) {
				list.add(new Transition<T>(e.getKey(), 
						e2.getValue(), e2.getKey()));
			}
		}
		return Collections.unmodifiableCollection(list);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<State> getNextStates(T input, State state) {
		MultiMap<T, State> map = transitions.get(state);
		if (map == null || !map.containsKey(input))
			return Collections.EMPTY_SET;
		return map.getAll(input);
	}
	
	@Override
	protected void addTransition(State from, State to, T input) {
		MultiMap<T, State> transitionMap = transitions.get(from);
		if (transitionMap == null) {
			transitionMap = new HashMultiMap<T, State>();
			transitions.put(from, transitionMap);
		}
		
		transitionMap.put(input, to);
	}
	
	@Override
	protected void clearTransitions() {
		transitions.clear();
	}
	
	@Override
	protected void setCurrentStates(Set<State> states) {
		SetUtils.copy(currentStates, states);
	}
}
