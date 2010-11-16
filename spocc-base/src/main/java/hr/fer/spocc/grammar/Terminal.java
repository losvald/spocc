/*
 * Terminal.java
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

import org.apache.commons.lang.Validate;

/**
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public final class Terminal<T> implements Symbol<T> {
	private final T value;
	
	public Terminal(T value) {
		Validate.notNull(value);
		this.value = value;
	}
	
	@Override
	public SymbolType getSymbolType() {
		return SymbolType.TERMINAL;
	}
	
	public T getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
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
		Terminal<T> other = (Terminal<T>) obj;
		return value.equals(other.value);
	}
	
	@Override
	public String toString() {
		return BnfEscaper.getInstance().escape(getValue().toString());
	}

}
