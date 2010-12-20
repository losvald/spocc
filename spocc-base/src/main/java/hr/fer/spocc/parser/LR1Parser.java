/*
 * LR1Parser.java
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


import hr.fer.spocc.Environment;
import hr.fer.spocc.Token;
import hr.fer.spocc.TokenType;
import hr.fer.spocc.Tokenizer;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.Symbols;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.Validate;
import org.sglj.util.ArrayStack;
import org.sglj.util.ArrayToStringUtils;
import org.sglj.util.Stack;

/**
 * LR1 parser.
 * 
 * @author Leo Osvald
 *
 */
public class LR1Parser extends AbstractParser {

	private final ParseStack parseStack = new ParseStack();
	
	private final Stack<Integer> stateStack = new ArrayStack<Integer>();
	
	private Token curToken;
	
	private boolean errorFlag;
	private boolean acceptFlag;
	
	private ParseTree parseTree;
	
	private int lastLine;
	
	public LR1Parser(Tokenizer tokenizer, 
			ParsingTable<TokenType> parsingTable) {
		setTokenizer(tokenizer);
		setParsingTable(parsingTable);
	}
	
	@Override
	public boolean parse() {
		Validate.notNull(getTokenizer());
		Validate.notNull(getParsingTable());
		
		reset();
		this.curToken = getTokenizer().nextToken();
		if (curToken == null)
			return true;
		
		this.acceptFlag = false;
		this.errorFlag = false;
		lastLine = 0;
		
		parseTree = new ParseTree();
		parseStack.clear();
		stateStack.clear();
		parseStack.push(ParseStackElements.EMPTY_STACK_ELEMENT);
		stateStack.push(getParsingTable().getStartStateId());
		
		
		while (!isOver()) {
			if (Environment.isDevelopment()) {
				System.out.println("-----------");
				System.out.println(this);
				System.out.println("Top: " + this.stateStack.top() + " | "
						+ curToken);
			}
			
			Symbol<TokenType> symbol;
			
			// ak je varijabla na vrhustoga
			if (parseStack.top().getType() == ParseStackElementType.VARIABLE
					&& stateStack.size() == parseStack.size() - 1) {
				ParseTreeNode node = (ParseTreeNode) parseStack.top();
				symbol = (Symbol<TokenType>) node.getVariable();
			} else {
				if (curToken != null)
					symbol = ParserSymbols.terminal(curToken.getTokenType());
				else
					symbol = Symbols.eofSymbol();
			}
			
			if (Environment.isDevelopment()) {
				System.out.println("TOP: type = "+symbol.getSymbolType()
						+ "; value = "+symbol);
			}
			
			Action action = getParsingTable().getAction(
					this.stateStack.top(),
					symbol);
			if (action != null) {
				action.perform(this);
			} else {
				error();
			}
		}
		
		if (this.acceptFlag) {
			parseTree.setRoot((ParseTreeNode) parseStack.top());
		} else if (!errorFlag && parseStack.size() > 1) {
			errorFlag = true;
			Token lastToken = findLastToken();
			if (lastToken != null) {
				System.err.println(lastToken.getLineNumber() + 
						": Syntax error after " 
						+ lastToken.getTokenType() + " token");
			} else {
				System.err.println("Unknown syntax error");
			}
		}
		
		return this.acceptFlag;
	}
	
	@Override
	public ParseTree getParseTree() {
		return parseTree;
	}
	
	@Override
	public void reduce(CfgProductionRule<TokenType> rule) {
		int popCount = rule.getRightSideSize();
		
		List<ParseStackElement> popList = parseStack.pop(popCount);
		
		// TODO provjeri dal je stvarno dobro popano
		
		for (int i = 0; i < popCount; ++i) {
			stateStack.pop();
		}
		
		// TODO sta ak je stackState prazan?
		
		ParseTreeNode node = ParseTree.createNode();
		node.initialize(rule.getLeftSideSymbol());
		for (ParseStackElement e : popList) {
			ParseTreeNode child = (ParseTreeNode) e;
			node.addChild(child);
		}
		parseStack.push(node);
	}
	
	@Override
	public void push(int stateId) {
		this.stateStack.add(stateId);
	}
	
	@Override
	public void shift() {
		if (curToken != null) {
			ParseTreeNode node = ParseTree.createNode();
			node.initialize(curToken);
			this.parseStack.push(node);
			lastLine = curToken.getLineNumber();
			
			Token readToken = getTokenizer().nextToken();
			this.curToken = readToken;
		}
	}
	
	@Override
	public void accept() {
		this.acceptFlag = true;
	}
	
	@Override
	public void error() {
		this.errorFlag = true;
		SyntaxError error;
		if (this.curToken != null) {
			System.err.println(curToken.getLineNumber()
					+ " : Parse error before " 
					+ curToken.getSymbolTableRow().getLexeme()
					+ " token.");
			error = new SyntaxError(curToken.getLineNumber(),
					"Parse error before "
					+ curToken.getSymbolTableRow().getLexeme()
					+ " token.");
		} else {
			System.err.println(lastLine 
					+ ": Syntax error at the end of input.");
			error = new SyntaxError(lastLine, 
					"Syntax error at the end of input.");
		}
		fireSyntaxError(error);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("State stack: ");
		sb.append(ArrayToStringUtils.toString(
				stateStack.toArray(), " ", "", ""));
		sb.append("\n");
		sb.append(parseStack);
		return sb.toString();
	}
	
	private static String toString(ParseStackElement e) {
		StringBuilder sb = new StringBuilder();
		if (e instanceof ParseTreeNode) {
			ParseTreeNode node = (ParseTreeNode) e;
			sb.append(ParseTreeIOUtils.toString(node));
		} else {
			sb.append(e);
		}
		sb.append("\n");
		return sb.toString();
	}
	
	private boolean isOver() {
		return this.acceptFlag || this.errorFlag;
	}
	
	private Token findLastToken() {
		if (curToken != null)
			return curToken;
		Token token = null;
		for (int i = parseStack.size() - 1; i >= 0; --i) {
			if (parseStack.get(i).getType()
					!= ParseStackElementType.EMPTY_STACK) {
				token = findLastToken((ParseTreeNode) parseStack.get(i));
			}
		}
		return token;
	}
	
	private Token findLastToken(ParseTreeNode node) {
		while (true) {
			if (node.getType() == ParseStackElementType.EMPTY_STACK)
				return null;
			if (node.getType() == ParseStackElementType.TERMINAL)
				return node.getToken();
			node = node.getChild(node.getNumberOfChildren() - 1);
		}
	}
	
	
	static class ParseStack extends ArrayStack<ParseStackElement> {
		
		private static final long serialVersionUID = 1L;

		public void add(List<ParseStackElement> elements) {
			ListIterator<ParseStackElement> it = elements.listIterator();
			while (it.hasPrevious())
				add(it.previous());
		}
		
		public void add(ParseStackElement[] elements) {
			for (int i = elements.length - 1; i >= 0; --i)
				add(elements[i]);
		}
		
		public List<ParseStackElement> pop(int count) {
			List<ParseStackElement> ret = new ArrayList<ParseStackElement>(
					count);
			while (count-- > 0) {
				ret.add(pop());
			}
			Collections.reverse(ret);
			return ret;
		}
		
		@Override
		public String toString() {
			List<ParseStackElement> list = new ArrayList<ParseStackElement>(
					this);
			Collections.reverse(list);
			StringBuilder sb = new StringBuilder();
			sb.append("Symbol stack: ");
			sb.append("\n");
			for (ParseStackElement e : list) {
				sb.append(LR1Parser.toString(e));
			}
			return sb.toString();
		}
		
	}

}
