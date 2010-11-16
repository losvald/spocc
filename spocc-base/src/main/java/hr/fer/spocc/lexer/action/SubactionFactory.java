/*
 * SubactionFactory.java
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
package hr.fer.spocc.lexer.action;

import hr.fer.spocc.lexer.Subaction;
import hr.fer.spocc.lexer.SubactionType;

import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

/**
 * Singleton factory za podakcije.
 * Trenutno su podrzane samo built-in podakcije.
 * 
 * @author Leo Osvald
 *
 */
public class SubactionFactory {

	private static final Map<SubactionType, Subaction> BUILTIN_ACTIONS
	= new EnumMap<SubactionType, Subaction>(SubactionType.class);
	
	public static synchronized Subaction getSubaction(SubactionType type) {
		Validate.notNull(type);
		Validate.isTrue(type.isBuiltIn());
		
		// check cache
		Subaction ret = BUILTIN_ACTIONS.get(type);
		if (ret != null) {
			return ret;
		}
		
		switch (type) {
		case ENTER_STATE:
			ret = new EnterStateSubaction();
			break;
		case NEW_LINE:
			ret = new NewLineSubaction();
			break;
		case TOKENIZE:
			ret = new TokenizeSubaction();
			break;
		case TOKENIZE_FIRST:
			ret = new TokenizeFirstSubaction();
			break;
		case SKIP:
			ret = new SkipSubaction();
			break;
		default:
			throw new UnsupportedOperationException(
					"Custom subactions are not yet implemented"); 
		}
		
		// cache the action to avoid new object allocations
		BUILTIN_ACTIONS.put(type, ret);
		
		return ret;
	}
}
