package hr.fer.spocc.lexer.nfa;

import static hr.fer.spocc.regex.NfaBuilderTest.printTransitions;
import hr.fer.spocc.automaton.fsm.NondeterministicFiniteAutomaton;
import hr.fer.spocc.lexer.gen.LexerGeneratorEscaper;
import hr.fer.spocc.regex.DefaultRegularExpression;
import hr.fer.spocc.regex.NfaBuilder;
import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

@Ignore("Zahtjeva prethodnu generaciju koda")
public class MinusLangNfaTest {

	@Test
	public void testRule1() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("\\t|\\_");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(1);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRule2() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("\\n");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(2);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRule3() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("#\\|");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(3);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule4() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("\\|#");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(4);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule5() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("\\n");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(5);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule6() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("\\(|\\)|\\{|\\}|\\||\\*|\\\\|\\$|\\t|\\n|\\_|!|\"|#|%|&|'|+|,|-|.|/|0|1|2|3|4|5|6|7|8|9|:|;|<|=|>|?|@|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|[|]|^|_|`|a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|~");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(6);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule7() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("(0|1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9)*|0x(0|1|2|3|4|5|6|7|8|9|a|b|c|d|e|f|A|B|C|D|E|F)(0|1|2|3|4|5|6|7|8|9|a|b|c|d|e|f|A|B|C|D|E|F)*");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(7);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule8() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("\\(");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(8);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule9() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("\\)");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(9);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule10() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("-");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(10);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule11() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("-(\\t|\\n|\\_)*-");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(11);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule12() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("\\((	|\n| )*-");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(12);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule13() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("\\t|\\_");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(13);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule14() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("\\n");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(14);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule15() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("-");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(15);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRule16() {
		NondeterministicFiniteAutomaton<Character> expected = createNfa("-(	|\n| )*-");
		NondeterministicFiniteAutomaton<Character> actual = getGeneratedNfa(16);
		printTransitions(expected);
		System.out.println("------------");
		printTransitions(actual);
		Assert.assertEquals(expected, actual);
	}
	
	static NondeterministicFiniteAutomaton<Character> getGeneratedNfa(
			int priority) {
		switch (priority) {
//		case 1:
//			return new _NfaFactory1().createNfa();
//		case 2:
//			return new _NfaFactory2().createNfa();
//		case 3:
//			return new _NfaFactory3().createNfa();
//		case 4:
//			return new _NfaFactory4().createNfa();
//		case 5:
//			return new _NfaFactory5().createNfa();
//		case 6:
//			return new _NfaFactory6().createNfa();
//		case 7:
//			return new _NfaFactory7().createNfa();
//		case 8:
//			return new _NfaFactory8().createNfa();
//		case 9:
//			return new _NfaFactory9().createNfa();
//		case 10:
//			return new _NfaFactory10().createNfa();
//		case 11:
//			return new _NfaFactory11().createNfa();
//		case 12:
//			return new _NfaFactory12().createNfa();
//		case 13:
//			return new _NfaFactory13().createNfa();
//		case 14:
//			return new _NfaFactory14().createNfa();
//		case 15:
//			return new _NfaFactory15().createNfa();
//		case 16:
//			return new _NfaFactory16().createNfa();
		default:
			return null;
		}
	}

	/**
	 * Convenient wrapper metoda za stvaranje automata na temelju regularnog
	 * izraza.
	 * 
	 * @param regexp
	 *            regularni izraz
	 * @return automat koji prihvaca taj regularni izraz
	 */
	static NondeterministicFiniteAutomaton<Character> createNfa(String regexp) {
		return new NfaBuilder<Character>()
				.build(new MyRegularExpression(regexp));
	}

	static class MyRegularExpression extends DefaultRegularExpression {

		protected MyRegularExpression(String regexp) {
			super(regexp, LexerGeneratorEscaper.getInstance());
		}
	}
}
