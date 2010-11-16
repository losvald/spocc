/*
 * NfaBuilderTest.java
 *
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 * Copyright (C) 2010 Lovro Biocic <lovro.biocic@gmail.com>
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

import hr.fer.spocc.automaton.fsm.FiniteStateMachine.Transition;
import hr.fer.spocc.automaton.fsm.NondeterministicFiniteAutomaton;
import hr.fer.spocc.automaton.fsm.State;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.sglj.util.ArrayToStringUtils;
import org.sglj.util.Tuple.Tuple3;

/**
 * Test klasa za testiranje da li se gradi valjani automati 
 * za regularni izraz.
 * 
 * @author Lovro Biocic
 * @author Leo Osvald
 *
 */
public class NfaBuilderTest {
	
	@Test
	public void testBasic1() {
		RegularExpression<Character> re = new MyRegularExpression("a");
		
		NondeterministicFiniteAutomaton<Character> nfa =
			new NfaBuilder<Character>().build(re);
		
		Assert.assertEquals(2, nfa.getStates().size());
		
		Assert.assertFalse(nfa.isAccept());
		
		nfa.process('a');
		Assert.assertTrue(nfa.isAccept());
		
		Assert.assertTrue(nfa.process((Character)null));
		Assert.assertTrue(nfa.isAccept());
		Assert.assertTrue(nfa.process((Character)null));
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('a');
		Assert.assertFalse(nfa.isAccept());
		
		assertTransitionsEqual(nfa, 
				new MyTran(0, 1, 'a')
		);
	}
	
	@Test
	public void testBasic1Auto() {
		final String regexp = "a";
		assertAccepts(regexp, "a");
		assertRejects(regexp, "b", "aa", "aaa");
	}
	
	
	@Test
	public void testBasic2() {
		RegularExpression<Character> re = new MyRegularExpression("$");
		
		NondeterministicFiniteAutomaton<Character> nfa =
			new NfaBuilder<Character>().build(re);
		
		Assert.assertEquals(2, nfa.getStates().size());
		
		Assert.assertTrue(nfa.isAccept());
		
		Assert.assertFalse(nfa.process('a'));
		Assert.assertFalse(nfa.isAccept());
		
		Assert.assertTrue(nfa.process((Character) null));
		Assert.assertFalse(nfa.isAccept());
		
		
		nfa.reset();
		
		Assert.assertTrue(nfa.isAccept());
		
		Assert.assertTrue(nfa.process((Character) null));
		Assert.assertTrue(nfa.isAccept());
		
		assertTransitionsEqual(nfa, 
				new MyTran(0, 1, null)
		);
	}
	
	@Test
	public void testBasic3() {
		final String regexStr = "ab";
		RegularExpression<Character> re = new MyRegularExpression(regexStr);
		
		NondeterministicFiniteAutomaton<Character> nfa =
			new NfaBuilder<Character>().build(re);
		
		Assert.assertEquals(4, nfa.getStates().size());
		
		System.out.println(ArrayToStringUtils.toString(
				nfa.getTransitions().toArray(), "\n"));
		
		Assert.assertFalse(nfa.isAccept());
		
		assertTransitionsEqual(nfa, 
				new MyTran(0, 1, 'a'),
				new MyTran(2, 3, 'b'),
				new MyTran(1, 2, null)
		);
		
		System.out.println("Start state: "+nfa.getStartState());
		
		Assert.assertEquals(nfa.getState(0), nfa.getStartState());
		
		Assert.assertFalse(nfa.getState(0).isAccept());
		Assert.assertFalse(nfa.getState(1).isAccept());
		Assert.assertFalse(nfa.getState(2).isAccept());
		Assert.assertTrue(nfa.getState(3).isAccept());
		
		assertAccepts(regexStr, "ab");
		assertRejects(regexStr, "ba", "a", "b", "aa");
	}
	
	@Test
	public void testBasic4() {
		final String regexStr = "abc";
		RegularExpression<Character> re = new MyRegularExpression(regexStr);
		
		NondeterministicFiniteAutomaton<Character> nfa =
			new NfaBuilder<Character>().build(re);
		
		Assert.assertEquals(6, nfa.getStates().size());
		
		System.out.println(ArrayToStringUtils.toString(
				nfa.getTransitions().toArray(), "\n"));
		
		Assert.assertFalse(nfa.isAccept());
		
		assertTransitionsEqual(nfa, 
				new MyTran(0, 1, 'a'),
				new MyTran(2, 3, 'b'),
				new MyTran(1, 2, null),
				new MyTran(3, 4, null),
				new MyTran(4, 5, 'c')
		);
	}
	
	@Test
	public void testBasic5() {
		final String regexStr = "a|b";
		RegularExpression<Character> re = new MyRegularExpression(regexStr);
		
		NondeterministicFiniteAutomaton<Character> nfa =
			new NfaBuilder<Character>().build(re);
		
		Assert.assertEquals(6, nfa.getStates().size());
		
		System.out.println(ArrayToStringUtils.toString(
				nfa.getTransitions().toArray(), "\n"));
		
		Assert.assertFalse(nfa.isAccept());
		
		assertTransitionsEqual(nfa, 
				new MyTran(0, 1, 'a'),
				new MyTran(2, 3, 'b'),
				new MyTran(4, 0, null),
				new MyTran(4, 2, null),
				new MyTran(1, 5, null),
				new MyTran(3, 5, null)
		);
		
		for (int i = 0; i < 5; ++i) {
			System.out.println("Assert !isAccept "+i);
			Assert.assertFalse(nfa.getState(i).isAccept());
		}
		Assert.assertTrue(nfa.getState(5).isAccept());
		
		System.out.println("Start state: "+nfa.getStartState());
		Assert.assertEquals(nfa.getState(4), nfa.getStartState());
		
		assertAccepts(regexStr, "a", "b");
		assertRejects(regexStr, "aa", "ab", "bb", "aaba", "");
	}
	
	@Test
	public void testBasic6() {
		final String regexStr = "a*";
		RegularExpression<Character> re = new MyRegularExpression(regexStr);
		
		NondeterministicFiniteAutomaton<Character> nfa =
			new NfaBuilder<Character>().build(re);
		
		System.out.println(ArrayToStringUtils.toString(
				nfa.getTransitions().toArray(), "\n"));
		
		assertTransitionsEqual(nfa, 
				new MyTran(0, 1, 'a'),
				new MyTran(2, 0, null),
				new MyTran(2, 3, null),
				new MyTran(1, 0, null),
				new MyTran(1, 3, null)
		);
		
		for (int i = 0; i < 3; ++i) {
			System.out.println("Assert !isAccept "+i);
			Assert.assertFalse(nfa.getState(i).isAccept());
		}
		Assert.assertTrue(nfa.getState(3).isAccept());
		
		Assert.assertEquals(nfa.getState(2), nfa.getStartState());
		
		assertAccepts(regexStr, "a", "aa", "aaa", "");
		assertRejects(regexStr, "b", "ba", "baaa", "ab", "aab");
	}
	
	@Test
	public void testBasic7() {
		final String regexStr = "a*b";
		RegularExpression<Character> re = new MyRegularExpression(regexStr);
		
		NondeterministicFiniteAutomaton<Character> nfa =
			new NfaBuilder<Character>().build(re);
		
		System.out.println(ArrayToStringUtils.toString(
				nfa.getTransitions().toArray(), "\n"));
		
		assertTransitionsContained(nfa, 
				new MyTran(0, 1, 'a'),
				new MyTran(2, 0, null),
				new MyTran(2, 3, null),
				new MyTran(1, 0, null),
				new MyTran(1, 3, null),
				new MyTran(4, 5, 'b'),
				new MyTran(3, 4, null)
		);
		
		for (int i = 0; i < 5; ++i) {
			System.out.println("Assert !isAccept "+i);
			Assert.assertFalse(nfa.getState(i).isAccept());
		}
		Assert.assertTrue(nfa.getState(5).isAccept());
		
		Assert.assertEquals(nfa.getState(2), nfa.getStartState());
		
		assertAccepts(regexStr, "ab", "aab", "aaab", "b");
		assertRejects(regexStr, "ba", "abb", "", "aaba", "aabaa");
	}
	
	@Test
	public void testSimple1() {
		// izgradimo regularni izraz "a|b"
		// (mozemo imat referencu tipa RegularExpression jer
		// MyRegularExpression implementira to sucelje)
		RegularExpression<Character> re = new MyRegularExpression("(a|b)*");
		
		// izgradimo automat na temelju tog regularnog izraza
		NondeterministicFiniteAutomaton<Character> nfa =
			new NfaBuilder<Character>().build(re);
		
		// damo automatu da procesira niz znakova "abababa"
		boolean ok = nfa.process(toList("abababa"));
		//provjerimo da li prihvaca taj niz
		Assert.assertTrue(nfa.isAccept());
		// provjerimo da li je automat u postojecem stanju (tj. nije izletio)
		// zbog nepostojeceg prijelaza
		Assert.assertTrue(ok);
		
		// sada jos dodamo znak 'c', 
		nfa.process('c');
		// sad automat nebi trebao prihvacati
		Assert.assertFalse(nfa.isAccept());
	}
	
	@Test
	public void testSimple2() {
		// izgradimo regularni izraz "ab"
		RegularExpression<Character> re = new MyRegularExpression("ab");
		
		NfaBuilder<Character> nfaBuilder = new NfaBuilder<Character>();
		
		// izgradimo automat na temelju tog regularnog izraza
		NondeterministicFiniteAutomaton<Character> nfa = nfaBuilder.build(re);
		
		// automat nebi smio prihvacati prazan niz
		Assert.assertFalse(nfa.isAccept());
		
		// automat nesmije izletiti nakon ucitanog "ab"
		Assert.assertTrue(nfa.process(toList("ab")));
		//takodjer bi trebao prihvacati
		Assert.assertTrue(nfa.isAccept());
		
		// znamo da je automat morao uci u nepostojece stanje
		// jer nema prijelaza za ucitani znak 'c' (nakon sto se ucita ab).
		Assert.assertFalse(nfa.process('c'));
		// automat tada nesmije prihvacati
		Assert.assertFalse(nfa.isAccept());
		
		// izgradimo novi automat koji prihvaca isti regularni izraz
		nfa = nfaBuilder.build(re);
	}
	
	@Test
	public void testSimple3() {
		// izgradimo automat koji prihvaca reg. izraz "a+b" pomocu metode
		NondeterministicFiniteAutomaton<Character> nfa =
			createNfa("(a|b)(a|b)*");
		
		// automat nebi smio prihvacati prazan niz (dok nijedan znak nije ucitan)
		Assert.assertFalse(nfa.isAccept());
		
		// ako procesiramo 'b', takodjer bi trebao prihvacati
		nfa.process('b');
		Assert.assertTrue(nfa.isAccept());
		
		// ako jos dodamo 'a', morao bi prihvcati jer prihvaca niz "ab"
		nfa.process('a');
		Assert.assertTrue(nfa.isAccept());
		
		// idemo jos vidjet jel ce dobro reci ako ucitamo jos 5 b-ova
		nfa.process(toList("bbbbb"));
		Assert.assertTrue(nfa.isAccept());
		
		// ako sad dodamo npr. c, onda automat nebi trebao prihvacati
		nfa.process('c');
		Assert.assertFalse(nfa.isAccept());
		
		// posto je automat "izletio" u nepostojece stanje
		// vise se nebi smio vratiti u neko postojece
		// tj. dalje ne bi smio prihvacati nista
		// pa idemo vidjeti...
		
		nfa.process('a');
		Assert.assertFalse(nfa.isAccept());
		nfa.process('b');
		Assert.assertFalse(nfa.isAccept());
		nfa.process(toList("abbaaab"));
		Assert.assertFalse(nfa.isAccept());
		
		// ok, idemo sad resetirat automat pa vidjet dal onda prihvaca
		// (to znaci da ode u pocetno stanje)
		nfa.reset();
		
		// sad bi automat ponovno trebao prihvacati
		Assert.assertFalse(nfa.isAccept());
		
		// cak iako dodamo jos b
		nfa.process('b');
		Assert.assertTrue(nfa.isAccept());
		
		// ili npr. da vidimo jel radi dobro ako jos dodamo "aab"
		nfa.process(toList("aab"));
		Assert.assertTrue(nfa.isAccept());
		
		// da vidimo da li jednom kad se resetira, automat ponovno
		// moze uci u nepostojece stanje
		
		// provjerimo da li je automat izletio
		Assert.assertFalse(nfa.process('c'));
		
		// ako je izletio, onda nebi smio prihvacati
		Assert.assertFalse(nfa.isAccept());
	}
	
	// AUTOMATSKI TESTOVI

	/*
	 * Automatske testove nemoj koristiti dok ne napises par finijih testove
	 * poput gornjih koji testiraju "postepeno" kako dolaze znakovi
	 * (npr. process('a')... assert...process('b')...assert)
	 */
	
	@Test
	public void testAutomaticSimple1() {
		// "a|b" regularni izraz bi trebao prihvacati ove ulaze
		assertAccepts("(a|b)(a|b)*", 
				"a", "aa", "aaaa", "b", "ba", "bbb", "bababaa");
		// a ne bi smio prihvacati ove ulaze
		assertRejects("a|b", 
				"c", "abc", "", "ca", "aababbaaac", "ababcabaa");
	}
	
	@Test
	public void testAutomaticSimple2() {
		String s = "\"(a|b|c)*\"";
		assertAccepts(s, "\"abc\""); 
	}
	
	@Test
	public void testAutomaticSimple3() {
		String sveOsimDvostrukogNavodnikaINovogReda
		= "\\(|\\)|{|}|\\||\\*|\\\\|\\$|\\t|\\_|!|#|%|&|'|+|,|-|.|/|0|1|2|3|4|5|6|7|8|9|:|;|<|=|>|?|@|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|[|]|^|_|`|a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|~";
		String s = "\"(" + sveOsimDvostrukogNavodnikaINovogReda + "|\\\\\")*\"";
		assertAccepts(s, "\"abc\\\"d\"");
	}
	
	@Test
	public void testAutomaticComplex1() {
		final String regexp = "((a|b)*caa*|b)*(b|a)|ab|d";
		// FIXME na "ca" pada jer ne moze biti ca (tvoj test pa ga nisam provjeravao) :)
		assertAccepts(regexp, 
				"d", "aaacaab", "cab"); 
		assertRejects(regexp, 
				"c", "da", "", "ccd", "caccad");
	}
	
	@Test
	public void testSimple4() {
		NondeterministicFiniteAutomaton<Character> nfa =
			createNfa("(a|b|c)(a|b|c)*");
		
		Assert.assertFalse(nfa.isAccept());
		
		nfa.process('b');
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('c');
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('a');
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process(toList("bbbbb"));
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process(toList("cca"));
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('d');
		Assert.assertFalse(nfa.isAccept());
		
		nfa.process('a');
		Assert.assertFalse(nfa.isAccept());
		nfa.process('b');
		Assert.assertFalse(nfa.isAccept());
		nfa.process(toList("abbaaabcaa"));
		Assert.assertFalse(nfa.isAccept());
		
		nfa.reset();
		
		Assert.assertFalse(nfa.isAccept());
		
		nfa.process('d');
		Assert.assertFalse(nfa.isAccept());
		
		nfa.reset();
		nfa.process('a');
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process(toList("bac"));
		Assert.assertTrue(nfa.isAccept());

		Assert.assertFalse(nfa.process('d'));
		
		Assert.assertFalse(nfa.isAccept());
	}

	@Test
	public void testSimple5() {
		NondeterministicFiniteAutomaton<Character> nfa =
			createNfa("(a|bc*)*");
		
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('b');
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process(toList("bccc"));
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('a');
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process(toList("bbbbb"));
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process(toList("aaabc"));
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('d');
		Assert.assertFalse(nfa.isAccept());
		
		nfa.process('a');
		Assert.assertFalse(nfa.isAccept());
		nfa.process(toList("abccc"));
		Assert.assertFalse(nfa.isAccept());
		nfa.process(toList("abbaaabcaa"));
		Assert.assertFalse(nfa.isAccept());
		
		nfa.reset();
		
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('a');
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('b');
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process(toList("baabccc"));
		Assert.assertTrue(nfa.isAccept());

		Assert.assertFalse(nfa.process('d'));
		
		Assert.assertFalse(nfa.isAccept());
		
		Assert.assertFalse(nfa.process(toList("bcccbaaaa")));
		
		Assert.assertFalse(nfa.isAccept());	
	}
	
	@Test
	public void testSimple6() {
		NondeterministicFiniteAutomaton<Character> nfa =
			createNfa("((a|b)*|(cd)*)*");
		
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('b');
		Assert.assertTrue(nfa.isAccept());
		
		// FIXME tu valjda fali reset?
		
		nfa.process(toList("cdcd"));
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('a');
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process(toList("bbbacd"));
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process(toList("aaabcdcd"));
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('d');
		Assert.assertFalse(nfa.isAccept());
		
		nfa.process('a');
		Assert.assertFalse(nfa.isAccept());
		nfa.process(toList("abcd"));
		Assert.assertFalse(nfa.isAccept());
		nfa.process(toList("dcda"));
		Assert.assertFalse(nfa.isAccept());
		
		nfa.reset();
		
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('a');
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process('b');
		Assert.assertTrue(nfa.isAccept());
		
		nfa.process(toList("cdcdabaaa"));
		Assert.assertTrue(nfa.isAccept());

		Assert.assertFalse(nfa.process('e'));
		
		Assert.assertFalse(nfa.isAccept());
		
		Assert.assertFalse(nfa.process(toList("dcabbbbe")));
		
		Assert.assertFalse(nfa.isAccept());	
	}
	
	@Test
	public void testAutomaticComplex2() {
		final String regexp = "(aa|ab(bb)*ba)*(b|ab(bb)*a)(a(bb)*a|(b|a(bb)*ba)(aa|ab(bb)*ba)*(b|ab(bb)*a))*";
		
		// FIXME na predzadnjem pada tj. "aaaaaaaaaa" da pada jer mora biti bar jedan b (popravljeno)
		assertAccepts(regexp, 
				"b", "aba", "aab","abbaabbba","aabaaaabbaabb","aaaaaaaaaaaaaabbbabba","bbbbbbb","aaaaaaaaaab","abababababababa");
		assertRejects(regexp, 
				"", "c", "a", "aabb", "d", "aaaaaaaaaaaabbbb","bb","abbbaab","aaabbbbbaabbaa");
	}
	
	@Test
	public void testAutomaticComplex3() {
		final String regexp = "aa*";
		assertAccepts(regexp, 
				"a","aaaa","aaaaaaaaaaa");
		assertRejects(regexp, 
				"","b","c","d","aaab","ab","aaaaaaaaac","abcd");
	}
	@Test
	public void testAutomaticComplex4() {
		// da ne copy/pasteamo i omogucimo lakse modifikacije, uvodimo konstantu
		// (final se moze izostaviti ali je preporucljiv jer je "read-only")
		final String regexp = "bb*a*";
		// "a|b" regularni izraz bi trebao prihvacati ove ulaze
		assertAccepts(regexp, 
				"b","ba","bbbbbbbaa","baaaaaaa","bbbbbbaaaaaaa");
		// a ne bi smio prihvacati ove ulaze
		assertRejects(regexp, 
				"","a","bab","c","d","bbbbbab","bbbbaaaaabbb","bc","bd","bac","bad");
	}
	@Test
	public void testAutomaticComplex5() {
		// da ne copy/pasteamo i omogucimo lakse modifikacije, uvodimo konstantu
		// (final se moze izostaviti ali je preporucljiv jer je "read-only")
		final String regexp = "cc*bb*aa*";
		// "a|b" regularni izraz bi trebao prihvacati ove ulaze
		assertAccepts(regexp, 
				"cba","ccba","ccbbaa","cbba","cbbbbaa","ccccba","cbaaaaa","ccccbbbbbaaaa");
		// a ne bi smio prihvacati ove ulaze
		assertRejects(regexp, 
				"","c","cb","cbb","ccbb","cbac","cbab","a","b","cccccccbbbbbbbaaaaacba");
	}
	@Test
	public void testAutomaticComplex6() {
		// da ne copy/pasteamo i omogucimo lakse modifikacije, uvodimo konstantu
		// (final se moze izostaviti ali je preporucljiv jer je "read-only")
		final String regexp = "a*";
		// "a|b" regularni izraz bi trebao prihvacati ove ulaze
		assertAccepts(regexp, 
				"","a","aa","aaaaaa");
		// a ne bi smio prihvacati ove ulaze
		assertRejects(regexp, 
				"b","c","d","ab","ac","ad","aaaaaaaac","aaaaab","abcd");
	}
	@Test
	public void testAutomaticComplex7() {
		// da ne copy/pasteamo i omogucimo lakse modifikacije, uvodimo konstantu
		// (final se moze izostaviti ali je preporucljiv jer je "read-only")
		final String regexp = "b*";
		// "a|b" regularni izraz bi trebao prihvacati ove ulaze
		assertAccepts(regexp, 
				"","b","bb","bbbbbbb");
		// a ne bi smio prihvacati ove ulaze
		assertRejects(regexp, 
				"a","ba","c","d","bbbbba","bbbbc","bc","bd","bac","bad");
	}
	@Test
	public void testAutomaticComplex8() {
		// da ne copy/pasteamo i omogucimo lakse modifikacije, uvodimo konstantu
		// (final se moze izostaviti ali je preporucljiv jer je "read-only")
		final String regexp = "c*";
		// "a|b" regularni izraz bi trebao prihvacati ove ulaze
		assertAccepts(regexp, 
				"","c","cc","cccccc");
		// a ne bi smio prihvacati ove ulaze
		assertRejects(regexp, 
				"a","b","ca","d","cb","cd","ccccccca","cccccccccb","cabd","cccccccd");
	}
	@Test
	public void testAutomaticComplex9() {
		// da ne copy/pasteamo i omogucimo lakse modifikacije, uvodimo konstantu
		// (final se moze izostaviti ali je preporucljiv jer je "read-only")
		final String regexp = "(a|b|c)*d";
		// "a|b" regularni izraz bi trebao prihvacati ove ulaze
		assertAccepts(regexp, 
				"d","ad","bd","cd","abd","acd","aaacd","bacd","bbbbcd","bbbd","cad","ccccad");
		// a ne bi smio prihvacati ove ulaze
		assertRejects(regexp, 
				"","a","b","c","ada","adb","adc","bda","bdb","bdc","cda","cdb","cdc","aaaaabbbccccc");
	}
	
	@Test
	public void testAutomaticComplex10() {
		final String regexp = "a(b|c)";
		assertAccepts(regexp, "ab", "ac");
		assertRejects(regexp, "", "a", "b", "abb", "abc", "acc", "aaa");
	}
	
	@Test
	public void testAutomaticComplex11() {
		final String regexp = "a(b*|c)";
		assertAccepts(regexp, "ab", "ac", "abb", "abbbb", "a");
		assertRejects(regexp, "", "b", "abba", "abc", "acc", "aaa");
	}
	
	@Test
	public void testAutomaticComplex12() {
		final String regexp = "a*b";
		
		NondeterministicFiniteAutomaton<Character> nfa = new NfaBuilder<Character>().build(
				new MyRegularExpression(regexp));
		System.out.println(ArrayToStringUtils.toString(
				nfa.getTransitions().toArray(), "\n"));
		
		assertAccepts(regexp, "ab", "aaab", "ab", "b");
		assertRejects(regexp, "", "a", "aaba", "aabb", "bb", "dab");
	}
	
	@Test
	public void testAutomaticComplex13() {
		final String regexp = "a(bb*|c)c";
		assertAccepts(regexp, "abc", "acc", "abbc", "abbbbc");
		assertRejects(regexp, "", "b", "abba", "ac", "acb", "aaa","a","c");
	}
	
	@Test
	public void testAutomaticComplex14() {
		final String regexp = "(a|(b|c)*d|b)";
		assertAccepts(regexp, "a", "bd", "cd", "bcd", "bbbd", "bbbbbccbcccd","b","ccccbbd","cccccd","d");
		assertRejects(regexp, "", "ac", "abba", "abc", "acc", "aaa","db","dc","ab");
	}
	@Test
	public void testAutomaticComplex15() {
		final String regexp = "(a|(b|c)*d|b)((cd)*|ab*d|a*)*";
		assertAccepts(regexp, "a", "bd", "cd", "bcd", "bbbd", "bbbbbccbcccd","b","ccccbbd","cccccd","d","acdcdaaa","bad","cccccdabbbbd");
		assertRejects(regexp, "", "ac", "abba", "abc", "acc","db","dc","ab","acdd","bcdbd");
	}
	@Test
	public void testAutomaticComplex16() {
		final String regexp = "(a(a|bd)*|c*daa*|((b|a)*|cc*|d*b))";
		assertAccepts(regexp, "a", "abd", "aaabda", "cda", "da", "ccccdaaaaa","abdbdbdaaa","","b","bbbbaaaa","bababab","c","cccc","db","ddddddb");
		assertRejects(regexp, "ac", "abbac", "abc", "acc", "aaad","dbb","dc","cd");
	}
	
	/**
	 * Pretvara string u listu znakova.
	 * 
	 * @param s niz znakova (string)
	 * @return lista znakova
	 */
	static List<Character> toList(String s) {
		List<Character> list = new ArrayList<Character>();
		for (int i = 0; i < s.length(); ++i)
			list.add(s.charAt(i));
		return list;
	}
	
	/**
	 * Resetira automat i zatim procesira zadani niz znakova.
	 * 
	 * @param nfa automat
	 * @param s niz znakova koji se procesira
	 * @return <code>true</code> ako automat nije izletio, <code>false</code>
	 * inace.
	 */
	static boolean resetAndProcess(
			NondeterministicFiniteAutomaton<Character> nfa,
			String s) {
		nfa.reset();
		return nfa.process(toList(s));
	}
	
	/**
	 * Convenient wrapper metoda za stvaranje automata na temelju
	 * regularnog izraza.
	 * @param regexp regularni izraz
	 * @return automat koji prihvaca taj regularni izraz
	 */
	static NondeterministicFiniteAutomaton<Character> createNfa(String regexp) 
	{
		return new NfaBuilder<Character>().build(
				new MyRegularExpression(regexp));
	}
	
	/**
	 * Provjerava da li automat koji prihvaca regularni izraz
	 * <tt>regexp</tt> prihvaca zadane nizove. Nakon svakog
	 * niza automat se resetira tj. krece iz pocetnog stanja.
	 * @param regexp regularni izraz
	 * @param inputs nizovi ulaznih znakova koje bi automat
	 * trebao prihvacati
	 */
	static void assertAccepts(String regexp, String... inputs) {
		assertAcceptsOrRejects(regexp, true, inputs);
	}
	
	/**
	 * Provjerava da li automat koji prihvaca regularni izraz
	 * <tt>regexp</tt> ne prihvaca zadane nizove. Nakon svakog
	 * niza automat se resetira tj. krece iz pocetnog stanja.
	 * @param regexp regularni izraz
	 * @param inputs nizovi ulaznih znakova koje bi automat
	 * trebao prihvacati
	 */
	static void assertRejects(String regexp, String... inputs) {
		assertAcceptsOrRejects(regexp, false, inputs);
	}
	
	static void assertTransitionsEqual(
			NondeterministicFiniteAutomaton<Character> nfa,
			MyTran... myTrans) {
		assertTransitionsContained(nfa, myTrans);
		Assert.assertEquals(myTrans.length, 
				nfa.getTransitions().size());
	}
	
	static void assertTransitionsContained(
			NondeterministicFiniteAutomaton<Character> nfa,
			MyTran... myTrans) {
		Set<Transition<Character>> transitions = 
			new HashSet<Transition<Character>>(nfa.getTransitions());
		
		for (MyTran t : myTrans) {
			System.out.println("Contains: "+t+" ?");
			Assert.assertTrue(transitions.contains(t.toTransition()));
		}
	}
	
	// ovo ne pozivaj direktno, bolje ti je ove wrappere gore pozivati
	// jer je tada jasniji kod
	private static void assertAcceptsOrRejects(String regexp, boolean accepts, 
			String... inputs) {
		NondeterministicFiniteAutomaton<Character> nfa = createNfa(regexp);
		for (String s : inputs) {
			System.out.println("Testing regexp: "+s);
			nfa.process(toList(s));
			Assert.assertFalse(accepts ^ nfa.isAccept());
			nfa.reset();
		}
	}
	
	static class MyRegularExpression extends DefaultRegularExpression {

		protected MyRegularExpression(String regexp) {
			super(regexp, DefaultRegularExpressionEscaper.getInstance());
		}
	}
	
	static class MyTran extends Tuple3<Integer, Integer, Character> {
		public MyTran(Integer from, Integer to, Character symbol) {
			super(from, to, symbol);
		}
		
		static MyTran fromTransition(Transition<Character> t) {
			return new MyTran(t.getFrom().getId(), t.getTo().getId(), 
					t.getSymbol());
		}
		
		Transition<Character> toTransition() {
			return new Transition<Character>(
					new State(first, false), 
					new State(second, false), 
					third);
		}
		
		@Override
		public String toString() {
			return "(" + first + " " + second + " " + third + ")";
		}
	}
	
	public static <T> void printTransitions(NondeterministicFiniteAutomaton<T> nfa) {
		System.out.println(ArrayToStringUtils.toString(
				nfa.getTransitions().toArray(), "\n"));
	}
}
