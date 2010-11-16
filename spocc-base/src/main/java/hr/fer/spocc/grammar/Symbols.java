/*
 * Symbols.java
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

/**
 * 
 * @author Leo Osvald
 *
 */
public class Symbols {

	private static final Symbol<Object> EPSILON_SYMBOL = new EpsilonSymbol();
	
	private static final Symbol<Object> EOF_SYMBOL 
	= new EofSymbol();
	
	protected Symbols() {
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Symbol<T> epsilonSymbol() {
		return (Symbol<T>) EPSILON_SYMBOL;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Symbol<Character> characterEpsilonSymbol() {
		return (Symbol) EPSILON_SYMBOL;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Symbol<T> eofSymbol() {
		return (Symbol<T>) EOF_SYMBOL;
	}
	
	private static class EpsilonSymbol implements Symbol<Object> {
		
		@Override
		public SymbolType getSymbolType() {
			return SymbolType.EPSILON;
		}

		@Override
		public int hashCode() {
			return getSymbolType().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EpsilonSymbol other = (EpsilonSymbol) obj;
			if (getSymbolType() != other.getSymbolType())
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return "$";
		}
		
	}
	
	private static class EofSymbol implements Symbol<Object> {
		
		@Override
		public SymbolType getSymbolType() {
			return SymbolType.EOF;
		}

		@Override
		public int hashCode() {
			return getSymbolType().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EofSymbol other = (EofSymbol) obj;
			if (getSymbolType() != other.getSymbolType())
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return "⊥";
		}
		
	}
}
