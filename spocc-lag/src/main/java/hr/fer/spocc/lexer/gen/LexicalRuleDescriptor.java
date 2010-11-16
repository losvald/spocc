/*
 * LexicalRuleDescriptor.java
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
package hr.fer.spocc.lexer.gen;

import hr.fer.spocc.lexer.LexicalRule;
import hr.fer.spocc.regex.RegularExpression;

/**
 * Opisnik leksickog pravila.<br>
 * 
 * Generator na temelju ovog opisnika treba izgenerirati kod koji
 * stvara instancu klase {@link LexicalRule}.
 * 
 * @author Leo Osvald
 *
 */
public final class LexicalRuleDescriptor {

	private final int priority;
	private final RegularExpression<Character> regularExpression;
	private final ActionDescriptor actionDescriptor;
	
	public LexicalRuleDescriptor(int priority, 
			RegularExpression<Character> regularExpression,
			ActionDescriptor descriptor) {
		this.priority = priority;
		this.regularExpression = regularExpression;
		this.actionDescriptor = descriptor;
	}

	public int getPriority() {
		return priority;
	}

	public RegularExpression<Character> getRegularExpression() {
		return regularExpression;
	}

	public ActionDescriptor getDescriptor() {
		return actionDescriptor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + priority;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LexicalRuleDescriptor other = (LexicalRuleDescriptor) obj;
		if (priority != other.priority)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LexicalRuleDescriptor [priority=" + priority
				+ ", regularExpression=" + regularExpression
				+ ", actionDescriptor=" + actionDescriptor + "]";
	}
	
}
