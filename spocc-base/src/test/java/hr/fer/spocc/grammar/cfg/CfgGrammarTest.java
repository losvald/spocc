/*
 * CfgGrammarTest.java
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
import hr.fer.spocc.grammar.SymbolType;
import hr.fer.spocc.grammar.Symbols;
import hr.fer.spocc.grammar.Terminal;
import hr.fer.spocc.grammar.Variable;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

/*
 * @author Leo Osvald
 *
 */
public class CfgGrammarTest {

	@Test
	public void testRule0() {
		CfgProductionRule<Character> rule = createCfg("S", 
				'a', "A", "B");
		System.out.println(rule);
		Assert.assertEquals(rule.getLeftSideSymbol().getSymbolType(), 
				SymbolType.VARIABLE);
		Assert.assertEquals(rule.getRightSideSymbol(0).getSymbolType(), 
				SymbolType.TERMINAL);
		Assert.assertEquals(rule.getRightSideSymbol(1).getSymbolType(), 
				SymbolType.VARIABLE);
		Assert.assertEquals(rule.getRightSideSymbol(2).getSymbolType(), 
				SymbolType.VARIABLE);
	}
	
	@Test
	public void testEquals() {
		Assert.assertEquals(createCfg("S", 'a', "A", "B", 'c'),
				createCfg("S", 'a', "A", "B", 'c'));
	}
	
	@Test
	public void testBnfParse0() {
		CfgGrammar<Character> parsedGrammer = BnfUtils
		.createCfgGrammar("<S>::=a<A><B>c");
		System.out.println(parsedGrammer);
		CfgGrammar<Character> expected = new CfgGrammar<Character>();
		expected.addProductionRule(createCfg("S", 'a', "A", "B", 'c'));
		Assert.assertEquals(expected, parsedGrammer);
	}
	
	@Test
	public void testBnfParse1() {
		CfgGrammar<Character> parsedGrammer = BnfUtils
		.createCfgGrammar("<Hehe>::=<\\<><Hehe><\\>>|a"
				+ "<\\<>::=\\<" + "<\\>>::=\\>");
		System.out.println(parsedGrammer);
		CfgGrammar<Character> expected = new CfgGrammar<Character>();
		expected.addProductionRule(createCfg("Hehe", "<", "Hehe", ">"));
		expected.addProductionRule(createCfg("Hehe", 'a'));
		expected.addProductionRule(createCfg("<", '<'));
		expected.addProductionRule(createCfg(">", '>'));
		Assert.assertEquals(expected, parsedGrammer);
	}
	
	@Test
	public void testBnfParse2() {
		CfgGrammar<Character> parsedGrammer = BnfUtils
		.createCfgGrammar("<S>::=a<A><B>c"+"<A>::=a"+"<B>::=a|b");
		System.out.println(parsedGrammer);
		CfgGrammar<Character> expected = new CfgGrammar<Character>();
		expected.addProductionRule(createCfg("S", 'a', "A", "B", 'c'));
		expected.addProductionRule(createCfg("A", 'a'));
		expected.addProductionRule(createCfg("B", 'a'));
		expected.addProductionRule(createCfg("B", 'b'));
		Assert.assertEquals(expected, parsedGrammer);
	}
	
	@Test
	public void testBnfParse3() {
		CfgGrammar<Character> parsedGrammer = BnfUtils
		.createCfgGrammar("<S>::=a<A><B>c"+"<A>::=a"+"<B>::=$|b");
		System.out.println(parsedGrammer);
		CfgGrammar<Character> expected = new CfgGrammar<Character>();
		expected.addProductionRule(createCfg("S", 'a', "A", "B", 'c'));
		expected.addProductionRule(createCfg("A", 'a'));
		expected.addProductionRule(createCfg("B", '$'));
		expected.addProductionRule(createCfg("B", 'b'));
		Assert.assertEquals(expected, parsedGrammer);
	}
	
	@Test
	public void testVariableIndexes1() {
		CfgGrammar<Character> parsedGrammer = BnfUtils
		.createCfgGrammar("<S>::=a<A><B>c"+"<A>::=a"+"<B>::=$|b"
				+"<C>::=<C><B><C><A><B>aa<D><C>");
		System.out.println(parsedGrammer);
		System.out.println(parsedGrammer.getEmptySymbols());
	}
	
	
	private Variable<Character> toVar(String name) {
		return new Variable<Character>(name);
	}
	
	private Terminal<Character> toTerminal(char c) {
		return new Terminal<Character>(c);
	}
	
	private CfgProductionRule<Character> createCfg(String leftVar,
			Object... rightSide) {
		List<Symbol<Character>> list = new ArrayList<Symbol<Character>>();
		for (Object o : rightSide) {
			if (o.getClass() == String.class) {
				list.add(toVar((String) o));
			} else if (o.equals('$')) {
				list.add(Symbols.characterEpsilonSymbol());
			} else {
				list.add(toTerminal((Character) o));
			}
		}
		return new CfgProductionRule<Character>(toVar(leftVar), list);
	}
}
