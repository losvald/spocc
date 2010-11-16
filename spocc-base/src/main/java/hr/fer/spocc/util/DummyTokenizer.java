/*
 * DummyTokenizer.java
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
package hr.fer.spocc.util;

import hr.fer.spocc.Token;
import hr.fer.spocc.Tokenizer;
import hr.fer.spocc.lexer.TokenList;

import java.util.Iterator;

/**
 * Dummy implementacije tokenizera, fetcha tokene jednog po jednog
 * na temelju sadrzaja objekta {@link TokenList} predanog u konstruktoru.
 * 
 * @author Leo Osvald
 *
 */
public class DummyTokenizer implements Tokenizer {

	private final TokenList tokenList;
	Iterator<Token> iterator;
	
	public DummyTokenizer(TokenList tokenList) {
		this.tokenList = tokenList;
		reset();
	}
	
	@Override
	public Token nextToken() {
		if (this.iterator.hasNext())
			return this.iterator.next();
		return null;
	}
	
	@Override
	public void reset() {
		this.iterator = tokenList.getAll().iterator();
	}

}
