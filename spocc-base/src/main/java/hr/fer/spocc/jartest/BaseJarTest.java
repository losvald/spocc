package hr.fer.spocc.jartest;

import hr.fer.spocc.automaton.fsm.FiniteStateMachines;
import hr.fer.spocc.automaton.fsm.NondeterministicFiniteAutomaton;

public class BaseJarTest {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		NondeterministicFiniteAutomaton<Character> nfa = FiniteStateMachines.createNfa();
		System.out.println("BaseJarTest successfully finished");
	}

}
