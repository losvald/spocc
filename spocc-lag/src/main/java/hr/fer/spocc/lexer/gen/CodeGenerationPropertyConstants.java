/*
 * CodeGenerationPropertyConstants.java
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

import hr.fer.spocc.util.BasePropertyConstants;

/**
 * Klasa s konstantama vezanim uz propertije koji se ticu
 * generacije koda.
 * 
 * @author Leo Osvald
 *
 */
public final class CodeGenerationPropertyConstants 
extends BasePropertyConstants {

	private static final String ROOT_PREFIX = buildPropertyPrefix("codegen");
	
	private static final String MAIN_PROGRAM_PREFIX
	= buildPropertyPrefix(ROOT_PREFIX, "mainProgram");
	
	private static final String LEXER_DESCRIPTOR_FACTORY_PREFIX
	= buildPropertyPrefix(ROOT_PREFIX, "lexerDescriptorFactory");

	private static final String ACTIONS_PREFIX
	= buildPropertyPrefix(ROOT_PREFIX, "actions");
	
	private static final String NFA_FACTORY_PREFIX
	= buildPropertyPrefix(ROOT_PREFIX, "nfaFactories");
	
	private static final String STATES_PREFIX
	= buildPropertyPrefix(ROOT_PREFIX, "states");
	
	private static final String TOKEN_TYPES_PREFIX
	= buildPropertyPrefix(ROOT_PREFIX, "tokenTypes");
	
	private static final String CLASS_NAME = "className";
	private static final String CLASSES_BASENAME = "classesBaseName";
	
	public static final String DEFAULT_DIRECTORY_PROPERTY
	= ROOT_PREFIX + "defaultDirectory";
	
	public static final String MAIN_PROGRAM_CLASS_NAME_PROPERTY
	= MAIN_PROGRAM_PREFIX + CLASS_NAME;
	
	public static final String LEXER_DESCRIPTOR_FACTORY_CLASS_NAME_PROPERTY
	= LEXER_DESCRIPTOR_FACTORY_PREFIX + CLASS_NAME;
	
	public static final String ACTIONS_CLASSES_BASENAME_PROPERTY
	= ACTIONS_PREFIX + CLASSES_BASENAME;
	
	public static final String ACTION_CLASS_NAME_PROPERTY
	= ACTIONS_PREFIX + CLASSES_BASENAME;
	
	public static final String NFA_FACTORY_CLASSES_BASENAME_PROPERTY
	= NFA_FACTORY_PREFIX + CLASSES_BASENAME;
	
	public static final String TOKEN_TYPES_CLASS_NAME_PROPERTY
	= TOKEN_TYPES_PREFIX + CLASS_NAME;
	
	public static final String STATES_CLASS_NAME_PROPERTY
	= STATES_PREFIX + CLASS_NAME;
	
	private CodeGenerationPropertyConstants() {
	}
	
}
