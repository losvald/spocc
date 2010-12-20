/*
 * ActionDescriptor.java
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

import hr.fer.spocc.grammar.Symbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Leo Osvald
 *
 */
public final class ActionDescriptor {
	private final int stateId;
	private final Symbol<String> symbol;
	private final List<MoveDescriptor> moveDescriptors;
	
	public ActionDescriptor(int stateId, Symbol<String> symbol,
			List<MoveDescriptor> moveDescriptors) {
		this.stateId = stateId;
		this.symbol = symbol;
		//copy list
		this.moveDescriptors = Collections.unmodifiableList(
				new ArrayList<MoveDescriptor>(
						moveDescriptors));
	}

	public int getStateId() {
		return stateId;
	}
	
	public Symbol<String> getSymbol() {
		return symbol;
	}
	
	public List<MoveDescriptor> getMoveDescriptors() {
		return moveDescriptors;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + stateId;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		ActionDescriptor other = (ActionDescriptor) obj;
		if (stateId != other.stateId)
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActionDescriptor [stateId=" + stateId + ", symbol=" + symbol
				+ ", moveDescriptors=" + moveDescriptors + "]";
	}
	
}
