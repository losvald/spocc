/*
 * CfgGrammar.java
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
package hr.fer.spocc.grammar.cfg;

import hr.fer.spocc.grammar.Grammar;
import hr.fer.spocc.grammar.ProductionRule;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.SymbolType;
import hr.fer.spocc.grammar.Terminal;
import hr.fer.spocc.grammar.Variable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.sglj.util.ArrayStack;
import org.sglj.util.HashMultiMap;
import org.sglj.util.MultiMap;
import org.sglj.util.Pair;
import org.sglj.util.Stack;

/**
 * Kontekstno neovisna gramatika (Context-Free Grammar).
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public class CfgGrammar<T> extends Grammar<T> {

	private final MultiMap<Variable<T>, CfgProductionRule<T>> rulesByVariable 
	= new HashMultiMap<Variable<T>, CfgProductionRule<T>>();

	private final Map<CfgProductionRule<T>, ProductionData> data
	= new HashMap<CfgProductionRule<T>, ProductionData>();
	
	private final Set<Variable<T>> emptyVariables
	= new HashSet<Variable<T>>();
	
	/**
	 * Multimapa koja za svaku varijablu sadrzi skup produkcija
	 * koje jos nisu prazne.
	 */
	private final MultiMap<Variable<T>, CfgProductionRule<T>> nonemptySymbolMap
	= new HashMultiMap<Variable<T>, CfgProductionRule<T>>();
	
	private final MultiMap<BeginsWithEntry<T>, CfgProductionRule<T>> beginsDirectlyRules 
	= new HashMultiMap<BeginsWithEntry<T>, CfgProductionRule<T>>();
	
	private final MultiMap<Variable<T>, Symbol<T>> beginsDirectlyWith
	= new HashMultiMap<Variable<T>, Symbol<T>>(); 
	
	@Override
	public void addProductionRule(ProductionRule<T> rule) {
		Validate.isTrue(rule instanceof CfgProductionRule<?>);
//		Validate.isTrue(rule.getLeftSide().size() == 1,
//				"Invalid rule for Context-Free Grammar");
//		Validate.isTrue(rule.getLeftSideSymbol(0) instanceof Variable<?>,
//				"Left side must be a variable");
		CfgProductionRule<T> cfgRule = (CfgProductionRule<T>) rule;
		super.addProductionRule(rule);
		
		this.rulesByVariable.put(cfgRule.getLeftSideSymbol(), cfgRule);
		ProductionData data = new ProductionData(cfgRule);
		this.data.put(cfgRule, data);
		
		boolean possiblyEmpty = true;
		for (Symbol<T> symbol : cfgRule.getRightSide()) {
			if (!isEmptySymbol(symbol)) {
				possiblyEmpty = false;
				if (symbol.getSymbolType() == SymbolType.VARIABLE) {
					this.nonemptySymbolMap.put((Variable<T>) symbol, cfgRule);
				}
			} else {
				markEmpty(cfgRule, data, symbol, true);
			}	
		}
		if (possiblyEmpty && isEmptySequence(cfgRule.getRightSide())) {
			markEmpty(cfgRule.getLeftSideSymbol());
		}
		
		// dodaj prvi znak na desnoj strani u skup zapocinje
		if (!cfgRule.isEpsilon()) {
			addBeginsWith(cfgRule.getLeftSideSymbol(), 
					cfgRule.getRightSideSymbol(0),
					cfgRule);
		} else {
			this.emptyVariables.add(cfgRule.getLeftSideSymbol());
		}
		
		// ako je ovo prva produkcija a pocetno stanje nije definirano
		// postavi lijevi nezavrsni znak za pocetno stanje
		if (getStartVariable() == null && size() == 1)
			setStartVariable(cfgRule.getLeftSideSymbol());
	}
	
	@Override
	public void removeProductionRule(ProductionRule<T> rule) {
		// TODO nije bitno za ovaj labos
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	@Override
	public void clear() {
		super.clear();
		this.rulesByVariable.clear();
		this.data.clear();
		this.nonemptySymbolMap.clear();
	}
	
	public Set<CfgProductionRule<T>> getSubstitutions(Variable<T> variable) {
		if (!this.rulesByVariable.containsKey(variable))
			return Collections.EMPTY_SET;
		return Collections.unmodifiableSet(
				this.rulesByVariable.getAll(variable));
	}
	
	public boolean hasSubstitution(Variable<T> variable) {
		return this.rulesByVariable.containsKey(variable);
	}
	
	public boolean isEmptySymbol(Symbol<T> symbol) {
		switch (symbol.getSymbolType()) {
		case EPSILON:
			return true;
		case TERMINAL:
			return false;
		case EOF: // TODO provjeriti
			return false;
		default: // variable
			return this.emptyVariables.contains((Variable<T>) symbol);
		}
	}
	
	public boolean isEmptySequence(List<Symbol<T>> symbols) {
		for (Symbol<T> symbol : symbols)
			if (!isEmptySymbol(symbol))
				return false;
		return true;
	}
	
	public Set<Variable<T>> getEmptySymbols() {
		Set<Variable<T>> ret = new HashSet<Variable<T>>();
		for (Variable<T> var : getVariables())
			if (isEmptySymbol(var))
				ret.add(var);
		return ret;
	}
	
	public Set<Terminal<T>> getBeginsWithSet(Variable<T> variable) {
		Set<Terminal<T>> ret = new HashSet<Terminal<T>>();
		Stack<Variable<T>> stack = new ArrayStack<Variable<T>>();
		Set<Variable<T>> visited = new HashSet<Variable<T>>();
		stack.push(variable);
		visited.add(variable);
		while (!stack.isEmpty()) {
			Variable<T> from = stack.pop();
			if (beginsDirectlyWith.getValueCount(from) == 0)
				continue;
			for (Symbol<T> to : beginsDirectlyWith.getAll(from)) {
				switch (to.getSymbolType()) {
				case VARIABLE:
					if (!visited.contains(to)) {
						visited.add((Variable<T>) to);
						stack.push((Variable<T>) to);
					}
					break;
				case TERMINAL:
					ret.add((Terminal<T>) to);
				}
			}
		}
		return ret;
	}
	
	public Set<Symbol<T>> getBeginsWithSet(List<Symbol<T>> sequence) {
		if (sequence.isEmpty())
			return Collections.EMPTY_SET;
		Set<Symbol<T>> ret = new HashSet<Symbol<T>>();
		for (Symbol<T> symbol : sequence) {
			if (symbol.getSymbolType() == SymbolType.VARIABLE)
				ret.addAll(getBeginsWithSet((Variable<T>)symbol));
			if (!isEmptySymbol(symbol)) {
				if (symbol.getSymbolType() == SymbolType.TERMINAL)
					ret.add(symbol);
				return ret;
			}
		}
		// TODO jel treba mozda EOF?
		return ret;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("CfgGrammar #"+hashCode()+":\n");
		sb.append("V = ").append(getVariables()).append('\n');
		sb.append("T = ").append(getTerminals()).append('\n');
		sb.append("size(T) = "+getTerminals().size()).append('\n'); // XXX
		sb.append("P = {");
		for (Variable<T> var : getVariables()) {
			if (!hasSubstitution(var))
				continue;
			
			sb.append("\n");
			boolean first = true;
			sb.append(var).append(" ::= ");
			for (CfgProductionRule<T> rule : getSubstitutions(var)) {
				if (first) {
					first = false;
				} else {
					sb.append(" | ");
				}
				// sb.append("[").append(data.get(rule)).append("]");
				sb.append(rule.toString().split("::=")[1]);
			}
		}
		sb.append("\n}\n");
		sb.append("S = ").append(getStartVariable());
		return sb.toString();
	}
	
	ProductionData getProductionData(CfgProductionRule<T> rule) {
		return this.data.get(rule);
	}
	
	private void markEmpty(Variable<T> variable) {
		markEmptyRecursive(variable, new HashSet<Variable<T>>(), 
				new HashSet<CfgProductionRule<T>>());
	}
	
	private void markEmptyRecursive(Variable<T> variable, 
			final Set<Variable<T>> visitedVariables,
			final Set<CfgProductionRule<T>> visitedRules) {
		if (isEmptySymbol(variable))
			return;
		this.emptyVariables.add(variable);
		visitedVariables.add(variable);
		Set<CfgProductionRule<T>> rulesWithVariableOnRight 
		= this.nonemptySymbolMap.getAll(variable);
		if (rulesWithVariableOnRight != null) {
			for (CfgProductionRule<T> rule : rulesWithVariableOnRight) {
				if (visitedRules.contains(rule))
					continue;
				
				ProductionData d = this.data.get(rule);
				Variable<T> leftSideVariable = rule.getLeftSideSymbol();
				markEmpty(rule, d, variable, true);
				if (d.isEmpty() // moramo pazit da se ne zavrtimo u krug
						&& !visitedVariables.contains(leftSideVariable)) {
					visitedRules.add(rule);
					markEmptyRecursive(leftSideVariable,
							visitedVariables, visitedRules);
				}
			}
			this.nonemptySymbolMap.remove(variable);
		}
	}
	
	private void markEmpty(CfgProductionRule<T> cfgRule,
			ProductionData data, Symbol<T> rightSideSymbol, boolean b) {
		data.markEmpty(rightSideSymbol, true);
		Variable<T> leftSideVar = cfgRule.getLeftSideSymbol();
		if (b) {
			int firstIndex = data.variableIndexes.get(rightSideSymbol)[0];
			int lastEmptyIndex = data.getLastEmptyIndex();
			if (lastEmptyIndex + 1 < cfgRule.getRightSideSize()) {
				++lastEmptyIndex;
			} else { //imamo praznu produkciju
				markEmpty(cfgRule.getLeftSideSymbol());
			}
			for (int i = firstIndex; i <= lastEmptyIndex; ++i) {
				addBeginsWith(leftSideVar, cfgRule.getRightSideSymbol(i), 
						cfgRule);
			}
		} else {
			// TODO nije bitno za ovaj labos
			throw new UnsupportedOperationException("Not yet implemented");
		}
	}
	
	private void addBeginsWith(Variable<T> variable, Symbol<T> symbol,
			CfgProductionRule<T> cfgRule) {
		beginsDirectlyRules.put(new BeginsWithEntry<T>(variable, symbol), cfgRule);
		beginsDirectlyWith.put(variable, symbol);
	}
	
//	private void addStartsWith(CfgProductionRule<T> cfgRule, Symbol<T> symbol) {
//		addStartsWith(cfgRule.getLeftSideSymbol(), symbol, cfgRule);
//	}
	
	class ProductionData extends FenwickTree {

		final Map<Symbol<T>, int[]> variableIndexes;
		final Set<Symbol<T>> nonemptyVariables; 
		final Set<Symbol<T>> emptyVariables;
		
		boolean empty;
		
		@SuppressWarnings("unchecked")
		ProductionData(CfgProductionRule<T> cfgProductionRule) {
			super(cfgProductionRule.getRightSide().size());
			
			if (cfgProductionRule.isEpsilon()) {
				variableIndexes = Collections.EMPTY_MAP;
				nonemptyVariables = Collections.EMPTY_SET;
				emptyVariables = Collections.EMPTY_SET;
			}
			else {
				MultiMap<Symbol<T>, Integer> tmpMap
				= new HashMultiMap<Symbol<T>, Integer>();
				nonemptyVariables = new HashSet<Symbol<T>>();
				int ind = 0;
				for (Symbol<T> symbol : cfgProductionRule.getRightSide()) {
					if (symbol.getSymbolType() == SymbolType.VARIABLE) {
						tmpMap.put(symbol, ind);
						nonemptyVariables.add(symbol);
					}
					++ind;
				}
				emptyVariables = new HashSet<Symbol<T>>(
						nonemptyVariables.size());
				
				variableIndexes = new HashMap<Symbol<T>, int[]>(
						tmpMap.keySet().size());
				for (Symbol<T> symbol : tmpMap.keySet()) {
					int[] sortedIndexes 
					= new int[tmpMap.getValueCount(symbol)];
					int k = 0;
					for (Integer index : tmpMap.getAll(symbol))
						sortedIndexes[k++] = index;
					Arrays.sort(sortedIndexes);
					variableIndexes.put(symbol, sortedIndexes);
				}
			}
		}
		
		public boolean isEmpty() {
			return isAllSet(0, size());
		}
		
		public void markEmpty(Symbol<T> variable, boolean b) {
			if (b) {
				if (emptyVariables.contains(variable))
					return ;
				emptyVariables.add(variable);
				nonemptyVariables.remove(variable);
			} else {
				if (nonemptyVariables.contains(variable))
					return ;
				nonemptyVariables.add(variable);
				emptyVariables.remove(variable);
			}
			
			for (int index : variableIndexes.get(variable)) {
				set(index, b);
			}
		}
		
		public int getFirstIndex(Symbol<T> rightSideVariable) {
			if (!this.variableIndexes.containsKey(rightSideVariable))
				return -1;
			return this.variableIndexes.get(rightSideVariable)[0];
		}
		
		public int getFirstIndex(Symbol<T> rightSideVariable,
				int fromIndex) {
			if (!this.variableIndexes.containsKey(rightSideVariable))
				return -1;
			int ret = Arrays.binarySearch(
					this.variableIndexes.get(rightSideVariable), 
					fromIndex);
			return ret >= 0 ? ret : -ret - 1;
		}
		
		public int getLastEmptyIndex() {
			int lo = -1, hi = size() - 1;
			while (lo < hi) {
				int mid = (lo + hi + 1)/2;
				if (isAllSet(0, mid + 1))
					lo = mid;
				else
					hi = mid - 1;
			}
			return lo;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(super.toString());
			sb.append("Indexes: ");
			for (Entry<Symbol<T>, int[]> e : variableIndexes.entrySet()) {
				sb.append(e.getKey()).append(':');
				sb.append(Arrays.toString(e.getValue())).append('\n');
			}
			return sb.toString();
		}
		
	}
	
	static class BeginsWithEntry<T> extends Pair<Variable<T>, Symbol<T>>{
		public BeginsWithEntry(Variable<T> variable, Symbol<T> symbol) {
			super(variable, symbol);
		}
	}
	
	static class FenwickTree {
		private final int[] a;
		
		public FenwickTree(int size) {
			this.a = new int[size + 1];
		}
		
		public int size() {
			return a.length - 1;
		}

		public void set(int index, boolean b) {
			if (isSet(index) != b)
				add(index, b ? 1 : -1);
		}
		
		public boolean isSet(int index) {
			return getSum(index, index+1) != 0 ? true : false;
		}
		
		public boolean isAllSet(int from, int to) {
			return getSum(from, to) == to - from;
		}
		
		public boolean isAllUnset(int from, int to) {
			return getSum(from, to) == 0;
		}
		
		private void add(int index, int value) {
			for (++index; index < a.length; index += index & -index)
				a[index] += value;
		}
		
		private int getSum(int to) {
			int ret = 0;
			for (++to; to > 0; to -= to & -to)
				ret += a[to];
			return ret;
		}
		
		private int getSum(int from, int to) {
			return getSum(to-1) - (from > 0 ? getSum(from-1) : 0);
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("[");
			for (int i = 0; i < size(); ++i) {
				if (i > 0) sb.append(' ');
				sb.append(isSet(i) ? '1' : '0');
			}
			sb.append("]");
			return sb.toString();
		}
	}
}
