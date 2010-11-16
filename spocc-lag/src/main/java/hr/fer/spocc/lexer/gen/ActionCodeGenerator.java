/*
 * ActionCodeGenerator.java
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
import hr.fer.spocc.lexer.Action;
import hr.fer.spocc.lexer.Lexer;
import hr.fer.spocc.lexer.SubactionType;
import hr.fer.spocc.lexer.action.SubactionFactory;
import hr.fer.spocc.lexer.gen.naming.ActionNameFactory;
import hr.fer.spocc.lexer.gen.naming.StateNameFactory;
import hr.fer.spocc.lexer.gen.naming.TokenTypeNameFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.sglj.util.TypeConversionUtils;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

/**
 * Generator koda koji mora izgenerirati kod za akciju ciji opisnik
 * dobiva preko konstruktora.
 * 
 * @author Marin Pranjic
 * @author Leo Osvald
 *
 */
class ActionCodeGenerator implements CodeGenerator {

	private final ActionDescriptor actionDescriptor;
	private final int priority;
	
	public ActionCodeGenerator(ActionDescriptor actionDescriptor, int priority) {
		this.actionDescriptor = actionDescriptor;
		this.priority = priority;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void generateSourceFile() throws JClassAlreadyExistsException, IOException {
		
		JCodeModel cm = new JCodeModel();

		JDefinedClass dc = cm._class(ActionNameFactory.getInstance().getFullClassName(this.priority), ClassType.CLASS);
		dc._implements(Action.class);
		// TODO DODATI @OVERRIDE
		JMethod method = dc.method(JMod.PUBLIC, void.class, "perform");
		JClass Lexer = cm.ref(Lexer.class);
		method.param(Lexer, "lexer");
		JBlock block = method.body();
		
		List<SubactionDescriptor> subactions = actionDescriptor.getSubactionDescriptors();
		
		for(SubactionDescriptor s : subactions){
			
			SubactionType type = s.getType();
			Object[] params = s.getParams();
			String ispis;
			ispis = SubactionFactory.class.getName()+".getSubaction("+SubactionType.class.getName() + "." + type;
			switch(type){
			case ENTER_STATE:
				//Name = StateNameFactory.getInstance().getFullEnumName() + "." + (String) params[0];
				ispis = ispis + ").perform(lexer, "+StateNameFactory.getInstance().getFullEnumName() + "." + (String) params[0]+");";
				break;
			case NEW_LINE:
			case SKIP:
				ispis = ispis + ").perform(lexer);";
				break;
			case TOKENIZE:
				//Name = TokenTypeNameFactory.getInstance().getFullEnumName() + "." + (String) params[0];
				ispis = ispis + ").perform(lexer, " + TokenTypeNameFactory.getInstance().getFullEnumName() + "." + (String) params[0] + ");";
				break;
			case TOKENIZE_FIRST:
				//Name = TokenTypeNameFactory.getInstance().getFullEnumName() + "." + (String) params[0];
				//int pomak = TypeConversionUtils.toInteger(params[1]);
				String blabla = TokenTypeNameFactory.getInstance().getFullEnumName() + "." + (String) params[0];
				if (params[0] == null)
					blabla = "null";
				ispis = ispis + ").perform(lexer, " + blabla + ", " + TypeConversionUtils.toInteger(params[1]) + ");";
				break;
			default: ispis = ispis + ").perform(lexer);";
			// default se nebi smio dogodit :(
			
			}
		
			block.directStatement(ispis);
			
			String dir = GeneratorProperties.getProperty(CodeGenerationPropertyConstants.DEFAULT_DIRECTORY_PROPERTY) + "/";
			File file = new File(dir);
			file.mkdirs();
			cm.build(file);
		}
			
	}
/*
	public static void main (String[] args) throws JClassAlreadyExistsException, IOException{
		
		JCodeModel cm = new JCodeModel();

		cm._package("hr.fer.spocc.lexer.action");
		JDefinedClass dc = cm._class(ActionNameFactory.getInstance().getFullClassName(0), ClassType.CLASS);
		dc._implements(Action.class);
		
		// TODO DODATI @OVERRIDE
		JMethod method = dc.method(JMod.PUBLIC, void.class, "perform");
		JClass Lexer = cm.ref(Lexer.class);
		method.param(Lexer, "lexer");
		JBlock block = method.body();

		SubactionType type = SubactionType.ENTER_STATE;
		Object[] params = new Object[]{"DRUGO"};
		
		String ispis = SubactionFactory.class.getName()+".getSubaction("+SubactionType.class.getName() + "." + type;
		switch(type){
		case ENTER_STATE:
			//Name = StateNameFactory.getInstance().getFullEnumName() + "." + (String) params[0];
			ispis = ispis + ").perform(lexer, "+StateNameFactory.getInstance().getFullEnumName() + "." + (String) params[0]+");";
			break;
		case NEW_LINE:
		case SKIP:
			ispis = ispis + ").perform(lexer)";
			break;
		case TOKENIZE:
			//Name = TokenTypeNameFactory.getInstance().getFullEnumName() + "." + (String) params[0];
			ispis = ispis + ").perform(lexer, " + TokenTypeNameFactory.getInstance().getFullEnumName() + "." + (String) params[0] + ");";
			break;
		case TOKENIZE_FIRST:
			//Name = TokenTypeNameFactory.getInstance().getFullEnumName() + "." + (String) params[0];
			//int pomak = TypeConversionUtils.toInteger(params[1]);
			ispis = ispis + ").perform(lexer, " + TokenTypeNameFactory.getInstance().getFullEnumName() + "." + (String) params[0] + ", " + TypeConversionUtils.toInteger(params[1]) + ");";
			break;
		default: ispis = ispis + ").perform(lexer)";
		// default se nebi smio dogodit :(
		
		}
		block.directStatement(ispis);
		
		String dir = GeneratorProperties.getProperty(CodeGenerationPropertyConstants.DEFAULT_DIRECTORY_PROPERTY) + "/";
		File file = new File(dir);
		file.mkdirs();
		cm.build(file);
		
	}
	*/
}
