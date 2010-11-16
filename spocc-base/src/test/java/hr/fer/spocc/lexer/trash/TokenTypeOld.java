/*
 * TokenTypeOld.java
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
package hr.fer.spocc.lexer.trash;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa koja predstavlja ime leksicke jedinke (tj. uniformni znak
 * u tablici leksickih jedinki).<br>
 * Ova klasa je immutable i cachiraju se objekti kako bi se izbjeglo
 * stvaranje novih instanci objekata - instance objekata se dobivaju
 * preko staticke metode {@link #valueOf(String)}.
 * 
 * @author Leo Osvald
 * 
 */
final class TokenTypeOld {

	private static final Map<String, TokenTypeOld> CACHE 
	= new HashMap<String, TokenTypeOld>();
	
	private final String id;
	
	private TokenTypeOld(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	/**
	 * Vraca objekt ove klase.
	 * 
	 * @param id identifikator objekta
	 * @return objekt
	 */
	public static synchronized TokenTypeOld valueOf(String id) {
		TokenTypeOld tt = CACHE.get(id);
		if (tt == null) {
			tt = new TokenTypeOld(id);
			CACHE.put(id, tt);
		}
		return tt;
	}
	
}
