/*
 * DefaultLexerGenerator.java
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
package hr.fer.spocc.lexer.gen;

import hr.fer.spocc.gen.CodeGeneratorCleaner;
import hr.fer.spocc.gen.TokenTypeDescriptor;
import hr.fer.spocc.lexer.LexerDescriptor;
import hr.fer.spocc.lexer.SubactionType;
import hr.fer.spocc.lexer.gen.naming.ActionNameFactory;
import hr.fer.spocc.lexer.gen.naming.LexerDescriptorFactoryNameFactory;
import hr.fer.spocc.lexer.gen.naming.MainProgramClassNameFactory;
import hr.fer.spocc.lexer.gen.naming.NfaFactoryNameFactory;
import hr.fer.spocc.lexer.gen.naming.StateNameFactory;
import hr.fer.spocc.lexer.gen.naming.TokenTypeNameFactory;
import hr.fer.spocc.regex.DefaultRegularExpression;
import hr.fer.spocc.regex.RegularExpression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.sun.codemodel.JClassAlreadyExistsException;

/*
 * @author Leo Osvald
 *
 */
public class DefaultLexerGenerator extends AbstractLexerGenerator 
implements LexerGenerator {

	public Map<String, String> regularDefinitions;
	public StateDescriptor states;
	public TokenTypeDescriptor tokenTypes;
	public List<LexicalRuleDescriptor> lexicalRuleDescriptors;
	private BufferedReader languageDefinition;

	@Override
	public void readFromStream(InputStream inputStream) throws IOException {
		languageDefinition = new BufferedReader(new InputStreamReader(inputStream));

		readRegularDefinitions();
		readTokenTypes();
		readLexicalRules();
	}

	private void readLexicalRules() throws IOException {
		lexicalRuleDescriptors = new ArrayList<LexicalRuleDescriptor>();
		int i = 1;
		while (true) {
			String line = languageDefinition.readLine();
			if (line == null || line.isEmpty()) break;
			//System.out.println("Line: "+line);
			
			String state = StringUtils.substringBetween(line, "<", ">");
			String regexString = StringUtils.substringAfter(line, ">");
			for (String regDef: regularDefinitions.keySet()) {
				regexString = replaceDefinition(regexString, regDef, "(" + regularDefinitions.get(regDef) + ")");
			}
			
			RegularExpression<Character> regex = new DefaultRegularExpression(regexString, LexerGeneratorEscaper.getInstance());
			//System.out.println(i+": "+regexString + "-> "+regex);
			List<SubactionDescriptor> subactions = new ArrayList<SubactionDescriptor>();
			String tokenName = null;
			String n = null;
			while (true) {
				line = languageDefinition.readLine();
				if (line.startsWith("{")) continue;
				if (line.startsWith("}")) {
					break;
				}
				String[] rule = line.split("\\s");
				if (rule[0].equalsIgnoreCase("NOVI_REDAK")) {
					subactions.add(new SubactionDescriptor(SubactionType.NEW_LINE));
				}
				else if (rule[0].equalsIgnoreCase("UDJI_U_STANJE")) {
					subactions.add(new SubactionDescriptor(SubactionType.ENTER_STATE, rule[1]));
				}
				else if (rule[0].equalsIgnoreCase("VRATI_SE")) {
					n = rule[1];
				}
				else if (!rule[0].equalsIgnoreCase("-")) {
					tokenName = rule[0];
				}
				
			}
			if (tokenName == null) {
				if (n == null) subactions.add(new SubactionDescriptor(SubactionType.SKIP));
				else subactions.add(new SubactionDescriptor(SubactionType.TOKENIZE_FIRST, null, n));
			}
			else {
				if (n == null) subactions.add(new SubactionDescriptor(SubactionType.TOKENIZE, tokenName));
				else subactions.add(new SubactionDescriptor(SubactionType.TOKENIZE_FIRST, tokenName, n));
			}
			
			ActionDescriptor action = new ActionDescriptor(state, subactions);
			lexicalRuleDescriptors.add(new LexicalRuleDescriptor(i, regex, action));
			i++;
		}
	}

	private void readTokenTypes() throws IOException {
		String line = languageDefinition.readLine();
		String[] lineSegments = line.split("\\s");
		tokenTypes = new TokenTypeDescriptor();
		for (int i = 1; i < lineSegments.length; i++) {
			tokenTypes.addTokenType(lineSegments[i]);
		}
	}

	private void readLAStates(String line) {
		states = new StateDescriptor();
		String[] lineSegments = line.split("\\s");
		for (int i = 1; i < lineSegments.length; i++) {
			states.addState(lineSegments[i]);
		}
	}

	private void readRegularDefinitions() throws IOException {
		regularDefinitions = new TreeMap<String, String>();
		while (true) {
			String line = languageDefinition.readLine();
			if (line.startsWith("%X")) {
				readLAStates(line);
				break;
			}
			String[] lineSegments = line.split("\\s");
			String regDef = lineSegments[1];
			for (String regex: regularDefinitions.keySet()) {
				regDef = replaceDefinition(regDef, regex, "(" + regularDefinitions.get(regex) + ")");
			}
			regularDefinitions.put(lineSegments[0], regDef);
		}
	}

	private String replaceDefinition(String regDef, String regex, String regDefValue) {
		while (true) {
			if (regDef.contains(regex)) {
				regDef = regDef.replace(regex, regDefValue);
			}
			else break;
		}
		return regDef;
	}

	@Override
	public LexerDescriptor generate() {

		System.out.println("Broj pravila: "+lexicalRuleDescriptors.size());

		CodeGeneratorCleaner.cleanup(
				GeneratorProperties.getProperty(CodeGenerationPropertyConstants
						.DEFAULT_DIRECTORY_PROPERTY),
						ActionNameFactory.getInstance().packageName(),
						MainProgramClassNameFactory.getInstance().packageName(),
						NfaFactoryNameFactory.getInstance().packageName(),
						TokenTypeNameFactory.getInstance().packageName(),
						StateNameFactory.getInstance().packageName(),
						LexerDescriptorFactoryNameFactory.getInstance()
						.packageName()
		);

		try {
			CodeGeneratorFactory.createStateGenerator(states)
			.generateSourceFile();

			CodeGeneratorFactory.createTokenTypeGenerator(tokenTypes)
			.generateSourceFile();

			for (LexicalRuleDescriptor lrd : lexicalRuleDescriptors) {

				CodeGeneratorFactory.createNfaFactoryGenerator(lrd)
				.generateSourceFile();
				CodeGeneratorFactory.createActionGenerator(
						lrd.getDescriptor(), lrd.getPriority())
						.generateSourceFile();
			}

			CodeGeneratorFactory.createLexerDescriptorFactoryGenerator(lexicalRuleDescriptors)
			.generateSourceFile();

			CodeGeneratorFactory.createMainProgramGenerator()
			.generateSourceFile();

		} catch (JClassAlreadyExistsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
