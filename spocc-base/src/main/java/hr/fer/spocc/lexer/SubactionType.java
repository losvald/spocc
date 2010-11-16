/*
 * SubactionType.java
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
package hr.fer.spocc.lexer;

/**
 * Tip argumenata akcije (podakcije) leksickog pravila:
 * <ul>
 * <li>{@link #SKIP} - procitani dio niza nije bitan za leksicku analizu</li>
 * <li>{@link #NEW_LINE} - specijalna akcija "NOVI_REDAK" - u izvornoj 
 * datoteci doslo je do promjene retka</li>
 * <li>{@link #ENTER_STATE} = "UDJI_U_STANJE" leksicki analizator treba prijeci iz
 * jednog stanja u drugo</li>
 * <li>{@link #TOKENIZE} - stvara se nova jedinka od svih znakova
 * koji zadovoljavaju leksicko pravilo 
 * grupira odredjeni broj znakova u leksicku jedinku</li>
 * <li>{@link #TOKENIZE_FIRST} = "VRATI_SE" - leksicki analizator 
 * grupira prvih n znakova u leksicku jedinku</li>
 * </ul>
 * 
 * @author Leo Osvald
 *
 */
public enum SubactionType {
	SKIP,
	NEW_LINE(true),
	ENTER_STATE(true),
	TOKENIZE(true),
	TOKENIZE_FIRST(true),
	CUSTOM;
	
	private final boolean special;
	
	private SubactionType() {
		this(false);
	}
	
	private SubactionType(boolean special) {
		this.special = special;
	}
	
	public boolean isSpecial() {
		return special;
	}
	
	public boolean isCustom() {
		return this == CUSTOM;
	}
	
	public boolean isBuiltIn() {
		return !isCustom();
	}
	
}
