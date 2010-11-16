/*
 * Grammar.java
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
package hr.fer.spocc.grammar;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.sglj.util.ArrayToStringUtils;
import org.sglj.util.HashMultiMap;
import org.sglj.util.MultiMap;

/**
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public class Grammar<T> {

	private final Set<ProductionRule<T>> productionRules
	= new HashSet<ProductionRule<T>>();
	
	private final Set<Variable<T>> variables
	= new HashSet<Variable<T>>();
	
	private final Set<Terminal<T>> terminals
	= new HashSet<Terminal<T>>();
	
	private Variable<T> startVariable;
	
	private final MultiMap<Symbol<T>, ProductionRule<T>> leftSideSymbolMap 
	= new HashMultiMap<Symbol<T>, ProductionRule<T>>();
	private final MultiMap<Symbol<T>, ProductionRule<T>> rightSideSymbolMap 
	= new HashMultiMap<Symbol<T>, ProductionRule<T>>();
	
	public void addProductionRule(ProductionRule<T> rule) {
		productionRules.add(rule);
		
		for (Symbol<T> symbol : rule.getLeftSide()) {
			addSymbol(symbol);
			this.leftSideSymbolMap.put(symbol, rule);
		}
		for (Symbol<T> symbol : rule.getRightSide()) {
			addSymbol(symbol);
			this.rightSideSymbolMap.put(symbol, rule);
		}
	}
	
	public <P extends ProductionRule<T>> void addProductionRules(
			Collection<P> rules) {
		for (ProductionRule<T> rule : rules)
			addProductionRule(rule);
	}
	
	public void removeProductionRule(ProductionRule<T> rule) {
		for (Symbol<T> symbol : rule.getLeftSide()) {
			if (removeEntryWithKeyIfLast(this.leftSideSymbolMap, symbol, rule)
					&& !isRightSideSymbol(symbol))
				removeSymbol(symbol);
		}
		for (Symbol<T> symbol : rule.getRightSide()) {
			removeEntryWithKeyIfLast(this.rightSideSymbolMap, symbol, rule);
		}
	}
	
	public void replaceProduction(ProductionRule<T> oldRule, 
			ProductionRule<T> newRule) {
		removeProductionRule(oldRule);
		addProductionRule(newRule);
	}

	public void clear() {
		productionRules.clear();
	}
	
	public void addTerminal(Terminal<T> terminal) {
		this.terminals.add(terminal);
	}
	
	public void addVariable(Variable<T> variable) {
		this.variables.add(variable);
	}
	
	public Variable<T> getStartVariable() {
		return startVariable;
	}
	
	public void setStartVariable(Variable<T> startVariable) {
		Validate.isTrue(this.variables.contains(startVariable),
				"Start variable must be a part of grammar");
		this.startVariable = startVariable;
	}
	
	public Set<ProductionRule<T>> getProductionRules() {
		return Collections.unmodifiableSet(productionRules);
	}

	public Set<Variable<T>> getVariables() {
		return Collections.unmodifiableSet(variables);
	}

	public Set<Terminal<T>> getTerminals() {
		return Collections.unmodifiableSet(terminals);
	}
	
	public Set<Symbol<T>> getLeftSideSymbols() {
		return Collections.unmodifiableSet(leftSideSymbolMap.keySet());
	}

	public Set<Symbol<T>> getRightSideSymbols() {
		return Collections.unmodifiableSet(rightSideSymbolMap.keySet());
	}
	
	public boolean containsVariable(Variable<T> variable) {
		return variables.contains(variable);
	}
	
	public boolean isLeftSideSymbol(Symbol<T> symbol) {
		return leftSideSymbolMap.containsKey(symbol);
	}
	
	public boolean isRightSideSymbol(Symbol<T> symbol) {
		return rightSideSymbolMap.containsKey(symbol);
	}

	public int size() {
		return productionRules.size();
	}
	
	@Override
	public int hashCode() {
		return 31
		+ ((productionRules == null) ? 0 : productionRules.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Grammar<T> other = (Grammar<T>) obj;
		if (productionRules == null) {
			if (other.productionRules != null)
				return false;
		} else if (!productionRules.equals(other.productionRules))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Grammar #"+hashCode()+":\n");
		sb.append("V = ").append(getVariables()).append('\n');
		sb.append("T = ").append(getTerminals()).append('\n');
		sb.append("P = {");
		sb.append(ArrayToStringUtils.toString(productionRules.toArray(),
				"\n", "", "")).append("}\n");
		sb.append("S = ").append(getStartVariable());
		return sb.toString();
	}
	
	protected void removeVariable(Variable<T> symbol) {
		this.variables.remove(symbol);
	}
	
	protected void removeTerminal(Terminal<T> terminal) {
		this.terminals.remove(terminal);
	}
	
	@SuppressWarnings("unchecked")
	protected Set<ProductionRule<T>> getProductionsLeftSymbol(
			Symbol<T> symbol) {
		if (!this.leftSideSymbolMap.containsKey(symbol))
			return Collections.EMPTY_SET;
		return this.leftSideSymbolMap.getAll(symbol);
	}
	
	@SuppressWarnings("unchecked")
	protected Set<ProductionRule<T>> getProductionsRightSymbol(
			Symbol<T> symbol) {
		if (!this.rightSideSymbolMap.containsKey(symbol))
			return Collections.EMPTY_SET;
		return this.rightSideSymbolMap.getAll(symbol);
	}
	
	private void addSymbol(Symbol<T> symbol) {
		if (symbol instanceof Variable<?>)
			addVariable((Variable<T>) symbol);
		else if (symbol instanceof Terminal<?>)
			addTerminal((Terminal<T>) symbol);
	}
	
	private void removeSymbol(Symbol<T> symbol) {
		if (symbol instanceof Variable<?>)
			removeVariable((Variable<T>) symbol);
		else if (symbol instanceof Terminal<?>)
			removeTerminal((Terminal<T>) symbol);
	}
	
	protected static <K, V> boolean removeEntryWithKeyIfLast(
			MultiMap<K, V> multiMap, K key, V value) {
		multiMap.removeEntry(key, value);
		if (multiMap.getValueCount(key) == 0) {
			multiMap.remove(key);
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	protected static <T, P extends ProductionRule<T>> Set<P> 
	filterProductionTypes(Collection<? extends ProductionRule<T>> rules,
			Class<P> clazz) {
		Set<P> ret = new HashSet<P>();
		for (ProductionRule<T> rule : rules) {
			if (clazz.isInstance(rule)) {
				ret.add((P) rule);
			}
		}
		return ret;
	}
	
}
