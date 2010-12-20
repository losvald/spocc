package hr.fer.spocc.parser.gen;

import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;
import hr.fer.spocc.parser.MoveType;
import hr.fer.spocc.parser.ParsingTable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.Validate;

public class HardcodedCParsingTableDescriptorFactory 
implements ParsingTableDescriptorFactory {
	
	private static final String DEFAULT_PROPERTIES_DIRECTORY 
	= "src/main/resources";
	
	ParsingTable<String> parsingTable;
	
	public HardcodedCParsingTableDescriptorFactory() {
	}
	
	void addActions(Collection<ActionDescriptor> actionDescriptors,
			String fileName) throws IOException {
		fileName = DEFAULT_PROPERTIES_DIRECTORY
		+ "/" + fileName;
//		System.out.println("Reading file: "+fileName);
		BufferedReader br = openReader(fileName);
		
		int lineNum = 0;
		for (String line; (line = br.readLine()) != null; ++lineNum) {
//			System.out.println(">> Line: "+(lineNum+1));
//			if (lineNum > 2) break;
			if (line.isEmpty())
				continue;
			
			String[] keyActions = line.split("\\s=\\s");
			Validate.isTrue(keyActions.length == 2);
			
			String key = keyActions[0];
			
			String[] stateSymbol = key.split("\\s+");
			Validate.isTrue(stateSymbol.length == 2);
			int stateId = Integer.parseInt(stateSymbol[0]);
			Symbol<String> symbol = GrammarIOUtils.toSymbol(stateSymbol[1]);

//			System.out.println("StateId: "+stateId);
//			System.out.println("Symbol: "+symbol);
//			System.out.println("SymbolClass: "+symbol.getClass());
			
			List<MoveDescriptor> moveDescriptors 
			= new ArrayList<MoveDescriptor>();
			
			String actions = keyActions[1];
			String[] movesStr = actions.split("\\&");
			
//			System.out.println(ArrayToStringUtils.toString(movesStr));
			
			for (String moveStr : movesStr) {
				moveStr = moveStr.trim();
//				System.out.println("Move: "+moveStr);
				String[] tokens = moveStr.split("\\s+");
				Validate.isTrue(tokens.length >= 1);
				String moveTypeStr = tokens[0];
//				System.out.println("Command: " + moveTypeStr);
				if (moveTypeStr.equals("push")) {
					int pushState = Integer.parseInt(tokens[1]);
//					System.out.println("Push state: "+pushState);
					moveDescriptors.add(new MoveDescriptor(
							MoveType.PUSH,
							pushState));
				} else if (moveTypeStr.equals("shift")) {
					moveDescriptors.add(new MoveDescriptor(
							MoveType.SHIFT));
				} else if (moveTypeStr.equals("accept")) {
					moveDescriptors.add(new MoveDescriptor(
							MoveType.ACCEPT));
				} else if (moveTypeStr.equals("reduce")) {
					StringBuilder cfgRuleStr = new StringBuilder();
					for (int j = 1; j < tokens.length; ++j)
						cfgRuleStr.append(tokens[j]).append(' ');
					CfgProductionRule<String> cfgRule 
					= GrammarIOUtils.toRule(cfgRuleStr.toString());
//					System.out.println("Rule: " + cfgRule);
					moveDescriptors.add(new MoveDescriptor(
							MoveType.REDUCE,
							cfgRule));
				} else {
					throw new IllegalArgumentException("Invalid move: "
							+moveStr);
				}
			}
			
			actionDescriptors.add(new ActionDescriptor(
					stateId,
					symbol,
					moveDescriptors));
		}
	}
	
	ParsingTable<String> getParsingTable() {
		return parsingTable;
	}
	
	static BufferedReader openReader(String fileName) throws IOException {
		return new BufferedReader(
				new InputStreamReader(
				new BufferedInputStream(
						new FileInputStream(new File(fileName)))));
	}

	@Override
	public ParsingTableDescriptor createDescriptor() {
		Collection<ActionDescriptor> actionDescriptors
		= new ArrayList<ActionDescriptor>();
		try {
			addActions(actionDescriptors, "reduction.properties");
//			System.err.println(ArrayToStringUtils.toString(actionDescriptors.toArray(), "\n"));
			addActions(actionDescriptors, "newstates.properties");
			addActions(actionDescriptors, "push.properties");
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		
		
		return new ParsingTableDescriptor(
				actionDescriptors, null, 0);
	}
	
	void printDescriptor(ParsingTableDescriptor d) {
		
	}
	
}
