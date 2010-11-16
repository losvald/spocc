package hr.fer.spocc.lexer;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Klasa koja testira da li se dobro leksicki analiziraju neki source fileovi
 * pisani u jeziku Minus Lang.
 * 
 * @author TODO staviti ime autora
 */
@Ignore("Zahtjeva generaciju koda")
public class MinusLangLexerTest extends LexerBaseTest {

	static final String MINUS_LANG_PREFIX = "minusLang";
	
	@Before
	public void beforeTestCase() {
		System.err.println("Starting test...");
	}
	
	@Test
	public void testAll01() {
		validateTokanization(MINUS_LANG_PREFIX, "01");
	}
	
	@Test
	public void testAll02() {
		validateTokanization(MINUS_LANG_PREFIX, "02");
	}
	
	@Test
	public void testAll03() {
		validateTokanization("minusLang-03.st", "minusLang-03.tl",
		"minusLang-03.source");
	}
	
	@Test
	public void testAll04() {
		validateTokanization(MINUS_LANG_PREFIX, "04");
	}
	
	@Test
	public void testAll05() {
		validateTokanization(MINUS_LANG_PREFIX, "05");
	}
	
	// template za copy-pasteanje 
	// selektiras sve i onda Ctrl+C / Ctrl+Shift+C za (un)comment)
//	@Test
//	public void testAllXY() {
//		validateTokanization(MINUS_LANG_PREFIX, "XY");
//	}
	
	// template za copy-pasteanje 
	// selektiras sve i onda Ctrl+C / Ctrl+Shift+C za (un)comment)
//	@Test
//	public void testAllXY() {
//		validateTokanization("minusLang-XY.st", "minusLang-XY.tl",
//		"minusLang-XY.source");
//	}
	
	@Override
	protected String getGeneratorInputShortFilename() {
		return "minusLang.def";
	}
	
	public static void main(String[] args) throws IOException {
		new MinusLangLexerTest().generateLexerSource();
	}

}
