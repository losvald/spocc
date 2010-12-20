/*
 * ParsingTable.java
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
package hr.fer.spocc.parser;

import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.Symbols;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.sglj.util.Pair;

/**
 * Tablica parsiranja.<br>
 * Simbol za prazan stog je EOF simbol, a moze se dohvatiti
 * sa {@link Symbols#eofSymbol()} 
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public class ParsingTable<T> {
	
	private final Map<Pair<Integer, Symbol<T>>, Action> actions
	= new HashMap<Pair<Integer, Symbol<T>>, Action>();
	
	private Integer startStateId;
	
	public ParsingTable(int startStateId) {
		this.startStateId = startStateId;
	}
	
	public void setAction(int stateId, Symbol<T> stackTop, Action action) {
		actions.put(makePair(stateId, stackTop), action);
	}
	
	public Action getAction(int stateId, Symbol<T> stackTop) {
		return actions.get(makePair(stateId, stackTop));
	}
	
	/**
	 * Vraca skup svih parova (stanje, simbol) za koji je definirana
	 * akcija razlicita od {@link MoveType#ERROR}.
	 * 
	 * @return set
	 */
	public Set<Pair<Integer, Symbol<T>>> getsStateIdSymbolPairs() {
		return Collections.unmodifiableSet(actions.keySet());
	}
	
	private static <T1, T2> Pair<T1, T2> makePair(T1 first, T2 second) {
		return new Pair<T1, T2>(first, second);
	}
	
	public int getStartStateId() {
		return startStateId;
	}
	
}
