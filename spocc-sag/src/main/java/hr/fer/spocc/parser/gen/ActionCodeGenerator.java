/*
 * ActionCodeGenerator.java
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

import hr.fer.spocc.gen.CodeGenerator;
import hr.fer.spocc.parser.Action;

import java.io.IOException;

import com.sun.codemodel.JClassAlreadyExistsException;

/**
 * @author Leo Osvald
 *
 */
public class ActionCodeGenerator implements CodeGenerator{

	private final Action action;
	private final int id;
	
	public ActionCodeGenerator(Action action, int id) {
		this.action = action;
		this.id = id;
	}
	
	@Override
	public void generateSourceFile() throws JClassAlreadyExistsException,
			IOException {
		// TODO Auto-generated method stub
		
	}

}
