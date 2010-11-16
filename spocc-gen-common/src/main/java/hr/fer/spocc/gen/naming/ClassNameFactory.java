/*
 * ClassNameFactory.java
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
package hr.fer.spocc.gen.naming;

import org.apache.commons.lang.StringUtils;

/**
 * Factory koji imenuje na temelju referentne klase.
 * 
 * @author Leo Osvald
 *
 */
public abstract class ClassNameFactory {
	
	private final String baseName;
	private final String packageName;
	
	public ClassNameFactory(String fullClassName) {
		boolean defaultPackage = !fullClassName.contains(".");
		this.baseName = (!defaultPackage
				? StringUtils.substringAfterLast(fullClassName, ".")
						: fullClassName);
		this.packageName = (!defaultPackage
				? StringUtils.substringBeforeLast(fullClassName, ".") 
						: "");
	}
	
	/**
	 * 
	 * @param clazz referentna klasa koja sluzi za imenovanje
	 */
	public ClassNameFactory(Class<?> clazz) {
		this.baseName = clazz.getSimpleName();
		this.packageName = (!clazz.getSimpleName().equals(clazz.getName())
				? clazz.getPackage().getName() : "");
	}
	
	/**
	 * Vraca ime paketa. Ako je defaultni paket, vraca "".
	 * 
	 * @return ime paketa
	 */
	public String packageName() {
		return packageName;
	}
	
	/**
	 * Vraca jednostavno ime referentne klase.
	 * 
	 * @return ime
	 */
	protected String baseName() {
		return baseName;
	}
	
}
