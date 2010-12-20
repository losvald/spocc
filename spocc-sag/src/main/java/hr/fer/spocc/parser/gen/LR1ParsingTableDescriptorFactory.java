/*
 * LR1ParsingTableDescriptorFactory.java
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

import hr.fer.spocc.Environment;
import hr.fer.spocc.automaton.fsm.FiniteStateMachine.Transition;
import hr.fer.spocc.automaton.fsm.FiniteStateMachines;
import hr.fer.spocc.automaton.fsm.State;
import hr.fer.spocc.grammar.Symbol;
import hr.fer.spocc.grammar.SymbolType;
import hr.fer.spocc.grammar.Symbols;
import hr.fer.spocc.grammar.Variable;
import hr.fer.spocc.grammar.cfg.CfgGrammar;
import hr.fer.spocc.parser.MoveType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * @author Leo Osvald
 *
 */
public class LR1ParsingTableDescriptorFactory 
implements ParsingTableDescriptorFactory {

	private final CfgGrammar<String> grammar;
	
	public LR1ParsingTableDescriptorFactory(CfgGrammar<String> cfgGrammar) {
		this.grammar = cfgGrammar;
	}
	
	@Override
	public ParsingTableDescriptor createDescriptor() {
		if (Environment.isDevelopment())
			System.err.println("Creating nfa");
		LR1Nfa lr1Nfa = new LR1Nfa(this.grammar);
		if (Environment.isDevelopment())
			System.err.println("NFA has "+lr1Nfa.getStates().size() + " states");
		
		if (Environment.isDevelopment()) {
			System.out.println("----- BEGINS-WITH ----");
			for (Variable<String> var : grammar.getVariables()) {
				System.out.println(var + ": " + grammar.getBeginsWithSet(var));
			}
			System.out.println("---------NFA--------");
			System.out.println(lr1Nfa.getTransitions());
			for (State state : lr1Nfa.getStates()) {
				System.out.println(state.getId() + ": " 
						+ lr1Nfa.getAssociated(state.getId()));
			}
		}
		
		return createDescriptor(createLR1Dfa(lr1Nfa));
	}
	
	private ParsingTableDescriptor createDescriptor(LR1Dfa lr1Dfa) {
		Collection<ActionDescriptor> actionDescriptors
		= new HashSet<ActionDescriptor>() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean add(ActionDescriptor e) {
				return super.add(e);
			}
		};

		if (Environment.isDevelopment()) {
			System.out.println("---------DFA--------");
			System.out.println(lr1Dfa.getTransitions());
			for (State state : lr1Dfa.getStates()) {
				System.out.println(state.getId() + ": " 
						+ lr1Dfa.getAssociated(state.getId()));
			}
		}
		
		
		for (State state : lr1Dfa.getStates()) {
			final int stateId = state.getId();
			for (LR1Item<String> item : lr1Dfa.getAssociated(stateId)) {
				if (item.isComplete())
					continue;
				
				final Symbol<String> x = item.getRightSideSymbol(
						item.getDotIndex());
				
				final State to = lr1Dfa.getNextState(x, state);
				if (to == null)
					continue;
				
				// ako postoji prijalaz(stateId, znak (x)) = t, napravi Akcija[s,x] = pomakni(t)	
				// ako postoji prijalaz(stanje (), znak (x)) = t, napravi Akcija[s,x] = stavi(t)
				List<MoveDescriptor> moveDescriptors = new ArrayList<MoveDescriptor>(2);
				if ( x.getSymbolType() == SymbolType.TERMINAL ) {
					moveDescriptors.add(new MoveDescriptor(MoveType.SHIFT));
				}
				moveDescriptors.add(new MoveDescriptor(MoveType.PUSH, to.getId()));
				
				actionDescriptors.add(new ActionDescriptor(stateId, x, moveDescriptors));
			}
		}
		
		for (State state : lr1Dfa.getStates()) {
			for (LR1Item<String> item : lr1Dfa.getAssociated(state.getId())) {
				if (item.isComplete()) {
					// ili redukcija ili prihvacanje
					if (item.getLeftSideSymbol().equals(grammar.getStartVariable())) {
						actionDescriptors.add(new ActionDescriptor(
								state.getId(), 
								(Symbol) Symbols.eofSymbol(), 
								Arrays.asList(MoveDescriptor.ACCEPT_DESCRIPTOR)));
					} else {
						for (Symbol<String> fs : item.getFollowingSymbols()) {
							actionDescriptors.add(new ActionDescriptor(
									state.getId(), 
									fs,
									Arrays.asList(new MoveDescriptor(MoveType.REDUCE, item))));
						}
					}
				}
			}
		}
		
		return new ParsingTableDescriptor(actionDescriptors, this.grammar, 
				lr1Dfa.getStartState().getId());
	}
	
	static LR1Dfa createLR1Dfa(LR1Nfa lr1Nfa) {
		if (Environment.isDevelopment())
			System.out.println("Converting NFA to DFA");
		
		LR1Dfa lr1Dfa = new LR1Dfa(FiniteStateMachines.toDfa(lr1Nfa, false));
		
		if (Environment.isDevelopment())
			System.out.println("LR1Dfa has "+lr1Dfa.getStates().size() + " states");

		Set<Integer> visited = new HashSet<Integer>();
		Queue<Integer> toVisit = new ArrayDeque<Integer>();
		Integer stateId = lr1Dfa.getStartState().getId();
		Set<State> nfastates = new HashSet<State>();
		Queue<Set<State>> n = new ArrayDeque<Set<State>>();
		Queue<Symbol<String>> simbols = new LinkedList<Symbol<String>>(); 
		toVisit.add(stateId);
		simbols.add(null);
		visited.add(stateId);
		Collection<Transition<Symbol<String>>> dfatransitions = lr1Dfa.getTransitions();
		while(!toVisit.isEmpty()){
			stateId = toVisit.remove();
			//System.out.println("stanje - "+stateId);
			Symbol<String> simbol = simbols.remove();

			if(simbol == null){
				//pocetno stanje, uzmi samo okolinu
				
				nfastates.add(lr1Nfa.getStartState());
				nfastates = lr1Nfa.powerSet(nfastates);
				}

			else{
				
				//uzmi okoline svih sljedecih stanja za zadani simbol
				nfastates = n.remove();
				//System.out.println("test - "+simbol+" - "+nfastates);
				nfastates = lr1Nfa.powerSet(getNextStates(lr1Nfa, simbol, nfastates));
			
			}
			Set<LR1Item<String>> xy = getAssociated(lr1Nfa, nfastates);
			//System.out.println(xy);
			lr1Dfa.associate(stateId, xy);
			//System.out.println("nfastates - "+nfastates);
			for(Transition<Symbol<String>> dfatransition  : dfatransitions){
				if(dfatransition.getFrom().getId() == stateId){
					if(!visited.contains(dfatransition.getTo().getId())){
						toVisit.add(dfatransition.getTo().getId());
						simbols.add(dfatransition.getSymbol());
						n.add(nfastates);
					}
				}
				
			}
			visited.add(stateId);
			//System.out.println("toVisit - " + toVisit);
			//System.out.println("symbols - "+simbols);
			//System.out.println("visited - "+visited);
		}
		lr1Dfa.getAssociated(lr1Dfa.getStartState().getId()).remove(null);
		return lr1Dfa;
	}
	
	private static Set<State> getNextStates(LR1Nfa lr1Nfa, Symbol<String> simbol, Set<State> states){
		Set<State> TMPnfastates = new HashSet<State>();
		for(State state : states)
			TMPnfastates.addAll(lr1Nfa.getNextStates(simbol, state));
		return TMPnfastates;
	}
	
	private static Set<LR1Item<String>> getAssociated(LR1Nfa lr1Nfa, Set<State> states){
		Set<LR1Item<String>> tmp = new HashSet<LR1Item<String>>();
		for(State state : states)
			tmp.add(lr1Nfa.getAssociated(state.getId()));
		return tmp;
	}
	
	// nismo sigurni ima li taj set zapravo samo jedan element
	private static LR1Item<String> getOnlyItem(Set<LR1Item<String>> someSet) {
		return someSet.iterator().next();
	}
	
}
