/*
 * ParserTest.java
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

import hr.fer.spocc.SymbolTable;
import hr.fer.spocc.SymbolTableRow;
import hr.fer.spocc.TokenType;
import hr.fer.spocc.Tokenizer;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.Symbols;
import hr.fer.spocc.grammar.Variable;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;
import hr.fer.spocc.lexer.TokenList;
import hr.fer.spocc.util.DummyTokenizer;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.sglj.util.ArrayToStringUtils;

/**
 * @author Leo Osvald
 *
 */
public abstract class ParserTest {
	
	protected static final Symbol<TokenType> SEQUENCE_TERMINATOR_SYMBOL
	= Symbols.eofSymbol();
	
	protected Parser parser;
	
	private Map<String, TokenType> tokenTypeMap
	= new HashMap<String, TokenType>();
	
	@Before
	public void initParser() {
		parser = new LR1Parser(null, null);
		parser.setSymbolTable(createSymbolTable(getTokenTypes()));
		for (TokenType tt : getTokenTypes()) {
			tokenTypeMap.put(tt.toString(), tt);
		}
	}
	
	boolean parse(String... tokenTypeNameList) {
		TokenType[] tokenTypeList = new TokenType[tokenTypeNameList.length];
		for (int i = 0; i < tokenTypeList.length; ++i) {
			tokenTypeList[i] = tokenTypeMap.get(tokenTypeNameList[i]);
		}
		System.out.println("Parsing: "
				+ArrayToStringUtils.toString(tokenTypeList));
		return parse(tokenTypeList);
	}
	
	boolean parse(TokenType... tokenTypeList) {
		Tokenizer tokenizer = createTokenizer(parser.getSymbolTable(),
				tokenTypeList);
		System.err.println(ArrayToStringUtils.toString(tokenTypeList));
		parser.setTokenizer(tokenizer);
		parser.setParsingTable(createParsingTable());
		return parser.parse();
	}
	
	void assertParseTreeEquals(String s) {
		ParseTree expected = ParseTreeIOUtils.fromString(s, tokenTypeMap());
		System.out.println("Expected: "+expected.toString());
		System.out.println("Received: "+parser.getParseTree().toString());
		Assert.assertEquals(expected.toString(), 
				parser.getParseTree().toString());
		//Assert.assertEquals(expected, parser.getParseTree());
	}
	
	abstract int getStartState();
	
	abstract TokenType[] getTokenTypes();
	
	abstract ParsingTable<TokenType> createParsingTable();
	
	Map<String, TokenType> tokenTypeMap() {
		return this.tokenTypeMap;
	}
	
	static CfgProductionRule<TokenType> createRule(
			Variable<TokenType> var, Symbol<TokenType>... rightSideSymbols) {
		return new CfgProductionRule<TokenType>(var, rightSideSymbols);
	}
	
	static Variable<TokenType> createVar(String name) {
		return new Variable<TokenType>(name);
	}
	
	static Tokenizer createTokenizer(SymbolTable symbolTable, 
			TokenType... tokenTypes) {
		TokenList tokenList = new TokenList();
		int lineNumber = 0;
		for (TokenType tt : tokenTypes) {
			tokenList.add(
					tt, 
					++lineNumber, 
					symbolTable.getRow(new SymbolTable.Key(tt, tt.toString())));
		}
		
		return new DummyTokenizer(tokenList);
	}
	
	static SymbolTable createSymbolTable(TokenType... tokenTypes) {
		SymbolTable symbolTable = new SymbolTable();
		for (TokenType tt : tokenTypes) {
			symbolTable.addRow(new SymbolTableRow(tt, tt.toString()));
		}
		return symbolTable;
	}
	
	
}
