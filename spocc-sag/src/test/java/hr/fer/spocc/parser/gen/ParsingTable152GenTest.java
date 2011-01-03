/*
 * ParsingTable152GenTest.java
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
package hr.fer.spocc.parser.gen;

import hr.fer.spocc.TokenType;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.Symbols;
import hr.fer.spocc.grammar.Variable;
import hr.fer.spocc.grammar.cfg.CfgGrammar;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;
import hr.fer.spocc.parser.MoveType;
import hr.fer.spocc.parser.ParserTest152;
import hr.fer.spocc.parser.ParsingTable;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;

import com.sun.codemodel.JClassAlreadyExistsException;

/**
 * @author Leo Osvald
 *
 */
@Ignore
public class ParsingTable152GenTest extends ParsingTableGenTest {

	private static final ParsingTable152GenTest INSTANCE
	= new ParsingTable152GenTest();

	protected static final Symbol<String> SEQUENCE_TERMINATOR_SYMBOL
	= Symbols.eofSymbol();

	@Override
	ParsingTable<TokenType> createExpected() {
		return new ParserTest152().createParsingTable();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParsingTableDescriptor createDescriptorManually() {
		// TODO
		final Map<String, CfgProductionRule<String>> productionMap
		= new HashMap<String, CfgProductionRule<String>>();

		CfgGrammar<String> parserGrammar = 
			new CfgGrammar<String>();

		productionMap.put("X->S", createRule(
				createVar("X"),
				createVar("S")));
		
		productionMap.put("S->CC", createRule(
				createVar("S"),
				createVar("C"),
				createVar("C")));

		productionMap.put("C->d", createRule(
				createVar("C"),
				createTerminal("d")));

		productionMap.put("C->cC", createRule(
				createVar("C"),
				createTerminal("c"),
				createVar("C")));

		// dodaj u gramatiku sve produkcije iz mape
		parserGrammar.addProductionRules(productionMap.values());

		List<ActionDescriptor> actionDescriptors = Arrays.asList(
				new ActionDescriptor(0, createTerminal("c"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 3))
				),
				new ActionDescriptor(0, createTerminal("d"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 4))
				),
				new ActionDescriptor(0, createTerminal("S"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 1))
				),
				new ActionDescriptor(0, createTerminal("X"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 10))
				),
				new ActionDescriptor(0, createTerminal("C"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 2))
				),
				new ActionDescriptor(1, SEQUENCE_TERMINATOR_SYMBOL,
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("X->S")))
				),
				new ActionDescriptor(2, createTerminal("c"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 6))
				),
				new ActionDescriptor(2, createTerminal("d"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 7))
				),
				new ActionDescriptor(2, createTerminal("C"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 5))
				),
				new ActionDescriptor(3, createTerminal("c"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 3))
				),
				new ActionDescriptor(3, createTerminal("d"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 4))
				),
				new ActionDescriptor(3, createTerminal("C"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 8))
				),
				new ActionDescriptor(4, createTerminal("C"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 8))
				),
				new ActionDescriptor(4, createTerminal("c"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("C->d")))
				),
				new ActionDescriptor(4, createTerminal("d"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("C->d")))
				),
				new ActionDescriptor(5, SEQUENCE_TERMINATOR_SYMBOL,
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("S->CC")))
				),
				new ActionDescriptor(6, createTerminal("c"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 6))
				),
				new ActionDescriptor(6, createTerminal("d"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 7))
				),
				new ActionDescriptor(6, createTerminal("C"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 9))
				),
				new ActionDescriptor(7, SEQUENCE_TERMINATOR_SYMBOL,
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("C->d")))
				),
				new ActionDescriptor(8, createTerminal("c"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("C->cC")))
				),
				new ActionDescriptor(8, createTerminal("d"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("C->cC")))
				),
				new ActionDescriptor(9, SEQUENCE_TERMINATOR_SYMBOL,
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("C->cC")))
				),
				new ActionDescriptor(10, SEQUENCE_TERMINATOR_SYMBOL,
						Arrays.asList(new MoveDescriptor(MoveType.ACCEPT))
				)
		);

		return new ParsingTableDescriptor(actionDescriptors, null, 0);
	}

	static CfgProductionRule<String> createRule(
			Variable<String> var, Variable<String>... rightSideSymbols) {
		return new CfgProductionRule<String>(var, rightSideSymbols);
	}

	public static void main(String[] args) throws JClassAlreadyExistsException, 
	IOException {
		new ParsingTableFactoryCodeGenerator(INSTANCE.createDescriptorManually())
		.generateSourceFile();
	}

	static Variable<String> createVar(String name) {
		return new Variable<String>(name);
	}


	static Variable<String> createTerminal(String name) {
		return new Variable<String>(name);
	}
}
