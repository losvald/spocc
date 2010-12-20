/*
 * StateAssociatedNfa.java
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

import java.util.Set;

/**
 * @author Leo Osvald
 *
 */
public abstract class StateAssociatedNfa<T, V> 
extends StateAssociatedFsm<T, V> 
implements NondeterministicFiniteAutomaton<T> {

	@SuppressWarnings("unchecked")
	public StateAssociatedNfa() {
		super((FiniteStateMachine<T>)FiniteStateMachines.createNfa());
	}
	
	public StateAssociatedNfa(NondeterministicFiniteAutomaton<T> nfa) {
		super(nfa);
	}
	
	@Override
	public Set<State> getCurrentStates() {
		return ((NondeterministicFiniteAutomaton<?>) fsm).getCurrentStates();
	}

	@Override
	public Set<State> powerSet(Set<State> states) {
		return ((NondeterministicFiniteAutomaton<?>) fsm).powerSet(states);
	}
	
	@Override
	public Set<State> getNextStates(T input, State state) {
		return ((NondeterministicFiniteAutomaton<T>) fsm).getNextStates(input, 
				state);
	}
	
	public NondeterministicFiniteAutomaton<T> nfa() {
		return (NondeterministicFiniteAutomaton<T>) fsm;
	}
	
	

}
