package hr.fer.spocc.lexer;

import hr.fer.spocc.CompileMessage;

public final class LexicalError extends CompileMessage {

	public LexicalError(int line, String message) {
		super(line, message, Level.ERROR);
	}

}
