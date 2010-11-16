/*
 * JAutomataTest.java
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

import hr.fer.spocc.regex.DefaultRegularExpression;
import hr.fer.spocc.regex.DefaultRegularExpressionEscaper;
import hr.fer.spocc.regex.NfaBuilder;
import junit.framework.Assert;

import org.junit.Test;

/*
 * @author Leo Osvald
 *
 */
public class JAutomataTest {

	@Test
	public void test1() {
		FiniteStateMachine<Character> fsm = createNfa("a*");
		System.out.println(fsm);
		
		rationals.Automaton automaton = FiniteStateMachines.ToJAutomataUtils
		.convert(fsm);
		System.out.println(automaton);
		System.out.println(FiniteStateMachines.FromJAutomatonUtils
				.toNfa(automaton));
	}
	
	@Test
	public void testConversion1() {
		checkConversion("a");
		checkConversion("b");
		checkConversion("$");
	}
	
	@Test
	public void testConversion2() {
		checkConversion("a|b");
		checkConversion("ab");
		checkConversion("a*");
	}
	
	@Test
	public void testConversion3() {
		checkConversion("((a|b)c**)d");
//		checkConversion("a*b*c|b(a|b)d"); //??? pada?
	}

	static <T> void checkConversion(String regexp) {
		checkConversion(createNfa(regexp));
	}
	
	static <T> void checkConversion(
			NondeterministicFiniteAutomaton<T> nfa) {
		Assert.assertEquals(FiniteStateMachines.FromJAutomatonUtils
				.toNfa(FiniteStateMachines.ToJAutomataUtils.convert(nfa)),
				nfa);
	}
	
	static NondeterministicFiniteAutomaton<Character> createNfa(String regexp) 
	{
		return new NfaBuilder<Character>().build(
				new DefaultRegularExpression(regexp,
						DefaultRegularExpressionEscaper.getInstance()));
	}
	

	
}
