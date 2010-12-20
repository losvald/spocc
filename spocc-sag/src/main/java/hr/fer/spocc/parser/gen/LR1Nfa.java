/*
 * LR1Nfa.java
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

import hr.fer.spocc.automaton.fsm.State;
import hr.fer.spocc.automaton.fsm.StateAssociatedNfa;
import hr.fer.spocc.grammar.ProductionRule;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.SymbolType;
import hr.fer.spocc.grammar.Symbols;
import hr.fer.spocc.grammar.Variable;
import hr.fer.spocc.grammar.cfg.CfgGrammar;
import hr.fer.spocc.grammar.cfg.CfgProductionRule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sglj.util.ArrayStack;
import org.sglj.util.Stack;

/**
 * @author Leo Osvald
 *
 */
public class LR1Nfa extends StateAssociatedNfa<Symbol<String>, LR1Item<String>> {

	private final Map<Integer, LR1Item<String>> map
	= new HashMap<Integer, LR1Item<String>>();

	private final Map<LR1Item<String>, Integer> reverseMap
	= new HashMap<LR1Item<String>, Integer>();

	private int curStateId;

	private static final Symbol<String> EOF_SYMBOL = Symbols.eofSymbol();

	public LR1Nfa(CfgGrammar<String> cfgGrammar) {
		Stack<LR1Item<String>> itemStack = new ArrayStack<LR1Item<String>>();

		// slucaj a)
		@SuppressWarnings("unchecked")
		List<Symbol<String>> eofSymbolList = Arrays.asList(EOF_SYMBOL);
		for (ProductionRule<String> rule : cfgGrammar.getProductionRules()) {
			CfgProductionRule<String> cfgRule = (CfgProductionRule<String>) rule;
			if (cfgRule.getLeftSideSymbol()
					.equals(cfgGrammar.getStartVariable())) {
				itemStack.push(new LR1Item<String>(
						cfgRule.getLeftSideSymbol(), 
						cfgRule.getRightSide(),
						0,
						eofSymbolList));
			}
		}

		// dodaj pocetno stanje q0 i asociraj stavku null
		addState(new State(curStateId, false), true);
		associate(curStateId, null); //q0 je null

		Set<LR1Item<String>> visited = new HashSet<LR1Item<String>>();
		while (!itemStack.isEmpty()) {
			LR1Item<String> item = itemStack.pop();
			visited.add(item);
			if (item.isComplete())
				continue;

			// slucaj a)
			if (item.getLeftSideSymbol().equals(cfgGrammar.getStartVariable())
					&& item.getDotIndex() == 0) {
				addTransition(null, item, null);
			}

			// slucaj b)
			LR1Item<String> shifted = new LR1Item<String>(
					item.getLeftSideSymbol(), 
					item.getRightSide(), 
					item.getDotIndex() + 1, 
					item.getFollowingSymbols());
			addTransition(item, shifted, 
					item.getRightSideSymbol(item.getDotIndex()));
			if (!visited.contains(shifted))
				itemStack.add(shifted);

			// slucaj c)
			Symbol<String> symbolAtDot = getSymbolAtDot(item);
			if(symbolAtDot.getSymbolType() == SymbolType.VARIABLE) {
				Variable<String> var = (Variable<String>) symbolAtDot;
				Set<CfgProductionRule<String>> cfgRules 
				= cfgGrammar.getSubstitutions(var);

				List<Symbol<String>> beta = item.getRightSide().subList(
						item.getDotIndex() + 1, item.getRightSideSize());

				Set<Symbol<String>> followingSymbols
				= new HashSet<Symbol<String>>();
				followingSymbols.addAll(cfgGrammar.getBeginsWithSet(beta));
				if (cfgGrammar.isEmptySequence(beta))
					followingSymbols.addAll(item.getFollowingSymbols());

				for (CfgProductionRule<String> substitution : cfgRules) {
					LR1Item<String> to = new LR1Item<String>(
							substitution.getLeftSideSymbol(),
							substitution.getRightSide(),
							0,
							followingSymbols);
					addTransition(item, to, null);
					if (!visited.contains(to))
						itemStack.add(to);
				}
			}
		}
	}

	@Override
	public void associate(int stateId, LR1Item<String> value) {
		map.put(stateId, value);
		reverseMap.put(value, stateId);
	}

	@Override
	public LR1Item<String> getAssociated(int stateId) {
		return map.get(stateId);
	}

	public int getStateId(LR1Item<String> item) {
		return reverseMap.get(item);
	}

	public void addTransition(LR1Item<String> from, LR1Item<String> to, 
			Symbol<String> input) {
		addTransition(getStateIdOrAdd(from), getStateIdOrAdd(to), input);
	}

	private Symbol<String> getSymbolAtDot(LR1Item<String> item) {
		return item.getRightSideSymbol(item.getDotIndex());
	}

	private int getStateIdOrAdd(LR1Item<String> item) {
		if (!containsLR1Item(item)) {
			associate(++curStateId, item);
			addState(new State(curStateId, true));
		}
		return getStateId(item);
	}

	private boolean containsLR1Item(LR1Item<String> item) {
		return reverseMap.containsKey(item);
	}

}
