/*
 * ParsingTableDescriptor.java
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
package hr.fer.spocc.parser.gen;

import hr.fer.spocc.grammar.cfg.CfgGrammar;

import java.util.Collection;

/**
 * @author Leo Osvald
 *
 */
public final class ParsingTableDescriptor {
	private final Collection<ActionDescriptor> actionDescriptors;
	private final CfgGrammar<String> cfgGrammar;
	private final int startStateId;
	
	public ParsingTableDescriptor(
			Collection<ActionDescriptor> actionDescriptors,
			CfgGrammar<String> cfgGrammar,
			int startStateId) {
		super();
		this.actionDescriptors = actionDescriptors;
		this.cfgGrammar = cfgGrammar;
		this.startStateId = startStateId;
	}

	public Collection<ActionDescriptor> getActionDescriptors() {
		return actionDescriptors;
	}

	public CfgGrammar<String> getCfgGrammar() {
		return cfgGrammar;
	}
	
	public int getStartStateId() {
		return startStateId;
	}
	
}
