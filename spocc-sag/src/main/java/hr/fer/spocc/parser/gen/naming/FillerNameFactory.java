/*
 * FillerNameFactory.java
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
package hr.fer.spocc.parser.gen.naming;

import hr.fer.spocc.gen.naming.ClassNameFactory;
import hr.fer.spocc.parser.gen.CodeGenerationPropertyConstants;
import hr.fer.spocc.parser.gen.GeneratorProperties;

/**
 * @author Leo Osvald
 *
 */
public class FillerNameFactory extends ClassNameFactory {

	private static final FillerNameFactory INSTANCE
	= new FillerNameFactory();
	
	private FillerNameFactory() {
		super(GeneratorProperties.getProperty(
				CodeGenerationPropertyConstants
				.PARSING_TABLE_FACTORY_FILLER_CLASSES_BASENAME_PROPERTY));
	}

	public String className() {
		return baseName();
	}
	
	public String getFullClassName(int id) {
		String packageName = packageName();
		return packageName.isEmpty() ? baseName()
				: packageName() + "." + baseName() + id;
	}
	
	public static FillerNameFactory getInstance() {
		return INSTANCE;
	}
	
}
