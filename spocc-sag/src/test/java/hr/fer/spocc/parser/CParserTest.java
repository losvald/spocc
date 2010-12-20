/*
 * CParserTest.java
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

import hr.fer.spocc.TokenType;
import hr.fer.spocc.export.DotExporter;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.Terminal;
import hr.fer.spocc.grammar.Variable;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;
import hr.fer.spocc.parser.gen.ActionDescriptor;
import hr.fer.spocc.parser.gen.HardcodedCParsingTableDescriptorFactory;
import hr.fer.spocc.parser.gen.MoveDescriptor;
import hr.fer.spocc.parser.gen.ParsingTableDescriptor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Leo Osvald
 *
 */
public class CParserTest extends ParserTest {

	private enum ParserTokens implements TokenType {
		L_VIT_ZAGRADA, OP_LTE, KR_IF, KR_VOID, NIZ_ZNAKOVA, L_ZAGRADA, OP_PRIDRUZI, D_UGL_ZAGRADA, ZAREZ, OP_BIN_ILI, OP_I, OP_GT, AMPERSAND, KR_FLOAT, TOCKAZAREZ, KR_CONST, KR_BREAK, OP_ILI, MINUS, OP_DEC, ZNAK, BROJ, OP_INC, ASTERISK, KR_CHAR, IDN, OP_MOD, OP_EQ, KR_RETURN, OP_TILDA, KR_FOR, OP_NEG, OP_NEQ, D_VIT_ZAGRADA, OP_GTE, TOCKA, KR_ELSE, KR_STRUCT, PLUS, D_ZAGRADA, OP_LT, KR_WHILE, L_UGL_ZAGRADA, OP_BIN_XILI, OP_DIJELI, KR_INT, KR_CONTINUE;
	}

	@Test
	public void test01() {
		Assert.assertTrue(parse("KR_INT", "IDN", "L_ZAGRADA", 
				"KR_VOID", "D_ZAGRADA", "L_VIT_ZAGRADA",  
				"KR_INT", "IDN", "OP_PRIDRUZI", "BROJ", "TOCKAZAREZ", 
				"KR_RETURN", "BROJ", "TOCKAZAREZ", "D_VIT_ZAGRADA"));
		
		System.out.println(DotExporter.toDotString(parser.getParseTree()));
		
	}
	
	@Test
	public void test02() {
		Assert.assertFalse(parse("KR_INT", "IDN", "L_ZAGRADA", 
				"KR_VOID", "D_ZAGRADA", "L_VIT_ZAGRADA",  
				"KR_INT", "IDN", "OP_PRIDRUZI", "BROJ", "TOCKAZAREZ", 
				"KR_RETURN", "BROJ", "D_VIT_ZAGRADA"));
	}
	
	@Test
	@Ignore("Krivi je test")
	public void test1() {
		// ovo je primjer { a = b }
		boolean ok = parse("L_ZAGRADA", "IDN", "OP_PRIDRUZI", "IDN",
				"D_ZAGRADA");
		
		Assert.assertTrue(ok);

		System.out.println(parser.getParseTree());
		assertParseTreeEquals("<BLOK>( <NAREDBA>( <LIJEVA_STRANA>( IDN ) OP_PRIDRUZI <DESNA_STRANA>( IDN ) ) )");
	}

	@Test
	@Ignore("Krivi je test")
	public void test2() {
		// ovo je primjer { int a = 2 }
		boolean ok = parse("L_ZAGRADA", "KR_INT", "IDN", "OP_PRIDRUZI", "BROJ",
				"D_ZAGRADA");
		
		Assert.assertTrue(ok);

		System.out.println(parser.getParseTree());
		assertParseTreeEquals("<BLOK>( <NAREDBA>( <LIJEVA_STRANA>( KR_INT IDN ) OP_PRIDRUZI <DESNA_STRANA>( BROJ ) ) )");
	}

	@Test
	@Ignore("Krivi je test")
	public void test3() {
		// ovo je primjer { a = b * 5 }
		boolean ok = parse("L_ZAGRADA", "IDN", "OP_PRIDRUZI", "IDN",
				"ASTERIKS", "BROJ", "D_ZAGRADA");

		Assert.assertTrue(ok);

		System.out.println(parser.getParseTree());
		assertParseTreeEquals("<BLOK>( <NAREDBA>( <LIJEVA_STRANA>( IDN ) OP_PRIDRUZI <DESNA_STRANA>( IDN ASTERIKS BROJ ) ) )");
	}

	@Test
	public void test4() {
		// ovo je primjer void foo(int a int b) { return a + b; }
		boolean ok = parse("KR_VOID", "IDN", "L_ZAGRADA", 
				"KR_INT", "IDN", "KR_INT", "IDN", "D_ZAGRADA", "L_VIT_ZAGRADA", 
				"KR_RETURN", "IDN", "PLUS", "IDN", "TOCKAZAREZ", "D_VIT_ZAGRADA");
		
		Assert.assertFalse(ok); // fali , izmedju parametra }
	}

	@Test
	public void test5() {
		// ovo je primjer void foo(int a, int b) { return a + b; }
		boolean ok = parse("KR_VOID", "IDN", "L_ZAGRADA", 
				"KR_INT", "IDN", "ZAREZ", "KR_INT", "IDN", "D_ZAGRADA", "L_VIT_ZAGRADA", 
				"KR_RETURN", "IDN", "PLUS", "IDN", "TOCKAZAREZ", "D_VIT_ZAGRADA");
		
		Assert.assertTrue(ok);

		System.out.println(parser.getParseTree());
		
		System.out.println(DotExporter.toDotString(parser.getParseTree()));
	}

	@Override
	int getStartState() {
		return 0;
	}

	@Override
	TokenType[] getTokenTypes() {
		return ParserTokens.values();
	}

	@Override
	ParsingTable<TokenType> createParsingTable() {
		ParsingTableDescriptor ptd = new HardcodedCParsingTableDescriptorFactory()
				.createDescriptor();

		ParsingTable<TokenType> parsingTable = new ParsingTable<TokenType>(
				ptd.getStartStateId());
		for (ActionDescriptor ad : ptd.getActionDescriptors()) {
			//System.err.println(ad);
			parsingTable.setAction(ad.getStateId(), convert(ad.getSymbol()),
					new MyAction(ad));
		}

		return parsingTable;
	}

	private static class MyAction implements Action {
		final ActionDescriptor descriptor;

		public MyAction(ActionDescriptor descriptor) {
			this.descriptor = descriptor;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void perform(Parser parser) {
			for (MoveDescriptor md : descriptor.getMoveDescriptors()) {
				switch (md.getType()) {
				case ACCEPT:
					MoveFactory.acceptMove().perform(parser);
					break;
				case SHIFT:
					MoveFactory.createShiftMove().perform(parser);
					break;
				case PUSH:
					MoveFactory.createPushMove((Integer) md.getParams()[0])
							.perform(parser);
					break;
				case REDUCE:
					MoveFactory
							.createReduceMove(
									convert(((CfgProductionRule<String>) md
											.getParams()[0]))).perform(parser);
				}
			}
		}
	}

	static Symbol<TokenType> convert(Symbol<String> symbol) {
		switch (symbol.getSymbolType()) {
		case TERMINAL:
			Terminal<String> t = (Terminal<String>) symbol;
			TokenType tokenType = ParserTokens.valueOf(t.getValue());
			return ParserSymbols.terminal(tokenType);
		case VARIABLE:
			Variable<String> v = (Variable<String>) symbol;
			return ParserSymbols.variable(v.getName());
		case EOF:
			return ParserSymbols.eof();
		default:
			throw new IllegalArgumentException("Epsilon symbol is forbidden");
		}
	}

	static CfgProductionRule<TokenType> convert(
			CfgProductionRule<String> cfgRule) {
		System.err.println("CfgRule<String>: " + cfgRule);
		System.err.println(cfgRule.getRightSideSize());
		List<Symbol<TokenType>> rightSide = new ArrayList<Symbol<TokenType>>();
		for (Symbol<String> symbol : cfgRule.getRightSide()) {
			System.err.println("[" + symbol + "]");
			if (!symbol.toString().equals("null"))
				rightSide.add(convert(symbol));
		}

		CfgProductionRule<TokenType> ret = new CfgProductionRule<TokenType>(
				ParserSymbols.variable(cfgRule.getLeftSideSymbol().getName()),
				rightSide);

		Assert.assertEquals(cfgRule.toString(), ret.toString());
		return ret;
	}

	public static void main(String[] args) {
		CParserTest test = new CParserTest();
		test.initParser();
		boolean b = test.parse(
				//"KR_INT", "IDN", "OP_PRIDRUZI", "IDN", "TOCKAZAREZ");
				"KR_INT", "IDN", "L_ZAGRADA", "KR_VOID", "D_ZAGRADA", "L_VIT_ZAGRADA",  "KR_INT", "IDN", "OP_PRIDRUZI", "BROJ", "TOCKAZAREZ", "KR_RETURN", "BROJ", "D_VIT_ZAGRADA");
		System.out.println("Syntax error?" + !b);
	}
}
