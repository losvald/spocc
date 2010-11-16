/*
 * CharacterEscaperTest.java
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
package hr.fer.spocc.util;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test for the {@link CharacterEscaper} class.
 * 
 * @author Leo Osvald
 *
 */
public class CharacterEscaperTest {

	@Test
	public void testSimpleEscaper() {
		check(SimpleEscaper.INSTANCE, "foo");
		check(SimpleEscaper.INSTANCE, "two words");
		check(SimpleEscaper.INSTANCE, "   a    _ ");
		check(SimpleEscaper.INSTANCE, "\\backslash");
		check(SimpleEscaper.INSTANCE, "\\1\\2");
	}
	
	@Test
	public void testComplexEscaper() {
		check(ComplexEscaper.INSTANCE, "\nunix-style");
		check(ComplexEscaper.INSTANCE, "\n\rwindows-style");
		check(ComplexEscaper.INSTANCE, "\rmac-style");
		check(ComplexEscaper.INSTANCE, "\r\n\n\rb");
	}
	
	@Test
	public void testRobustness() {
		boolean ok = false;
		try {
			new CharacterEscaper(new char[]{}, new char[]{'f'})
			.getEscapeCharacter();
		} catch (Exception e) {
			ok = true;
		}
		Assert.assertTrue(ok);
		
		ok = false;
		try {
			new CharacterEscaper(new char[]{'e'}, new char[]{'f'})
			.getEscapeCharacter();
		} catch (Exception e) {
			ok = true;
		}
		Assert.assertTrue(ok);
	}
	
	@Test
	public void testUnescapeIndexes() {
		String s = "one\\_two\\_three\\_forty-four";
		//          012 34567 8901234 567890123456
		//      -> "one  two  three f orty-four
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		SimpleEscaper.INSTANCE.unescape(s, indexes);
		System.out.println(indexes);
		Assert.assertEquals(Arrays.asList(3, 7, 13), indexes);
	}
	
	private void check(CharacterEscaper ce, String s) {
		String escaped = ce.escape(s);
		System.out.println("escape(" + s + ")=" + escaped);
		Assert.assertEquals(s, ce.unescape(escaped));
	}
	
	private static class SimpleEscaper extends CharacterEscaper {
		static SimpleEscaper INSTANCE = new SimpleEscaper();
		
		public SimpleEscaper() {
			super(new char[]{'\\', ' '}, new char[]{'\\', '_'});
		}
		
	}
	
	private static class ComplexEscaper extends CharacterEscaper {
		static ComplexEscaper INSTANCE = new ComplexEscaper();
		
		public ComplexEscaper() {
			super(new char[]{'\\', '\n', '\r', '-', 'b'}, 
					new char[]{'\\', 'n', 'r', '-', 'B'});
		}
		
	}
}
