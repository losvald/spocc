/*
 * AbstractDeterministicFiniteAutomaton.java
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

import org.apache.commons.lang.Validate;

/**
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public abstract class AbstractDeterministicFiniteAutomaton<T> 
extends AbstractFiniteStateMachine<T>
implements DeterministicFiniteAutomaton<T> {

	private State currentState;
	
	@Override
	public void setStartState(int id) {
		super.setStartState(id);
		setCurrentState(getStartState());
	}
	
	@Override
	public void addTransition(int fromId, int toId, T input) {
		State fromState = getState(fromId);
		Validate.notNull(fromState, "state fromId must be existent");
		State toState = getState(toId);
		Validate.notNull(toState, "state toId must be existent");
		
		Validate.notNull(input, "Epsilon transitions are not allowed in DFA");
		addTransition(fromState, toState, input);
	}

	@Override
	public boolean process(T inputSymbol) throws IllegalStateException {
		if (getStartState() == null)
			throw new IllegalStateException("No start state defined");
		if (inputSymbol == null)
			throw new IllegalStateException();
		
		State currentState = getCurrentState();
		if (currentState == null)
			return false;
		
		setCurrentState(getNextState(inputSymbol, currentState));
		return getCurrentState() != null;
	}

	@Override
	public boolean isAccept() {
		return getCurrentState() != null && getCurrentState().isAccept();
	}

	@Override
	public State getCurrentState() {
		return currentState;
	}
	
	@Override
	public void reset() {
		setCurrentState(getStartState());	
	}
	
	protected void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

}
