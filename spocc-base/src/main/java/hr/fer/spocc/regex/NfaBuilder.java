/*
 * NfaBuilder.java
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
package hr.fer.spocc.regex;

import hr.fer.spocc.automaton.fsm.FiniteStateMachines;
import hr.fer.spocc.automaton.fsm.NondeterministicFiniteAutomaton;
import hr.fer.spocc.automaton.fsm.State;

import java.util.HashSet;
import java.util.Set;

/**
 * Klasa koja sluzi za gradjenje e-NKA automata na temelju regularnih
 * izraza.
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public class NfaBuilder<T> {
	
	public NondeterministicFiniteAutomaton<T> build(
			RegularExpression<T> regexp) {
		NondeterministicFiniteAutomaton<T> nfa 
		= FiniteStateMachines.createNfa();
		Part part = buildPart(regexp, nfa, new IdCounter());
		nfa.setStartState(part.getStartState().getId());
		nfa.reset();
		return nfa;
	}
	
	private Part buildPart(RegularExpression<T> regexp, 
			NondeterministicFiniteAutomaton<T> nfa,
			IdCounter idCounter) {
		if (regexp.isTrivial()) {
			RegularExpressionType tt = regexp.getType();
			State s1 = new State(idCounter.getAndIncrement(), false);
			nfa.addState(s1, true);
			State s2 = new State(idCounter.getAndIncrement(), true);
			nfa.addState(s2, false);
			
			switch (tt) {
			case TERMINAL:
				nfa.addTransition(s1.getId(), 
						s2.getId(), 
						regexp.getTrivialValue());
				break;
			case EPSILON:
				nfa.addTransition(s1.getId(), s2.getId(), null);
				break;
			}
			return new Part(s1, s1, s2);
		}
		
		// non-trivial cases
		
		RegularExpression<T> subregex1 = regexp.getSubexpression(0);
		
		if (regexp.getOperator().isUnary()) {
			// only one case here - STAR operator
			Part part = buildPart(subregex1, nfa, idCounter);
			State state1 = new State(idCounter.getAndIncrement(), false);
			nfa.addState(state1, true);
			nfa.addTransition(state1.getId(), 
					part.getStartState().getId(), 
					null);
			State state2 = new State(idCounter.getAndIncrement(), true);
			nfa.addState(state2, false);
			nfa.addTransition(state1.getId(), state2.getId(), null);
			
			for (State s : part.getAcceptStates()) {
				nfa.addTransition(s.getId(), 
						part.getStartState().getId(),
						null);
				nfa.addTransition(s.getId(), 
						state2.getId(),
						null);
			}
			// we need separate traversal in order to avoid concurrent
			// modifications to the set over which we iterate
			for (State s : part.getAcceptStates())
				part.setAccept(s, false);
			
			Part compositePart = new Part(state1, part);
			compositePart.addState(state1);
			compositePart.addState(state2);
			return compositePart;
		} else {
			RegularExpression<T> subregex2 = regexp.getSubexpression(1);
			
			Part[] parts = new Part[]{
					buildPart(subregex1, nfa, idCounter),
					buildPart(subregex2, nfa, idCounter)
			};

			switch (regexp.getOperator()) {
			case CONCATENATION:
				for (State s : parts[0].getAcceptStates()) {
					nfa.addTransition(s.getId(), 
							parts[1].getStartState().getId(), 
							null);
					
				}
				// we need separate traversal in order to avoid concurrent
				// modifications to the set over which we iterate
				for (State s : parts[0].getAcceptStates())
					parts[0].setAccept(s, false);
				
				return new Part(parts[0].getStartState(),
						parts);
			case UNION:
				// add new start state
				State state1 = new State(idCounter.getAndIncrement(), 
						false);
				nfa.addState(state1, true);
				
				// connect it with both parts using epsilon transition
				for (int i = 0; i < parts.length; ++i) {
					nfa.addTransition(state1.getId(), 
							parts[i].getStartState().getId(), 
							null);
				}

				// add new accept state and transitions from both parts
				State state2 = new State(idCounter.getAndIncrement(), 
						true);
				nfa.addState(state2, false);
				
				// connect both parts with it using epsilon transition
				for (int i = 0; i < parts.length; ++i) {
					for (State s : parts[i].getAcceptStates()) {
						nfa.addTransition(s.getId(), 
								state2.getId(), 
								null);
					}
					// we need separate traversal in order to avoid concurrent
					// modifications to the set over which we iterate
					for (State s : parts[i].getAcceptStates())
						parts[i].setAccept(s, false);
				}

				Part compositePart = new Part(state1, parts);
				compositePart.addState(state1);
				compositePart.addState(state2);
				return compositePart;
			default: // this case is impossible under normal circumstances
				return null;
			}
		}
	}
	
	private static final class Part {
		
		private final Set<State> states;
		private final State startState;
		
		private final Set<State> acceptStates;
		
		public Part(State startState, State... states) {
			this.startState = startState;
			this.states = new HashSet<State>();
			this.acceptStates = new HashSet<State>();
			for (State s : states) {
				this.states.add(s);
				if (s.isAccept())
					this.acceptStates.add(s);
			}
			if (startState.isAccept())
				this.acceptStates.add(startState);
		}
		
		public Part(State startState, Part... parts) {
			this.startState = startState;
			Part biggest = getBiggest(parts);
			this.states = biggest.getStates();
			this.acceptStates = biggest.getAcceptStates();
			for (Part p : parts) {
				if (p != biggest) {
					this.states.addAll(p.getStates());
					this.acceptStates.addAll(p.getAcceptStates());
				}
			}
			if (startState.isAccept())
				this.acceptStates.add(startState);
		}
		
		public void addState(State state) {
			states.add(state);
			if (state.isAccept())
				this.acceptStates.add(state);
		}
		
		public State getStartState() {
			return startState;
		}

		public Set<State> getStates() {
			return states;
		}
		
		/**
		 * Vraca prihvatljiva stanja ovog dijela.<br>
		 * 
		 * <b>Napomena</b>: sva stanja cija se prihvatljivost mijenja
		 * jednom kad su dodana, moraju biti mijenjana preko metode
		 * {@link #setAccept(State, boolean)}. U protivnom ova metoda
		 * ne garantira da ce vratiti tocan skup prihvatljivih stanja.
		 * 
		 * @return
		 */
		public Set<State> getAcceptStates() {
			return acceptStates;
		}
		
		void setAccept(State s, boolean accept) {
			s.setAccept(accept);
			if (this.states.contains(s)) {
				if (accept) {
					this.acceptStates.add(s);
				} else {
					this.acceptStates.remove(s);
				}
			}
		}
		
		int size() {
			return states.size();
		}
		
		static Part getBiggest(Part[] parts) {
			Part ret = null;
			for (int i = 0; i < parts.length; ++i) {
				if (ret == null || parts[i].size() > ret.size()) {
					ret = parts[i];
				}
			}
			return ret;
		}
		
	}
	
	private final class IdCounter {
		int id;
		
		int getAndIncrement() {
			return id++;
		}
	}
}
