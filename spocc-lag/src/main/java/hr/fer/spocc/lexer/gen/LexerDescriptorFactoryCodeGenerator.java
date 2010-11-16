/*
 * LexerDescriptorFactoryCodeGenerator.java
 *
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 * Copyright (C) 2010 Marin Pranjic <marin.pranjic@gmail.com>
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
import hr.fer.spocc.lexer.LexerDescriptorFactory;
import hr.fer.spocc.lexer.gen.naming.ActionNameFactory;
import hr.fer.spocc.lexer.gen.naming.NfaFactoryNameFactory;
import hr.fer.spocc.lexer.gen.naming.StateNameFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

/**
 * Generator koda koji mora izgenerirati kod za implementaciju
 * sucelja {@link LexerDescriptorFactory}.
 * 
 * @author Marin Pranjic
 * @author Leo Osvald
 *
 */
public class LexerDescriptorFactoryCodeGenerator implements CodeGenerator {

	private final Set<LexicalRuleDescriptor> lexicalRulePriorities;
	
	public LexerDescriptorFactoryCodeGenerator(Set<LexicalRuleDescriptor> lexicalRulePriorities) {
		this.lexicalRulePriorities = lexicalRulePriorities;
	}
	
	@Override
	public void generateSourceFile() throws JClassAlreadyExistsException,
			IOException {
		JCodeModel cm = new JCodeModel();

		cm._package("hr.fer.spocc.lexer");
		JDefinedClass dc = cm._class(
				GeneratorProperties.getProperty(CodeGenerationPropertyConstants
						.LEXER_DESCRIPTOR_FACTORY_CLASS_NAME_PROPERTY), ClassType.CLASS);
		dc._implements(LexerDescriptorFactory.class);
		
		// TODO DODATI @OVERRIDE
		JMethod method = dc.method(JMod.PUBLIC, LexerDescriptor.class, "createLexerDescriptor");
		JBlock block = method.body();

		for (LexicalRuleDescriptor descriptor : lexicalRulePriorities){
			Integer priority = descriptor.getPriority();
			String state = StateNameFactory.getInstance().getFullEnumName()+"."+descriptor.getDescriptor().getState();
			String action = ActionNameFactory.getInstance().getFullClassName(priority);
			String Nfa = NfaFactoryNameFactory.getInstance().getFullClassName(priority);
			String ispis = "LexicalRule lr" + priority.toString() + " = new LexicalRule (" + priority.toString() + ", new " + action + "(), " + state + ", new " + Nfa + "().createNfa());";
			block.directStatement(ispis);			
		}
		
		Iterator<LexicalRuleDescriptor> iter = lexicalRulePriorities.iterator();
		StringBuffer buffer = new StringBuffer("return new LexerDescriptor (");
		while(iter.hasNext()){
			buffer.append("lr");
			buffer.append(iter.next().getPriority());
			if(iter.hasNext()) buffer.append(", ");
		}
		buffer.append(");");
		
		block.directStatement(buffer.toString());
		String dir = GeneratorProperties.getProperty(CodeGenerationPropertyConstants.DEFAULT_DIRECTORY_PROPERTY) + "/";
		// TODO staviti ispravan dir
		File file = new File(dir);
		file.mkdirs();
		cm.build(file);
	}
/*
	public static void main(String[] args) throws JClassAlreadyExistsException, IOException{
		
		CodeGeneratorCleaner.cleanup();
		
		Set<Integer> lexicalRulePriorities = new HashSet<Integer>();
		lexicalRulePriorities.add(1);
		lexicalRulePriorities.add(2);
		lexicalRulePriorities.add(3);
		lexicalRulePriorities.add(4);
		lexicalRulePriorities.add(5);
		
	
		
		JCodeModel cm = new JCodeModel();

		cm._package("hr.fer.spocc.lexer");
		JDefinedClass dc = cm._class(GeneratorProperties.getProperty(CodeGenerationPropertyConstants
				.LEXER_DESCRIPTOR_FACTORY_CLASS_NAME_PROPERTY), ClassType.CLASS);
		dc._implements(LexerDescriptorFactory.class);
		
		// TODO DODATI @OVERRIDE
		JMethod method = dc.method(JMod.PUBLIC, LexerDescriptor.class, "createLexerDescriptor");
		JBlock block = method.body();

		for (Integer priority : lexicalRulePriorities){
			String action = ActionNameFactory.getInstance().getFullClassName(priority);
			String Nfa = NfaFactoryNameFactory.getInstance().getFullClassName(priority);
			String ispis = "LexicalRule lr" + priority.toString() + " = new LexicalRule (" + priority.toString() + ", new " + action + "(), null, new " + Nfa + "().createNfa());";
			block.directStatement(ispis);
			
		}
		
		Iterator<Integer> iter = lexicalRulePriorities.iterator();
		StringBuffer buffer = new StringBuffer("return new LexerDescriptor ("); 
		while(iter.hasNext()){
			buffer.append("lr");
			buffer.append(iter.next());
			if(iter.hasNext()) buffer.append(", ");
		}
		buffer.append(");");
		
		block.directStatement(buffer.toString());
		String dir = GeneratorProperties.getProperty(CodeGenerationPropertyConstants.DEFAULT_DIRECTORY_PROPERTY) + "/";
		// TODO staviti ispravan dir
		File file = new File(dir);
		file.mkdirs();
		cm.build(file);
	}
	*/
}
