/*
 * AbstractNondeterministicFiniteAutomaton.java
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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * Apstraktna implementacija sucelja {@link NondeterministicFiniteAutomaton}.
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public abstract class AbstractNondeterministicFiniteAutomaton<T>
extends AbstractFiniteStateMachine<T> 
implements NondeterministicFiniteAutomaton<T> {

	@Override
	public void addTransition(int fromId, int toId, T input) {
		State fromState = getState(fromId);
		Validate.notNull(fromState, "state fromId must be existent");
		State toState = getState(toId);
		Validate.notNull(toState, "state toId must be existent");
		
		addTransition(fromState, toState, input);
	}

	@Override
	public boolean process(T input) {
		if (getStartState() == null)
			throw new IllegalStateException("No start state defined");
		
		// za epsilon prijelaz ignoriramo
		if (input == null)
			return true;
		
		Set<State> nextStates = powerSet(
				getNextStates(input, powerSet(getCurrentStates())));
		setCurrentStates(nextStates);
		return !nextStates.isEmpty();
	}
	
	/**
	 * Vraca skup stanja koji predstavlja neposrednu okolinu. 
	 * Ova metoda ne uzima u obzir ε-okolinu; za to koristite metodu 
	 * {@link #powerSet(Set)} nakon/prije ove.
	 * 
	 * @param input ulazni znak
	 * @param states skup stanja iz kojih se razmatra prijelaz
	 * @return skup stanja koji se dobije tocno jednim prijelazom iz
	 * pocetnih stanja
	 */
	protected Set<State> getNextStates(T input, Set<State> states) {		
		Set<State> ret = new HashSet<State>();
		for (State state : states) {
			ret.addAll(getNextStates(input, state));
		}
		return ret;
	}
	
	protected abstract void setCurrentStates(Set<State> states);

}
