/*
 * FiniteStateMachine.java
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
 * Klasa koja predstavlja konacni automat.
 * 
 * @author Leo Osvald
 *
 * @param <T> tip ulaznog znaka
 */
public interface FiniteStateMachine<T> {

	/**
	 * Vraca stanje s zadanim id-em.
	 * 
	 * @param id identifikator stanja
	 * @return stanje koje ima zadani identifikator ili
	 * <code>null</code> ako takvo nije nadjeno.
	 */
	State getState(int id);
	
	/**
	 * Dodaje prijelaz. Stanja moraju biti prethodno dodana u 
	 * automat preko metode {@link #addState(State, boolean)}.
	 * 
	 * @param fromId id stanja od iz kojeg se prelazi
	 * @param toId id stanje u koje se prelazi
	 * @param input ulaz tj. "znak" koji je procitan
	 */
	void addTransition(int fromId, int toId, T input);
	
	/**
	 * Vraca pocetno stanje. To je stanje u kojem se
	 * automat nalazi prije pocetka citanja niza, ili
	 * nakon poziva metode {@link #reset()}.
	 * 
	 * @return pocetno stanje
	 */
	State getStartState();
	
	/**
	 * Resetira automat u pocetno stanje. Prijelazi se nece izbrisati.
	 */
	void reset();
	
	/**
	 * Unistava sva stanja i prijelaze koje automat sadrzi. Takodjer,
	 * pocetno stanje postat ce nedefinirano.
	 */
	void clear();
	
	/**
	 * Procesira zadani ulaz. Ako se automat dovede u nepostojece
	 * stanje
	 * 
	 * @param inputSequence 
	 * @return <code>true</code> ako automat nije usao u nepostojece
	 * stanje, <code>false</code> u slucaju da je.
	 * @throws IllegalStateException ako automat nema definirano pocetno stanje
	 */
	boolean process(List<T> inputSequence) throws IllegalStateException;
	
	/**
	 * Procesira zadani ulazni znak.
	 * 
	 * @param inputSymbol znak koji automat treba procesirati (ucitati)
	 * @return <code>true</code> ako automat nije usao u nepostojece
	 * stanje, <code>false</code> u slucaju da je.
	 * @throws IllegalStateException ako automat nema definirano pocetno stanje
	 */
	boolean process(T inputSymbol) throws IllegalStateException;
	
	/**
	 * Dodaje stanje u automat. Ako je drugi parametar
	 * <code>true</code> a vec postoji pocetno stanje, 
	 * stanje koje se upravo dodaje ce postati pocetno.
	 * 
	 * @param state stanje
	 * @param start <code>true</code> da li je to stanje pocetno.
	 */
	void addState(State state, boolean start);
	
	/**
	 * Dodaje stanje u automat.
	 * 
	 * @param state stanje koje se dodaje
	 */
	void addState(State state);
	
	/**
	 * Postavlja zadano stanje u pocetno.
	 * 
	 * @param id identifikator stanja
	 */
	void setStartState(int id);
	
	/**
	 * Provjerava da li je automat u prihvatljivom stanju.
	 * 
	 * @return <code>true</code> ako je automat u prihvatljivom stanju,
	 * <code>false</code> u protivnom.
	 */
	boolean isAccept();
	
	/**
	 * Vraca kolekciju stanja od kojih se automat sastoji.
	 * 
	 * @return neizmjenjiva kolekcija stanja
	 */
	Collection<State> getStates();
	
	/**
	 * Vraca kolekciju prijelaza od kojih se automat sastoji.
	 * 
	 * @return neizmjenjiva kolekcija tripleta
	 */
	Collection<Transition<T>> getTransitions();
	
	
	/**
	 * Prijelaz konacnog automata.<br>
	 * Ova klasa je immutable.
	 * 
	 * @author Leo Osvald
	 *
	 * @param <T>
	 */
	public static final class Transition<T> {
		private final State from;
		private final T symbol;
		private final State to;
		
		public Transition(State from, State to, T symbol) {
			this.from = from;
			this.to = to;
			this.symbol = symbol;
		}

		public State getFrom() {
			return from;
		}

		public T getSymbol() {
			return symbol;
		}

		public State getTo() {
			return to;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result
					+ ((symbol == null) ? 0 : symbol.hashCode());
			result = prime * result + ((to == null) ? 0 : to.hashCode());
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
			Transition<?> other = (Transition<?>) obj;
			if (from == null) {
				if (other.from != null)
					return false;
			} else if (!from.equals(other.from))
				return false;
			if (symbol == null) {
				if (other.symbol != null)
					return false;
			} else if (!symbol.equals(other.symbol))
				return false;
			if (to == null) {
				if (other.to != null)
					return false;
			} else if (!to.equals(other.to))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "[" + from.getId() + " " + to.getId() + " " + symbol + "]";
//			return "Transition [from=" + from + ", symbol=" + symbol + ", to="
//					+ to + "]";
		}
		
	}
}
