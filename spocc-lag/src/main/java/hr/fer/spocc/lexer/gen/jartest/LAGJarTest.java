/*
 * LAGJarTest.java
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
package hr.fer.spocc.lexer.gen.jartest;

import hr.fer.spocc.automaton.fsm.FiniteStateMachines;
import hr.fer.spocc.automaton.fsm.NondeterministicFiniteAutomaton;
import hr.fer.spocc.lexer.gen.LexerGenerator;
import hr.fer.spocc.lexer.gen.LexerGeneratorFactory;

/**
 * Ova klasa je namjerno ovdje kako bi se moglo testirati da li radi stvaranja .jara,
 * prije nego glavni program generatora bude gotov.
 * 
 * @author Leo Osvald
 *
 */
public class LAGJarTest {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		LexerGenerator gen = LexerGeneratorFactory.getInstance();
		NondeterministicFiniteAutomaton<Character> nfa = FiniteStateMachines.createNfa();
		System.out.println("LAGJarTest successfully finished");
	}

}
