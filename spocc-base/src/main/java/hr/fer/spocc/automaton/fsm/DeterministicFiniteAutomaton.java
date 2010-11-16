/*
 * DeterministicFiniteAutomaton.java
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
 * Deterministicki konacni automat (DKA).
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public interface DeterministicFiniteAutomaton<T> 
extends FiniteStateMachine<T> {
	
	/**
	 * Vraca stanje u kojem se automat trenutno nalazi.
	 * 
	 * @return stanje
	 */
	State getCurrentState();
	
	/**
	 * Vraca slijedece stanje automata.
	 * 
	 * @param input ulazni znak
	 * @param state stanje iz kojeg se razmatraju prijelazi
	 * @return stanja koji se dobije tocno jednim prijelazom iz
	 * pocetnog stanja
	 */
	State getNextState(T input, State state);
}
