package hr.fer.spocc.parser.gen;

import hr.fer.spocc.util.CharacterEscaper;


/**
 * 
 * KOPIRANO IZ lexer.gen
 *
 */
public class ParserGeneratorEscaper extends CharacterEscaper {
	
	private static final char[] FROM = new char[]{
		'(', ')', '{', '}', '|', '*', '\\', '$', ' ', '\n', '\t', '\r'
	};
	private static final char[] TO = new char[]{
		'(', ')', '{', '}', '|', '*', '\\', '$', '_',  'n',  't', 'r'
	};
	
	private static final ParserGeneratorEscaper INSTANCE
	= new ParserGeneratorEscaper();
	
	private ParserGeneratorEscaper() {
		super(FROM, TO);
	}

	public static ParserGeneratorEscaper getInstance() {
		return INSTANCE;
	}
	
}
