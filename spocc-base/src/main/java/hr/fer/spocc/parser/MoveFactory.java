/*
 * MoveFactory.java
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

import hr.fer.spocc.TokenType;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;

/**
 * @author Leo Osvald
 *
 */
public class MoveFactory {

	private MoveFactory() {
	}
	
	public static Move createShiftMove() {
		return ShiftMove.getInstance();
	}
	
	public static Move createReduceMove(
			CfgProductionRule<TokenType> cfgRule) {
		return new ReduceMove(cfgRule);
	}
	
	public static Move createPushMove(int stateId) {
		return new PushMove(stateId);
	}
	
	public static Move acceptMove() {
		return AcceptMove.getInstance();
	}
	
}
