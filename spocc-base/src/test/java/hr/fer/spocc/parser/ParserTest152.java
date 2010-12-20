package hr.fer.spocc.parser;

import hr.fer.spocc.TokenType;
import hr.fer.spocc.grammar.Terminal;
import hr.fer.spocc.grammar.cfg.CfgGrammar;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;

import java.util.HashMap;
import java.util.Map;

public class ParserTest152 extends ParserTest {
	
	private enum ParserTokens implements TokenType {
		c, d;
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
		
		productionMap.put("S->CC", createRule(
				createVar("S"),
				createVar("C"),
				createVar("C")));
		
		productionMap.put("C->d", createRule(
				createVar("C"),
				createTerminal("d")));
		
		productionMap.put("C->cC", createRule(
				createVar("C"),
				createTerminal("c"),
				createVar("C")));
		
		// dodaj u gramatiku sve produkcije iz mape
		parserGrammar.addProductionRules(productionMap.values());
		
		ParsingTable<TokenType> parsingTable
		= new ParsingTable<TokenType>(getStartState());
		
		parsingTable.setAction(0, createTerminal("c"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(3).perform(parser);
			}
		});
		parsingTable.setAction(0, createTerminal("d"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(4).perform(parser);
			}
		});
		parsingTable.setAction(0, createTerminal("S"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(1).perform(parser);
			}
		});
		parsingTable.setAction(0, createTerminal("C"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(2).perform(parser);
			}
		});
		parsingTable.setAction(1, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.acceptMove().perform(parser);
			}
		});
		parsingTable.setAction(2, createTerminal("c"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(6).perform(parser);
			}
		});
		parsingTable.setAction(2, createTerminal("d"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(7).perform(parser);
			}
		});
		parsingTable.setAction(2, createTerminal("C"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(5).perform(parser);
			}
		});
		parsingTable.setAction(3, createTerminal("c"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(3).perform(parser);
			}
		});
		parsingTable.setAction(3, createTerminal("d"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(4).perform(parser);
			}
		});
		parsingTable.setAction(4, createTerminal("C"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(8).perform(parser);
			}
		});
		parsingTable.setAction(4, createTerminal("c"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(3).perform(parser);
			}
		});
		parsingTable.setAction(4, createTerminal("d"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(3).perform(parser);
			}
		});
		parsingTable.setAction(5, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("S->CC")).perform(parser);
			}
		});
		parsingTable.setAction(6, createTerminal("c"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(6).perform(parser);
			}
		});
		parsingTable.setAction(6, createTerminal("d"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(7).perform(parser);
			}
		});
		parsingTable.setAction(6, createTerminal("C"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(9).perform(parser);
			}
		});
		parsingTable.setAction(7, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("C->d")).perform(parser);
			}
		});
		parsingTable.setAction(8, createTerminal("c"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(2).perform(parser);
			}
		});
		parsingTable.setAction(8, createTerminal("d"), new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(2).perform(parser);
			}
		});
		parsingTable.setAction(9, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
			
			@Override
			public void perform(Parser parser) {
				MoveFactory.createReduceMove(productionMap.get("C->cC")).perform(parser);
			}
		});		
		return parsingTable;
	}

	static Terminal<TokenType> createTerminal(String name) {
		return new Terminal<TokenType>(ParserTokens.valueOf(name));
	}

}
