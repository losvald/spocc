/*
 * LR1Item.java
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sglj.util.ArrayToStringUtils;

/**
 * LR1 stavka. 
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public class LR1Item<T> extends LRItem<T> {

	private final Set<Symbol<T>> followingTerminals;
	
	public LR1Item(Symbol<T> leftSideSymbol, List<Symbol<T>> rightSideSymbols,
			int dotIndex, Collection<Symbol<T>> followingTerminals) {
		super(leftSideSymbol, rightSideSymbols, dotIndex);
		this.followingTerminals = Collections.unmodifiableSet(
				new HashSet<Symbol<T>>(followingTerminals));
	}
	
	public LR1Item(Variable<T> leftSideSymbol, int dotIndex,
			Collection<Symbol<T>> followingTerminals,
			Symbol<T>... rightSideSymbols) {
		super(leftSideSymbol, dotIndex, rightSideSymbols);
		this.followingTerminals = Collections.unmodifiableSet(
				new HashSet<Symbol<T>>(followingTerminals));
	}
	
	public boolean isFollowingTerminal(Symbol<T> terminal) {
		return this.followingTerminals.contains(terminal);
	}
	
	public Set<Symbol<T>> getFollowingSymbols() {
		return followingTerminals;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (this == null)
			return false;
		if (!(obj instanceof LR1Item<?>))
			return false;
		@SuppressWarnings("unchecked")
		LR1Item<T> other = (LR1Item<T>) obj;
		return getFollowingSymbols().equals(other.getFollowingSymbols()) 
		&& super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() ^ getFollowingSymbols().hashCode();
	}
	
	@Override
	public String toString() {
		return super.toString() + ", "
		+ ArrayToStringUtils.toString(getFollowingSymbols().toArray(),
				", ", "{", "}");
	}
	
}
