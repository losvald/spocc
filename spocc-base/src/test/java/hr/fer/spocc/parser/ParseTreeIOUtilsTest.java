/*
 * ParseTreeIOUtilsTest.java
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
package hr.fer.spocc.parser;

import hr.fer.spocc.TokenType;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Leo Osvald
 *
 */
public class ParseTreeIOUtilsTest {

	enum MyTokenTypes implements TokenType {
		a, b, c, d, e, f;
	};
	
	Map<String, TokenType> map = new HashMap<String, TokenType>();
	
	public ParseTreeIOUtilsTest() {
		for (TokenType tt : MyTokenTypes.values()) {
			map.put(tt.toString(), tt);
		}
	}
	
	@Test
	public void test1() {
		ParseTree parseTree = ParseTreeIOUtils.fromString(
				"<A> (a   b <B>(b d c) d <C>(a <D>(e f)))",
				map);
		
		String s = parseTree.toString();
		
		System.out.println(s);
		
		ParseTree reconstructed = ParseTreeIOUtils.fromString(s, map);
		
		System.out.println(reconstructed);
		
		Assert.assertEquals(parseTree, reconstructed);
	}
	
	@Test
	public void testReconstruction0() {
		checkReconstruction("a");
		checkReconstruction("c");
	}
	
	@Test
	public void testReconstruction1() {
		checkReconstruction("<A>( a b c d )");
	}
	
	@Test
	public void testReconstruction2() {
		try {
			checkReconstruction("<A>( <B>( a ) <C> ( c  ) <D> ( <D> (d d ) c ) )");
			Assert.fail();
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testReconstruction3() {
		checkReconstruction("<A>( <B>( a ) <C>( c ) <D>( <D>( d d ) c ) )");
	}
	
	
	void checkReconstruction(String parseTreeDescription) {
		ParseTree parseTree = ParseTreeIOUtils.fromString(
				parseTreeDescription, map);
		System.out.println("Checking: "+parseTree);
		ParseTree reconstructed = ParseTreeIOUtils.fromString(
				parseTree.toString(), map);
		Assert.assertEquals(parseTree, reconstructed);
	}
}
