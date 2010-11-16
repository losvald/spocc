/*
 * RegularExpressionElements.java
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
 * Klasa s nekim elementima regularnih izraza.
 * 
 * @author Leo Osvald
 *
 */
public class RegularExpressionElements {

	public static final RegularExpressionElement LEFT_PARENTHESIS
	= new SingletonTypeRegularExpressionElement()  {
		@Override
		public RegularExpressionElementType getElementType() {
			return RegularExpressionElementType.LEFT_PARENTHESIS;
		}
		
		@Override
		public String toString() {
			return "(";
		}
	};
	
	public static final RegularExpressionElement RIGHT_PARENTHESIS
	= new SingletonTypeRegularExpressionElement() {
		@Override
		public RegularExpressionElementType getElementType() {
			return RegularExpressionElementType.RIGHT_PARENTHESIS;
		}
		
		@Override
		public String toString() {
			return ")";
		}
	};
	
	private static abstract class SingletonTypeRegularExpressionElement 
	implements RegularExpressionElement {

		@Override
		public int hashCode() {
			return 31 + ((getElementType() == null) ? 0 
					: getElementType().hashCode());
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SingletonTypeRegularExpressionElement other = 
				(SingletonTypeRegularExpressionElement) obj;
			if (getElementType() != other.getElementType())
				return false;
			return true;
		}
		
	}
}
