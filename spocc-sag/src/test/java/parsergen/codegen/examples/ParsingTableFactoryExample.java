package parsergen.codegen.examples;

import hr.fer.spocc.TokenType;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;
import hr.fer.spocc.parser.Action;
import hr.fer.spocc.parser.MoveFactory;
import hr.fer.spocc.parser.Parser;
import hr.fer.spocc.parser.ParserSymbols;
import hr.fer.spocc.parser.ParsingTable;
import hr.fer.spocc.parser.ParsingTableFactory;

public class ParsingTableFactoryExample implements ParsingTableFactory {

	private enum _TokenType implements TokenType {
		a, b, c, d;
	}
	
	@Override
	public ParsingTable<TokenType> createParsingTable() {
		ParsingTable<TokenType> parsingTable = new ParsingTable<TokenType>(0);
		
//		parsingTable.setAction(0, createTerminal("a"), new Action() {
//			
//			@Override
//			public void perform(Parser parser) {
//				MoveFactory.createShiftMove().perform(parser);
//				MoveFactory.createPushMove(3).perform(parser);
//			}
//		});
		
		parsingTable.setAction(0, ParserSymbols.terminal(_TokenType.a), new Action() {
			@Override
			public void perform(Parser parser) {
				MoveFactory.createShiftMove().perform(parser);
				MoveFactory.createPushMove(3).perform(parser);
			}
		});
		
//		parsingTable.setAction(0, createVar("S"), new Action() {
//			
//			@Override
//			public void perform(Parser parser) {
//				MoveFactory.createPushMove(1).perform(parser);
//			}
//		});
		
		parsingTable.setAction(0, ParserSymbols.variable("S"), new Action() {
			@Override
			public void perform(Parser parser) {
				MoveFactory.createPushMove(1).perform(parser);
			}
		});
		
//		parsingTable.setAction(4, SEQUENCE_TERMINATOR_SYMBOL, new Action() {
//			
//			@Override
//			public void perform(Parser parser) {
//				MoveFactory.createReduceMove(productionMap.get("S'->Sc")).perform(parser);
//				MoveFactory.acceptMove().perform(parser);
//			}
//		});
		
		parsingTable.setAction(0, ParserSymbols.eof(), new Action() {
			@Override
			public void perform(Parser parser) {
				CfgProductionRule<TokenType> cfgRule = new CfgProductionRule<TokenType>(
						ParserSymbols.variable("S'"),
						ParserSymbols.variable("S"),
						ParserSymbols.terminal(_TokenType.c));
				MoveFactory.createReduceMove(cfgRule).perform(parser);
				MoveFactory.acceptMove().perform(parser);
			}
		});
		
		return parsingTable;
	}

}
