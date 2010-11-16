/*
 * LexerDescriptorFactoryNameFactory.java
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

/*
 * @author Leo Osvald
 *
 */
public class LexerDescriptorFactoryNameFactory extends ClassNameFactory {

	private static final LexerDescriptorFactoryNameFactory INSTANCE
	= new LexerDescriptorFactoryNameFactory();
	
	private LexerDescriptorFactoryNameFactory() {
		super(GeneratorProperties.getProperty(CodeGenerationPropertyConstants
				.LEXER_DESCRIPTOR_FACTORY_CLASS_NAME_PROPERTY));
	}
	
	public String className() {
		return baseName();
	}
	
	public static LexerDescriptorFactoryNameFactory getInstance() {
		return INSTANCE;
	}
	
	public static void main(String[] args) {
		System.out.println(getInstance().className());
		System.out.println(getInstance().packageName());
	}

}
