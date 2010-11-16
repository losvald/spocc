/*
 * RegularExpressionOperator.java
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

/**
 * Operator regularnog izlaza. Moze biti
 * <ul>
 * <li>{@link #UNION} - binarni operator unije</li>
 * <li>{@link #CONCATENATION} - binarni operator nadovezivanja</li>
 * <li>{@link #STAR} - Kleenov operator (to je unarni operator)</li>
 * </ul>
 * @author Leo Osvald
 *
 */
public enum RegularExpressionOperator
implements RegularExpressionElement {
	UNION,
	CONCATENATION,
	STAR;
	
	public boolean isUnary() {
		return this == STAR;
	}
	
	@Override
	public RegularExpressionElementType getElementType() {
		return RegularExpressionElementType.OPERATOR;
	}
	
	public int getPriority() {
		switch (this) {
		case UNION:
			return 1;
		case CONCATENATION:
			return 2;
		case STAR:
			return 3;
		default:
			return 0;
		}
	}
	
	public int getArity() {
		return isUnary() ? 1 : 2;
	}
	
	@Override
	public String toString() {
		switch (this) {
		case UNION:
			return "|";
		case CONCATENATION:
			return "-";
		case STAR:
			return "*";
		default:
			return "";
		}
	}
	
}