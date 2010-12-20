/*
 * DotExporter.java
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
package hr.fer.spocc.export;

import hr.fer.spocc.parser.ParseTree;
import hr.fer.spocc.parser.ParseTreeNode;
import hr.fer.spocc.util.AbstractEntityWriter;
import hr.fer.spocc.util.CharacterEscaper;
import hr.fer.spocc.util.EntityWriter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * @author Leo Osvald
 *
 */
public class DotExporter {
	
	public static String DEFAULT_PARSE_TREE_GRAPH_NAME = "ParseTree";
	
	public static String toDotString(ParseTree parseTree) {
		return toDotString(parseTree, DEFAULT_PARSE_TREE_GRAPH_NAME);
	}
	
	public static void exportToFile(File file, ParseTree parseTree) 
	throws IOException {
		FILE_WRITER.print(parseTree, file);
	}
	
	public static String toDotString(ParseTree parseTree,
			String graphName) {
		Validate.isTrue(!graphName.isEmpty() 
				&& StringUtils.isAlphanumeric(graphName) 
				&& Character.isLetter(graphName.charAt(0)));
		StringBuilder dotString = new StringBuilder();
		dotString.append("digraph ").append(graphName).append(" {\n");
		Map<ParseTreeNode, String> nodePrefixMap 
		= new LinkedHashMap<ParseTreeNode, String>();
		toDotNodes(parseTree.getRoot(), new StringBuilder(), 
				nodePrefixMap, dotString);
		toDotEdges(parseTree.getRoot(), nodePrefixMap, dotString);
		dotString.append("}\n");
		return dotString.toString();
	}
	
	private static void toDotEdges(ParseTreeNode node,
			Map<ParseTreeNode, String> nodePrefixMap,
			StringBuilder dotString) {
		if (node.isLeaf())
			return ;
		for (int i = 0; i < node.getNumberOfChildren(); ++i) {
			ParseTreeNode child = node.getChild(i);
			toDotEdges(child, nodePrefixMap, dotString);
			dotString.append(toNodeName(nodePrefixMap.get(node)))
			.append(" -> ").append(toNodeName(nodePrefixMap.get(child)))
			.append(";\n");
		}
	}
	
	private static void toDotNodes(ParseTreeNode node, 
			StringBuilder curPrefix,
			Map<ParseTreeNode, String> prefixMap,
			StringBuilder dotString) {
		prefixMap.put(node, curPrefix.toString());
		dotString.append('"').append(curPrefix).append('"')
		.append(" [").append(" label = ")
		.append(toLabel(node))
		.append(" ];\n");
		if (!node.isLeaf()) {
			for (int i = 0; i < node.getNumberOfChildren(); ++i) {
				extendTreePrefix(curPrefix, i);
				toDotNodes(node.getChild(i), curPrefix, prefixMap, dotString);
				reduceTreePrefix(curPrefix);
			}
		}
	}
	
	private static void extendTreePrefix(StringBuilder prefix, int n) {
		prefix.append('.').append(Integer.toString(n));
	}
	
	private static void reduceTreePrefix(StringBuilder prefix) {
		int index = prefix.lastIndexOf(".");
		if (index != -1)
			prefix.delete(index, prefix.length());
	}
	
	private static String toNodeName(ParseTreeNode node) {
		return "\"" + (node.isLeaf() ? node.getToken().getTokenType().toString()
				: node.getVariable().toString()) + "\"";
	}
	
	private static String toLabel(ParseTreeNode node) {
		return "\"" + (node.isLeaf() ? node.getToken().getTokenType().toString()
				+ "\\n" + DotLabelEscaper.getInstance().escape(
						node.getToken().getSymbolTableRow().getLexeme())
				: node.getVariable().toString()) + "\"";
	}
	
	private static String toNodeName(String prefix) {
		return "\"" + prefix + "\"";
	}
	
	private static EntityWriter<ParseTree> FILE_WRITER 
	= new AbstractEntityWriter<ParseTree>() {

		@Override
		public boolean print(ParseTree entity, PrintStream printStream) {
			printStream.append(toDotString(entity));
			return true;
		}
	};
	
	public static class DotLabelEscaper extends CharacterEscaper {

		private static final char[] FROM = new char[]{
			'"', '\n', '\t', '\r', '\\'
		};
		private static final char[] TO = new char[]{
			'"', 'n',  't', 'r', '\\'
		};
		
		private static final DotLabelEscaper INSTANCE
		= new DotLabelEscaper(FROM, TO);
		
		public DotLabelEscaper(char[] from, char[] to) {
			super(from, to);
		}

		public static DotLabelEscaper getInstance() {
			return INSTANCE;
		}
		
	}
}
