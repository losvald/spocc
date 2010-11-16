package hr.fer.spocc.lexer;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Klasa koja testira da li se dobro leksicki analiziraju neki source fileovi
 * pisani u jeziku Minus Lang.
 * 
 * @author TODO staviti ime autora
 */
@Ignore("Zahtjeva generaciju koda")
public class CLexerTest extends LexerBaseTest {

	static final String C_PREFIX = "C";
	
	@Test
	public void testAll1() {
		validateTokanization(C_PREFIX, "01");
	}
	
	@Test
	public void testAll2() {
		validateTokanization(C_PREFIX, "02");
	}
	
	@Test
	public void testAll3() {
		validateTokanization(C_PREFIX, "03");
	}
	
	@Test
	public void testAll20() {
		validateTokanization(C_PREFIX, "20");
	}
	
	// template za copy-pasteanje 
	// selektiras sve i onda Ctrl+C / Ctrl+Shift+C za (un)comment)
//	@Test
//	public void testAllXY() {
//		validateTokanization("C-XY.st", "C-XY.tl",
//		"C-XY.source");
//	}
	
	@Override
	protected String getGeneratorInputShortFilename() {
		return "C.def";
	}
	
	public static void main(String[] args) throws IOException {
		new CLexerTest().generateLexerSource();
	}

}
