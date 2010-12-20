/*
 * DefaultParserGenerator.java
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

import hr.fer.spocc.gen.TokenTypeDescriptor;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.Terminal;
import hr.fer.spocc.grammar.Variable;
import hr.fer.spocc.grammar.cfg.CfgGrammar;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * 
 * @author Leo Osvald
 *
 */
public class DefaultParserGenerator extends AbstractParserGenerator {

	private static final String VARIABLES_DECLARATION_PREFIX = "%V";
	private static final String TERMINALS_DECLARATION_PREFIX = "%T";
	private static final String SYNCHRONIZATION_TOKENS_DECLARATION_PREFIX 
	= "%Syn";
	
	private final CfgGrammar<String> grammar = new CfgGrammar<String>();
	
	private final Set<String> synchronizationTokens = new HashSet<String>();
	
	private TokenTypeDescriptor tokenTypeDescriptor = new TokenTypeDescriptor();
	
	@Override
	public void readFromStream(InputStream inputStream) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(inputStream));
		try {
			grammar.clear();
			synchronizationTokens.clear();
			Variable<String> leftSideVariable = null;
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				if (line.isEmpty())
					continue;
				if (line.startsWith(TERMINALS_DECLARATION_PREFIX)) {
					line = line.substring(
							TERMINALS_DECLARATION_PREFIX.length()).trim();
					for (String value : line.split("\\s+")) {
						grammar.addTerminal(new Terminal<String>(value));
						tokenTypeDescriptor.addTokenType(value);
					}
				} else if (line.startsWith(VARIABLES_DECLARATION_PREFIX)) {
					line = line.substring(
							VARIABLES_DECLARATION_PREFIX.length()).trim();
					String[] variableNames = line.split("\\s+");
					for (String name : variableNames) {
						Variable<String> var = GrammarIOUtils.toVariable(name);
						grammar.addVariable(var);
					}
					Validate.isTrue(variableNames.length > 0);
					Variable<String> startVariable = GrammarIOUtils.toVariable(
							variableNames[0]);
					grammar.setStartVariable(startVariable);
				} else if (line.startsWith(
						SYNCHRONIZATION_TOKENS_DECLARATION_PREFIX)) {
					line = line.substring(
							SYNCHRONIZATION_TOKENS_DECLARATION_PREFIX.length())
							.trim();
					for (String tokenName : line.split("\\s")) {
						synchronizationTokens.add(tokenName);
					}
				} else {
					if (line.charAt(0) != ' ') {
						line = line.trim();
						leftSideVariable = GrammarIOUtils.toVariable(line);
					} else {
						String[] tokens = line.split("\\s+");
						List<Symbol<String>> rightSide 
						= new ArrayList<Symbol<String>>(tokens.length);
						for (String s : tokens) {
							if (s.isEmpty())
								continue;
							rightSide.add(GrammarIOUtils.toSymbol(s));
						}
						grammar.addProductionRule(
								new CfgProductionRule<String>(
										leftSideVariable,
										rightSide));
					}
				}
			}
			System.out.println(grammar);
		} finally {
			br.close();
		}
	}
	
	@Override
	public CfgGrammar<String> getGrammar() {
		return grammar;
	}
	
	public static void main(String[] args) throws IOException {
		new DefaultParserGenerator().readFromStream(System.in);
	}
	
	protected ParsingTableDescriptorFactory 
	createParsingTableDescriptorFactory() {
		return new LR1ParsingTableDescriptorFactory(getGrammar());
	}
	
	@Override
	protected TokenTypeDescriptor tokenTypeDescriptor() {
		return tokenTypeDescriptor;
	}

}
