/*
 * LexerTestExample.java
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
package hr.fer.spocc.lexer;

import hr.fer.spocc.SymbolTable;
import hr.fer.spocc.SymbolTableRow;
import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.sglj.util.Pair;

/**
 * Primjer kako trebaju izgledati testovi leksickog analizatora
 * 
 * @author Leo Osvald
 *
 */
public class LexerTestExample extends LexerBaseTest {

	@Test
	@Ignore("Jos nije implementirana funkcionalnost lexera pa test ne radi")
	public void test1() {
		// stvara leksicki analizator
		Lexer lexer = createLexer();
		
		// pustimo ga da izracuna tablice
		Pair<SymbolTable, TokenList> result = lexer.tokenizeAll(
				toFile("minusLang-01.source"));
		
		// sad izgradimo rucno tablicu simbola recimo
		// !! nikako ne koristiti _TokenType.nesto jer je to izgenerirani file
		// !! pa ce biti errora kad se promijeni
		SymbolTable expectedSymbolTable = new SymbolTable();
		SymbolTableRow r1 = new SymbolTableRow(
				getTokenType("OPERAND"), "2");
		SymbolTableRow r2 = new SymbolTableRow(
				getTokenType("UMINUS"), "-");
		
		expectedSymbolTable.addRow(r1);
		expectedSymbolTable.addRow(r2);
		
		
		// i provjerimo da li ona odgovara onom sto trebamo dobiti
		Assert.assertEquals(expectedSymbolTable, result.first());
		
		// izgradimo tablicu uniformnih zn. kako mislimo da treba izgledati
		TokenList expectedTokenList = new TokenList();
		expectedTokenList.add(getTokenType("UMINUS"), 1, r2);		
		expectedTokenList.add(getTokenType("OPERAND"), 1, r1);
		
		// i provjerimo da li ona odgovara onom sto trebamo dobiti
		Assert.assertEquals(expectedTokenList, result.second());
	}
	
	@Test
	@Ignore("Jos nije implementirana funkcionalnost lexera pa test ne radi")
	public void test2() {
		SymbolTable symbolTable = new SymbolTable();
		// ... izgradimo tablicu
		
		
		TokenList tokenTable = new TokenList();
		// ... izgradimo tablicu
		
		Pair<SymbolTable, TokenList> expectedTables
		= new Pair<SymbolTable, TokenList>(symbolTable, tokenTable);
		
		// ovo radi isto sto i gornji
		validateTokanization(expectedTables, "minusLang-02.source");
	}
	
	@Test
	@Ignore("Jos nije implementirana funkcionalnost lexera pa test ne radi")
	public void test3Simple() {
		// ovo automatski provjerava da li se tablice dobro generiraju
		// ocekivane tablice loadaju se automatsk iz fileova .st i .tl
		// i usporedjuju se s onim koje je izgenerirao lexer
		// kad mu je dan "minusLang-03.source" izvorni program
		validateTokanization("minusLang-03.st", "minusLang-03.tl",
				"minusLang-03.source");
	}
	
	/**
	 * Vraca file s definicijama za leksickog analizator (ulazni file za GLA)
	 */
	@Override
	protected String getGeneratorInputShortFilename() {
		return getRelativePath("minusLang.def");
	}
	
	// ovo inace netreba raditi, ovo je sam test testa
	@Test
	public void testGetDescriptor() {
		LexerDescriptor ld = getLexerDescriptor();
		Assert.assertNotNull(ld);
	}
	
	// ovo inace netreba raditi, ovo je sam test testa
	@Test
	public void testExistsDefFile() {
		Assert.assertTrue(getGeneratorInputFile().exists());
	}
	
	// ovo inace netreba raditi, ovo je sam test testa
	@Test
	public void testExistsSourceFiles() {
		Assert.assertTrue(toFile("minusLang-01.source").exists());
		Assert.assertTrue(toFile("minusLang-02.source").exists());
	}
	
	// ovo inace netreba raditi, ovo je sam test testa
	@Test
	public void testLexerCreation() {
		Lexer lexer = createLexer();
		Assert.assertNotNull(lexer);
	}
	
	// ovo inace netreba raditi, ovo je sam test testa
//	@Test
//	public void testTokenType() {
//		Assert.assertSame(_TokenType.IDENTIFIER, getTokenType("IDENTIFIER"));
//	}
	
}
