/*
 * LRItem.java
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
package hr.fer.spocc.parser.gen;

import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.Variable;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Stavka LR gramatike.<br>
 * Ima sve sto i produkcija kontekstno neovisne gramatike i jos pamti poziciju
 * ispred koje je tocka, koja se moze dobiti pozivom metode
 * {@link #getDotIndex()}.<br>
 * Stavke se mogu iz produkcije kontekstno neovisne gramatike pozivom
 * metode {@link #toLREntries(CfgProductionRule)}.
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public class LRItem<T> extends CfgProductionRule<T> {

	private final int dotIndex;
	
	public LRItem(Symbol<T> leftSideSymbol,
			List<Symbol<T>> rightSideSymbols,
			int dotIndex) {
		super(leftSideSymbol, rightSideSymbols);
		this.dotIndex = dotIndex;
		checkBounds();
	}
	
	public LRItem(Variable<T> leftSideSymbol,
			int dotIndex,
			Symbol<T>... rightSideSymbols) {
		super(leftSideSymbol, rightSideSymbols);
		this.dotIndex = dotIndex;
		checkBounds();
	}

	public int getDotIndex() {
		return dotIndex;
	}

	public boolean isComplete() {
		return this.dotIndex == getRightSide().size();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (this == null)
			return false;
		if (!(obj instanceof LRItem<?>))
			return false;
		@SuppressWarnings("unchecked")
		LRItem<T> other = (LRItem<T>) obj;
		return getDotIndex() == other.getDotIndex() && super.equals(obj); 
	}
	
	@Override
	public int hashCode() {
		return getDotIndex() ^ super.hashCode();
	}

	@Override
	public String toString() {
		final char dotCharacter = '•';
		StringBuilder sb = new StringBuilder();
		sb.append(getLeftSideSymbol().toString());
		sb.append(" ::= ");
		if (!isEpsilon()) {
			int ind = 0;
			for (Symbol<T> s : getRightSide()) {
				if (ind++ == getDotIndex())
					sb.append(dotCharacter);
				sb.append(s.toString());
			}
		}
		if (isComplete())
			sb.append(dotCharacter);
		return sb.toString();
	}
	
	/**
	 * Vraca skup svih mogucih
	 * Npr. za &lt;A&gt; ::= a&lt;B&gt;&lt;C&gt; vraca sljedece:
	 * <pre>
	 * &lt;A&gt; ::= *a&lt;B&gt;&lt;C&gt;
	 * &lt;A&gt; ::= a*&lt;B&gt;&lt;C&gt;
	 * &lt;A&gt; ::= a&lt;B&gt;*&lt;C&gt;
	 * &lt;A&gt; ::= a&lt;B&gt;&lt;C&gt;*
	 * </pre>
	 * 
	 * @param <T>
	 * @param cfgRule
	 * @return
	 */
	public static <T> Collection<LRItem<T>> toLREntries(
			CfgProductionRule<T> cfgRule) {
		int size = cfgRule.getRightSideSize(); 
		ArrayList<LRItem<T>> ret = new ArrayList<LRItem<T>>(size);
		for (int i = 0; i <= size; ++i)
			ret.add(new LRItem<T>(cfgRule.getLeftSideSymbol(),
					cfgRule.getRightSide(), i));
		return ret;
	}
	
	private void checkBounds() throws IllegalArgumentException {
		if (getDotIndex() < 0 || getDotIndex() > getRightSide().size())
			throw new IllegalArgumentException(
					"Dot index must be from range [0, "
					+ getRightSide().size() + "], but is " + getDotIndex());
	}
	
}
