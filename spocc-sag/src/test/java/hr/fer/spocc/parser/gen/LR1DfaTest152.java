/*
 * LR1DfaTest152.java
 *
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
  *
  */
@SuppressWarnings("all")
public class LR1DfaTest152 extends LR1DfaTest {

	Map<Integer, LR1Item<String>> items
	= new HashMap<Integer, LR1Item<String>>();

	public LR1DfaTest152() {
		
		items.put(0, item(variable('X'), // lijeva strana ( S' -> )
				toRightSide('S'), // -> *S
				0, // -> *S (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(10, item(variable('S'), // lijeva strana ( S -> )
				toRightSide('C', 'C'), // -> *CC
				0, // -> *CC (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(1, item(variable('X'), // lijeva strana ( S' -> )
				toRightSide('S'), // -> S*
				1, // -> S* (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(21, item(variable('S'), // lijeva strana ( S -> )
				toRightSide('C', 'C'), // -> C*C
				1, // -> C*C (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(22, item(variable('C'), // lijeva strana ( C -> )
				toRightSide('c', 'C'), // -> *cC
				0, // -> *cC (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(23, item(variable('C'), // lijeva strana ( C -> )
				toRightSide('d'), // -> *d
				0, // -> *d (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(31, item(variable('C'), // lijeva strana ( C -> )
				toRightSide('c', 'C'), // -> c*C
				1, // -> c*C (indeks pocevsi od nule)
				createFollowingSet('c', 'd'))); // {c, d}
		
		items.put(32, item(variable('C'), // lijeva strana ( C -> )
				toRightSide('c', 'C'), // -> *cC
				0, // -> *cC (indeks pocevsi od nule)
				createFollowingSet('c', 'd'))); // {c, d}
		
		items.put(33, item(variable('C'), // lijeva strana ( C -> )
				toRightSide('d'), // -> *d
				0, // -> *d (indeks pocevsi od nule)
				createFollowingSet('c', 'd'))); // {c, d}
		
		items.put(4, item(variable('C'), // lijeva strana ( C -> )
				toRightSide('d'), // -> d*
				1, // -> d* (indeks pocevsi od nule)
				createFollowingSet('c', 'd'))); // {c, d}
		
		items.put(5, item(variable('S'), // lijeva strana ( S -> )
				toRightSide('C', 'C'), // -> CC*
				2, // -> CC* (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(6, item(variable('C'), // lijeva strana ( C -> )
				toRightSide('c', 'C'), // -> c*C
				1, // -> c*C (indeks pocevsi od nule)
				createFollowingSet((null)))); // { T }
		
		items.put(7, item(variable('C'), // lijeva strana ( C -> )
				toRightSide('d'), // -> d*
				1, // -> d* (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
		items.put(8, item(variable('C'), // lijeva strana ( C -> )
				toRightSide('c', 'C'), // -> cC*
				2, // -> cC* (indeks pocevsi od nule)
				createFollowingSet('c', 'd'))); // {c, d}
		
		items.put(9, item(variable('C'), // lijeva strana ( C -> )
				toRightSide('c', 'C'), // -> cC*
				2, // -> cC* (indeks pocevsi od nule)
				createFollowingSet(null))); // {T}
		
	}
	
	@Test
	public void test00() {
		checkContains("", items.get(0), items.get(10), items.get(32), items.get(33));
	}
	
	@Test
	public void test01() {
		checkContains("ccd", items.get(4));
	}
	
	@Test
	public void test011() {
		checkContains("cd", items.get(4));
	}
	
	@Test
	public void test012() {
		checkContains("d", items.get(4));
	}
	
	@Test
	public void test02() {
		checkContains("ccC", items.get(8));
	}
	
	@Test
	public void test03() {
		checkContains("CC", items.get(5));
	}
	
	@Test
	public void test04() {
		checkContains("S", items.get(1));
	}
	
	@Test
	public void test05() {
		checkContains("CccccC", items.get(9));
	}
	
	@Test
	public void test051() {
		checkContains("CcC", items.get(9));
	}
	
	@Test
	public void test06() {
		checkContains("Ccd", items.get(7));
	}
	
	@Test
	public void test061() {
		checkContains("Cd", items.get(7));
	}
	
	@Test
	public void test062() {
		checkContains("Ccccccd", items.get(7));
	}
	
	@Test
	public void test07() {
		checkContains("Ccccc", items.get(6), items.get(22), items.get(23));
	}
	
	@Test
	public void test08() {
		checkContains("C", items.get(21), items.get(22), items.get(23));
	}
	
	@Test
	public void test09() {
		checkContains("c", items.get(31), items.get(32), items.get(33));
	}
	
	@Test
	public void test091() {
		checkContains("cccc", items.get(31), items.get(32), items.get(33));
	}
	
	@Test
	public void test0() {
		// OVO znaci da ne postoji
		checkContains("ccCC");
	}
	
	@Test
	public void test001() {
		// OVO znaci da ne postoji
		checkContains("ccdd");
	}
	
	@Test
	public void test002() {
		// OVO znaci da ne postoji
		checkContains("SS");
	}

	@Test
	public void test003() {
		// OVO znaci da ne postoji
		checkContains("CCC");
	}
	
	@Test
	public void test004() {
		// OVO znaci da ne postoji
		checkContains("CccCd");
	}
	
	@Test
	public void test005() {
		// OVO znaci da ne postoji
		checkContains("Cdd");
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
			gen.readFromFile(new File("src/test/resources/srbljic-152-syntax-rules.def"));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return gen.getGrammar();
	}
	
}
