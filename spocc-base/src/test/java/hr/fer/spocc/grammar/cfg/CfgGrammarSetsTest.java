/*
 * CfgGrammarSetsTest.java
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
package hr.fer.spocc.grammar.cfg;

import hr.fer.spocc.grammar.BnfUtils;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.Symbols;
import hr.fer.spocc.grammar.Terminal;
import hr.fer.spocc.grammar.Variable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

/*
 * @author Leo Osvald
 *
 */
public class CfgGrammarSetsTest {

	static final Symbol<Character> EOF = Symbols.eofSymbol();
	
	@After
	public void afterCase() {
		System.out.println("--------------");
	}
	
//	@Test
//	public void permutationTest() {
//		new PermutationVisitor(new String[]{"a", "b", "c"}) {
//			@Override
//			void check(String[] permutedRules) {
//				System.out.println(Arrays.toString(permutedRules));
//			}
//		}.checkAllPermutations();
//	}
	
	@Test
	public void testSimple00() {
		new PermutationVisitor(new String[]{
				"<S>::=<A>b", 
				"<A>::=a|d"}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A", 'a', 'd');
				checkBeginsWith(g, "S", 'a', 'd');
			}
		}.checkAllPermutations();
	}
	
	@Test
	public void testSimple01() {
		new PermutationVisitor(new String[]{
				"<S>::=<A>b", 
				"<A>::="}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A");
				checkBeginsWith(g, "S", 'b');
			}
		}.checkAllPermutations();
	}
	
	@Test
	public void testSimple02() {
		new PermutationVisitor(new String[]{
				"<S>::=<A><B>b", 
				"<A>::=",
				"<B>::="}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A");
				checkBeginsWith(g, "B");
				checkBeginsWith(g, "S", 'b');
			}
		}.checkAllPermutations();
	}
	
	@Test
	public void testSimple03() {
		new PermutationVisitor(new String[]{
				"<S>::=<A><B>c", 
				"<A>::=",
				"<B>::=|b"}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A");
				checkBeginsWith(g, "B", 'b');
				checkBeginsWith(g, "S", 'b', 'c');
			}
		}.checkAllPermutations();
	}
	
	@Test
	public void testSimple04() {
		new PermutationVisitor(new String[]{
				"<S>::=<A><B>c", 
				"<A>::=a",
				"<B>::=|b"}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A", 'a');
				checkBeginsWith(g, "B", 'b');
				checkBeginsWith(g, "S", 'a');
			}
		}.checkAllPermutations();
	}
	
	@Test
	public void testSimple05() {
		new PermutationVisitor(new String[]{
				"<S>::=<A><B>c", 
				"<A>::=|a",
				"<B>::=|b"}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A", 'a');
				checkBeginsWith(g, "B", 'b');
				checkBeginsWith(g, "S", 'a', 'b', 'c');
			}
		}.checkAllPermutations();
	}
	
	@Test
	public void testSimple06() {
		new PermutationVisitor(new String[]{
				"<S>::=<A><B>", 
				"<A>::=|a",
				"<B>::=|b"}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A", 'a');
				checkBeginsWith(g, "B", 'b');
				checkBeginsWith(g, "S", 'a', 'b');
			}
		}.checkAllPermutations();
	}
	
	@Test
	public void testTransitivity01() {
		new PermutationVisitor(new String[]{
				"<S>::=<A>c", 
				"<A>::=a|<C>",
				"<C>::="}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A", 'a');
				checkBeginsWith(g, "S", 'a', 'c');
			}
		}.checkAllPermutations();
	}
	
	@Test
	public void testTransitivity02() {
		new PermutationVisitor(new String[]{
				"<S>::=<A>c", 
				"<A>::=a|<C>",
				"<C>::=<D>",
				"<D>::="}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A", 'a');
				checkBeginsWith(g, "S", 'a', 'c');
			}
		}.checkAllPermutations();
	}
	
	@Test
	public void testRecursive01() {
		new PermutationVisitor(new String[]{
				"<S>::=<A><B>c", 
				"<A>::=<A>a|<B>",
				"<B>::=<A>|<B>b|c|"}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A", 'a', 'b', 'c');
				checkBeginsWith(g, "B", 'a', 'b', 'c');
				checkBeginsWith(g, "S", 'a', 'b', 'c');
			}
		}.checkAllPermutations();
	}
	
	@Test
	public void testComplex01() {
		new PermutationVisitor(new String[]{
				"<S>::=<A><B><C><D>e",
				"<A>::=|a",
				"<B>::=|b|<C>|<D>",
				"<C>::=c",
				"<D>::=d"}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A", 'a');
				checkBeginsWith(g, "B", 'b', 'c', 'd');
				checkBeginsWith(g, "C", 'c');
				checkBeginsWith(g, "D", 'd');
				checkBeginsWith(g, "S", 'a', 'b', 'c', 'd');
			}
		}.checkAllPermutations();
	}
	
	@Test
	public void testComplex02() {
		new PermutationVisitor(new String[]{
				"<S>::=<A><B><C><D>e",
				"<A>::=|a",
				"<B>::=|b|<C>|<D>",
				"<C>::=c|<D>",
				"<D>::=d|"}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A", 'a');
				checkBeginsWith(g, "B", 'b', 'c', 'd');
				checkBeginsWith(g, "C", 'c', 'd');
				checkBeginsWith(g, "D", 'd');
				checkBeginsWith(g, "S", 'a', 'b', 'c', 'd', 'e');
			}
		}.checkAllPermutations();
	}
	
	@Test
	public void testSrbljicPage100() {
		new PermutationVisitor(new String[]{
				"<A>::=<B><C>c|e<D><B>",
				"<B>::=|b<C><D><E>",
				"<C>::=<D>a<B>|ca",
				"<D>::=|d<D>",
				"<E>::=e<A>f|c"}) {
			@Override
			void check(String[] permutatedRules) {
				CfgGrammar<Character> g = createGrammar(permutatedRules);
				checkBeginsWith(g, "A", 'a', 'b', 'c', 'd', 'e');
				checkBeginsWith(g, "B", 'b');
				checkBeginsWith(g, "C", 'a', 'c', 'd');
				checkBeginsWith(g, "D", 'd');
				checkBeginsWith(g, "E", 'c', 'e');
			}
		}.checkAllPermutations();
	}
	
	static Terminal<Character> terminal(char c) {
		return new Terminal<Character>(c);
	}
	
	static Variable<Character> var(String name) {
		return new Variable<Character>(name);
	}
	
	static CfgGrammar<Character> createGrammar(String[] rules,
			int[] permutation) {
		StringBuilder sb = new StringBuilder();
		for (int i : permutation)
			sb.append(rules[i]);
		CfgGrammar<Character> g = BnfUtils.createCfgGrammar(sb.toString());
		//System.out.println(g);
		return g;
	}
	
	static CfgGrammar<Character> createGrammar(String[] rules) {
		return createGrammar(rules, zeroPermutation(rules.length));
	}
	
	static int[] zeroPermutation(int n) {
		int[] order = new int[n];
		for (int i = 0; i < n; ++i)
			order[i] = i;
		return order;
	}
	
	static void checkBeginsWith(CfgGrammar<Character> g,
			String var, char... terminals) {
		Set<Terminal<Character>> expected = toTerminalSet(terminals);
		Assert.assertEquals(expected, g.getBeginsWithSet(var(var)));
	}
	
	private static Set<Terminal<Character>> toTerminalSet(char... terminals) {
		Set<Terminal<Character>> ret = new HashSet<Terminal<Character>>();
		for (char c : terminals) {
			ret.add(terminal(c));
		}
		return ret;
	}
	
	static abstract class PermutationVisitor {
		String[] rules;
		PermutationVisitor(String[] permutation) {
			this.rules = permutation;
			System.out.println(createGrammar(permutation));
		}
		abstract void check(String[] permutatedRules);
		void checkAllPermutations() {
			permute(0, new String[rules.length], new HashSet<Integer>());
		}
		void permute(int k, String[] permutatedRules, Set<Integer> visited) {
			if (visited.size() == rules.length) {
				System.out.println(Arrays.toString(permutatedRules));
				check(permutatedRules);
				return ;
			}
			for (int i = 0; i < rules.length; ++i) {
				if (!visited.contains(i)) {
					visited.add(i);
					permutatedRules[k] = this.rules[i];
					permute(k + 1, permutatedRules, visited);
					visited.remove(i);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		CfgGrammar<Character> g = createGrammar(new String[]{
				"<C>::=",
				"<A>::=a|<C>",
				"<S>::=<A>c"});
		checkBeginsWith(g, "A", 'a');
		checkBeginsWith(g, "S", 'a', 'c');
	}
	
}
