/*
 * ProductionRule.java
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
package hr.fer.spocc.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Produkcija gramatike.<br>
 * Epsilon simboli bit ce izbaceni iz lijeve i desne strane. 
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public class ProductionRule<T> {
	private final List<Symbol<T>> leftSide;
	private final List<Symbol<T>> rightSide;
	
	public ProductionRule(List<Symbol<T>> leftSide, List<Symbol<T>> rightSide) {
		List<Symbol<T>> filteredLeftSide = new ArrayList<Symbol<T>>(
				leftSide.size());
		for (Symbol<T> s : leftSide)
			if (s.getSymbolType() != SymbolType.EPSILON)
				filteredLeftSide.add(s);
		this.leftSide = Collections.unmodifiableList(filteredLeftSide);

		List<Symbol<T>> filteredRightSide = new ArrayList<Symbol<T>>(
				rightSide.size());
		for (Symbol<T> s : rightSide)
			if (s.getSymbolType() != SymbolType.EPSILON)
				filteredRightSide.add(s);
		this.rightSide = Collections.unmodifiableList(filteredRightSide);
	}
	
	@SuppressWarnings("unchecked")
	public ProductionRule(Symbol<T> leftSideSymbol, List<Symbol<T>> rightSide) {
		if (leftSideSymbol.getSymbolType() != SymbolType.EPSILON) {
			this.leftSide =  Collections.unmodifiableList(
					Arrays.asList(leftSideSymbol));
		} else {
			this.leftSide = Collections.EMPTY_LIST;
		}
		
		List<Symbol<T>> filteredRightSide = new ArrayList<Symbol<T>>(
				rightSide.size());
		for (Symbol<T> s : rightSide)
			if (s.getSymbolType() != SymbolType.EPSILON)
				filteredRightSide.add(s);
		
		this.rightSide = Collections.unmodifiableList(filteredRightSide);
	}
	
	@SuppressWarnings("unchecked")
	public ProductionRule(Symbol<T> leftSideSymbol, 
			Symbol<T>... rightSideSymbols) {
		this.leftSide =  Collections.unmodifiableList(
				Arrays.asList(leftSideSymbol));
		this.rightSide =  Collections.unmodifiableList(
				Arrays.asList(rightSideSymbols));
	}

	/**
	 * Vraca lijevu stranu produkcije.<br> Ova metoda ne stvara listu
	 * isponova, pa ima vremensku slozenost O(1).
	 * 
	 * @return nepromjenjiva lista
	 */
	public List<Symbol<T>> getLeftSide() {
		return leftSide;
	}

	/**
	 * Vraca desnu stranu produkcije.<br> Ova metoda ne stvara listu
	 * isponova, pa ima vremensku slozenost O(1).
	 * 
	 * @return nepromjenjiva lista
	 */
	public List<Symbol<T>> getRightSide() {
		return rightSide;
	}
	
	public Symbol<T> getLeftSideSymbol(int index) {
		return this.leftSide.get(index);
	}
	
	public Symbol<T> getRightSideSymbol(int index) {
		return this.rightSide.get(index);
	}
	
	public int getLeftSideSize() {
		return this.leftSide.size();
	}
	
	public int getRightSideSize() {
		return this.rightSide.size();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((leftSide == null) ? 0 : leftSide.hashCode());
		result = prime * result
				+ ((rightSide == null) ? 0 : rightSide.hashCode());
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
		@SuppressWarnings("unchecked")
		ProductionRule<T> other = (ProductionRule<T>) obj;
		if (leftSide == null) {
			if (other.leftSide != null)
				return false;
		} else if (!leftSide.equals(other.leftSide))
			return false;
		if (rightSide == null) {
			if (other.rightSide != null)
				return false;
		} else if (!rightSide.equals(other.rightSide))
			return false;
		return true;
	}
	
}
