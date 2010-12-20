/*
 * MoveDescriptor.java
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

import hr.fer.spocc.parser.MoveType;

import org.sglj.util.ArrayToStringUtils;

/**
 * @author Leo Osvald
 *
 */
public final class MoveDescriptor {
	public final MoveType type;
	public final Object[] args;
	
	public MoveDescriptor(MoveType type, Object... params){
		this.type = type;
		this.args = params;
	}

	public MoveType getType(){
		return type;
	}
	
	public Object[] getParams(){
		return args;
	}

	@Override
	public String toString() {
		return "MoveDescriptor [type=" + type + ", args="
				+ ArrayToStringUtils.toString(args) + "]";
	}
	
	public static final MoveDescriptor ACCEPT_DESCRIPTOR 
	= new MoveDescriptor(MoveType.ACCEPT);
}
