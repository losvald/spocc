/*
 * LexicalRule.java
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
package hr.fer.spocc.lexer;

import hr.fer.spocc.automaton.fsm.NondeterministicFiniteAutomaton;

/**
 * Klasa koja predstavlja leksicko pravilo.<br>
 * Napomena: niti jedna dva pravila ne smiju imati isti prioritet
 * 
 * @author Leo Osvald
 *
 */
public final class LexicalRule implements Comparable<LexicalRule> {
	private final int priority;
	private final Action action;
	private final State state;
	private final NondeterministicFiniteAutomaton<Character> nfa;
	
	public LexicalRule(int priority, Action action, State state,
			NondeterministicFiniteAutomaton<Character> nfa) {
		this.priority = priority;
		this.action = action;
		this.state = state;
		this.nfa = nfa;
	}
	
	public int getPriority() {
		return priority;
	}

	public Action getAction() {
		return action;
	}

	public State getState() {
		return state;
	}

	/**
	 * Vraca automat koji je u stanju prepoznati da li se leksicko pravilo
	 * moze primijeniti.
	 * 
	 * @return ε-NKA
	 */
	public NondeterministicFiniteAutomaton<Character> getAutomaton() {
		return nfa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + priority;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LexicalRule other = (LexicalRule) obj;
		if (priority != other.priority)
			return false;
		return true;
	}

	@Override
	public int compareTo(LexicalRule o) {
		return o != null ? (getPriority() - o.getPriority()) 
				: Integer.MAX_VALUE;
	}

	@Override
	public String toString() {
		return "LexicalRule [priority=" + priority + ", state=" + state
				+ ", action=" + action + "]";
	}
	
	
	
}
