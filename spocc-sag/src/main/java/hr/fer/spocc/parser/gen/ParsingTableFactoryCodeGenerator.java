/*
 * ParsingTableFactoryCodeGenerator.java
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
package hr.fer.spocc.parser.gen;

import hr.fer.spocc.TokenType;
import hr.fer.spocc.gen.CodeGenerator;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.Terminal;
import hr.fer.spocc.grammar.Variable;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;
import hr.fer.spocc.parser.AbstractParsingTableFactory;
import hr.fer.spocc.parser.Action;
import hr.fer.spocc.parser.MoveType;
import hr.fer.spocc.parser.ParsingTable;
import hr.fer.spocc.parser.gen.naming.FillerNameFactory;
import hr.fer.spocc.parser.gen.naming.TokenTypeNameFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
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
public class ParsingTableFactoryCodeGenerator implements CodeGenerator {

	private static final int MAX_ACTIONS_PER_FILE = 300;
	
	private final ParsingTableDescriptor descriptor;
	
	public ParsingTableFactoryCodeGenerator(ParsingTableDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public void generateSourceFile() throws JClassAlreadyExistsException,
	IOException {

		JCodeModel cm = new JCodeModel();
		String generatedClassName = GeneratorProperties.getProperty(CodeGenerationPropertyConstants.PARSING_TABLE_FACTORY_CLASS_NAME_PROPERTY);
		JDefinedClass dc = cm._class(generatedClassName, ClassType.CLASS);
		dc._extends(AbstractParsingTableFactory.class);
		JMethod method = dc.method(JMod.PUBLIC, ParsingTable.class, "<TokenType> createParsingTable");
		method.annotate(SuppressWarnings.class).param("value", "unused");
		JBlock block = method.body();
		String name = "token";
		JClass tokentype = cm.ref(TokenType.class);
		block.decl(tokentype, name);
		name = "action";
		JClass action = cm.ref(Action.class);
		block.decl(action,name);
		block.directStatement("ParsingTable<TokenType> pt = new ParsingTable<TokenType>("
				+ descriptor.getStartStateId() + ");\n");

		Queue<ActionDescriptor> queue = new ArrayDeque<ActionDescriptor>(
				MAX_ACTIONS_PER_FILE);
		Collection<ActionDescriptor> actionDescriptors = descriptor.getActionDescriptors();
		int curId = 0;
		for(ActionDescriptor desc : actionDescriptors) {
			queue.add(desc);
			if (queue.size() == MAX_ACTIONS_PER_FILE) {
				generateFiller(queue, curId++);
			}
		}
		
		if (!queue.isEmpty())
			generateFiller(queue, curId++);

		for (int i = 0; i < curId; ++i) {
			block.directStatement(FillerNameFactory.getInstance().getFullClassName(i)
					+".fill(pt);");
		}
		
		block.directStatement("return pt;");
		
		String dir = GeneratorProperties.getProperty(CodeGenerationPropertyConstants.DEFAULT_DIRECTORY_PROPERTY);
		File file = new File(dir);
		file.mkdirs();
		cm.build(file);
	}
	
	private static void generateFiller(Queue<ActionDescriptor> actionDescriptors,
			int fillerId) throws JClassAlreadyExistsException, IOException {
		
		JCodeModel cm = new JCodeModel();
		String generatedClassName = FillerNameFactory.getInstance().getFullClassName(fillerId);
		JDefinedClass dc = cm._class(generatedClassName, ClassType.CLASS);
		dc._extends(AbstractParsingTableFactory.class);
		dc.annotate(SuppressWarnings.class).param("value","unchecked");
		JMethod method = dc.method(JMod.PUBLIC | JMod.STATIC, void.class, "fill");
		method.param(ParsingTable.class, "<TokenType> pt");
		JBlock block = method.body();
		
		while (!actionDescriptors.isEmpty()) {
			ActionDescriptor desc = actionDescriptors.remove();
			
			List<MoveDescriptor> potezi = desc.getMoveDescriptors();
			int stanje = desc.getStateId();
			Symbol<String> simbol = desc.getSymbol();

			StringBuilder sb = new StringBuilder();

			sb.append("pt.setAction(")
			.append(stanje);//.append(", ");
			sb.append(createSymbolCode(simbol)).append(", ");
			sb.append("new Action(){");//\n\n@Override\n");
			sb.append("public void perform(");
			sb.append("Parser p");
			sb.append("){\n");

			for(MoveDescriptor potez : potezi) {
				MoveType type = potez.getType();
				Object[] params = potez.getParams();

				switch(type){

				case SHIFT:
					sb.append("shift(p);");
					break;
				case REDUCE:
					@SuppressWarnings("unchecked")
					CfgProductionRule<String> cfgRule = (CfgProductionRule<String>) params[0];

					// dodamo lijevu stranu
					sb.append("reduce(p, r(v(\"")
					.append(cfgRule.getLeftSideSymbol().getName())
					.append("\")");

					// dodamo desnu stranu
					for (Symbol<String> symbol : cfgRule.getRightSide()) {
						sb.append(createSymbolCode(symbol));
					}

					// zatvorimo desne zagrade
					sb.append("));");

					break;
				case PUSH:
					int stateId = (Integer) params[0];
					sb.append("push(p, ").append(stateId).append(");");
					break;
				case ACCEPT:
					sb.append("accept(p);");
					break;
				}
			}
			// zatvorimo metodu
			sb.append("}");
			// zatvorimo action
			sb.append("});");

			block.directStatement(sb.toString());
		}
		
		method = dc.method(JMod.PUBLIC, ParsingTable.class, "<TokenType> createParsingTable");
		method.annotate(SuppressWarnings.class).param("value", "unused");
		block = method.body();
		String name = "token";
		JClass tokentype = cm.ref(TokenType.class);
		block.decl(tokentype, name);
		name = "action";
		JClass action = cm.ref(Action.class);
		block.decl(action,name);
		block.directStatement("return null;");
		
		String dir = GeneratorProperties.getProperty(CodeGenerationPropertyConstants.DEFAULT_DIRECTORY_PROPERTY);
		File file = new File(dir);
		file.mkdirs();
		cm.build(file);
	}

	private static String createSymbolCode(Symbol<String> symbol) {
		StringBuilder sb = new StringBuilder();
		switch (symbol.getSymbolType()) {
		case VARIABLE:
			sb.append(", v(\"");
			Variable<String> var = (Variable<String>) symbol;
			sb.append(var.getName());
			sb.append("\")");
			break;
		case TERMINAL:
			Terminal<String> terminal = (Terminal<String>) symbol;
			sb.append(", t(");
			sb.append(TokenTypeNameFactory.getInstance().getFullEnumName());
			sb.append('.').append(terminal.getValue());
			sb.append(')');
			break;
		case EOF:
			sb.append(", EOF");
			break;
		default:
			//sb.append(", EPS");
			break;			
		}
		return sb.toString();
	}

}
