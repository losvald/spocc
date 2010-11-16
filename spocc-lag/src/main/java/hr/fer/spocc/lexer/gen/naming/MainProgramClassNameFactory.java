/*
 * MainProgramClassNameFactory.java
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
package hr.fer.spocc.lexer.gen.naming;

import hr.fer.spocc.gen.naming.ClassNameFactory;
import hr.fer.spocc.lexer.gen.CodeGenerationPropertyConstants;
import hr.fer.spocc.lexer.gen.GeneratorProperties;

/**
 * Klasa za imenovanje izgenerirane klase sa glavnim programom LA.
 * 
 * @author Leo Osvald
 *
 */
public class MainProgramClassNameFactory extends ClassNameFactory {
	
	private static final MainProgramClassNameFactory INSTANCE
	= new MainProgramClassNameFactory();
	
	private MainProgramClassNameFactory() {
		super(GeneratorProperties.getProperty(
				CodeGenerationPropertyConstants
				.MAIN_PROGRAM_CLASS_NAME_PROPERTY));
	}
	
	public String getFullClassName() {
		String packageName = packageName();
		return packageName.isEmpty() ? baseName()
				: packageName() + "." + baseName();
	}
	
	public String getClassName() {
		return baseName();
	}
	
	public static MainProgramClassNameFactory getInstance() {
		return INSTANCE;
	}
}
