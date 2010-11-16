/*
 * SubactionDescriptor.java
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

import hr.fer.spocc.lexer.Subaction;
import hr.fer.spocc.lexer.SubactionType;

import org.sglj.util.ArrayToStringUtils;

/**
 * Opisnik podakcije.<br>
 * 
 * Generator na temelju ovog opisnika treba izgenerirati kod koji
 * stvara instancu klase {@link Subaction}.
 * 
 * @author Leo Osvald
 *
 */
public final class SubactionDescriptor {
	private final SubactionType type;
	private final Object[] args;
	
	public SubactionDescriptor(SubactionType type, Object... params) {
		this.type = type;
		this.args = params;
	}

	public SubactionType getType() {
		return type;
	}

	public Object[] getParams() {
		return args;
	}

	@Override
	public String toString() {
		return "SubactionDescriptor [type=" + type + ", args="
				+ ArrayToStringUtils.toString(args) + "]";
	}

}
