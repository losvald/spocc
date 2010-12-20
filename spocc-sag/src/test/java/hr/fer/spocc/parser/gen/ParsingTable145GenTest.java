package hr.fer.spocc.parser.gen;

import hr.fer.spocc.TokenType;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.Symbols;
import hr.fer.spocc.grammar.Terminal;
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
public class ParsingTable145GenTest extends ParsingTableGenTest {

	private static final ParsingTable145GenTest INSTANCE
	= new ParsingTable145GenTest();
	
	protected static final Symbol<String> SEQUENCE_TERMINATOR_SYMBOL
	= Symbols.eofSymbol();
	
	@Override
	ParsingTable<TokenType> createExpected() {
		return new ParserTest151().createParsingTable();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ParsingTableDescriptor createDescriptorManually() {
		CfgGrammar<String> parserGrammar = 
			new CfgGrammar<String>();
		
		// ova mapa tebi sluzi za laksu identifikaciju i dohvacanje produkcija
		final Map<String, CfgProductionRule<String>> productionMap
		= new HashMap<String, CfgProductionRule<String>>();
		
		productionMap.put("X->Sc", createRule(
				createVar("X"),
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
		
		ParsingTable<String> parsingTable
		= new ParsingTable<String>(0);
		
		List<ActionDescriptor> actionDescriptors = Arrays.asList(
				new ActionDescriptor(0, createTerminal("a"), 
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 3)
						)
				),
				new ActionDescriptor(0, createVar("S"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 1)
						)
				),
				new ActionDescriptor(0, createVar("A"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 2)
						)
				),
				new ActionDescriptor(0, createTerminal("a"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 3)
						)
				),
				new ActionDescriptor(1, createTerminal("c"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 4)
						)
				),
				new ActionDescriptor(1, createVar("A"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 5)
						)
				),
				new ActionDescriptor(2, createTerminal("a"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("S->A"))
						)
				),
				new ActionDescriptor(2, createTerminal("b"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("S->A"))
						)
				),
				new ActionDescriptor(2, createTerminal("c"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("S->A"))
						)
				),
				new ActionDescriptor(3, createTerminal("a"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 3)
						)
				),
				new ActionDescriptor(3, createTerminal("b"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 7)
						)
				),
				new ActionDescriptor(3, createVar("S"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 6)
						)
				),
				new ActionDescriptor(3, createVar("A"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 2)
						)
				),
				new ActionDescriptor(4, SEQUENCE_TERMINATOR_SYMBOL,
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("X->Sc")),
								new MoveDescriptor(MoveType.ACCEPT)
						)
				),
				new ActionDescriptor(5, createTerminal("a"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("S->SA"))
						)
				),
				new ActionDescriptor(5, createTerminal("b"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("S->SA"))
						)
				),
				new ActionDescriptor(5, createTerminal("c"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("S->SA"))
						)
				),
				new ActionDescriptor(6, createTerminal("a"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 3)
						)
				),
				new ActionDescriptor(6, createTerminal("b"),
						Arrays.asList(
								new MoveDescriptor(MoveType.SHIFT),
								new MoveDescriptor(MoveType.PUSH, 8)
						)
				),
				new ActionDescriptor(6, createVar("A"),
						Arrays.asList(
								new MoveDescriptor(MoveType.PUSH, 5)
						)
				),
				new ActionDescriptor(7, createTerminal("a"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("A->ab"))
						)
				),
				new ActionDescriptor(7, createTerminal("b"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("A->ab"))
						)
				),
				new ActionDescriptor(7, createTerminal("c"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("A->ab"))
						)
				),
				new ActionDescriptor(8, createTerminal("a"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("S->aSb"))
						)
				),
				new ActionDescriptor(8, createTerminal("b"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("S->aSb"))
						)
				),
				new ActionDescriptor(8, createTerminal("c"),
						Arrays.asList(
								new MoveDescriptor(MoveType.REDUCE,
										productionMap.get("S->aSb"))
						)
				)
		);
		
		return new ParsingTableDescriptor(actionDescriptors, null, 0);
	}

	static CfgProductionRule<String> createRule(
			Variable<String> var, Symbol<String>... rightSideSymbols) {
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
	
	
	static Terminal<String> createTerminal(String name) {
		return new Terminal<String>(name);
	}
}
