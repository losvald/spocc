/*
 * AbstractRegularExpression.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public abstract class AbstractRegularExpression<T> 
implements RegularExpression<T> {
	
	private final List<RegularExpressionElement> elements;
	
	private final RegularExpressionOperator operator;
	private final RegularExpression<T> subexpression1;
	private final RegularExpression<T> subexpression2;
	
	private final RegularExpressionType type;
	
	public AbstractRegularExpression(
			Collection<RegularExpressionElement> c) 
	throws IllegalArgumentException {
		Validate.isTrue(c != null && !c.isEmpty(),
				"Element list must be non-empty");
		
		this.elements = Collections.unmodifiableList(
				new ArrayList<RegularExpressionElement>(c));

		RegularExpression<T> root = createParseTree(elements);
		
		this.type = root.getType();
		if (root.isTrivial()) {
			this.operator = null;
			this.subexpression1 = null;
			this.subexpression2 = null;
		} else {
			this.operator = root.getOperator();
			int arity = operator.getArity();
			this.subexpression1 = (arity > 0 ? root.getSubexpression(0) : null);
			this.subexpression2 = (arity > 1 ? root.getSubexpression(1) : null);
		}
	}
	
	protected AbstractRegularExpression(
			RegularExpression<T> unexpectedRegex,
			List<RegularExpressionElement> list) {
		this.elements = list;
		this.type = unexpectedRegex.getType();
		if (unexpectedRegex.isTrivial()) {
			this.operator = null;
			this.subexpression1 = null;
			this.subexpression2 = null;
		} else {
			this.operator = unexpectedRegex.getOperator();
			int arity = operator.getArity();
			this.subexpression1 = (arity > 0 ? unexpectedRegex.getSubexpression(0) : null);
			this.subexpression2 = (arity > 1 ? unexpectedRegex.getSubexpression(1) : null);
		}
	}
	
	protected AbstractRegularExpression(List<RegularExpressionElement> list,
			RegularExpressionType type,
			RegularExpressionOperator operator, 
			RegularExpression<T>... subexpressions) {
		this.elements = list;
		this.type = type;
		this.operator = operator;
		this.subexpression1 = (subexpressions.length > 0 
				? subexpressions[0] : null);
		this.subexpression2 = (subexpressions.length > 1
				? subexpressions[1] : null);
		
//		System.out.println("Cons("+list+"; op = "+operator
//				+"; subexps = "+ArrayToStringUtils.toString(
//						subexpressions, "\n"));
//		System.out.println("Cons-list = "+list);
//		System.out.println("Cons-op = "+operator);
//		System.out.println("Cons-subexp1 = "+subexpression1);
//		System.out.println("Cons-subexp2 = "+subexpression2);
	}
	
	@Override
	public boolean isTrivial() {
		return !getType().isComplex();
	}
	
	@Override
	public RegularExpressionType getType() {
		return type;
		
//		if (elements.isEmpty())
//			return RegularExpressionType.EPSILON;
//		if (elements.size() > 1)
//			return RegularExpressionType.COMPLEX;
//		
//		@SuppressWarnings("unchecked")
//		RegularExpressionSymbol<T> symbol 
//		= (RegularExpressionSymbol<T>) elements.get(0);
//		switch (symbol.getSymbolType()) {
//		case TERMINAL:
//			return RegularExpressionType.TERMINAL;
//		default:
//			return RegularExpressionType.EPSILON;
//		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getTrivialValue() {
		return isTrivial()
		? ((RegularExpressionSymbol<T>) elements.get(elements.size()/2))
				.getValue() : null;
	}
	
	@Override
	public RegularExpressionElement get(int index) {
		return elements.get(index);
	}
	
	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public RegularExpressionOperator getOperator() {
		return operator;
	}
	
	@Override
	public RegularExpression<T> getSubexpression(int n) {
		if (isTrivial() || n < 0 || n >= getOperator().getArity())
			throw new IndexOutOfBoundsException();

		return n == 0 ? subexpression1 : subexpression2;
	}
	
//	@Override
//	public RegularExpression<T>[] getSubexpressions() {
//		RegularExpressionOperator operator = getOperator();
//		if (operator == null)
//			return null;
//		
//		@SuppressWarnings("unchecked")
//		RegularExpression<T>[] ret = new RegularExpression[operator.getArity()];
//		if (ret.length > 0)
//			ret[0] = subexpression1;
//		if (ret.length > 1)
//			ret[1] = subexpression2;
//		return ret;
//	}
	
	protected abstract RegularExpression<T> createTrivial(
			List<RegularExpressionElement> sublist);
	
	protected abstract RegularExpression<T> createComposite(
			List<RegularExpressionElement> list,
			RegularExpressionOperator operator, 
			RegularExpression<T>... subexpressions);
	
	protected abstract RegularExpression<T> createExpanded(
			RegularExpression<T> unexpandedRegex,
			List<RegularExpressionElement> list);
	
	protected RegularExpression<T> createParseTree(
			List<RegularExpressionElement> elements) {
		
//		System.out.println(">>> Parsing regexp: "+elements);
		
		/**
		 * Stack which contains parts of regular expression 
		 * which are not yet used
		 * by the operator. In addition, <code>null</code> values
		 * can be pushed onto this stack to indicate that 
		 * the symbols to the right are grouped by the parenthesis.
		 * 
		 */
		LinkedList<RegularExpression<T>> symbolStack 
		= new LinkedList<RegularExpression<T>>();
		
		/**
		 * Operator stack
		 */
		LinkedList<RegularExpressionOperator> opStack 
		= new LinkedList<RegularExpressionOperator>();
		
		boolean sentinelParentheses = false;
		
//		if (this.elements.get(0).getElementType() 
//				!= RegularExpressionElementType.LEFT_PARENTHESIS
//			|| this.elements.get(elements.size()-1).getElementType()
//			!= RegularExpressionElementType.RIGHT_PARENTHESIS) {
			sentinelParentheses = true;
			symbolStack.push(null);
			opStack.push(null);
//		}
		
		int ind = -1;
		
		Iterator<RegularExpressionElement> iter = elements.iterator();
		while (iter.hasNext() || sentinelParentheses) {
			++ind;
			RegularExpressionElement e;
			if (iter.hasNext()) {
				e = iter.next();
			} else { // osiguraj dodatnu iteraciju za umjetnu zadnju )
				e = RegularExpressionElements.RIGHT_PARENTHESIS;
				sentinelParentheses = false;
			}
			
			switch (e.getElementType()) {
			case SYMBOL:
				symbolStack.push(createTrivial(elements.subList(ind, ind+1)));
				break;
			default:
				RegularExpressionOperator curOp = (e.getElementType()
						== RegularExpressionElementType.OPERATOR
						? (RegularExpressionOperator) e
								: null);
				
				int priority = (curOp != null ? curOp.getPriority() : -1);
				
				if (e.getElementType() != RegularExpressionElementType
						.LEFT_PARENTHESIS) {
					
//					System.out.println("Pre-while symbolStack: "+symbolStack);
					
					while (!opStack.isEmpty() && opStack.getFirst() != null
							&& opStack.getFirst().getPriority() >= priority 
							&& symbolStack.getFirst() != null) {
						
						RegularExpressionOperator op = opStack.pop();
						int arity = op.getArity();
						int elementCount = 0;
						
//						System.out.println("POP: "+op);
						
						@SuppressWarnings("unchecked")
						RegularExpression<T>[] operands = 
							new RegularExpression[arity];
						for (int i = arity - 1; i >= 0; --i) {
							if (symbolStack.isEmpty()) {
								throw new IllegalArgumentException(
										"Missing ( after");
							} else if (symbolStack.getFirst() == null) {
								throw new IllegalArgumentException(
										"Missing operand #" + (arity-i)
										+ " for the operator " + op
										+ " before index " + ind);
							}
							operands[i] = symbolStack.pop();
							elementCount += operands[i].size();
						}
						
						RegularExpression<T> regex = createComposite(
								elements.subList(ind - elementCount - 1, ind), 
								op, operands);
						
//						System.err.println(regex);
//						System.err.println(regex.getSubexpression(0));
						
						symbolStack.push(regex);
						
//						System.out.println("Group: "+
//								ArrayToStringUtils.toString(operands, "\n"));
//						System.out.println("End group");
//						System.out.println("Evaluated [" + (ind-elementCount-1)
//								+ ", " + ind + "): "+regex);
//						System.out.println("Symbol stack: "+symbolStack);
//						System.out.println("Op stack: "+opStack);
//						System.out.println("---");
						
					}
				}
				
				if (curOp != null) {
					opStack.push(curOp);
				} else {
					switch (e.getElementType()) {
					case LEFT_PARENTHESIS:
						symbolStack.push(null);
						opStack.push(null);
						break;
					default: // ako je )
						Validate.isTrue(symbolStack.size() >= 2,
								"Exactly one expression is expected "
								+ "inside parentheses before index "
								+ ind);
						
						// pop left bracket (null) from the operator stack
						Object nullValue = opStack.pop();
						Validate.isTrue(nullValue == null);
						
						// pop left bracket (null) from the symbol stack
						RegularExpression<T> regex = symbolStack.pop();
						nullValue = symbolStack.pop();
						
						// check if left bracket was removed indeed
//						Validate.isTrue(nullValue == null, 
//								"Expected ( at index " + (ind-regex.size()-1));
						
						// expand the expression if parentheses are not sentinel
						if (sentinelParentheses) { // XXX neki drugi flag bolje
//							System.out.print("Expand [" 
//									+ (ind - regex.size() - 1) + ", "
//									+ (ind + 1) + "]: ");
//							System.out.println("[regex size = "+regex.size()
//									+ "]");
							regex = createExpanded(regex, elements.subList(
									ind - regex.size() - 1, ind + 1));
							
//							System.out.println(" -> "+regex);
						}
						
						// and put back the expression inside parentheses
						symbolStack.push(regex);
					}
				}
				
			} // end of switch
			
			
//			System.out.println("----- " + ind + " ----");
//			System.out.println("Symbol stack: "+symbolStack);
//			System.out.println("Op stack: "+opStack);
		}
		
		//Validate.isTrue(symbolStack.size() == 1);
		//Validate.isTrue(opStack.isEmpty());
		
		return symbolStack.pop();
	}

	@Override
	public int hashCode() {
		return 31 + ((elements == null) ? 0 : elements.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractRegularExpression<?> other = (AbstractRegularExpression<?>) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}
	
}
