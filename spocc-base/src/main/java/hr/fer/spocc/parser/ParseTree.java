/*
 * ParseTree.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Leo Osvald
 *
 */
public class ParseTree {
	
	private ParseTreeNode root;
	
	public ParseTreeNode getRoot() {
		return root;
	}
	
	public void setRoot(ParseTreeNode root) {
		this.root = root;
	}
	
	public List<Token> linearize() {
		List<Token> list = new ArrayList<Token>();
		linearize(getRoot(), list);
		return list;
	}
	
	private void linearize(ParseTreeNode node,
			final List<Token> list) {
		if (node.isLeaf()) {
			list.add(node.getToken());
			return ;
		}
		for (ParseTreeNode child : node.getChildren()) {
			linearize(child, list);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((root == null) ? 0 : root.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParseTree other = (ParseTree) obj;
		if (root == null) {
			if (other.root != null)
				return false;
		} else if (!root.equals(other.root))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ParseTreeIOUtils.toString(this);
	}
	
	public static ParseTreeNode createNode() {
		return new Node();
	}
	
	private static class Node implements ParseTreeNode {

		Object val;
		List<ParseTreeNode> children;
		boolean internal;
		
		@Override
		public void initialize(Token token) {
			this.val = token;
			this.internal = false;
			this.children = null;
		}
		
		@Override
		public void initialize(Variable<?> variable) {
			this.val = variable;
			this.internal = true;
			if (this.children == null) {
				this.children = new ArrayList<ParseTreeNode>();
			}
		}
		
		@Override
		public void initialize(ParseTreeNode node) {
			if (node.isLeaf()) {
				this.val = node.getToken();
				this.internal = false;
				this.children = null;
			} else {
				this.val = getVariable();
				this.internal = true;
				this.children = new ArrayList<ParseTreeNode>(
						node.getChildren());
			}
		}
		
		@Override
		public void addChild(ParseTreeNode node) {
			this.children.add(node);
		}
		
		@Override
		public int getNumberOfChildren() {
			return this.children.size();
		}
		
		@Override
		public ParseTreeNode getChild(int index) {
			return this.children.get(index);
		}
		
		@Override
		public List<ParseTreeNode> getChildren() {
			return Collections.unmodifiableList(this.children);
		}
		
		@Override
		public boolean isLeaf() {
			return !this.internal;
		}
		
		@Override
		public Token getToken() {
			checkLeaf();
			return (Token) val;
		}
		
		@Override
		public Variable<?> getVariable() {
			return (Variable<?>) this.val;
		}
		
		@Override
		public TokenType getTokenType() {
			return getToken().getTokenType();
		}
		
		@Override
		public String getTokenText() {
			return getToken().getSymbolTableRow().getLexeme();
		}
		
		@Override
		public int getLineNumber() {
			return getToken().getLineNumber();
		}
		
		@Override
		public ParseStackElementType getType() {
			return isLeaf() ? ParseStackElementType.TERMINAL
					: ParseStackElementType.VARIABLE;
		}
		
		private void checkLeaf() throws IllegalStateException {
			if (!isLeaf())
				throw new IllegalStateException(
						"Internal node cannot be token");
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((children == null) ? 0 : children.hashCode());
			result = prime * result + (internal ? 1231 : 1237);
			result = prime * result + ((val == null) ? 0 : val.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (children == null) {
				if (other.children != null)
					return false;
			} else if (!children.equals(other.children))
				return false;
			if (internal != other.internal)
				return false;
			if (val == null) {
				if (other.val != null)
					return false;
			} else if (!val.equals(other.val))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Node [val=" + val + ", childrenCount=" 
			+ (children != null ? children.size() : "0")
					+ ", internal=" + internal + "]";
		}
		
	}
	
}
