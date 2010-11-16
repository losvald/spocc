/*
 * BnfUtils.java
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
package hr.fer.spocc.grammar;

import hr.fer.spocc.grammar.cfg.CfgGrammar;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.sglj.util.StringBuilderUtils;

/*
 * @author Leo Osvald
 *
 */
public class BnfUtils {

	private BnfUtils() {
	}

	/**
	 * Stvara pravila konteksno neovisne gramatike na temelju stringa koji
	 * je u BNF notaciji.
	 * Nezavrsni znakovi opisuju se regularnim izrazom: &lt;{znak}*&gt;<br>
	 * Zavrsni znakovi opisuju se regularnim izrazom: {znak}<br>
	 * Epsilon se opisuje regularnim izrazom: $<br>
	 * Izbor se opisuje pipe operatorom: |<br>
	 * Pridruzivanje se opisuje regularnim izrazom: ::=<br>
	 * Pravila moraju biti razdvojena jednom od bjelina
	 * <p>Primjeri:
	 * &lt;BROJ&gt; ::= &lt;ZNAMENKA&gt;&lt;BROJ&gt; | &lt;ZNAMENKA&gt;<br>
	 * &lt;ZNAMENKA&gt; ::= 0|1|2|3| 4  | 5|  6 |7|8|  9
	 * </p>
	 * 
	 * @param bnfNotation
	 * @return
	 * @see BnfEscaper
	 */
	public static List<CfgProductionRule<Character>> createCfgProductionRules(
			String bnfNotation) {
		List<CfgProductionRule<Character>> rules 
		= new ArrayList<CfgProductionRule<Character>>();
		
		// insert sentinel variable at the end, to make parsing easier
		bnfNotation += "<>";
		Set<Integer> unescapedIndexes = new HashSet<Integer>();
		String[] bnfRules = bnfNotation.split("::=");
		Validate.isTrue(bnfRules.length > 1
				&& bnfRules[0].charAt(0) == '<'
					&& bnfRules[0].charAt(bnfRules[0].length() -1) == '>', 
		"Invalid first BNF rule");
		String leftSideVariable = bnfRules[0].substring(1,
				bnfRules[0].length() - 1);

		for (int ruleNumber = 1; ruleNumber < bnfRules.length; ++ruleNumber) {
			String unescapedBnf = BnfEscaper.getInstance()
			.unescape(bnfRules[ruleNumber], unescapedIndexes);

			List<Symbol<Character>> rightSideSymbols 
			= new ArrayList<Symbol<Character>>();
			StringBuilder rightSideVariable = new StringBuilder();
			String nextLeftSideVariable = null;
			boolean opened = false;
			for (int i = 0; i < unescapedBnf.length(); ++i) {
				boolean terminalOrVariablePart = false;
				char c = unescapedBnf.charAt(i);
				if (!unescapedIndexes.contains(i)) {
					switch (c) {
					case '<':
						opened = true;
						break;
					case '>':
						rightSideSymbols.add(new Variable<Character>(
								rightSideVariable.toString()));
						nextLeftSideVariable = rightSideVariable.toString();
						StringBuilderUtils.clear(rightSideVariable);
						opened = false;
						break;
					case '|':
						rules.add(new CfgProductionRule<Character>(
								new Variable<Character>(
										leftSideVariable),
										rightSideSymbols));
						rightSideSymbols.clear();
						nextLeftSideVariable = null;
						break;
					case '$':
						Symbol<Character> epsilon = Symbols.epsilonSymbol();
						rightSideSymbols.add(epsilon);
						break;
					case ':':
					case '=':
						break;
					default:
						terminalOrVariablePart = true;
					}
				} else {
					terminalOrVariablePart = true;
				}

				if (terminalOrVariablePart) {
					if (opened)
						rightSideVariable.append(c);
					else {
						rightSideSymbols.add(new Terminal<Character>(c));
					}
				}
			}

			// pop left side variable
			Validate.isTrue(!rightSideSymbols.isEmpty(),
					"Right side of BNF rule #"+(ruleNumber+1)
					+" or left side of BNF rule #"+(ruleNumber+2)+" is invalid");
			rightSideSymbols.remove(rightSideSymbols.size() - 1);

			rules.add(new CfgProductionRule<Character>(
					new Variable<Character>(leftSideVariable),
					rightSideSymbols));

			Validate.notNull(nextLeftSideVariable,
					"Right side of BNF rule #"+(ruleNumber+1)
					+" or left side of BNF rule #"+(ruleNumber+2)+" is invalid");
			leftSideVariable = nextLeftSideVariable;
		}
		
		return rules;
	}
	
	/**
	 * Stvara konteksno neovisnu gramatiku na temelju stringa koji
	 * je u BNF notaciji.
	 * Nezavrsni znakovi opisuju se regularnim izrazom: &lt;{znak}*&gt;<br>
	 * Zavrsni znakovi opisuju se regularnim izrazom: {znak}<br>
	 * Epsilon se opisuje regularnim izrazom: $<br>
	 * Izbor se opisuje pipe operatorom: |<br>
	 * Pridruzivanje se opisuje regularnim izrazom: ::=<br>
	 * Pravila moraju biti razdvojena jednom od bjelina
	 * <p>Primjeri:
	 * &lt;BROJ&gt; ::= &lt;ZNAMENKA&gt;&lt;BROJ&gt; | &lt;ZNAMENKA&gt;<br>
	 * &lt;ZNAMENKA&gt; ::= 0|1|2|3| 4  | 5|  6 |7|8|  9
	 * </p>
	 * 
	 * @param bnfNotation
	 * @return
	 * @see BnfEscaper
	 */
	public static CfgGrammar<Character> createCfgGrammar(String bnfNotation) {
		CfgGrammar<Character> cfgGrammar = new CfgGrammar<Character>();
		cfgGrammar.addProductionRules(createCfgProductionRules(bnfNotation));
		return cfgGrammar;
	}
	
	/**
	 * Stvara varijablu iz BNF zapisa, npr. "<A>", "<Start>" i sl.
	 * 
	 * @param <T>
	 * @param bnfVariable string u BNF notaciji
	 * @return varijabla
	 */
	@SuppressWarnings("unchecked")
	public static <T> Variable<T> createVariable(String bnfVariable) {
		Validate.isTrue(bnfVariable.length() >= 2
				&& bnfVariable.charAt(0) == '<'
					&& bnfVariable.charAt(bnfVariable.length() - 1) == '>');
		return (Variable<T>) new Variable<Object>(bnfVariable.substring(1,
				bnfVariable.length() - 1));
	}

}
