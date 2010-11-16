/*
 * DefaultNondeterministicFiniteAutomatonTest.java
 *
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 * Copyright (C) 2010 Matko Raguz <m.raguz@gmail.com>
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

import hr.fer.spocc.automaton.fsm.FiniteStateMachine.Transition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.junit.Assert;
import org.junit.Test;

/*
 * @author Leo Osvald
 * @author Matko Raguz
 *
 */
public class DefaultNondeterministicFiniteAutomatonTest {

	@Test
	public void testAddState() {
		NondeterministicFiniteAutomaton<Character> nfa = createNfa();
		
		nfa.addState(new State(1, false));
		
		// provjeri da li se stanje zapamtilo pod id-em 1
		State state = nfa.getState(1);
		Assert.assertNotNull(state);
		
		// provjeri da li to zapamceno stanje stvarno ima id 1
		Assert.assertEquals(1, state.getId());
	}
	
	@Test
	public void testAddTransition1() {
		NondeterministicFiniteAutomaton<Character> nfa = createNfa();
		
		// dodaj dva stanja s id-evima 1 i 2
		nfa.addState(new State(1, false));
		nfa.addState(new State(2, false));
		
		// dodaj prijelaz izmedju ta dva stanja
		nfa.addTransition(1, 2, 'x');
		
		// vrati kolekciju svih prijelaza
		Collection<Transition<Character>> transitions = nfa.getTransitions();
		
		// testiraj da li kolekcija sadrzi tocno jedan prijelaz
		Assert.assertEquals(1, transitions.size());
		
		// testiraj da li kolekcija sadrzi pravi prijelaz
		Transition<Character>[] tranArray = toTransitionArray(transitions);
		Transition<Character> singleTransition = tranArray[0];
		State state1 = nfa.getState(1), state2 = nfa.getState(2);
		// assertSame ti testira da li se radi o istom objektu
		// (ekvivalentno sa operatorom ==)
		Assert.assertSame(state1, singleTransition.getFrom());
		Assert.assertSame(state2, singleTransition.getTo());
		// assertEquals testira da li su objekti (kopije) jednaki
		// a to se ustanovljuje pozivom .equals() metode
		Assert.assertEquals(Character.valueOf('x'), // mogli smo i castati 
				singleTransition.getSymbol());
		
		// Character je objekt koji sadrzi char
		// on moze biti null (to je nama epsilon prijelaz onda)
		
		// jos jednom test da li je prijelaz isti (na drugi nacin)
		// ovo za rezliku od proslog usporedjuje da li je prijelaz
		// jednak, a ne da li je isti (da li ima reference na ista stanja)
		checkTransition(nfa, 1, 2, 'x', singleTransition);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAddTransition2() {
		NondeterministicFiniteAutomaton<Character> nfa = createNfa();
		
		// dodaj dva stanja s id-evima 11, 22 i 33
		nfa.addState(new State(11, false));
		nfa.addState(new State(22, false));
		nfa.addState(new State(33, false));
		
		// dodaj prijelaze
		nfa.addTransition(11, 33, 'c');
		nfa.addTransition(22, 11, null);
		nfa.addTransition(22, 22, 'a');
		nfa.addTransition(11, 22, 'b');
		
		// testiraj da li sadrzi sve ove prijelaze automat
		checkTransitions(nfa, 
				createTransition(nfa, 11, 22, 'b'),
				createTransition(nfa, 22, 11, null),
				createTransition(nfa, 11, 33, 'c'),
				createTransition(nfa, 22, 22, 'a')
		);
	}
	
	@Test
	public void testNoStartStateProcess() {
		NondeterministicFiniteAutomaton<Character> nfa = createNfa();
		
		// dodaj dva stanja s id-evima 1 i 2 i povezi ih sa 'a'
		nfa.addState(new State(1, false));
		nfa.addState(new State(2, false));
		nfa.addTransition(1, 2, 'a');
		
		boolean ok = false;
		// probaj pokrenuti automat - daj mu na ulaz znak 'a'
		try {
			nfa.process('a'); // ocekujemo da ce tu puknuti program
			// naime u dokumentaciji ove metode pise da baca exception
			// ako nije definirano pocetno stanje a automat se pokrene
			// (Ctrl + left click na metodu pa procitaj)
		} catch (IllegalStateException e) {
			// ALI cekaj, pa uopce nismo definirali poceetno stanje
			// pa bi bilo logicno da automat baci nekakvu izniku i na 
			// taj nacin nas upozori da smo napravili logicku gresku
			ok = true;
			// da vidimo o cem se zapravo radi
		}
		
		Assert.assertTrue(ok); // ako je false onda nije doslo do exceptiona
		// a trebalo je - dakle ne valja
	}
	
	@Test
	public void testProcess1() {
		NondeterministicFiniteAutomaton<Character> nfa = createNfa();
		nfa.addState(new State(1, false), true); // ovo je pocetno stanje
		nfa.addState(new State(2, false));
		nfa.addState(new State(3, false));
		nfa.addState(new State(4, false));
		nfa.addState(new State(5, false));
		nfa.addTransition(1, 2, 'a');
		nfa.addTransition(1, 3, null); // ovo je epsilon prijelaz
		nfa.addTransition(3, 5, null); // ovo je epsilon prijelaz
		nfa.addTransition(5, 4, 'b');
		nfa.addTransition(4, 1, 'a');
		nfa.addTransition(2, 4, 'b');
		nfa.addTransition(2, 1, 'a');
		
		
		nfa.reset();
		
		// provjeri dal je 1 u skupu trenutnih stanja (mora bit jel je to pocetno stanje)
		Assert.assertTrue(nfa.getCurrentStates().contains(nfa.getState(1)));

		// posto je epsilon okolina od 1 skup {3, 5}, 
		// onda bi i ona trebala biti u skupu trenutnik
		Assert.assertTrue(nfa.getCurrentStates().contains(nfa.getState(3)));
		Assert.assertTrue(nfa.getCurrentStates().contains(nfa.getState(5)));
		
		// sad ucitaj znak 'a'
		nfa.process('a');
		
		// sad bi automat trebao biti u stanjima {2}
		
		nfa.process(toList("ab"));
		
		// restiraj automat - dovedi ga u pocetno stanje
		nfa.reset();
		
		// sad ucitaj niz "aaaa"
		nfa.process(toList("aba"));
		
		// automat bi trebao biti u {1, 3, 5}
		assertCurrentStates(nfa, 1, 3, 5);
	}
	
	@Test
	public void testInfiniteLoop() {
		// gradimo automat koji se stalno moze saltati izmedju stanja 1 i 2
		NondeterministicFiniteAutomaton<Character> nfa = createNfa();
		nfa.addState(new State(1, false), true); // ovo je pocetno stanje
		nfa.addState(new State(2, false));
		nfa.addState(new State(3, false));
		nfa.addTransition(1, 2, null);
		nfa.addTransition(2, 1, null);
		nfa.addTransition(1, 1, 'x');
		nfa.addTransition(2, 2, 'x');
		nfa.addTransition(2, 3, 'x');
		nfa.addTransition(3, 2, 'x');
		
		nfa.reset();
		
		assertCurrentStates(nfa, 1, 2); // skup pocetnih stanja je {1, 2}
		
		nfa.process('x'); // sad je neparan broj x-eva, znaci {1, 2, 3}
		assertCurrentStates(nfa, 1, 2, 3);
		
		nfa.process(toList("x"));
		assertCurrentStates(nfa, 1, 2, 3);
		
		nfa.process(toList("xxxx"));
		assertCurrentStates(nfa, 1, 2, 3);
		
		nfa.process('x');
		assertCurrentStates(nfa, 1, 3, 2); // nije bitan redoslijed
		
		nfa.process('f'); // ovo je nedefiniran prijelaz, pa je trenutni skup stanja {}
		assertCurrentStates(nfa); // predajemo prazan skup (nista)
		// mogli smo i Assert.assertTrue(nfa.getCurrentStates().isEmpty());
	}
	
	// NASTAVAK TESTOVA
	// OVDJE
	
	@Test
	public void test1() {
		NondeterministicFiniteAutomaton<Character> nfa = createNfa();
		
		// dodavanje stanja
		nfa.addState(new State(0, false));
		nfa.addState(new State(1, false));
		nfa.addState(new State(2, false));
		nfa.addState(new State(3, false));
		nfa.addState(new State(4, false));
		nfa.addState(new State(5, false));
		nfa.addState(new State(6, false));
		nfa.addState(new State(7, true));
		nfa.setStartState(0);
		
		
		// dodaj prijelaze
		nfa.addTransition(0, 1, 'a');
		nfa.addTransition(0, 4, 'b');
		nfa.addTransition(0, 4, 'c');
		nfa.addTransition(1, 4, 'a');
		nfa.addTransition(1, 4, 'c');
		nfa.addTransition(1, 2, 'b');
		nfa.addTransition(2, 3, 'c');
		nfa.addTransition(2, 4, 'b');
		nfa.addTransition(2, 4, 'a');
		nfa.addTransition(3, 4, 'b');
		nfa.addTransition(3, 4, 'c');
		nfa.addTransition(3, 5, 'a');
		nfa.addTransition(5, 6, 'a');
		nfa.addTransition(6, 7, 'b');
		nfa.addTransition(5, 4, 'c');
		nfa.addTransition(4, 4, 'c');
		nfa.addTransition(4, 4, 'a');
		nfa.addTransition(4, 4, 'b');
		nfa.addTransition(6, 4, 'c');
		nfa.addTransition(6, 4, 'a');
		nfa.addTransition(5, 2, 'b');
		
		nfa.reset();
		
		//provjera 1
		
		nfa.process('a');
		assertCurrentStates(nfa, 1);
		
		//provjera 2
		
		nfa.process('b');
		assertCurrentStates(nfa, 2);
		
		//provjera 3
		
		nfa.process('c');
		assertCurrentStates(nfa, 3);
		
		//provjera 4
		
		nfa.process('a');
		assertCurrentStates(nfa, 5);
		
		//provjera 5
		
		nfa.process('b');
		assertCurrentStates(nfa, 2);
		
		//provjera 6
		
		nfa.process('c');
		assertCurrentStates(nfa, 3);
		
		//provjera 7
		
		nfa.process('a');
		assertCurrentStates(nfa, 5);
		
		//provjera 8
		
		nfa.process('a');
		assertCurrentStates(nfa, 6);
		
		//provjera 9
		
		nfa.process('b');
		assertCurrentStates(nfa, 7);
		
		//kraj provjera jednog po jednog znaka za niz "abcabcaab"
		
		nfa.reset();
		
		//provjera istog automata sa nizovima znakova
		//provjera 10 - niz "abc"
		
		nfa.process(toList("abc"));
		assertCurrentStates(nfa, 3);
		
		nfa.reset();
		
		//provjera 11 - niz "abcaa"
		
		nfa.process(toList("abcaa")); 
		assertCurrentStates(nfa, 6);
		
		nfa.reset();
		
		//provjera 12 - niz "abcabc"
		
		nfa.process(toList("abcabc"));
		assertCurrentStates(nfa, 3);
		
		nfa.reset();
		
		//provjera 13 - niz "abcabcaab"
		
		nfa.process(toList("abcabcaab")); 
		 
		assertCurrentStates(nfa, 7);
	}

	
	
	@Test
	public void test2() {
		NondeterministicFiniteAutomaton<Character> nfa = createNfa();
		
		// dodavanje stanja
		nfa.addState(new State(0, false));
		nfa.addState(new State(1, false));
		nfa.addState(new State(2, true));
		nfa.addState(new State(3, false));
		nfa.setStartState(0);
		
		//prijelazi
		
		nfa.addTransition(0, 1, 'a');
		nfa.addTransition(0, 2, 'b');
		nfa.addTransition(0, 1, 'c');
		nfa.addTransition(0, 1, null);
		nfa.addTransition(0, 3, null);
		nfa.addTransition(1, 2, 'a');
		nfa.addTransition(1, 1, 'a');
		nfa.addTransition(1, 1, 'b');
		nfa.addTransition(1, 3, 'c');
		nfa.addTransition(1, 2, null);
		nfa.addTransition(2, 2, 'a');
		nfa.addTransition(2, 1, 'b');
		nfa.addTransition(2, 3, 'c');
		nfa.addTransition(3, 2, 'a');
		
		nfa.reset();
		
		//test 1 - znak "c"
		
		nfa.process('c');
		assertCurrentStates(nfa, 1, 2, 3);
		
		nfa.reset();
		
		
		//test 2 - niz "ab"
		
		nfa.process(toList("ab"));
		assertCurrentStates(nfa, 1, 2);
		
		nfa.reset();

		
		//test 3 - niz "aca"
		
		nfa.process(toList("aca"));
		assertCurrentStates(nfa, 2);
		
		nfa.reset();

		
		//test 4 - niz "ca"
		
		nfa.process(toList("ca"));
		assertCurrentStates(nfa, 1, 2);
		
		nfa.reset();

		
		
		//test 5 - niz "ab"
		
		Assert.assertTrue(nfa.process(toList("ab")));
		assertCurrentStates(nfa, 1, 2);
		
		nfa.reset();

		
		
		//test 6 - niz "bcab"
		
		nfa.process(toList("bcab"));
		assertCurrentStates(nfa, 1, 2);
		
		nfa.reset();

		
		
		//test 7 - niz "ababacaabba"
		
		nfa.process(toList("abacaabba"));
		assertCurrentStates(nfa, 1, 2);
		
		nfa.reset();

		
		
		//test 8 - niz "abbabbabcac"
		
		Assert.assertTrue(nfa.process(toList("abbabbabcac")));
		assertCurrentStates(nfa, 3);
		
		nfa.reset();

		
		
		//test 9 - niz "abcc"
		//ovdje je prijelaz u nedefinirano stanje
		
		Assert.assertFalse(nfa.process(toList("abcc")));
		assertCurrentStates(nfa);
		
		nfa.reset();

		
		
		//test 10 - niz "abcbaa"
		//ovdje je prijelaz u nedefinirano stanje
		//nakon abcb je vec nedefinirano, a aa sam jos dodo, valjda sam smio - u svakom slucaju ostaje u nedefiniranom
		
		Assert.assertFalse(nfa.process(toList("abcbaa")));
		assertCurrentStates(nfa);
		
		nfa.reset();

		
		
		//test 11 - niz "ccc"
		//ovdje je prijelaz u nedefinirano stanje
		
		Assert.assertFalse(nfa.process(toList("ccc")));
		assertCurrentStates(nfa);
		
		nfa.reset();

		
		
		//test 12 - niz "babacabacb"
		//ovdje je prijelaz u nedefinirano stanje
		
		Assert.assertFalse(nfa.process(toList("babacabacb")));
		assertCurrentStates(nfa);
		
		nfa.reset();

		
		
	}
	
	
	@Test
	public void test3() {
		NondeterministicFiniteAutomaton<Character> nfa = createNfa();
		
		// dodavanje stanja
		nfa.addState(new State(0, false));
		nfa.addState(new State(1, false));
		nfa.addState(new State(2, true));
		nfa.addState(new State(3, false));
		nfa.addState(new State(4, true));
		nfa.setStartState(0);
		
		//prijelazi
		
		nfa.addTransition(0, 0, 'a');
		nfa.addTransition(0, 2, 'b');
		nfa.addTransition(0, 1, 'b');
		nfa.addTransition(0, 3, 'c');
		nfa.addTransition(0, 4, 'c');
		nfa.addTransition(0, 2, null);
		nfa.addTransition(1, 3, 'a');
		nfa.addTransition(1, 1, 'b');
		nfa.addTransition(1, 1, 'c');
		nfa.addTransition(1, 2, 'd');
		nfa.addTransition(1, 2, null);
		nfa.addTransition(1, 1, null);
		nfa.addTransition(2, 4, 'a');
		nfa.addTransition(2, 0, 'b');
		nfa.addTransition(2, 2, 'c');
		nfa.addTransition(2, 4, null);
		nfa.addTransition(3, 1, 'a');
		nfa.addTransition(3, 3, 'a');
		nfa.addTransition(3, 3, null);
		nfa.addTransition(3, 0, null);
		nfa.addTransition(4, 0, 'a');
		nfa.addTransition(4, 4, 'a');
		
		
		nfa.reset();
		
		//test 1 - niz "ab"
		
		nfa.process(toList("ab"));
		assertCurrentStates(nfa, 1, 2, 0, 4);
		
		nfa.reset();
		
		//test 2 - niz "bca"
		
		nfa.process(toList("bca"));
		assertCurrentStates(nfa, 1, 2, 3, 4, 0);
		
		nfa.reset();
		
		//test 3 - niz "bcab"
		
		nfa.process(toList("bcab"));
		assertCurrentStates(nfa, 1, 0, 2, 4);
		
		nfa.reset();
		
		//test 4 - niz "acaba"
		
		nfa.process(toList("acaba"));
		assertCurrentStates(nfa, 2, 3, 4, 0);
		
		nfa.reset();
		
		//test 5 - niz "abdacab"
		
		nfa.process(toList("abdacab"));
		assertCurrentStates(nfa, 0, 1, 2, 4);
		
		nfa.reset();
		
		//test 6 - niz "abbaaa"
		
		Assert.assertTrue(nfa.process(toList("abaaa")));
		assertCurrentStates(nfa, 0, 1, 2, 4, 3);
		
		nfa.reset();
		
		//test 7 - niz "cacaa"
		//ovdje je u nedefiniranom stanju
		
		Assert.assertTrue(nfa.process(toList("cacaa")));
		assertCurrentStates(nfa, 0, 1, 2, 3, 4);
		
		nfa.reset();
		
		//test 8 - niz "abdd"
		//ovaj je u nedefiniranom podrucju
		
		Assert.assertFalse(nfa.process(toList("abdd")));
		assertCurrentStates(nfa);
		
		nfa.reset();
		
		//test 9 - niz "abd"
		//ovaj je u nedefiniranom podrucju
		
		nfa.process(toList("abd"));
		assertCurrentStates(nfa, 2, 4);
		
		nfa.reset();
		
		
		
		
	}
	
	// privatne metode za lakse testiranje
	
	// ovo ti je da se ne moras mucit s stvaranjem templatiranog polja
	@SuppressWarnings("unchecked")
	private static <T> Transition<T>[] toTransitionArray(
			Collection<Transition<T>> transitions) {
		return transitions.toArray(new Transition[0]);
	}
	
	// ovo ti je da se ne moras mucit s stvaranjem prijelaza
	private static <T> Transition<T> createTransition(
			NondeterministicFiniteAutomaton<T> nfa,
			int fromId, int toId, T inputSymbol) {
		return new Transition<T>(
				nfa.getState(fromId), nfa.getState(toId), inputSymbol);
	}
	
	private static <T> void checkTransition(
			NondeterministicFiniteAutomaton<T> nfa,
			int fromId, int toId, T inputSymbol,
			Transition<T> actual) {
		Assert.assertEquals(
				createTransition(nfa, fromId, toId, inputSymbol), 
				actual);
	}
	
	// ovo ti je da se ne moras mucit s assertanjem jednog po jednog prijelaza
	// - samo dodas kao zadnji parametar proizvoljan broj prijelaza
	private static <T> void checkTransitions(
			NondeterministicFiniteAutomaton<T> nfa,
			Transition<T>... expectedTransitions) {
		Validate.isTrue(expectedTransitions.length > 0,
				"Moras dat bar jedan prijelaz, inace nema smisla");
		Collection<Transition<T>> transitions = nfa.getTransitions();
		for (Transition<T> transition : expectedTransitions) {
			Assert.assertTrue(transitions.contains(transition));
		}
	}
	
	private static <T> void assertCurrentStates(NondeterministicFiniteAutomaton<T> nfa,
			Integer... ids) {
		Set<State> expectedCurrentStates = new HashSet<State>(ids.length);
		for (Integer i : ids) 
			expectedCurrentStates.add(nfa.getState(i));
		Assert.assertEquals(expectedCurrentStates, nfa.getCurrentStates());
	}
	
	@SuppressWarnings("unused")
	private static <T> void assertCurrentStatesContains(
			NondeterministicFiniteAutomaton<T> nfa,
			Integer... ids) {
		Set<State> expectedCurrentStatesSubset = new HashSet<State>(ids.length);
		for (Integer i : ids) 
			expectedCurrentStatesSubset.add(nfa.getState(i));
		Assert.assertTrue(nfa.getCurrentStates().containsAll(expectedCurrentStatesSubset));
	}
	
	/**
	 * Pretvara string u listu znakova.
	 * 
	 * @param s niz znakova (string)
	 * @return lista znakova
	 */
	private static List<Character> toList(String s) {
		List<Character> list = new ArrayList<Character>();
		for (int i = 0; i < s.length(); ++i)
			list.add(s.charAt(i));
		return list;
	}
	
	private static <T> NondeterministicFiniteAutomaton<T> createNfa() {
		return new DefaultNondeterministicFiniteAutomaton<T>();
	}
	
}
