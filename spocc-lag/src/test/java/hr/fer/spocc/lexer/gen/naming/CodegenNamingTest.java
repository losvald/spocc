/*
 * CodegenNamingTest.java
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

import hr.fer.spocc.TokenType;
import hr.fer.spocc.lexer.Action;
import hr.fer.spocc.lexer.State;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leo Osvald
 *
 */
public class CodegenNamingTest {

	@Test
	public void testActionNaming() {
		Assert.assertEquals("_"+Action.class.getSimpleName()+(1),
				ActionNameFactory.getInstance().getClassName(1));
		Assert.assertEquals("_"+Action.class.getSimpleName()+(-2),
				ActionNameFactory.getInstance().getClassName(-2));
	}
	
	@Test
	public void testStateNaming() {
		Assert.assertEquals("_"+State.class.getSimpleName(),
				StateNameFactory.getInstance().getEnumName());
	}
	
	@Test
	public void testTokenTypeNaming() {
		Assert.assertEquals("_"+TokenType.class.getSimpleName(),
				TokenTypeNameFactory.getInstance().getEnumName());
	}
	
}
