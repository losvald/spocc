package hr.fer.spocc.parser;

import hr.fer.spocc.TokenType;
import hr.fer.spocc.grammar.Terminal;
import hr.fer.spocc.grammar.cfg.CfgGrammar;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest145 extends ParserTest {
	
	@Test
	public void test0() {
		Assert.assertFalse(parse("a", "b", "c", "c"));
	}
	
	@Test
	public void test1() {
		Assert.assertFalse(parse("a", "b"));
	}
	
	@Test
	public void test2() {
		boolean ok = parse("a", "b", "c");
		
		Assert.assertTrue(ok);
		
		System.out.println(parser.getParseTree());
		
		assertParseTreeEquals("<S'>( <S>( <A>( a b ) ) c )");
	}
	
	@Test
	public void test3() {
		boolean ok = parse("a", "a", "b", "b", "a", "a", "b", "b", "c");
		
		Assert.assertTrue(ok);
		
		System.out.println(parser.getParseTree());
		
		assertParseTreeEquals("<S'>( <S>( <S>( <A>( a <S>( <A>( a b ) ) b) ) <A>( a <S>( <A>( a b ) ) b ) ) c )");
	}
	
	@Test
	public void test4() {
		boolean ok = parse("a", "b", "a", "a", "b", "b", "a", "a", "b", "b", "c");
		
		Assert.assertTrue(ok);
		
		System.out.println(parser.getParseTree());
		
		assertParseTreeEquals("<S'>( <S>( <S>( <S>( <A>( a b ) ) <A>( a <S>( <A>( a b ) ) b) ) <A>( a <S>( <A>( a b ) ) b ) ) c )");
	}
	
	@Test
	public void test5() {
		boolean ok = parse("a", "b", "a", "a", "b", "b", "a", "b", "c");
		
		Assert.assertTrue(ok);
		
		System.out.println(parser.getParseTree());
		
		assertParseTreeEquals("<S'>( <S>( <S>( <S>( <A>( a b ) ) <A>( a <S>( <A>( a b ) ) b) ) <A>( a b ) ) c )");
	}
	
	@Test
	public void test6() {
		boolean ok = parse("a", "b", "a", "b", "b", "a", "b", "c");
		Assert.assertFalse(ok);
	}
	
	public enum ParserTokens implements TokenType {
		a, b, c, d;
	}
	
	@Override
	int getStartState() {
		return 0;
	}
	
	@Override
	TokenType[] getTokenTypes() {
		return ParserTokens.values();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	ParsingTable<TokenType> createParsingTable() {
		CfgGrammar<TokenType> parserGrammar = 
			new CfgGrammar<TokenType>();
		
		// ova mapa tebi sluzi za laksu identifikaciju i dohvacanje produkcija
		final Map<String, CfgProductionRule<TokenType>> productionMap
		= new HashMap<String, CfgProductionRule<TokenType>>();
		
		productionMap.put("S'->Sc", createRule(
				createVar("S'"),
				createVar("S"),
				createTerminal("c")));
		
		productionMap.put("S->SA", createRule(
				createVar("S"),
				createVar("S"),
				createVar("A")));
		
		productionMap.put("S->A", createRule(
				createVar("S"),
				createVar("A")));
		
		productionMap.put("S->aSb", createRule(
				createVar("A"),
				createTerminal("a"),
				createVar("S"),
				createTerminal("b")));
		
		productionMap.put("A->ab", createRule(
				createVar("A"),
				createTerminal("a"),
				createTerminal("b")));
		
		// dodaj u gramatiku sve produkcije iz mape
		parserGrammar.addProductionRules(productionMap.values());
		
		ParsingTable<TokenType> parsingTable
		= new ParsingTable<TokenType>(getStartState());
		
		parsingTable.setAction(0, createTerminal("a"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(3).perform(parser);
			}
		});
		parsingTable.setAction(0, createVar("S"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(1).perform(parser);
			}
		});
		parsingTable.setAction(0, createVar("A"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(2).perform(parser);
			}
		});
		
		parsingTable.setAction(1, createTerminal("a"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(3).perform(parser);
			}
		});
		parsingTable.setAction(1, createTerminal("c"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(4).perform(parser);
			}
		});
		parsingTable.setAction(1, createVar("A"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(5).perform(parser);
			}
		});
		
		parsingTable.setAction(2, createTerminal("a"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("S->A")).perform(parser);
			}
		});
		parsingTable.setAction(2, createTerminal("b"), new Action() {
			
			@Override // TODO ...
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("S->A")).perform(parser);
			}
		});
		parsingTable.setAction(2, createTerminal("c"), new Action() {
			
			@Override // TODO ...
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("S->A")).perform(parser);
			}
		});
		
		parsingTable.setAction(3, createTerminal("a"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(3).perform(parser);
			}
		});
		parsingTable.setAction(3, createTerminal("b"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(7).perform(parser);
			}
		});
		parsingTable.setAction(3, createVar("S"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(6).perform(parser);
			}
		});
		parsingTable.setAction(3, createVar("A"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(2).perform(parser);
			}
		});
		
		parsingTable.setAction(4, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("S'->Sc")).perform(parser);
				MoveFactory.acceptMove().perform(parser);
			}
		});
		
		parsingTable.setAction(5, createTerminal("a"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("S->SA")).perform(parser);
			}
		});
		parsingTable.setAction(5, createTerminal("b"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("S->SA")).perform(parser);
			}
		});
		parsingTable.setAction(5, createTerminal("c"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("S->SA")).perform(parser);
			}
		});
		
		parsingTable.setAction(6, createTerminal("a"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(3).perform(parser);
			}
		});
		parsingTable.setAction(6, createTerminal("b"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(8).perform(parser);
			}
		});
		parsingTable.setAction(6, createVar("A"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(5).perform(parser);
			}
		});
		
		parsingTable.setAction(7, createTerminal("a"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("A->ab")).perform(parser);
			}
		});
		parsingTable.setAction(7, createTerminal("b"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("A->ab")).perform(parser);
			}
		});
		parsingTable.setAction(7, createTerminal("c"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("A->ab")).perform(parser);
			}
		});
		
		parsingTable.setAction(8, createTerminal("a"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("S->aSb")).perform(parser);
			}
		});
		parsingTable.setAction(8, createTerminal("b"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("S->aSb")).perform(parser);
			}
		});
		parsingTable.setAction(8, createTerminal("c"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("S->aSb")).perform(parser);
			}
		});
		
		return parsingTable;
	}
	
	static Terminal<TokenType> createTerminal(String name) {
		return new Terminal<TokenType>(ParserTokens.valueOf(name));
	}
	
}
