/*
 * FiniteStateMachines.java
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

import hr.fer.spocc.automaton.fsm.FiniteStateMachine.Transition;

import java.util.HashMap;
import java.util.Map;

import rationals.NoSuchStateException;
import rationals.transformations.Reducer;
import rationals.transformations.ToDFA;

/**
 * Klasa koja nudi staticke metode vezane uz konacne automate.
 * 
 * @author Leo Osvald
 *
 */
public final class FiniteStateMachines {

	private FiniteStateMachines() {
	}
	
	/**
	 * Stvara nedeterministicki konacni automat.
	 * 
	 * @param <T> tip znaka
	 * @return automat
	 */
	public static <T> NondeterministicFiniteAutomaton<T> createNfa() {
		return new DefaultNondeterministicFiniteAutomaton<T>();
	}
	
	/**
	 * Stvara deterministicki konacni automat.
	 * 
	 * @param <T> tip znaka
	 * @return automat
	 */
	public static <T> DeterministicFiniteAutomaton<T> createDfa() {
		return new DefaultDeterministicFiniteAutomaton<T>();
	}
	
	public static <T> DeterministicFiniteAutomaton<T> toDfa(
			NondeterministicFiniteAutomaton<T> nfa,
			boolean reduce) {
		rationals.Automaton automaton = ToJAutomataUtils.convert(nfa);
		automaton = new ToDFA().transform(automaton);
		if (reduce)
			automaton = new Reducer().transform(automaton);
		return FromJAutomatonUtils.toDfa(automaton);
	}
	
	public static <T> DeterministicFiniteAutomaton<T> toDfa(
			NondeterministicFiniteAutomaton<T> nfa) {
		return toDfa(nfa, true);
	}
	
	public static <T> DeterministicFiniteAutomaton<T> reduceDfa(
			DeterministicFiniteAutomaton<T> nfa) {
		rationals.Automaton automaton = ToJAutomataUtils.convert(nfa);
		automaton = new Reducer().transform(automaton);
		return FromJAutomatonUtils.toDfa(automaton);
	}

	static class ToJAutomataUtils {

		static <T> rationals.Automaton convert(FiniteStateMachine<T> fsm) {
			rationals.Automaton ret = new rationals.Automaton();

			Map<State, rationals.State> stateMap 
			= new HashMap<State, rationals.State>();

			for (State state : fsm.getStates()) {
				boolean start = state.equals(fsm.getStartState());
				boolean terminal = state.isAccept();
				stateMap.put(state, ret.addState(start, terminal));
			}

			try {
				for (Transition<T> transition : fsm.getTransitions()) {
					ret.addTransition(new rationals.Transition(
							stateMap.get(transition.getFrom()),
							transition.getSymbol(),
							stateMap.get(transition.getTo())
					));
				}
			} catch (NoSuchStateException e) {
				e.printStackTrace();
				throw new IllegalStateException(e);
			}

			return ret;
		}

	}


	static class FromJAutomatonUtils {

		static State fromState(rationals.State state) {
			return new State(Integer.valueOf(state.toString()), 
					state.isTerminal()); 
		}

		@SuppressWarnings("unchecked")
		static <T> Transition<T> fromTransition(
				rationals.Transition transition) {
			return new Transition<T>(
					fromState(transition.start()),
					fromState(transition.end()),
					(T) transition.label());

		}

		static <T> NondeterministicFiniteAutomaton<T> toNfa(
				rationals.Automaton automaton) {
			NondeterministicFiniteAutomaton<T> nfa 
			= FiniteStateMachines.createNfa();
			fillFsm(nfa, automaton);
			return nfa;
		}

		static <T> DeterministicFiniteAutomaton<T> toDfa(
				rationals.Automaton automaton) {
			DeterministicFiniteAutomaton<T> dfa 
			= FiniteStateMachines.createDfa();
			fillFsm(dfa, automaton);
			return dfa;
		}

		private static <T> void fillFsm(
				FiniteStateMachine<T> fsm, rationals.Automaton automaton) {

			rationals.State startState = null;

			for (Object o : automaton.states()) {
				rationals.State state = (rationals.State) o;
				fsm.addState(fromState(state));
				if (state.isInitial())
					startState = state;
			}

			for (Object o : automaton.delta()) {
				rationals.Transition transition = (rationals.Transition) o;
				Transition<T> myTran = fromTransition(transition);
				fsm.addTransition(
						myTran.getFrom().getId(),
						myTran.getTo().getId(),
						myTran.getSymbol());
			}

			fsm.setStartState(fromState(startState).getId());

			if (startState == null)
				throw new IllegalStateException("Undefined start state");
		}
	}
	
}
