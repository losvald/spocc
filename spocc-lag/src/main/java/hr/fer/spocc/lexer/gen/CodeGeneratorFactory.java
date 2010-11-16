/*
 * CodeGeneratorFactory.java
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


import hr.fer.spocc.gen.CodeGenerator;
import hr.fer.spocc.gen.TokenTypeDescriptor;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Factory koji stvara generatore koda.
 * 
 * @author Leo Osvald
 *
 */
public final class CodeGeneratorFactory {

	// disable instantiation
	private CodeGeneratorFactory() {
	}
	
	public static CodeGenerator createActionGenerator(
			ActionDescriptor actionDescriptor, int priority) {
		return new ActionCodeGenerator(actionDescriptor, priority);
	}
	
	public static CodeGenerator createNfaFactoryGenerator(
			LexicalRuleDescriptor lexicalRuleDescriptor) {
		return new NfaFactoryCodeGenerator(lexicalRuleDescriptor);
	}
	
	public static CodeGenerator createTokenTypeGenerator(
			TokenTypeDescriptor tokenTypeDescriptor) {
		return new TokenTypeCodeGenerator(tokenTypeDescriptor);
	}
	
	public static CodeGenerator createStateGenerator(
			StateDescriptor stateDescriptor) {
		return new StateCodeGenerator(stateDescriptor);
	}
	
	public static CodeGenerator createMainProgramGenerator() {
		return MainClassCodeGenerator.getInstance();
	}
	
	public static CodeGenerator createLexerDescriptorFactoryGenerator(
			Collection<LexicalRuleDescriptor> lexicalRules) {
		return new LexerDescriptorFactoryCodeGenerator(
				new LinkedHashSet<LexicalRuleDescriptor>(lexicalRules));
	}
	
}
