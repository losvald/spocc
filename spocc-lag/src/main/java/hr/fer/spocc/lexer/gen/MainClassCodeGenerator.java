/*
 * MainClassCodeGenerator.java
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
import hr.fer.spocc.lexer.LexerDescriptor;
import hr.fer.spocc.lexer.LexicalRule;
import hr.fer.spocc.lexer.gen.naming.MainProgramClassNameFactory;

import java.io.File;
import java.io.IOException;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

/**
 * Generator koda koji stvara glavni program (klasu s main statickom metodom).
 * <br>
 * 
 * Unutar glavnog programa generira se sljedece: 
 * <ol>
 * <li>za svako leksicko pravilo, instancira se po jedan objekt
 * klase {@link LexicalRule}, a kao parametar se instanciraju
 * generirane klase za akcije i automate:<br>
 * <pre>
 * LexicalRule lr1 = new LexicalRule(1, 
 *                               new _Action1(),
 *                               new _NfaFactory1().createNfa());
 * ...
 * </pre></li>
 * <li>Instancira se objekt klase {@link LexerDescriptor}, te se u njega
 * dodaju novostvorene instance leksickih pravila:
 * <pre>
 * LexerDescriptor lexerDescriptor = new LexerDescriptor();
 * lexerDescriptor.addLexicalRule(lr1);
 * ...
 * </pre></li>
 * <li>Stvori se instanca leksickog analizatora preko factoryja:
 * <pre>
 * Lexer lexer = LexerFactory.createLexer(lexerDescriptor);
 * </pre></li>
 * </ol>
 * 
 * @author Leo Osvald
 *
 */
class MainClassCodeGenerator implements CodeGenerator {

	private static final MainClassCodeGenerator INSTANCE
	= new MainClassCodeGenerator();
	
	private MainClassCodeGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void generateSourceFile() throws JClassAlreadyExistsException, IOException {
		// TODO Auto-generated method stub
		JCodeModel cm = new JCodeModel();

		JDefinedClass dc = cm._class(MainProgramClassNameFactory
				.getInstance().getFullClassName());
		
		JMethod method = dc.method(JMod.PUBLIC | JMod.STATIC, 
				void.class, "main");
		
		method.param(String[].class, "args");
		
		JBlock block = method.body();
		StringBuilder sb = new StringBuilder();
		sb.append("hr.fer.spocc.lexer.Lexer lexer = LexerFactory.createLexer(new _LexerDescriptorFactory().createLexerDescriptor()); \n")
		.append("		org.sglj.util.Pair<hr.fer.spocc.SymbolTable, TokenList> result = lexer.tokenizeAll(new java.io.File(args[0]));\n")
		.append("		\n")
		.append("		hr.fer.spocc.SymbolTableWriter.getInstance().print(result.first(), System.err);\n")
		.append("		System.err.println(\"----\");\n")
		.append("		new hr.fer.spocc.lexer.TokenListWriter(result.first()).print(result.second(), System.err);\n")
		.append("		\n")
		.append("		System.out.println(result.first());\n")
		.append("		System.out.println(result.second());\n")
		.append("		\n")
		.append("		System.out.println(\"Kraj\");");
		block.directStatement(sb.toString());
				
		String dir = GeneratorProperties.getProperty(CodeGenerationPropertyConstants.DEFAULT_DIRECTORY_PROPERTY) + "/";
		File file = new File(dir);
		file.mkdirs();
		cm.build(file);
		
	}

	public static MainClassCodeGenerator getInstance() {
		return INSTANCE;
	}
	
	

}
