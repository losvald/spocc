/*
 * TokenTypeDescriptor.java
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
package hr.fer.spocc.gen;

import hr.fer.spocc.TokenType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Opisnik tipova/imena leksicke jedinki.<br>
 * 
 * Generator na temelju ovog opisnika treba izgenerirati kod koji
 * stvara instancu sucelja {@link TokenType}.
 * 
 * @author Leo Osvald
 *
 */
public final class TokenTypeDescriptor {

	private final Set<String> tokenTypes = new HashSet<String>();

	public void addTokenType(String typeToken) {
		tokenTypes.add(typeToken);
	}
	
	public void addTokenTypes(String... tokenTypes) {
		for (String tt : tokenTypes)
			this.tokenTypes.add(tt);
	}
	
	public Set<String> getTypeTokens() {
		return Collections.unmodifiableSet(tokenTypes);
	}

	@Override
	public String toString() {
		return "TypeTokenDescriptor [typeTokens=" + tokenTypes + "]";
	}
	
}
