package hr.fer.spocc.regex;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

public class RegexParseDebugTest {
	
	@Test
	public void testVeryBasic0() {
		RegularExpression<Character> re = createRegex("a|b");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(3, re.size());
		
		
		assertSubregex("a", re, "0");
		assertSubregex("b", re, "1");
	}
	
	@Test
	public void testVeryBasic1() {
		RegularExpression<Character> re = createRegex("ab");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(3, re.size());
		
		
		assertSubregex("a", re, "0");
		assertSubregex("b", re, "1");
	}
	
	@Test
	public void testVeryBasic2() {
		RegularExpression<Character> re = createRegex("abc");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(5, re.size());
		
		assertSubregex("ab", re, "0");
		assertSubregex("c", re, "1");
	}
	
	@Test
	public void testVeryBasic3() {
		RegularExpression<Character> re = createRegex("a*");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(2, re.size());
		
//		System.out.println(re.getSubexpression(0));
		
		Assert.assertEquals(RegularExpressionOperator.STAR, re.getOperator());
		
		//assertSubregex("a", re, "0");
	}
	
	@Test
	public void testVeryBasic4() {
		RegularExpression<Character> re = createRegex("a|bc");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(5, re.size());
		
		System.out.println(re.getSubexpression(0));
		
		assertSubregex("a", re, "0");
		assertSubregex("bc", re, "1");
	}
	
	@Test
	public void testVeryBasic5() {
		RegularExpression<Character> re = createRegex("ab|c");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(5, re.size());
		
		System.out.println(re.getSubexpression(0));
		
		assertSubregex("ab", re, "0");
		assertSubregex("c", re, "1");
	}
	
	@Test
	public void testVeryBasic6() {
		RegularExpression<Character> re = createRegex("ab|cd");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(7, re.size());
		
		System.out.println(re.getSubexpression(0));
		
		assertSubregex("ab", re, "0");
		assertSubregex("cd", re, "1");
	}
	
	@Test
	public void testParenthesis1() {
		RegularExpression<Character> re = createRegex("((a))");
		System.out.println(re);

		Assert.assertTrue(re.isTrivial());
		Assert.assertEquals(5, re.size());
	}
	
	@Test
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
	}
	
	@Test
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
	}
	
	@Test
	public void testParenthesis5() {
		RegularExpression<Character> re = createRegex("(b|c)*");
		System.out.println(re);

		Assert.assertFalse(re.isTrivial());
		Assert.assertEquals(6, re.size());
		
		System.out.println(re.getSubexpression(0));
		
		assertSubregex("(b|c)", re, "0");
		assertSubregex("b", re, "00");
		assertSubregex("c", re, "01");
	}
	
	@Test
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
	public void test1() {
		RegularExpression<Character> re1 = createRegex("a(b|c)");

		Assert.assertFalse(re1.isTrivial());
		
		RegularExpression<Character> re1_1
		= createRegex("a");
		RegularExpression<Character> re1_2
		= createRegex("(b|c)");
		RegularExpression<Character> re12_1
		= createRegex("b");
		RegularExpression<Character> re12_2
		= createRegex("c");
		
		Assert.assertEquals(re1_1, re1.getSubexpression(0));
		Assert.assertEquals(re1_2, re1.getSubexpression(1));
		Assert.assertEquals(re12_1, re1_2.getSubexpression(0));
		Assert.assertEquals(re12_2, re1_2.getSubexpression(1));
				
		RegularExpressionElement elem0 = re1.get(0);
		Assert.assertEquals(RegularExpressionElementType.SYMBOL,
				elem0.getElementType());
	}
	
	@Test
	public void test2() {
		RegularExpression<Character> Tre = createRegex("a");
		RegularExpression<Character> re = createRegex("a(b|c)");

		Assert.assertTrue(Tre.isTrivial());
		Assert.assertFalse(re.isTrivial());	
		Assert.assertEquals(7, re.size());
				
		RegularExpressionElement elem0 = re.get(2);
		Assert.assertEquals(RegularExpressionElementType.LEFT_PARENTHESIS,
				elem0.getElementType());
	}
	
	@Test
	public void test3() {
		RegularExpression<Character> re1 = createRegex("a(b|c)");
		RegularExpression<Character> re2 = createRegex("a");
		RegularExpression<Character> re3 = createRegex("$");

		Assert.assertEquals(RegularExpressionType.COMPLEX,
				re1.getType());
		Assert.assertEquals(RegularExpressionType.TERMINAL,
				re2.getType());
		Assert.assertEquals(RegularExpressionType.EPSILON,
				re3.getType());
	}
	
	@Test
	public void test4() {
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
	}
	
	@Test
	@Ignore("Test ne radi jer se element '(' ne prepoznaje kao tip LEFT_PARENTHESIS.")
	public void test4Alt() {
		RegularExpression<Character> re = createRegex("(((a|b))c)*");

		Assert.assertFalse(re.isTrivial());
		
		// evo jednog nacina
		RegularExpression<Character> re0 = getSubregex(re, "0");
		Assert.assertEquals(createRegex("(((a|b))c)"), re0);
		
		//sad se moze dodatno provjerit nesto tipa operator, 
		// dal je 0-ti element zagrada
		// OVDJE PADA
		Assert.assertEquals(RegularExpressionElementType.LEFT_PARENTHESIS, 
				re.get(0));
		
		assertSubregex("(((a|b))c)", re, "0");
		assertSubregex("((a|b))", re, "00");
		assertSubregex("c", re, "01");
		assertSubregex("a", re, "010");
		assertSubregex("b", re, "011");
	}
	
	@Test
	public void test5() {
		RegularExpression<Character> re1 = createRegex("ne( |$)cu");

		Assert.assertEquals(re1.get(7), 
				RegularExpressionSymbol.epsilonSymbol());
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
	
	/*static <T> RegularExpression<T>[] getElements(RegularExpression<T> re) {
		if (re.isTrivial())
			return null;
		return (RegularExpression<T>[]) new RegularExpression[] {
				re.getSubexpression(0), re.getSubexpression(1)};
	}*/
}
