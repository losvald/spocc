/*
 * CharacterEscaper.java
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
package hr.fer.spocc.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

/**
 * Generic character escaper and unescaper. The escape mappings
 * are specified through the constructor.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public class CharacterEscaper {

	protected final Map<Character, Character> escapeMap;
	protected final Map<Character, Character> unescapeMap;
	
	private final char escapeCharacter;
	
	/**
	 * Creates an escaper which escapes with the specified character.
	 * The <tt>from</tt> array is used to determine
	 * which characters should be escaped, and the corresponding
	 * element in the <tt>to</tt> array is used after the escape character
	 * in the escaped string.
	 * 
	 * @param from the array of escapable characters
	 * @param to the array of the escaped values for the escapable character
	 * (order is relevant)
	 * @param escapeCharacter the character used for escaping
	 */
	public CharacterEscaper(char[] from, char[] to, char escapeCharacter) {
		Validate.notNull(from);
		Validate.notNull(to);
		Validate.isTrue(from.length == to.length);
		
		Map<Character, Character> modifiableEscapeMap 
		= new HashMap<Character, Character>(from.length);
		Map<Character, Character> modifiableUnescapeMap 
		= new HashMap<Character, Character>(from.length);
		for (int i = 0; i < from.length; ++i) {
			Validate.isTrue(!modifiableEscapeMap.containsKey(from[i])
					&& !modifiableUnescapeMap.containsKey(to[i]),
					"Mapping must be a bijection");
			modifiableEscapeMap.put(from[i], to[i]);
			modifiableUnescapeMap.put(to[i], from[i]);
		}
		// ensure that the escape character is also escaped
		Validate.isTrue(modifiableEscapeMap.containsKey(escapeCharacter),
				"Escape character must also be escapable");
		
		this.escapeMap = Collections.unmodifiableMap(modifiableEscapeMap);
		this.unescapeMap = Collections.unmodifiableMap(modifiableUnescapeMap);
		this.escapeCharacter = escapeCharacter;
	}
	
	/**
	 * A convenient wrapper for the {@link #CharacterEscaper(char[], 
	 * char[], char)} constructor. Creates the escaper which uses
	 * backslash (\) for escaping.
	 * 
	 * @param from the array of escapable characters
	 * @param to the array of the escaped values for the escapable character
	 * (order is relevant)
	 */
	public CharacterEscaper(char[] from, char[] to) {
		this(from, to, '\\');
	}
	
	/**
	 * Returns the character used for escaping.
	 * 
	 * @return the escape character
	 */
	public char getEscapeCharacter() {
		return escapeCharacter;
	}
	
	public boolean needsEscape(char c) {
		return escapeMap.containsKey(c);
	}
	
	public String escape(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			if (needsEscape(c)) {
				sb.append(escapeCharacter);
			}
			Character escaped = escapeMap.get(c);
			sb.append(escaped != null ? escaped : c);
		}
		return sb.toString();
	}
	
	public boolean isUnescape(char c) {
		return unescapeMap.containsKey(c);
	}
	
	public String unescape(String s) throws IllegalArgumentException {
		return unescape(s, null);
	}
	
	/**
	 * Unescapes the given string. The indexes of the characters
	 * which were obtained by unescaping are added to the specified
	 * collection (which will be cleared beforehand).
	 * 
	 * @param s the string to be unescaped
	 * @param escapedIndexes the collection which will contain the indexes
	 * of the unescaped characters
	 * @return the unescaped string
	 * @throws IllegalArgumentException if the string cannot be unescaped
	 */
	public String unescape(String s, Collection<Integer> escapedIndexes) 
	throws IllegalArgumentException {
		if (escapedIndexes != null)
			escapedIndexes.clear();
		
		boolean escapeStarted = false;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			if (c == escapeCharacter && !escapeStarted) {
				escapeStarted = true;
				continue;
			}
			if (escapeStarted) {
				escapeStarted = false;
				Character unescaped = unescapeMap.get(c);
				Validate.notNull(unescaped, 
						"unknown unescape pattern after on position " + i 
						+ " in string: " + s);
				if (unescaped != null && escapedIndexes != null) {
					escapedIndexes.add(sb.length());
				}
				sb.append(unescaped != null ? unescaped : c);
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
}
