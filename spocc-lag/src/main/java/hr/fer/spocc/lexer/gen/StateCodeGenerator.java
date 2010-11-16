/*
 * StateCodeGenerator.java
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

import hr.fer.spocc.TokenType;
import hr.fer.spocc.gen.CodeGenerator;
import hr.fer.spocc.lexer.State;

import java.io.File;
import java.io.IOException;
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
 * sucelja {@link TokenType} ciji opisnik dobiva preko konstruktora.
 * 
 * @author Marin Pranjic
 * @author Leo Osvald
 *
 */
class StateCodeGenerator implements CodeGenerator {

	private StateDescriptor stateDescriptor;
	private Set<String> states;
	
	public StateCodeGenerator(StateDescriptor stateDescriptor) {
		this.stateDescriptor = stateDescriptor;
		
	}

	@Override
	public void generateSourceFile() throws JClassAlreadyExistsException, IOException {
		states = this.stateDescriptor.getStates();
		JCodeModel cm = new JCodeModel();
		JDefinedClass dc = cm._class("hr.fer.spocc.lexer._State", ClassType.ENUM);
		dc._implements(State.class);

		Boolean gotfirst = false;
		String first = "";
		for (String state : states) {
			if(!gotfirst){
				first = state;
				gotfirst = true;
				}
			dc.enumConstant(state);
		}
		
		JMethod isStart = dc.method(JMod.PUBLIC, boolean.class, "isStart");
		JBlock block = isStart.body();
		block.directStatement("return this == "+first+";");
		
		String dir = GeneratorProperties.getProperty(
				CodeGenerationPropertyConstants.
				DEFAULT_DIRECTORY_PROPERTY);
		
		File file = new File(dir);
		file.mkdirs();
		cm.build(file);
	}

}
