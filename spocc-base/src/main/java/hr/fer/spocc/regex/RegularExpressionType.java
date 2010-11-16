package hr.fer.spocc.regex;

enum RegularExpressionType {
	EPSILON,
	EMPTY,
	TERMINAL,
	COMPLEX;
	
	public boolean isComplex() {
		return this == COMPLEX;
	}
}