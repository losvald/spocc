/*
 * StateAssociatedFsm.java
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

import java.util.Collection;
import java.util.List;

/**
 * @author Leo Osvald
 *
 */
public abstract class StateAssociatedFsm<T, V> 
implements FiniteStateMachine<T> {

	protected final FiniteStateMachine<T> fsm;
	
	public StateAssociatedFsm(FiniteStateMachine<T> fsm) {
		this.fsm = fsm;
	}
	
	@Override
	public State getState(int id) {
		return fsm.getState(id);
	}

	@Override
	public void addTransition(int fromId, int toId, T input) {
		fsm.addTransition(fromId, toId, input);
	}

	@Override
	public State getStartState() {
		return fsm.getStartState();
	}

	@Override
	public void reset() {
		fsm.reset();
	}

	@Override
	public void clear() {
		fsm.clear();
	}

	@Override
	public boolean process(List<T> inputSequence) throws IllegalStateException {
		return fsm.process(inputSequence);
	}

	@Override
	public boolean process(T inputSymbol) throws IllegalStateException {
		return fsm.process(inputSymbol);
	}

	@Override
	public void addState(State state, boolean start) {
		fsm.addState(state, start);
	}

	@Override
	public void addState(State state) {
		fsm.addState(state);
	}

	@Override
	public void setStartState(int id) {
		fsm.setStartState(id);
	}

	@Override
	public boolean isAccept() {
		return fsm.isAccept();
	}

	@Override
	public Collection<State> getStates() {
		return fsm.getStates();
	}

	@Override
	public Collection<Transition<T>> getTransitions() {
		return fsm.getTransitions();
	}
	
	public abstract void associate(int stateId, V value);
	
	public abstract V getAssociated(int stateId);

}
