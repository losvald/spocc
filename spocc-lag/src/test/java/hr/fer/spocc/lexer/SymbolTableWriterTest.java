package hr.fer.spocc.lexer;

import hr.fer.spocc.SymbolTable;
import hr.fer.spocc.SymbolTableRow;
import hr.fer.spocc.SymbolTableWriter;

import org.junit.Ignore;
import org.junit.Test;

@Ignore("Zahtjeva generaciju koda")
public class SymbolTableWriterTest {

	@Test
	public void SymbolTableSort() {
		SymbolTable st = createTestSymbolTable();
		SymbolTableWriter.getInstance().print(st, System.out);
	}

	public static SymbolTable createTestSymbolTable() {
		SymbolTable st = new SymbolTable();
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("IDENTIFIER"), "3"));
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("KEYWORD"), ")"));
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("IDENTIFIER"), "0x12"));
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("OPERATOR"), "-"));
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("OPERATOR"), "-"));
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("IDENTIFIER"), "076"));
		st.addRow(new SymbolTableRow(LexerBaseTest.getTokenType("KEYWORD"), "("));
		return st;
	}
}
