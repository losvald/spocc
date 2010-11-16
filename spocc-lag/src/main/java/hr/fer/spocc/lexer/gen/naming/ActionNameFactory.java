/*
 * ActionNameFactory.java
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
package hr.fer.spocc.lexer.gen.naming;

import hr.fer.spocc.gen.naming.ClassNameFactory;
import hr.fer.spocc.lexer.Action;
import hr.fer.spocc.lexer.gen.CodeGenerationPropertyConstants;
import hr.fer.spocc.lexer.gen.GeneratorProperties;

/**
 * Klasa koja sluzi za stvaranje imena za implementacija klasa
 * {@link Action}.
 * 
 * @author Leo Osvald
 *
 */
public final class ActionNameFactory extends ClassNameFactory {
	
	private static final ActionNameFactory INSTANCE
	= new ActionNameFactory();
	
	private ActionNameFactory() {
		super(GeneratorProperties.getProperty(
				CodeGenerationPropertyConstants
				.ACTION_CLASS_NAME_PROPERTY));
	}
	
	/**
	 * Vraca ime klase dobiveno na temelju imena baznog imena i prioriteta.
	 * 
	 * @param lexicalRulePriority prioritet leksickog pravila
	 * @return ime klase
	 */
	public String getClassName(int lexicalRulePriority) {
		return baseName() + lexicalRulePriority;
	}
	
	/**
	 * Vraca puno ime klase dobiveno na temelju imena baznog imena i prioriteta.
	 * 
	 * @param lexicalRulePriority prioritet leksickog pravila
	 * @return puno ime klase
	 */
	public String getFullClassName(int lexicalRulePriority) {
		if (packageName().isEmpty())
			return getClassName(lexicalRulePriority);
		return packageName() + "." + getClassName(lexicalRulePriority);
	}

	public static ActionNameFactory getInstance() {
		return INSTANCE;
	}
	
}
