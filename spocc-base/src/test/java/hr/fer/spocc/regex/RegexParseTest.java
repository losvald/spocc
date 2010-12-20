/*
 * RegexParseTest.java
 *
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 * Copyright (C) 2010 Lovro Biocic <lovro.biocic@gmail.com>
 * Copyright (C) 2010 Matko Raguz <m.raguz@gmail.com>
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
package hr.fer.spocc.regex;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Leo Osvald
 * @author Lovro Biocic
 * @author Matko Raguz
 *
 */
public class RegexParseTest {
	
	@Test
	@Ignore("Krivi test")
	public void test() {
		RegularExpression<Character> re = createRegex("-(	|\n| )*-");
		
		assertSubregex("-", re, "00");
		assertSubregex("-", re, "1");
		assertSubregex(" ", re, "011");
	}
	
	@Test
	//@Ignore("")
	public void testVeryBasic0() {
		RegularExpression<Character> re = createRegex("\\(");
		
		System.out.println(re);
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testVeryBasic1() {
		RegularExpression<Character> re = createRegex("ab");
		
		Assert.assertEquals(3, re.size());
		
		assertSubregex("a", re, "0");
		assertSubregex("b", re, "1");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testVeryBasic2() {
		RegularExpression<Character> re = createRegex("a|b");
		
		Assert.assertEquals(3, re.size());
		
		assertSubregex("a", re, "0");
		assertSubregex("b", re, "1");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testVeryBasic3() {
		RegularExpression<Character> re = createRegex("abc");

		Assert.assertFalse(re.isTrivial());
		
		assertSubregex("ab", re, "0");
		assertSubregex("c", re, "1");
		assertSubregex("a", re, "00");
		assertSubregex("b", re, "01");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testVeryBasic4() {
		RegularExpression<Character> re = createRegex("a*b");
		
		Assert.assertEquals(4, re.size());
		
		assertSubregex("a*", re, "0");
		assertSubregex("b", re, "1");
		assertSubregex("a", re, "00");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testVeryBasic5() {
		RegularExpression<Character> re = createRegex("ne( |$)cu");

		Assert.assertEquals(re.get(7), 
				RegularExpressionSymbol.epsilonSymbol());
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testVeryBasic6() {
		RegularExpression<Character> re = createRegex("a*");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(2, re.size());
		
		System.out.println(re.getSubexpression(0));
		
		assertSubregex("a", re, "0");
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testVeryBasic7() {
		RegularExpression<Character> re = createRegex("a|bc");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(5, re.size());
		
		System.out.println(re.getSubexpression(0));
		
		assertSubregex("a", re, "0");
		assertSubregex("bc", re, "1");
		assertSubregex("b", re, "10");
		assertSubregex("c", re, "11");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testVeryBasic8() {
		RegularExpression<Character> re = createRegex("ab|c");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(5, re.size());
		
		System.out.println(re.getSubexpression(0));
		
		assertSubregex("ab", re, "0");
		assertSubregex("c", re, "1");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testVeryBasic9() {
		RegularExpression<Character> re = createRegex("ab|cd");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(7, re.size());
		
		System.out.println(re.getSubexpression(0));
		
		assertSubregex("ab", re, "0");
		assertSubregex("cd", re, "1");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testVeryBasic10() {
		RegularExpression<Character> re = createRegex("a*b");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(4, re.size());
		
		System.out.println(re.getSubexpression(0));
		
		assertSubregex("a*", re, "0");
		assertSubregex("b", re, "1");
		assertSubregex("a", re, "00");
		
		Assert.assertEquals(RegularExpressionOperator.STAR,
				getSubregex(re, "0").getOperator());
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testParenthesis0() {
		RegularExpression<Character> re = createRegex("((a|b))");
		System.out.println(re);

		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testParenthesis1() {
		RegularExpression<Character> re = createRegex("((a))");
		System.out.println(re);

		Assert.assertTrue(re.isTrivial());
		Assert.assertEquals(5, re.size());
		
		RegularExpressionElement elem0 = re.get(0);
		Assert.assertEquals(RegularExpressionElementType.LEFT_PARENTHESIS,
				elem0.getElementType());
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testParenthesis2() {
		RegularExpression<Character> re = createRegex("((a))b");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(7, re.size());
		
		RegularExpressionElement elem0 = re.get(1);
		Assert.assertEquals(RegularExpressionElementType.LEFT_PARENTHESIS,
				elem0.getElementType());
		
		assertSubregex("((a))", re, "0");
		assertSubregex("b", re, "1");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testParenthesis3() {
		RegularExpression<Character> re = createRegex("a(b|c)");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(7, re.size());
		
		System.out.println(re.getSubexpression(0));
		
		assertSubregex("a", re, "0");
		assertSubregex("(b|c)", re, "1");
		assertSubregex("b", re, "10");
		assertSubregex("c", re, "11");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testParenthesis4() {
		RegularExpression<Character> re = createRegex("(b|c)d");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(7, re.size());
		
		System.out.println(re.getSubexpression(0));
		
		assertSubregex("(b|c)", re, "0");
		assertSubregex("d", re, "1");
		assertSubregex("b", re, "00");
		assertSubregex("c", re, "01");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void testParenthesis5() {
		RegularExpression<Character> re = createRegex("(b|c)*");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(6, re.size());
		
		System.out.println(re.getSubexpression(0));
		
		assertSubregex("(b|c)", re, "0");
		assertSubregex("b", re, "00");
		assertSubregex("c", re, "01");
		
		assertSelf(re);
	}
	
	@Test
	public void testTrivialValue1() {
		Assert.assertEquals(Character.valueOf('a'), 
				createRegex("a").getTrivialValue());
	}
	
	@Test
	public void testTrivialValue2() {
		Assert.assertEquals(null, 
				createRegex("ab").getTrivialValue());
	}
	
	@Test
	public void testTrivialValue3() {
		Assert.assertEquals(null, 
				createRegex("$").getTrivialValue());
	}
	
	@Test
	public void testTrivialValue4() {
		Assert.assertEquals(Character.valueOf('a'), 
				createRegex("((a))").getTrivialValue());
	}
	
	@Test
	public void testTrivialValue5() {
		Assert.assertEquals(null, 
				createRegex("($)*").getSubexpression(0).getTrivialValue());
	}
	
	@Test
	//@Ignore("")
	public void testTypes1() {
		Assert.assertEquals(RegularExpressionType.TERMINAL, 
				createRegex("a").getType());
		
		Assert.assertEquals(RegularExpressionType.TERMINAL, 
				createRegex("\\$").getType());
		
		Assert.assertEquals(RegularExpressionType.EPSILON, 
				createRegex("$").getType());
		
		Assert.assertEquals(RegularExpressionType.COMPLEX, 
				createRegex("ab").getType());
		
		Assert.assertEquals(RegularExpressionType.COMPLEX, 
				createRegex("a*").getType());
		
		Assert.assertEquals(RegularExpressionType.TERMINAL, 
				createRegex("((a))").getType());
	}
	
	@Test
	//@Ignore("")
	public void test1() {
		RegularExpression<Character> re = createRegex("(((a|b))c)*");

		Assert.assertFalse(re.isTrivial());
		
		RegularExpression<Character> re0
		= createRegex("(((a|b))c)");
		RegularExpression<Character> re00
		= createRegex("((a|b))");
		RegularExpression<Character> re01
		= createRegex("c");
		RegularExpression<Character> re010
		= createRegex("a");
		RegularExpression<Character> re011
		= createRegex("b");
		
		Assert.assertEquals(re0, re.getSubexpression(0));
		Assert.assertEquals(re00, re0.getSubexpression(0));
		Assert.assertEquals(re01, re0.getSubexpression(1));
		Assert.assertEquals(re010, re00.getSubexpression(0));
		Assert.assertEquals(re011, re00.getSubexpression(1));
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void test1Alt() {
		RegularExpression<Character> re = createRegex("(((a|b))c)*");

		Assert.assertFalse(re.isTrivial());
		
		// evo jednog nacina
		RegularExpression<Character> re0 = getSubregex(re, "0");
		Assert.assertEquals(createRegex("(((a|b))c)"), re0);
		
		//sad se moze dodatno provjerit nesto tipa operator, 
		// dal je 0-ti element zagrada
		Assert.assertEquals(RegularExpressionElementType.LEFT_PARENTHESIS, 
				re0.get(0).getElementType());
		
		assertSubregex("((a|b))", re, "00");
		assertSubregex("c", re, "01");
		assertSubregex("a", re, "000");
		assertSubregex("b", re, "001");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void test2() {
		RegularExpression<Character> re = createRegex("abc|abc|abc");

		Assert.assertFalse(re.isTrivial());
		
		assertSubregex("abc|abc", re, "0");
		assertSubregex("abc", re, "1");
		assertSubregex("abc", re, "00");
		assertSubregex("abc", re, "01");
		assertSubregex("ab", re, "10");
		assertSubregex("c", re, "11");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void test3() {
		RegularExpression<Character> re = createRegex("(a|b|c)(b|c)a");

		Assert.assertFalse(re.isTrivial());
		
		assertSubregex("(a|b|c)(b|c)", re, "0");
		assertSubregex("a", re, "1");
		assertSubregex("(a|b|c)", re, "00");
		assertSubregex("(b|c)", re, "01");
		assertSubregex("a|b", re, "000");
		assertSubregex("c", re, "001");
		assertSubregex("b", re, "010");
		assertSubregex("c", re, "011");
		assertSubregex("a", re, "0000");
		assertSubregex("b", re, "0001");
		
		Assert.assertEquals(13, getSubregex(re, "0").size());
		Assert.assertEquals(1, getSubregex(re, "1").size());
		Assert.assertEquals(1, getSubregex(re, "0001").size());
		Assert.assertEquals(7, getSubregex(re, "00").size());
		Assert.assertEquals(5, getSubregex(re, "01").size());
		Assert.assertEquals(3, getSubregex(re, "000").size());
		Assert.assertEquals(1, getSubregex(re, "001").size());
		Assert.assertEquals(1, getSubregex(re, "010").size());
		Assert.assertEquals(1, getSubregex(re, "011").size());
		Assert.assertEquals(1, getSubregex(re, "0000").size());
		Assert.assertEquals(1, getSubregex(re, "0001").size());
		
		// assertSelf(re); // FIXME -> popraviti metodu assertSelf
	}
	
	@Test
	//@Ignore("")
	public void test4() {
		RegularExpression<Character> re = createRegex("abcde");

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(9, re.size());
		
		assertSubregex("abcd", re, "0");
		assertSubregex("e", re, "1");
		assertSubregex("abc", re, "00");
		assertSubregex("d", re, "01");
		assertSubregex("ab", re, "000");
		assertSubregex("c", re, "001");
		assertSubregex("a", re, "0000");
		assertSubregex("b", re, "0001");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void test5() {
		RegularExpression<Character> re = createRegex("(((((a))bc)d|(a*b)*))*");

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(26, re.size());
		
		assertSubregex("(((((a))bc)d|(a*b)*))", re, "0");
		assertSubregex("(((a))bc)d", re, "00");
		assertSubregex("(a*b)*", re, "01");
		assertSubregex("(((a))bc)", re, "000");
		assertSubregex("d", re, "001");
		assertSubregex("(a*b)", re, "010");
		assertSubregex("((a))b", re, "0000");
		assertSubregex("c", re, "0001");
		assertSubregex("a*", re, "0100");
		assertSubregex("b", re, "0101");
		assertSubregex("((a))", re, "00000");
		assertSubregex("b", re, "00001");
		assertSubregex("a", re, "01000");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void test6() {
		RegularExpression<Character> re = createRegex("((a*)*)*");

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(8, re.size());
		
		assertSubregex("((a*)*)", re, "0");
		assertSubregex("(a*)", re, "00");
		assertSubregex("a", re, "000");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void test7() {
		RegularExpression<Character> re = createRegex("((((a*))**))**");

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(14, re.size());
		
		assertSubregex("((((a*))**))*", re, "0");
		assertSubregex("((((a*))**))", re, "00");
		assertSubregex("((a*))*", re, "000");
		assertSubregex("((a*))", re, "0000");
		assertSubregex("a", re, "00000");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void test8() {
		RegularExpression<Character> re = createRegex("((a))b**c|$");

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(13, re.size());
		
		assertSubregex("((a))b**c", re, "0");
		assertSubregex("$", re, "1");
		assertSubregex("((a))b**", re, "00");
		assertSubregex("c", re, "01");
		assertSubregex("((a))", re, "000");
		assertSubregex("b**", re, "001");
		assertSubregex("b*", re, "0010");
		assertSubregex("b", re, "00100");
		
		assertSelf(re);
	}
	
	@Test
	//@Ignore("")
	public void speedTest() {
		
		StringBuilder sb = new StringBuilder();

		for (int i =0 ; i < 80000; ++i) sb.append('a'+(i%26));
		
		createRegex(sb.toString());

		//Assert.assertFalse(re.isTrivial());
		
		//assertSelf(re);
	}
	
	
	
	
	
	static RegularExpression<Character> createRegex(String s) {
		return  new DefaultRegularExpression(s,
				DefaultRegularExpressionEscaper.getInstance());
	}
	
	/**
	 * Vraca podizraz koji se dobije setanje po stablu.
	 * Npr. ako se pod <tt>treePath</tt> da "010" dobije se:
	 * <pre>
	 * baseRegex.getSubexpression(0).getSubexpression(1).getSubexpression(0);
	 * </pre>
	 * 
	 * @param baseRegex bazni izraz
	 * @param treePath string koji predstavlja setnju po stablu odozgo
	 * prema dolje
	 * @return podizraz
	 */
	static RegularExpression<Character> getSubregex(
			RegularExpression<Character> baseRegex, String treePath) {
		for (int i = 0; i < treePath.length(); ++i)
			baseRegex = baseRegex.getSubexpression(treePath.charAt(i)-'0');
		return baseRegex;
	}

	static void assertSubregex(String expectedSubregexString,
			RegularExpression<Character> baseRegex, String treePath) {
		RegularExpression<Character> subregex = getSubregex(
				baseRegex, treePath);
		RegularExpression<Character> expectedSubregex = createRegex(
				expectedSubregexString);
		// prvo provjeri jel velicina dobra (da se zna kolko je velik bug)
		Assert.assertEquals(expectedSubregex.size(), subregex.size());
		// tek provjeri jel su isti
		Assert.assertEquals(expectedSubregex, subregex);
	}
	
	static int assertSelf(RegularExpression<Character> regex) {
		if (regex.isTrivial()) {
			// trivijal mora imat bar 1 elelment (ili vise ako je u zagradama)
			Assert.assertTrue(regex.size() >= 1);
			return regex.size();
		}
		int expectedSize = 1 + 2*getExtraBracketCount(regex);
		for (int i = 0; i < regex.getOperator().getArity(); ++i) {
			expectedSize += assertSelf(regex.getSubexpression(i));
		}
		Assert.assertEquals(expectedSize, regex.size());
		return expectedSize;
	}
	
	static <T> int getExtraBracketCount(RegularExpression<T> regex) {
		
		if (!(regex.get(0) == RegularExpressionElements.LEFT_PARENTHESIS
				&& regex.get(regex.size() - 1) 
				== RegularExpressionElements.RIGHT_PARENTHESIS)) {
			return 0;
		}
			
		
		int ret = 1;
		for (ret = 1; 2*ret < regex.size()
		&& regex.get(ret) == RegularExpressionElements.LEFT_PARENTHESIS
		&& regex.get(regex.size() - ret - 1) 
		== RegularExpressionElements.RIGHT_PARENTHESIS
		&& isValid(regex, ret, regex.size() - ret); ++ret)
			;
		
		return ret;
	}
	
	private static <T> boolean isValid(RegularExpression<T> regex,
			int from, int to) {
		if (from > to)
			return false;
		
		System.err.println("isValid ["+from+", "+to+": "+regex);
		
		int cnt = 0;
		for (int i = from; i < to; ++i) {
			if (regex.get(i) == RegularExpressionElements.LEFT_PARENTHESIS)
				++cnt;
			else if (regex.get(i) == RegularExpressionElements.RIGHT_PARENTHESIS
					&& --cnt < 0 )
				return false;
		}
		return cnt == 0;
	}
	
	
	/*static <T> RegularExpression<T>[] getElements(RegularExpression<T> re) {
		if (re.isTrivial())
			return null;
		return (RegularExpression<T>[]) new RegularExpression[] {
				re.getSubexpression(0), re.getSubexpression(1)};
	}*/
}
