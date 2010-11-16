/*
 * LexerDescriptor.java
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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Opisnik leksickog analizatora. Primarna svrha ovog opisnika je da
 * se njegovom deserijalizacijom mogu instancirati svi objekti
 * nuzni za rad LA.
 * 
 * @author Leo Osvald
 *
 */
public final class LexerDescriptor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Set<LexicalRule> lexicalRules;
	
	public LexerDescriptor(LexicalRule... lexicalRules) {
		Set<LexicalRule> tmpSet = new HashSet<LexicalRule>();
		for (LexicalRule rule : lexicalRules)
			tmpSet.add(rule);
		this.lexicalRules = Collections.unmodifiableSet(tmpSet);
	}
	
	public void addLexicalRule(LexicalRule rule) {
		lexicalRules.add(rule);
	}
	
	/**
	 * Vraca skup svih leksickih pravila. 
	 * 
	 * @return nepromjeniv skup
	 */
	public Set<LexicalRule> getLexicalRules() {
		return lexicalRules;
	}
	
}
