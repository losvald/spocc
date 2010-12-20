package hr.fer.spocc.parser;

import hr.fer.spocc.TokenType;
import hr.fer.spocc.grammar.Terminal;
import hr.fer.spocc.grammar.cfg.CfgGrammar;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest151 extends ParserTest {
	
	@Test
	public void SimpleTest0() {
		Assert.assertFalse(parse("a"));
	}
	
	@Test
	public void SimpleTest1() {
		Assert.assertTrue(parse("b"));
		
		assertParseTreeEquals("<X>( <A>( <B>( b ) <A>( ) ) )");
	}
	
	@Test
	public void test0() {
		Assert.assertFalse(parse("a", "b", "a"));
	}
	
	@Test
	public void test1() {
		Assert.assertFalse(parse("b", "b", "b", "a"));
	}
	
	@Test
	public void test2() {
		Assert.assertFalse(parse("a", "a", "a", "b", "a"));
	}
	
	@Test
	public void test3() {
		Assert.assertTrue(parse("a", "b"));
		
		assertParseTreeEquals("<X>( <A>( <B>( a <B>( b ) ) <A>( ) ) )");
	}
	
	@Test
	public void test4() {
		Assert.assertTrue(parse("a", "a", "b", "a", "a", "b", "b"));
		
		assertParseTreeEquals("<X>( <A>( <B>( a <B>( a <B>( b ) ) ) <A>( <B>( a <B>( a <B>( b ) ) ) <A>( <B>( b ) <A>( ) ) ) ) )");
	}
	
	@Test
	public void test5() {
		Assert.assertTrue(parse("a", "a", "a", "a", "b", "b", "b"));
		
		assertParseTreeEquals("<X>( <A>( <B>( a <B>( a <B>( a <B>( a <B>( b ) ) ) ) ) <A>( <B>( b ) <A>( <B>( b ) <A>( ) ) ) ) )");
	}
	
	@Test
	public void test6() {
		Assert.assertTrue(parse("b", "b", "b", "a", "b", "b", "b"));
		
		assertParseTreeEquals("<X>( <A>( <B>( b ) <A>( <B>( b ) <A>( <B>( b ) <A>( <B>( a <B>( b ) ) <A>( <B>( b ) <A>( <B>( b ) <A>( ) ) ) ) ) ) ) )");
	}
	
	private enum ParserTokens implements TokenType {
		a, b;
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
	public ParsingTable<TokenType> createParsingTable() {
		CfgGrammar<TokenType> parserGrammar = 
			new CfgGrammar<TokenType>();
		
		final Map<String, CfgProductionRule<TokenType>> productionMap
		= new HashMap<String, CfgProductionRule<TokenType>>();
		
		productionMap.put("X->S", createRule(
				createVar("X"),
				createVar("S")));
		
		productionMap.put("S->A", createRule(
				createVar("S"),
				createVar("A")));
		
		productionMap.put("A->BA", createRule(
				createVar("A"),
				createVar("B"),
				createVar("A")));
		
		productionMap.put("A->", createRule(
				createVar("A")));
		
		productionMap.put("B->aB", createRule(
				createVar("B"),
				createTerminal("a"),
				createVar("B")));
		
		productionMap.put("B->b", createRule(
				createVar("B"),
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
		parsingTable.setAction(0, createTerminal("b"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(4).perform(parser);
			}
		});
		parsingTable.setAction(0, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("A->")).perform(parser);
			}
		});
		parsingTable.setAction(0, createVar("B"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(2).perform(parser);
			}
		});
		parsingTable.setAction(0, createVar("A"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(1).perform(parser);
			}
		});
		
		parsingTable.setAction(0, createVar("X"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(7).perform(parser);
			}
		});
		
		parsingTable.setAction(1, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
			
			@Override
			public void perform(Parser parser) {
//				MoveFactory.acceptMove().perform(parser);
				MoveFactory.createReduceMove(productionMap.get("X->S")).perform(parser);
			}
		});
	
		parsingTable.setAction(2, createTerminal("a"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(3).perform(parser);
			}
		});
		parsingTable.setAction(2, createTerminal("b"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(4).perform(parser);
			}
		});
		parsingTable.setAction(2, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("A->")).perform(parser);
			}
		});
		parsingTable.setAction(2, createVar("B"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(2).perform(parser);
			}
		});
		parsingTable.setAction(2, createVar("A"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(5).perform(parser);
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
				MoveFactory.createPushMove(4).perform(parser);
			}
		});
		parsingTable.setAction(3, createVar("B"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(6).perform(parser);
			}
		});

		parsingTable.setAction(4, createTerminal("a"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("B->b")).perform(parser);
			}
		});
		parsingTable.setAction(4, createTerminal("b"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("B->b")).perform(parser);
			}
		});
		parsingTable.setAction(4, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("B->b")).perform(parser);
			}
		});
		
		parsingTable.setAction(5, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("A->BA")).perform(parser);
			}
		});
		
		parsingTable.setAction(6, createTerminal("a"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("B->aB")).perform(parser);
			}
		});
		parsingTable.setAction(6, createTerminal("b"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("B->aB")).perform(parser);
			}
		});
		parsingTable.setAction(6, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("B->aB")).perform(parser);
			}
		});
		
		parsingTable.setAction(7, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.acceptMove().perform(parser);
//				MoveFactory.createReduceMove(productionMap.get("X->S")).perform(parser);
			}
		});
		
		return parsingTable;
	}

	static Terminal<TokenType> createTerminal(String name) {
		return new Terminal<TokenType>(ParserTokens.valueOf(name));
	}

}
