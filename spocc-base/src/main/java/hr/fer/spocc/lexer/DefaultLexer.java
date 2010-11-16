/*
 * DefaultLexer.java
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

import hr.fer.spocc.Environment;
import hr.fer.spocc.SymbolTable;
import hr.fer.spocc.SymbolTableRow;
import hr.fer.spocc.Token;
import hr.fer.spocc.TokenType;
import hr.fer.spocc.Tokenizer;
import hr.fer.spocc.util.DefaultFileContentGrabber;
import hr.fer.spocc.util.DummyTokenizer;
import hr.fer.spocc.util.FileContentGrabber;

import java.io.IOException;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.Validate;
import org.sglj.util.HashMultiMap;
import org.sglj.util.MultiMap;
import org.sglj.util.Pair;

/**
 * Defaultna implementacija leksickog analizatora.
 * 
 * @author Leo Osvald
 *
 */
public class DefaultLexer extends AbstractLexer {

	private final SortedSet<LexicalRule> possibleRules
	= new TreeSet<LexicalRule>();
	
	private SymbolTable symbolTable = new SymbolTable();
	private TokenList tokenList = new TokenList();
	
	private int pos;
	
	private State state;
	
	private LexicalRule matchingRule;
	
	private boolean errorFlag;
	
	private final MultiMap<State, LexicalRule> rulesByState 
	= new HashMultiMap<State, LexicalRule>();
	
	private Tokenizer innerTokenizer;
	
	public DefaultLexer(LexerDescriptor descriptor) {
		super(descriptor);
		Validate.notNull(descriptor);
		for (LexicalRule lexicalRule : descriptor.getLexicalRules()) {
			if (lexicalRule.getState().isStart())
				state = lexicalRule.getState();
			rulesByState.put(lexicalRule.getState(), lexicalRule);
		}
		if (Environment.isDevelopment())
			System.err.println("Start state: "+state);
	}
	
	@Override
	public void enterState(State newState) {
		state = newState;
		if (Environment.isDevelopment())
			System.out.println("Usao u stanje: "+state);
	}

	@Override
	public void tokenizeFirst(TokenType tokenType, int n) {
		if (tokenType != null) {
			if (Environment.isDevelopment())
				System.out.println("New token " + n + "["+from+" "+to+"): "+tokenType);
			addToken(tokenType, from, from + n);
		} else {
			if (Environment.isDevelopment())
				System.out.println("No token " + n + "["+from+" "+to+")");
		}
		pos = (from += n);
		prepareForNextToken();
	}

	@Override
	public Token nextToken() {
		return innerTokenizer.nextToken();
	}

	@Override
	public void reset() {
		errorFlag = false;
		if (innerTokenizer != null)
			innerTokenizer.reset();
	}
	
	@Override
	public boolean isError() {
		return errorFlag;
	}
	
	@Override
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}
	
	@Override
	protected FileContentGrabber createSourceReader() {
		return new DefaultFileContentGrabber();
	}
	
	@Override
	protected Pair<SymbolTable, TokenList> tokenizeAll(
			) throws IOException {
		symbolTable = new SymbolTable();
		tokenList = new TokenList();
		
		from = to = pos = 0;
		prepareForNextToken();
		for (Character c; ; ) {
			c = getSourceReader().getCharAt(pos++);
			
//			System.out.println("----------");
//			System.out.println("Read("+c+"): "+pos+" ["+from+" "+to+") State = "
//					+ state);
			boolean anyMatched = false;
			
			if (c != null) {
				Iterator<LexicalRule> it = possibleRules.iterator();
				while (it.hasNext()) {
					LexicalRule rule = it.next();
					if (!rule.getAutomaton().process(c)) {
						//System.out.println("izletio "+rule.getPriority());
						it.remove();
						continue;
					}
					if (rule.getAutomaton().isAccept()) {
						//System.out.println("Prihvaca "+rule.getPriority());
						if (!anyMatched) {
							matchingRule = rule;
							anyMatched = true;
						}
					}
				}
				
				if (anyMatched)
					to = pos;
			}
			
			if (c == null || (!anyMatched && possibleRules.isEmpty())) {
				if (matchingRule == null) { // leksicka greska
					errorFlag = true;
					if (c != null) {
						fireLexicalError(new LexicalError(lineNumber, 
								"Lexical error at character "
								+ getSourceReader().getCharAt(from)));
						
						discardCharacter(getSourceReader().getCharAt(from));
					} else {
						fireLexicalError(new LexicalError(lineNumber, 
								"Lexical error at the end of input "));
					}
					prepareForNextToken();
					pos = ++from;
					
					if (isEndOfFile(from-1))
						break;
				} else {
					if (Environment.isDevelopment())
						System.out.println("Perform: "+matchingRule.getPriority());
					matchingRule.getAction().perform(this);
				}
			}
			
			if (c == null)
				break;
		}
		innerTokenizer = new DummyTokenizer(tokenList);
		return new Pair<SymbolTable, TokenList>(symbolTable, tokenList);
	}
	
	private void prepareForNextToken() {
		possibleRules.addAll(rulesByState.getAll(state));
		for (LexicalRule rule : possibleRules) {
			rule.getAutomaton().reset();
		}
		matchingRule = null;
	}
	
	private void addToken(TokenType tokenType, int from, int to) {
		if (from == to)
			return ;
		
		// dodaj u tablicu znakova
		SymbolTableRow symbolTableRow = new SymbolTableRow(
				tokenType, formToken(from, to));
		SymbolTable.Key key = symbolTable.addRow(symbolTableRow);
		
		// dodaj u listu uniformnih znakova
		tokenList.add(tokenType, lineNumber, symbolTable.getRow(key));
	}
	
	private void discardCharacter(char c) {
		System.out.printf("Discarding character '%c' at %d : %d\n", c, 
				lineNumber, 0); // TODO implement column count
	}
	
	private boolean isEndOfFile(int pos) {
		return getSourceReader().getCharAt(pos) == null;
	}
	
}
