/*
 * ArrayToStringUtils.java
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
package org.sglj.util;

/**
 * Utility class for building toString from arrays.
 * 
 * @author Leo Osvald
 *
 */
public abstract class ArrayToStringUtils {
	
	public static final String DEFAULT_BEGIN = "(";
	public static final String DEFAULT_END = ")";
	public static final String DEFAULT_SEPARATOR = ", ";
	public static final String DEFAULT_NULL_TO_STRING = "(null)";
	
	public static String toString(Object[] arr) {
		return toString(arr, DEFAULT_SEPARATOR,
				DEFAULT_BEGIN, DEFAULT_END,
				DEFAULT_NULL_TO_STRING);
	}
	
	public static String toString(Object[] arr, String separator) {
		return toString(arr, separator, DEFAULT_BEGIN, DEFAULT_END,
				DEFAULT_NULL_TO_STRING);
	}
	
	public static String toString(Object[] arr, String separator, 
			String nullToString) {
		return toString(arr, separator, DEFAULT_BEGIN, DEFAULT_END,
				nullToString);
	}
	
	public static String toString(Object[] arr, CharSequence separator, 
			CharSequence begin, CharSequence end) {
		return toString(arr, separator, begin, end, DEFAULT_NULL_TO_STRING);
	}
	
	public static String toString(Object[] arr, CharSequence separator, 
			CharSequence begin, CharSequence end, String nullToString) {
		if (arr == null)
			return nullToString;
		
		StringBuilder sb = new StringBuilder(begin);
		for (int i = 0; i < arr.length; ++i) {
			if (i > 0) sb.append(separator);
			sb.append(arr[i] != null ? arr[i].toString() : nullToString);
		}
		return sb.append(end).toString();
	}
	
}
