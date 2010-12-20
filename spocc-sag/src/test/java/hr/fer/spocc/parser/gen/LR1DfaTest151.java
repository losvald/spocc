/*
 * LR1DfaTest151.java
 *
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 * Copyright (C) 2010 Sasa Ledinscak <sledinscak@gmail.com>
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
package hr.fer.spocc.parser.gen;

import hr.fer.spocc.automaton.fsm.FiniteStateMachines;
import hr.fer.spocc.automaton.fsm.State;
import hr.fer.spocc.grammar.cfg.CfgGrammar;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
  * @author Sasa Ledinscak
  * @author Leo Osvald
  *
  */
@SuppressWarnings("all")
public class LR1DfaTest151 extends LR1DfaTest {

	Map<Integer, LR1Item<String>> items
	= new HashMap<Integer, LR1Item<String>>();
	
	public LR1DfaTest151() {
		
		items.put(0, item(variable('S'), // lijeva strana ( S -> )
				toRightSide('A'), // -> *A
				0, // -> *A (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(1, item(variable('S'), // lijeva strana ( S -> )
				toRightSide('A'), // -> A*
				1, // -> A* (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(21, item(variable('A'), // lijeva strana( A -> )
				toRightSide('B', 'A'), // -> B*A
				1, // -> B*A (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(22, item(variable('A'), // lijeva strana ( A -> )
				toRightSide('B', 'A'), // -> *BA
				0, // -> *BA (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(23, item(variable('A'), // lijeva strana ( A -> )
				toRightSide(), // -> *
				0, // -> * (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(24, item(variable('B'), // lijeva strana ( B -> )
				toRightSide('a', 'B'), // -> *aB
				0, // -> *aB (indeks pocevsi od nule)
				createFollowingSet('a', 'b', null))); // { a, b, T}
		
		items.put(25, item(variable('B'), // lijeva strana ( B -> )
				toRightSide('b'), // -> *b
				0, // -> *b (indeks pocevsi od nule)
				createFollowingSet('a', 'b', null))); // { a, b, T}
		
		items.put(31, item(variable('B'), // lijeva strana ( B -> )
				toRightSide('a', 'B'), // -> a*B
				1, // -> a*B (indeks pocevsi od nule)
				createFollowingSet('a', 'b', null))); // { a, b, T}
		
		items.put(4, item(variable('B'), // lijeva strana ( B -> )
				toRightSide('b'), // -> b*
				1, // -> b* (indeks pocevsi od nule)
				createFollowingSet('a', 'b', null))); // { a, b, T}
		
		items.put(5, item(variable('A'), // lijeva strana ( A -> )
				toRightSide('B', 'A'), // -> BA*
				2, // -> BA* (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(6, item(variable('B'), // lijeva strana ( B -> )
				toRightSide('a', 'B'), // -> aB*
				2, // -> aB* (indeks pocevsi od nule)
				createFollowingSet('a', 'b', null))); // { a, b, T}
		
	}
	
	@Test
	public void test00() {
		checkContains("", items.get(0), items.get(22), items.get(23), items.get(24), items.get(25));
	}
	
	@Test
	public void test01() {
		checkContains("BBA", items.get(5));
	}
	
	@Test
	public void test011() {
		checkContains("BA", items.get(5));
	}
	
	@Test
	public void test02() {
		checkContains("A", items.get(1));
	}
	
	@Test
	public void test03() {
		checkContains("BBaaaB", items.get(6));
	}
	
	@Test
	public void test031() {
		checkContains("BaB", items.get(6));
	}
	
	@Test
	public void test032() {
		checkContains("aaaB", items.get(6));
	}
	
	
	@Test
	public void test04() {
		checkContains("BBb", items.get(4));
	}
	
	@Test
	public void test041() {
		checkContains("b", items.get(4));
	}
	
	@Test
	public void test042() {
		checkContains("aaaab", items.get(4));
	}
	
	
	@Test
	public void test05() {
		checkContains("a", items.get(31), items.get(24), items.get(25));
	}
	
	@Test
	public void test051() {
		checkContains("aaaa", items.get(31), items.get(24), items.get(25));
	}
	
	@Test
	public void test0() {
		// OVO znaci da ne postoji
		checkContains("ccCA");
	}
	
	@Test
	public void test001() {
		// OVO znaci da ne postoji
		checkContains("AA");
	}
	
	@Test
	public void test002() {
		// OVO znaci da ne postoji
		checkContains("aaBbb");
	}
	
	@Test
	public void test003() {
		// OVO znaci da ne postoji
		checkContains("BabB");
	}
	
	@Test
	public void test0004() {
		// OVO znaci da ne postoji
		checkContains("BBBAaa");
	}

	@Override
	void loadDfa() {
		LR1Nfa lr1Nfa = createLR1Nfa();
		System.out.println("---------NFA--------");
		System.out.println(lr1Nfa.getTransitions());
		for (State state : lr1Nfa.getStates()) {
			System.out.println(state.getId() + ": " 
					+ lr1Nfa.getAssociated(state.getId()));
		}
		
		System.out.println("---------Itemless dfa--------");
		System.out.println(FiniteStateMachines.toDfa(lr1Nfa, false));
		
		lr1dfa = LR1ParsingTableDescriptorFactory.createLR1Dfa(lr1Nfa);
		System.out.println("---------DFA--------");
		System.out.println(lr1dfa.getTransitions());
		for (State state : lr1dfa.getStates()) {
			System.out.println(state.getId() + ": " 
					+ lr1dfa.getAssociated(state.getId()));
		}
	}
	
	static LR1Nfa createLR1Nfa() {
		CfgGrammar<String> grammar = loadGrammar();
		System.out.println(grammar);
		return new LR1Nfa(grammar);
	}
	
	static CfgGrammar<String> loadGrammar() {
		DefaultParserGenerator gen = new DefaultParserGenerator();
		try {
			gen.readFromFile(new File("src/test/resources/srbljic-151-syntax-rules.def"));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return gen.getGrammar();
	}
	
	public static void main(String[] args) {
		try {
			Main.main(new String[]{"src/test/resources/srbljic-151-syntax-rules.def",
					"-o", "/tmp/"});
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
}
