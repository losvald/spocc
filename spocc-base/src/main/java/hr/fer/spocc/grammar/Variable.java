/*
 * Variable.java
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
 * Nezavrsni znak gramatike.
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public final class Variable<T> implements Symbol<T> {
	private final String name;
	
	public Variable(String name) {
		Validate.notNull(name);
		this.name = name;
	}

	@Override
	public SymbolType getSymbolType() {
		return SymbolType.VARIABLE;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable<?> other = (Variable<?>) obj;
		return name.equals(other.name);
	}
	
	@Override
	public String toString() {
		return "<" + BnfEscaper.getInstance().escape(this.name) + ">";
	}
	
}
