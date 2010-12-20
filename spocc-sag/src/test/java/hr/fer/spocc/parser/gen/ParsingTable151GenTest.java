package hr.fer.spocc.parser.gen;

import hr.fer.spocc.TokenType;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.Symbols;
import hr.fer.spocc.grammar.Variable;
import hr.fer.spocc.grammar.cfg.CfgGrammar;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;
import hr.fer.spocc.parser.MoveType;
import hr.fer.spocc.parser.ParserTest151;
import hr.fer.spocc.parser.ParsingTable;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;

import com.sun.codemodel.JClassAlreadyExistsException;

@Ignore
public class ParsingTable151GenTest extends ParsingTableGenTest {

	private static final ParsingTable151GenTest INSTANCE
	= new ParsingTable151GenTest();
	
	protected static final Symbol<String> SEQUENCE_TERMINATOR_SYMBOL
	= Symbols.eofSymbol();
	
	@Override
	ParsingTable<TokenType> createExpected() {
		return new ParserTest151().createParsingTable();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ParsingTableDescriptor createDescriptorManually() {
		// TODO
		final Map<String, CfgProductionRule<String>> productionMap
		= new HashMap<String, CfgProductionRule<String>>();
		
		CfgGrammar<String> parserGrammar = 
			new CfgGrammar<String>();
		
		productionMap.put("X->S", createRule(
				createVar("X"),
				createVar("S")));
		
		productionMap.put("S->A", createRule(
				createVar("S"),
				createVar("A")));
		
		productionMap.put("S->BA", createRule(
				createVar("S"),
				createVar("B"),
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
		
		List<ActionDescriptor> actionDescriptors = Arrays.asList(
			new ActionDescriptor(0, createTerminal("a"),
			Arrays.asList(
				new MoveDescriptor(MoveType.SHIFT),
				new MoveDescriptor(MoveType.PUSH, 3))
			),
			new ActionDescriptor(0, createTerminal("b"),
					Arrays.asList(
						new MoveDescriptor(MoveType.SHIFT),
						new MoveDescriptor(MoveType.PUSH, 4))
			),
			new ActionDescriptor(0, SEQUENCE_TERMINATOR_SYMBOL,
					Arrays.asList(
						new MoveDescriptor(MoveType.REDUCE,
							productionMap.get("A->")))
			),
			new ActionDescriptor(0, createTerminal("B"),
					Arrays.asList(
						new MoveDescriptor(MoveType.PUSH, 2))
			),
			new ActionDescriptor(0, createTerminal("A"),
					Arrays.asList(
						new MoveDescriptor(MoveType.PUSH, 1))
			),
			new ActionDescriptor(0, createTerminal("X"),
					Arrays.asList(
						new MoveDescriptor(MoveType.PUSH, 7))
			),
			new ActionDescriptor(1, SEQUENCE_TERMINATOR_SYMBOL,
					Arrays.asList(new MoveDescriptor(MoveType.REDUCE,
							productionMap.get("X->S")))
					
			),
			new ActionDescriptor(2, createTerminal("a"),
					Arrays.asList(
						new MoveDescriptor(MoveType.SHIFT),
						new MoveDescriptor(MoveType.PUSH, 3))
			),
			new ActionDescriptor(2, createTerminal("b"),
					Arrays.asList(
						new MoveDescriptor(MoveType.SHIFT),
						new MoveDescriptor(MoveType.PUSH, 4))
			),
			new ActionDescriptor(2, SEQUENCE_TERMINATOR_SYMBOL,
					Arrays.asList(
						new MoveDescriptor(MoveType.REDUCE,
							productionMap.get("A->")))
			),
			new ActionDescriptor(2, createTerminal("B"),
					Arrays.asList(
						new MoveDescriptor(MoveType.PUSH, 2))
			),
			new ActionDescriptor(2, createTerminal("A"),
					Arrays.asList(
						new MoveDescriptor(MoveType.PUSH, 5))
			),
			new ActionDescriptor(3, createTerminal("a"),
					Arrays.asList(
						new MoveDescriptor(MoveType.SHIFT),
						new MoveDescriptor(MoveType.PUSH, 3))
			),
			new ActionDescriptor(3, createTerminal("b"),
					Arrays.asList(
						new MoveDescriptor(MoveType.SHIFT),
						new MoveDescriptor(MoveType.PUSH, 4))
			),
			new ActionDescriptor(3, createTerminal("B"),
					Arrays.asList(
						new MoveDescriptor(MoveType.PUSH, 6))
			),
			new ActionDescriptor(4, createTerminal("a"),
					Arrays.asList(
						new MoveDescriptor(MoveType.REDUCE,
								productionMap.get("B->b")))
			),
			new ActionDescriptor(4, createTerminal("b"),
					Arrays.asList(
						new MoveDescriptor(MoveType.REDUCE,
								productionMap.get("B->b")))
			),
			new ActionDescriptor(4, SEQUENCE_TERMINATOR_SYMBOL,
					Arrays.asList(
						new MoveDescriptor(MoveType.REDUCE,
								productionMap.get("B->b")))
			),
			new ActionDescriptor(5, SEQUENCE_TERMINATOR_SYMBOL,
					Arrays.asList(
						new MoveDescriptor(MoveType.REDUCE,
								productionMap.get("A->BA")))
			),
			new ActionDescriptor(6, createTerminal("a"),
					Arrays.asList(
						new MoveDescriptor(MoveType.REDUCE,
								productionMap.get("B->aB")))
			),
			new ActionDescriptor(6, createTerminal("b"),
					Arrays.asList(
						new MoveDescriptor(MoveType.REDUCE,
								productionMap.get("B->aB")))
			),
			new ActionDescriptor(6, SEQUENCE_TERMINATOR_SYMBOL,
					Arrays.asList(
						new MoveDescriptor(MoveType.REDUCE,
								productionMap.get("B->aB")))
			),
			new ActionDescriptor(7, SEQUENCE_TERMINATOR_SYMBOL,
					Arrays.asList(new MoveDescriptor(MoveType.ACCEPT))
			)
		);
		
		return new ParsingTableDescriptor(actionDescriptors, null, 0);
		
		//return null;
	}

	static CfgProductionRule<String> createRule(
			Variable<String> var, Variable<String>... rightSideSymbols) {
		return new CfgProductionRule<String>(var, rightSideSymbols);
	}

	public static void main(String[] args) throws JClassAlreadyExistsException, 
	IOException {
		new ParsingTableFactoryCodeGenerator(INSTANCE.createDescriptorManually())
		.generateSourceFile();
	}
	
	static Variable<String> createVar(String name) {
		return new Variable<String>(name);
	}
	
	
	static Variable<String> createTerminal(String name) {
		return new Variable<String>(name);
		//return new Variable<String>(""+ParserTokens.valueOf(name));
	}
}
