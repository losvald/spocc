/*
 * StateAssociatedDfa.java
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

/**
 * @author Leo Osvald
 *
 */
public abstract class StateAssociatedDfa<T, V> 
extends StateAssociatedFsm<T, V>
implements DeterministicFiniteAutomaton<T> {

	@SuppressWarnings("unchecked")
	public StateAssociatedDfa() {
		super((FiniteStateMachine<T>)FiniteStateMachines.createDfa());
	}
	
	public StateAssociatedDfa(DeterministicFiniteAutomaton<T> dfa) {
		super(dfa);
	}
	
	@Override
	public State getCurrentState() {
		return ((DeterministicFiniteAutomaton<T>) fsm).getCurrentState();
	}
	
	public State getNextState(T input, State state) {
		return ((DeterministicFiniteAutomaton<T>) fsm).getNextState(input, state);
	}
	
	public DeterministicFiniteAutomaton<T> dfa() {
		return (DeterministicFiniteAutomaton<T>) fsm;
	}
	
}
