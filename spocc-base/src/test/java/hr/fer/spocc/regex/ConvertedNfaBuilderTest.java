/*
 * ConvertedNfaBuilderTest.java
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

import hr.fer.spocc.automaton.fsm.DeterministicFiniteAutomaton;
import hr.fer.spocc.automaton.fsm.FiniteStateMachines;
import hr.fer.spocc.automaton.fsm.NondeterministicFiniteAutomaton;
import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.sglj.util.ArrayToStringUtils;

/*
 * @author Leo Osvald
 *
 */
public class ConvertedNfaBuilderTest extends NfaBuilderTest {

	@Test
	@Ignore
	public void testManual() {
		DeterministicFiniteAutomaton<Character> dfa = createDfa("ab");
		System.out.println(dfa);
		System.out.println(dfa.getStates());
		
		dfa.reset();
		System.out.println(dfa.getCurrentState());
		Assert.assertEquals(0, dfa.getCurrentState().getId());
		Assert.assertTrue(dfa.process('a'));
		Assert.assertEquals(1, dfa.getCurrentState().getId());
		Assert.assertTrue(dfa.process('b'));
	}
	
	@Test
	public void testBasic3() {
		final String regexStr = "ab";
		
		assertAccepts(regexStr, "ab");
		assertRejects(regexStr, "ba", "a", "b", "aa");
	}
	
	@Test
	public void testBasic5() {
		final String regexStr = "a|b";
		
		assertAccepts(regexStr, "a", "b");
		assertRejects(regexStr, "aa", "ab", "bb", "aaba", "");
	}
	
	@Test
	public void testBasic6() {
		final String regexStr = "a*";
		
		assertAccepts(regexStr, "a", "aa", "aaa", "");
		assertRejects(regexStr, "b", "ba", "baaa", "ab", "aab");
	}
	
	@Test
	public void testBasic7() {
		final String regexStr = "a*b";
		
		assertAccepts(regexStr, "ab", "aab", "aaab", "b");
		assertRejects(regexStr, "ba", "abb", "", "aaba", "aabaa");
	}
	
	@Test
	public void testSimple1() {
		RegularExpression<Character> re = new MyRegularExpression("(a|b)*");
		DeterministicFiniteAutomaton<Character> dfa
		= createDfa("(a|b)*");
		
		// damo automatu da procesira niz znakova "abababa"
		boolean ok = dfa.process(toList("abababa"));
		//provjerimo da li prihvaca taj niz
		Assert.assertTrue(dfa.isAccept());
		// provjerimo da li je automat u postojecem stanju (tj. nije izletio)
		// zbog nepostojeceg prijelaza
		Assert.assertTrue(ok);
		
		// sada jos dodamo znak 'c', 
		dfa.process('c');
		// sad automat nebi trebao prihvacati
		Assert.assertFalse(dfa.isAccept());
	}
	
	@Test
	public void testSimple2() {
		// izgradimo regularni izraz "ab"
		DeterministicFiniteAutomaton<Character> dfa = createDfa("ab");
		
		// automat nebi smio prihvacati prazan niz
		Assert.assertFalse(dfa.isAccept());
		
		// automat nesmije izletiti nakon ucitanog "ab"
		Assert.assertTrue(dfa.process(toList("ab")));
		//takodjer bi trebao prihvacati
		Assert.assertTrue(dfa.isAccept());
		
		// znamo da je automat morao uci u nepostojece stanje
		// jer nema prijelaza za ucitani znak 'c' (nakon sto se ucita ab).
		Assert.assertFalse(dfa.process('c'));
		// automat tada nesmije prihvacati
		Assert.assertFalse(dfa.isAccept());
	}
	
	@Test
	public void testSimple3() {
		// izgradimo automat koji prihvaca reg. izraz "a+b" pomocu metode
		DeterministicFiniteAutomaton<Character> dfa =
			createDfa("(a|b)(a|b)*");
		
		// automat nebi smio prihvacati prazan niz (dok nijedan znak nije ucitan)
		Assert.assertFalse(dfa.isAccept());
		
		// ako procesiramo 'b', takodjer bi trebao prihvacati
		dfa.process('b');
		Assert.assertTrue(dfa.isAccept());
		
		// ako jos dodamo 'a', morao bi prihvcati jer prihvaca niz "ab"
		dfa.process('a');
		Assert.assertTrue(dfa.isAccept());
		
		// idemo jos vidjet jel ce dobro reci ako ucitamo jos 5 b-ova
		dfa.process(toList("bbbbb"));
		Assert.assertTrue(dfa.isAccept());
		
		// ako sad dodamo npr. c, onda automat nebi trebao prihvacati
		dfa.process('c');
		Assert.assertFalse(dfa.isAccept());
		
		// posto je automat "izletio" u nepostojece stanje
		// vise se nebi smio vratiti u neko postojece
		// tj. dalje ne bi smio prihvacati nista
		// pa idemo vidjeti...
		
		dfa.process('a');
		Assert.assertFalse(dfa.isAccept());
		dfa.process('b');
		Assert.assertFalse(dfa.isAccept());
		dfa.process(toList("abbaaab"));
		Assert.assertFalse(dfa.isAccept());
		
		// ok, idemo sad resetirat automat pa vidjet dal onda prihvaca
		// (to znaci da ode u pocetno stanje)
		dfa.reset();
		
		// sad bi automat ponovno trebao prihvacati
		Assert.assertFalse(dfa.isAccept());
		
		// cak iako dodamo jos b
		dfa.process('b');
		Assert.assertTrue(dfa.isAccept());
		
		// ili npr. da vidimo jel radi dobro ako jos dodamo "aab"
		dfa.process(toList("aab"));
		Assert.assertTrue(dfa.isAccept());
		
		// da vidimo da li jednom kad se resetira, automat ponovno
		// moze uci u nepostojece stanje
		
		// provjerimo da li je automat izletio
		Assert.assertFalse(dfa.process('c'));
		
		// ako je izletio, onda nebi smio prihvacati
		Assert.assertFalse(dfa.isAccept());
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
		DeterministicFiniteAutomaton<Character> dfa =
			createDfa("(a|b|c)(a|b|c)*");
		
		Assert.assertFalse(dfa.isAccept());
		
		dfa.process('b');
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('c');
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('a');
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process(toList("bbbbb"));
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process(toList("cca"));
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('d');
		Assert.assertFalse(dfa.isAccept());
		
		dfa.process('a');
		Assert.assertFalse(dfa.isAccept());
		dfa.process('b');
		Assert.assertFalse(dfa.isAccept());
		dfa.process(toList("abbaaabcaa"));
		Assert.assertFalse(dfa.isAccept());
		
		dfa.reset();
		
		Assert.assertFalse(dfa.isAccept());
		
		dfa.process('d');
		Assert.assertFalse(dfa.isAccept());
		
		dfa.reset();
		dfa.process('a');
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process(toList("bac"));
		Assert.assertTrue(dfa.isAccept());

		Assert.assertFalse(dfa.process('d'));
		
		Assert.assertFalse(dfa.isAccept());
	}

	@Test
	public void testSimple5() {
		DeterministicFiniteAutomaton<Character> dfa =
			createDfa("(a|bc*)*");
		
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('b');
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process(toList("bccc"));
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('a');
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process(toList("bbbbb"));
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process(toList("aaabc"));
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('d');
		Assert.assertFalse(dfa.isAccept());
		
		dfa.process('a');
		Assert.assertFalse(dfa.isAccept());
		dfa.process(toList("abccc"));
		Assert.assertFalse(dfa.isAccept());
		dfa.process(toList("abbaaabcaa"));
		Assert.assertFalse(dfa.isAccept());
		
		dfa.reset();
		
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('a');
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('b');
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process(toList("baabccc"));
		Assert.assertTrue(dfa.isAccept());

		Assert.assertFalse(dfa.process('d'));
		
		Assert.assertFalse(dfa.isAccept());
		
		Assert.assertFalse(dfa.process(toList("bcccbaaaa")));
		
		Assert.assertFalse(dfa.isAccept());	
	}
	
	@Test
	public void testSimple6() {
		DeterministicFiniteAutomaton<Character> dfa =
			createDfa("((a|b)*|(cd)*)*");
		
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('b');
		Assert.assertTrue(dfa.isAccept());
		
		// FIXME tu valjda fali reset?
		
		dfa.process(toList("cdcd"));
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('a');
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process(toList("bbbacd"));
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process(toList("aaabcdcd"));
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('d');
		Assert.assertFalse(dfa.isAccept());
		
		dfa.process('a');
		Assert.assertFalse(dfa.isAccept());
		dfa.process(toList("abcd"));
		Assert.assertFalse(dfa.isAccept());
		dfa.process(toList("dcda"));
		Assert.assertFalse(dfa.isAccept());
		
		dfa.reset();
		
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('a');
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process('b');
		Assert.assertTrue(dfa.isAccept());
		
		dfa.process(toList("cdcdabaaa"));
		Assert.assertTrue(dfa.isAccept());

		Assert.assertFalse(dfa.process('e'));
		
		Assert.assertFalse(dfa.isAccept());
		
		Assert.assertFalse(dfa.process(toList("dcabbbbe")));
		
		Assert.assertFalse(dfa.isAccept());	
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
	
	static DeterministicFiniteAutomaton<Character> createDfa(String regexp) {
		return FiniteStateMachines.toDfa(createNfa(regexp));
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
	
	private static void assertAcceptsOrRejects(String regexp, boolean accepts, 
			String... inputs) {
		DeterministicFiniteAutomaton<Character> dfa = createDfa(regexp);
		for (String s : inputs) {
			System.out.println("Testing regexp: "+s);
			dfa.process(toList(s));
			Assert.assertFalse(accepts ^ dfa.isAccept());
			dfa.reset();
		}
	}
}
