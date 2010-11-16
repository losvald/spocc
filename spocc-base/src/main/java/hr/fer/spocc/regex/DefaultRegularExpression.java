/*
 * DefaultRegularExpression.java
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
package hr.fer.spocc.regex;

import hr.fer.spocc.util.CharacterEscaper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * 
 * @author Leo Osvald
 *
 */
public class DefaultRegularExpression 
extends AbstractRegularExpression<Character> {
	
	public static final char UNION_OPERATOR = '|';
	public static final char STAR_OPERATOR = '*';
	public static final char LEFT_PARENTHESIS = '(';
	public static final char RIGHT_PARENTHESIS = ')';
	public static final char EPSILON_SYMBOL = '$';

//	private static final Map<RegularExpressionOperator, Character> CHAR;
//	private static final Map<Character, RegularExpressionOperator> OPERATOR;
//	
//	static {
//		int n = RegularExpressionOperator.values().length;
//		Map<RegularExpressionOperator, Character> charMap
//		= new HashMap<RegularExpressionOperator, Character>(n);
//		Map<Character, RegularExpressionOperator> operatorMap
//		= new HashMap<Character, RegularExpressionOperator>(n);
//		
//		charMap.put(RegularExpressionOperator.UNION, UNION_OPERATOR);
//		charMap.put(RegularExpressionOperator.STAR, STAR_OPERATOR);
//		
//		operatorMap.put(UNION_OPERATOR, RegularExpressionOperator.UNION);
//		operatorMap.put(STAR_OPERATOR, RegularExpressionOperator.STAR);
//		
//		CHAR = Collections.unmodifiableMap(charMap);
//		OPERATOR = Collections.unmodifiableMap(operatorMap);
//	}
	
	public DefaultRegularExpression(String string, 
			CharacterEscaper escaper) {
		super(toElements(string, escaper));
	}
	
	protected DefaultRegularExpression(List<RegularExpressionElement> list) {
		super(list);
	}
	
	protected DefaultRegularExpression(List<RegularExpressionElement> list, 
			RegularExpressionType type,
			RegularExpressionOperator operator, 
			RegularExpression<Character>... subexpressions) {
		super(list, type, operator, subexpressions);
	}
	
	protected DefaultRegularExpression(
			RegularExpression<Character> unexpandedRegex,
			List<RegularExpressionElement> list) {
		super(unexpandedRegex, list);
	}

	@Override
	protected RegularExpression<Character> createComposite(
		List<RegularExpressionElement> list, 
		RegularExpressionOperator operator, 
		RegularExpression<Character>... subexpressions) {
		
//		System.out.println("Complex("+list+"; op = "+operator
//				+"; subexps = "+ArrayToStringUtils.toString(subexpressions, "\n"));
		return new DefaultRegularExpression(list, RegularExpressionType.COMPLEX,
				operator, subexpressions);
	}
	
	@Override
	protected RegularExpression<Character> createTrivial(
			List<RegularExpressionElement> sublist) {
		Validate.isTrue(sublist.size() == 1);
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		RegularExpressionSymbol symbol = 
			(RegularExpressionSymbol<Character>) sublist.get(0);
		return new DefaultRegularExpression(sublist, 
				(symbol.isEpsilon() ? RegularExpressionType.EPSILON
						: RegularExpressionType.TERMINAL), null);
	}
	
	@Override
	protected RegularExpression<Character> createExpanded(
			RegularExpression<Character> unexpandedRegex,
			List<RegularExpressionElement> list) {
		return new DefaultRegularExpression(unexpandedRegex, list);
	}
	
	
	private static List<RegularExpressionElement> toElements(
			String escapedString,
			CharacterEscaper escaper) {
		
		List<RegularExpressionElement> elements 
		= new ArrayList<RegularExpressionElement>();
		
		Set<Integer> unescapedIndexes = new HashSet<Integer>();
		String unescapedString = escaper.unescape(escapedString, 
				unescapedIndexes);
		
		for (int i = 0; i < unescapedString.length(); ++i) {
			RegularExpressionElement lastElement 
			= (!elements.isEmpty() 
					? elements.get(elements.size()-1) : null);
			char c = unescapedString.charAt(i);
			RegularExpressionElement e = null;
			if (!unescapedIndexes.contains(i)) {
				switch (c) {
				case STAR_OPERATOR:
					e = RegularExpressionOperator.STAR;
					break;
				case UNION_OPERATOR:
					e = RegularExpressionOperator.UNION;
					break;
				case LEFT_PARENTHESIS:
					e = RegularExpressionElements.LEFT_PARENTHESIS;
					break;
				case RIGHT_PARENTHESIS:
					e = RegularExpressionElements.RIGHT_PARENTHESIS;
					break;
				case EPSILON_SYMBOL:
					e = new RegularExpressionSymbol<Character>(null);
					break;
				default:
					e = new RegularExpressionSymbol<Character>(c);
					break;
				}
			} else { // escaped character is always a symbol
				e = new RegularExpressionSymbol<Character>(c);
			}
			
			// insert the concatenation operator if needed
			if (requiresConcatenation(lastElement, e)) {
				elements.add(RegularExpressionOperator.CONCATENATION);
			}
			elements.add(e);
		}
		
		return Collections.unmodifiableList(elements);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("regex[value = ");
		for (int i = 0; i < size(); ++i) {
			RegularExpressionElement e = get(i);
			if (e.getElementType() == RegularExpressionElementType
					.SYMBOL) {
				@SuppressWarnings("unchecked")
				RegularExpressionSymbol<Character> symbol
				= (RegularExpressionSymbol<Character>) e;
				if (symbol.isEpsilon()) {
					sb.append(EPSILON_SYMBOL);
				} else {
					sb.append(DefaultRegularExpressionEscaper.getInstance()
							.escape(""+symbol.getValue()));
				}
			} else if (e.getElementType() == RegularExpressionElementType
					.OPERATOR) {
				RegularExpressionOperator op = (RegularExpressionOperator) e;
				if (op != RegularExpressionOperator.CONCATENATION)
					sb.append(op.toString());
			} else {
				sb.append(e);
			}
		}
		sb.append("]\n, elements = [");
		for (int i = 0; i < size(); ++i) {
			if (i > 0) sb.append(" ");
			sb.append(get(i));
		}
		sb.append("]\n, e. types = [");
		for (int i = 0; i < size(); ++i) {
			if (i > 0) sb.append(" ");
			sb.append(get(i).getElementType().toString().charAt(0));
		}
		return sb.append("]").toString();
	}
	
	/**
	 * Provjerava da li izmedju dva elementa treba ici konkatenacija
	 * (ako je izostavljena).
	 * 
	 * @param e1 tip lijevog elementa
	 * @param e2 tip desnog elementa
	 * @return <code>true</code> ako da, <code>false</code> inace.
	 */
	private static boolean requiresConcatenation(
			RegularExpressionElement e1,
			RegularExpressionElement e2) {
		if (e1 == null || e2 == null)
			return false;
		
		RegularExpressionElementType t1 = e1.getElementType();
		RegularExpressionElementType t2 = e2.getElementType();
		
		return t1.isOperand() && t2.isOperand()
		|| (t2.isOperand() || t2 == RegularExpressionElementType.LEFT_PARENTHESIS) 
		&& (t1.isOperand() || t1 == RegularExpressionElementType.RIGHT_PARENTHESIS
				|| e1 == RegularExpressionOperator.STAR);
		
	}
	
}
