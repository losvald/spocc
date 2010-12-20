/*
 * BasePropertyConstants.java
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


import org.apache.commons.lang.StringUtils;
import org.sglj.util.StringBuilderUtils;

/**
 * Base class for holding property constants.
 * Property names should conform to the following rules:
 * <ul>
 * <li>must not be an empty or <code>null</code> string</li>
 * <li>must not contain whitespace characters</li>
 * <li>must not contain the equals sign ('=')</li>
 * <li>if consist of several parts, all parts must be separated by
 * {@link #SEPARATOR}</li>
 * </ul>
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public abstract class BasePropertyConstants {
	
	protected static final String SEPARATOR = ".";
	
	/**
	 * Return the property name based on the specified prefix.
	 * For example, if the prefix is "some.example." or "some.example", 
	 * "some.example" will be returned. 
	 * 
	 * @param prefix the prefix
	 * @return property name
	 */
	protected static String prefixToPropertyName(String prefix) {
		return StringUtils.substringBeforeLast(prefix, SEPARATOR);
	}
	
	/**
	 * Returns the relative name of the property; for example, relative
	 * name of the "some.example" is "example", but relative name
	 * of the "test" is again "test".
	 * 
	 * @param propertyName apsolute or relative name
	 * @return relative name
	 */
	protected static String relativePropertyName(String propertyName) {
		if (!propertyName.contains(SEPARATOR))
			return propertyName;
		return StringUtils.substringAfterLast(propertyName, SEPARATOR);
	}

	
	protected static String buildPropertyPrefix(String... parts) {
		if (parts == null || parts.length == 0)
			throw new IllegalArgumentException(
					"Prefix must consist of at least one part");
		if (parts.length == 1)
			return parts[0] + SEPARATOR;
		
		StringBuilder sb = new StringBuilder();
		for (String part : parts) {
			if (part.isEmpty())
				continue;
			sb.append(part);
			if (!StringBuilderUtils.endsWith(sb, SEPARATOR))
				sb.append(SEPARATOR);
		}
		if (!StringBuilderUtils.endsWith(sb, SEPARATOR))
			sb.append(SEPARATOR);
		
		return sb.toString();
	}
	
	
	protected static final class PropertyPrefixBuilder {
		
		private final StringBuilder sb = new StringBuilder();
		
		public PropertyPrefixBuilder() {
		}
		
		public PropertyPrefixBuilder(String name) {
			sb.append(name);
		}
		
		public PropertyPrefixBuilder append(String name) {
			sb.append(name);
			if (!StringBuilderUtils.endsWith(sb, SEPARATOR))
				sb.append(SEPARATOR);
			return this;
		}
		
		public String toPropertyPrefix() {
			return sb.toString();
		}
	}
	
}