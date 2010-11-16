/*
 * NfaFactoryCodeGenerator.java
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

import hr.fer.spocc.automaton.fsm.FiniteStateMachine.Transition;
import hr.fer.spocc.automaton.fsm.FiniteStateMachines;
import hr.fer.spocc.automaton.fsm.NondeterministicFiniteAutomaton;
import hr.fer.spocc.automaton.fsm.State;
import hr.fer.spocc.gen.CodeGenerator;
import hr.fer.spocc.lexer.NfaFactory;
import hr.fer.spocc.lexer.gen.naming.NfaFactoryNameFactory;
import hr.fer.spocc.regex.NfaBuilder;
import hr.fer.spocc.util.CharacterEscaper;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

/**
 * @author Marin Pranjic
 * @author Leo Osvald
 *
 */
class NfaFactoryCodeGenerator implements CodeGenerator {

	private final int priority;
	private final NondeterministicFiniteAutomaton<Character> nfa;
	
	public NfaFactoryCodeGenerator(LexicalRuleDescriptor lexicalRuleDescriptor) {
		this.priority = lexicalRuleDescriptor.getPriority();
		this.nfa = new NfaBuilder<Character>().build(lexicalRuleDescriptor
				.getRegularExpression());
	}
	
	@Override
	public void generateSourceFile() throws JClassAlreadyExistsException, IOException {
	
		JCodeModel cm = new JCodeModel();

		JDefinedClass dc = cm._class(NfaFactoryNameFactory.getInstance()
				.getFullClassName(this.priority), ClassType.CLASS);
		cm._ref(FiniteStateMachines.class);
		dc._implements(NfaFactory.class);

		// TODO DODATI @OVERRIDE
		JMethod method = dc.method(JMod.PUBLIC, NondeterministicFiniteAutomaton.class, "<Character> createNfa");
		JBlock block = method.body();

		
		
		block.directStatement("NondeterministicFiniteAutomaton<Character> nfa = hr.fer.spocc.automaton.fsm.FiniteStateMachines.createNfa();");
		
	Collection<State> states = nfa.getStates();
		
		for (State state : states) {
			
			int id = state.getId();
			boolean accept = state.isAccept();
			
			String inject = StringUtils.join(new String[] {"nfa.addState(new hr.fer.spocc.automaton.fsm.State(",new Integer(id).toString(),", ",new Boolean(accept).toString(),"));"});
			block.directStatement(inject);
		}
		
		Collection<Transition<Character>> transitions = nfa.getTransitions();
		
		for (Transition<Character> t : transitions) {
			
			
			int fromId = t.getFrom().getId();
			int to = t.getTo().getId();	
			
			Character symbol = t.getSymbol();
			
			String sym = "null";
			if (symbol != null) {
				sym = StringUtils.join(new String[] { "'", 
						TransitionEscaper.INSTANCE.escape(String.valueOf(symbol)),
						"'"});
			}
			
			String inject1 = StringUtils.join(new String[] {
					"nfa.addTransition(",
					new Integer(fromId).toString(), 
					", ", new Integer(to).toString(),
					", ", sym,	");"});
			block.directStatement(inject1);
		}
		
		block.directStatement("nfa.setStartState("
				+ nfa.getStartState().getId() + ");");
		block.directStatement("return nfa;");
		
		
		String dir =  
			 GeneratorProperties.getProperty(CodeGenerationPropertyConstants.DEFAULT_DIRECTORY_PROPERTY) +"/";
		File file = new File(dir);
		file.mkdirs();
		cm.build(file);
		
		System.err.println("Nfa factory codegen: "+file);
	}
/*
	public static void main(String[] args) throws IOException, JClassAlreadyExistsException{
	JCodeModel cm = new JCodeModel();
	JDefinedClass dc = cm._class("hr.fer.spocc.lexer.nfa._NfaFactory0", ClassType.CLASS);
	dc._implements(NfaFactory.class);
	// DODATI @OVERRIDE
	JMethod method = dc.method(JMod.PUBLIC, NondeterministicFiniteAutomaton.class, "<Character> createNfa");
	JBlock block = method.body();

	block.directStatement("NondeterministicFiniteAutomaton<Character> nfa = hr.fer.spocc.automaton.fsm.FiniteStateMachines.createNfa();");
	int id = 1;
	boolean accept = true;
	String inject = StringUtils.join(new String[] {"nfa.addState(new hr.fer.spocc.automaton.fsm.State(",new Integer(id).toString(),", ",new Boolean(accept).toString(),"));"});
	block.directStatement(inject);
	
	int fromId = 1;
	int to = 3;
	
	Character symbol = 'z';
	String sym = "null";
	if (symbol != null) {
		sym = StringUtils.join(new String[] { "'", new Character(symbol).toString(),"'"});
	}
	
	String inject1 = StringUtils.join(new String[] {"nfa.addTransition(",new Integer(fromId).toString(), 
			", ", new Integer(to).toString(),
			", ", sym,	");"});
	block.directStatement(inject1);
	block.directStatement("return nfa;");
	String dir = GeneratorProperties.getProperty(CodeGenerationPropertyConstants.DEFAULT_DIRECTORY_PROPERTY);
	File file = new File(dir);
	file.mkdirs();
	cm.build(file);
}
*/
	
	private static class TransitionEscaper extends CharacterEscaper {
		
		private static final char[] FROM = new char[]{
			'\n', '\t', '\'', '\\', '\r',
		};
		private static final char[] TO = new char[]{
			'n',  't', '\'', '\\', 'r'
		};
		
		private static final TransitionEscaper INSTANCE
		= new TransitionEscaper();
		
		private TransitionEscaper() {
			super(FROM, TO);
		}
		
	}
	
}
