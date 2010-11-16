/*
 * RegularExpressionSymbol.java
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
package hr.fer.spocc.regex;

import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.SymbolType;

/**
 * Simbol koji cini regularni izraz. Tip simbola moze se dobiti
 * pozivom metode {@link #getSymbolType()}, a on moze biti:
 * {@link SymbolType#TERMINAL} -
 * <ul>
 * <li>{@link SymbolType#TERMINAL} - ako se radi o obicnom simbolu</li>
 * <li>{@link SymbolType#EPSILON} - ako se radi o epsilon simbolu</li>
 * </ul>
 * 
 * @author Leo Osvald
 * 
 * @param <T>
 */
public final class RegularExpressionSymbol<T> 
implements RegularExpressionElement, Symbol<T> {
	
	private final T value;
	
	private static final RegularExpressionSymbol<Object> EPSILON_SYMBOL
	= new RegularExpressionSymbol<Object>(null);
	
	public RegularExpressionSymbol(T value) {
		this.value = value;
	}
	
	@Override
	public RegularExpressionElementType getElementType() {
		return RegularExpressionElementType.SYMBOL;
	}

	@Override
	public SymbolType getSymbolType() {
		return getValue() != null ? SymbolType.TERMINAL : SymbolType.EPSILON;
	}
	
	/**
	 * Vraca vrijednost simbola.
	 * 
	 * @return vrijednost simbola ili <code>null</code> ako se radi
	 * o epsilon simbolu.
	 */
	public T getValue() {
		return value;
	}
	
	/**
	 * Provjerava da li se radi o epsilon znaku.
	 * 
	 * @return <code>true</code> ako je znak epsilon znak, <code>false</code>
	 * inace.
	 */
	public boolean isEpsilon() {
		return getSymbolType() == SymbolType.EPSILON;
	}
	
	@Override
	public int hashCode() {
		return 31 + ((value == null) ? 0 : value.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegularExpressionSymbol<?> other = (RegularExpressionSymbol<?>) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getValue() != null ? getValue().toString() : null;
	}

	@SuppressWarnings("unchecked")
	public static <T> RegularExpressionSymbol<T> epsilonSymbol() {
		return (RegularExpressionSymbol<T>) EPSILON_SYMBOL;
	}
}
