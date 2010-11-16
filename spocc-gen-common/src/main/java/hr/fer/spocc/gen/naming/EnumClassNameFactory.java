/*
 * EnumClassNameFactory.java
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

/*
 * @author Leo Osvald
 *
 */
public class EnumClassNameFactory extends ClassNameFactory {

	public EnumClassNameFactory(String fullClassName) {
		super(fullClassName);
	}
	
	public EnumClassNameFactory(Class<?> clazz) {
		super(clazz);
	}
	
	/**
	 * Vraca ime klase
	 * @return
	 */
	public String getFullEnumName() {
		String packageName = packageName();
		return packageName.isEmpty() ? baseName()
				: packageName() + "." + baseName();
	}
	
	public String getEnumName() {
		return baseName();
	}
}
