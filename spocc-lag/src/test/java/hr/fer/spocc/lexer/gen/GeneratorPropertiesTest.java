/*
 * GeneratorPropertiesTest.java
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

import hr.fer.spocc.lexer.gen.CodeGenerationPropertyConstants;
import hr.fer.spocc.lexer.gen.GeneratorProperties;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testni kod za klasu {@link GeneratorProperties}
 * 
 * @author Leo Osvald
 *
 */
public class GeneratorPropertiesTest {

	@Test
	public void testLoadProperties() {
		final String key = "codegen.nfaFactories.classesBaseName";
		final String value = "hr.fer.spocc.lexer.nfa._NfaFactory";
//		GeneratorProperties.getProperties().setProperty(key, value);
		
		String fetchedProperty =
			GeneratorProperties.getProperty(key);
		
		System.out.println(fetchedProperty);
		
		Assert.assertEquals(value, fetchedProperty);
	}
	
	@Test
	public void testDefaultDirectory() {
		Assert.assertEquals("../spocc-base/src/generated/java", 
				GeneratorProperties.getProperty(
						CodeGenerationPropertyConstants.
						DEFAULT_DIRECTORY_PROPERTY));
	}
}
