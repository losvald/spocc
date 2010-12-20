/*
 * ParsingTableGenTest.java
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

import hr.fer.spocc.TokenType;
import hr.fer.spocc.parser.ParsingTable;
import hr.fer.spocc.parser.ParsingTableFactory;

import java.lang.reflect.InvocationTargetException;

import junit.framework.Assert;

import org.apache.commons.lang.reflect.MethodUtils;
import org.junit.Test;

/**
 * @author Leo Osvald
 *
 */
public abstract class ParsingTableGenTest {

	@Test
	public void testEqual() {
		ParsingTable<TokenType> expected = createExpected();
		ParsingTable<TokenType> actual = createDescriptorByGenerator();
		Assert.assertEquals(expected, actual);
	}
	
	@SuppressWarnings("unchecked")
	static ParsingTable<TokenType> createDescriptorByGenerator() {
		String className = GeneratorProperties.getProperty(
				CodeGenerationPropertyConstants.PARSING_TABLE_FACTORY_CLASS_NAME_PROPERTY);
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
			ParsingTableFactory factory = (ParsingTableFactory) clazz.newInstance();
			return (ParsingTable<TokenType>) MethodUtils
			.invokeMethod(factory, "createParsingTable", new Object[]{});
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	abstract ParsingTableDescriptor createDescriptorManually();
	abstract ParsingTable<TokenType> createExpected();
	
}
