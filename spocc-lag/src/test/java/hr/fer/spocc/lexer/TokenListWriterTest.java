package hr.fer.spocc.lexer;

import hr.fer.spocc.SymbolTable;
import hr.fer.spocc.SymbolTableRow;

import org.junit.Ignore;
import org.junit.Test;

@Ignore("Zahtjeva generaciju koda")
public class TokenListWriterTest {
	
	@Test
	public void testTokenList() {
		SymbolTable st = new SymbolTable();
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("IDENTIFIER"), "3"));
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("KEYWORD"), ")"));
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("IDENTIFIER"), "0x12"));
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("OPERATOR"), "-"));
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("OPERATOR"), ")"));
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("IDENTIFIER"), "076"));
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("KEYWORD"), "("));
		
		TokenListWriter tlw = new TokenListWriter(st);
		
		TokenList tl = new TokenList();
		int i = 1;
		for (SymbolTableRow elem: tlw.symbolTableRows) {
			tl.add(elem.getTokenType(), i, elem);
			i++;
		}
		
		tlw.print(tl);
	}

}
