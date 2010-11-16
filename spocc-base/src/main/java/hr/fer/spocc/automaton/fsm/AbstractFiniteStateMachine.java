/*
 * AbstractFiniteStateMachine.java
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.sglj.util.ArrayToStringUtils;

/**
 * Apstraktna implementacija sucelja {@link FiniteStateMachine}. 
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public abstract class AbstractFiniteStateMachine<T>
implements FiniteStateMachine<T> {
	
	private State startState;
	
	private final Map<Integer, State> statesById 
	= new HashMap<Integer, State>();
	
	@Override
	public State getState(int id) {
		return statesById.get(id);
	}
	
	@Override
	public State getStartState() {
		return startState;
	}
	
	@Override
	public boolean process(List<T> inputSequence) {
		for (T input : inputSequence) {
			if (!process(input))
				return false;
		}
		return true;
	}
	
	@Override
	public void clear() {
		clearStates();
		clearTransitions();
		setStartState(null);
	}
	
	@Override
	public void addState(State state, boolean start) {
		addState(state);
		if (start) {
			setStartState(state);
		}
	}
	
	@Override
	public void addState(State state) {
		Validate.notNull(state, "Cannot add null state");
		Validate.isTrue(!statesById.containsKey(state.getId()),
				"State with id " + state.getId() + " already exists");
		statesById.put(state.getId(), state);
	}
	
	@Override
	public void setStartState(int id) {
		State state = getState(id);
		if (state == null)
			throw new IllegalArgumentException("Uknown state");
		setStartState(state);
	}
	
	@Override
	public Collection<State> getStates() {
		return statesById.values();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NondeterministicFiniteAutomaton))
			return false;
		
		NondeterministicFiniteAutomaton<T> other 
		= (NondeterministicFiniteAutomaton<T>) obj;
		
		HashSet<Transition<T>> t1 = new HashSet<Transition<T>>(getTransitions());
		
		HashSet<Transition<T>> t2 = new HashSet<Transition<T>>(other.getTransitions());
		
		return t1.equals(t2)
		&& getStartState().equals(other.getStartState());
	}
	
	@Override
	public int hashCode() {
		return 23; // XXX
	}
	
	@Override
	public String toString() {
		
		return new ToStringBuilder(this)
		.append("start", getStartState())
		.append("transitions", "\n"+ArrayToStringUtils.toString(
				getTransitions().toArray(), "\n"))
				.toString();
	}
	
	/**
	 * Dodaje prijelaz.
	 * 
	 * @param from stanje iz kojeg se prelazi
	 * @param to stanje u koje se prelazi
	 * @param input ulazni znak (ako je ε-prijelaz, onda je to 
	 * <code>null</code>)
	 */
	protected abstract void addTransition(State from, State to, T input);
	
	/**
	 * Brise sve prijelaze koje automat sadrzi.
	 */
	protected abstract void clearTransitions();
	
	/**
	 * Postavlja pocetno stanje automata.
	 * 
	 * @param state
	 */
	protected void setStartState(State state) {
		this.startState = state;
	}
	
	/**
	 * Brise sva stanje koja automat sadrzi.
	 */
	protected void clearStates() {
		statesById.clear();
		setStartState(null);
	}
	
}
