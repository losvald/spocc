/*
 * ParseTreeIOUtils.java
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

import hr.fer.spocc.Token;
import hr.fer.spocc.TokenType;
import hr.fer.spocc.grammar.Variable;
import hr.fer.spocc.util.CharacterEscaper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * @author Leo Osvald
 *
 */
public class ParseTreeIOUtils {

	public static String toString(ParseTreeNode node) {
		StringBuilder sb = new StringBuilder();
		toString(node, sb);
		return sb.toString();
	}
	
	public static String toString(ParseTree parseTree) {
		return toString(parseTree.getRoot());
	}

	public static ParseTree fromString(String s,
			Map<String, TokenType> tokenTypes) {
		ParseTree parseTree = new ParseTree();
		parseTree.setRoot(createNodes(s, tokenTypes));
		return parseTree;
	}

	private static void toString(ParseTreeNode node, StringBuilder sb) {
		if (node.isLeaf()) {
			sb.append(' ');
			sb.append(Escaper.INSTANCE.escape(node.getTokenType().toString()));
			return ;
		}
		sb.append(' ');
		sb.append('<');
		sb.append(Escaper.INSTANCE.escape(node.getVariable().getName()));
		sb.append('>');
		sb.append('(');
		for (int i = 0; i < node.getNumberOfChildren(); ++i)
			toString(node.getChild(i), sb);
		sb.append(' ');
		sb.append(')');
	}
	
	private static ParseTreeNode createNodes(String s,
			Map<String, TokenType> tokenTypes) {
		if (isTerminal(s)) {
			
			ParseTreeNode leaf = ParseTree.createNode();
//			System.err.println("["+s+"]");
			Token token = new Token(
					tokenTypes.get(Escaper.INSTANCE.unescape(s).trim()), 
					0, null);
//			System.err.println(Escaper.INSTANCE.unescape(s));
//			System.err.println(token);
			leaf.initialize(token); // XXX
			return leaf;
		}
		String var = StringUtils.substringBetween(s, "<", ">");
		int indOfOpenPar = s.indexOf('(', var.length() + 2);
		s = s.substring(indOfOpenPar);
		int indOfClosedPar = s.lastIndexOf(')');
		String rest = s.substring(1, indOfClosedPar);
//		System.out.println("Var: "+var);
//		System.out.println("Rest: "+rest);
		
		String[] tokens = rest.split("\\s+");
		
		List<String> merged = new ArrayList<String>();
		int k; 
		for (int i = 0; i < tokens.length; i = k + 1) {
			int cnt = 0;
			for (k = i; k < tokens.length; ++k) {
				if (k == i && !containsAnyParenthesis(tokens[k])) {
					break;
				}
				if (containsParenthesis(tokens[k], '(')) {
					++cnt;
				} else if (containsParenthesis(tokens[k], ')')) {
					if (--cnt <= 0) {
						break;
					}
				}
			}
			
			StringBuilder sb = new StringBuilder();
			for (int j = i; j <= k && j < tokens.length; ++j) {
				sb.append(tokens[j]).append(' ');
			}
			String part = sb.toString().trim();
			if (!part.isEmpty())
				merged.add(part);
		}

//		for (int i = 0; i < merged.size(); ++i) {
//			System.out.println(i+": "+merged.get(i));
//		}
		
		ParseTreeNode internal = ParseTree.createNode();
		internal.initialize(new Variable<String>(
				Escaper.INSTANCE.unescape(var)));
		
		
		for (String part : merged) {
			internal.addChild(createNodes(part, tokenTypes));
		}
		
		return internal;
	}
	
	private static boolean containsAnyParenthesis(String s) {
		return containsParenthesis(s, '(')
		|| containsParenthesis(s, ')');
	}
	
	private static boolean containsParenthesis(String s, char par) {
		Set<Integer> escapedIndexes = new HashSet<Integer>();
		String unescaped = Escaper.INSTANCE.unescape(s, escapedIndexes);
		for (int i = 0; i < unescaped.length(); ++i) {
			char c = unescaped.charAt(i);
			if (c == par && !escapedIndexes.contains(i))
				return true;
		}
		return false;
	}
	
	private static boolean isTerminal(String s) {
		return !s.startsWith("<") && !s.endsWith(")");
	}

	public static class Escaper extends CharacterEscaper {

		private static final char[] FROM = new char[]{
			'<', '>', '(', ')', ' ', '\n', '\t', '\\', '\r'
		};
		private static final char[] TO = new char[]{
			'<', '>', '(', ')', '_',  'n',  't', '\\', 'r'
		};

		private static final Escaper INSTANCE
		= new Escaper(FROM, TO);

		public Escaper(char[] from, char[] to) {
			super(from, to);
		}
	}
}
