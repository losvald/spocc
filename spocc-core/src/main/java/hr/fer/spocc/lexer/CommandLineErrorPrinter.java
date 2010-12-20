package hr.fer.spocc.lexer;

import hr.fer.spocc.parser.SyntaxError;
import hr.fer.spocc.parser.SyntaxErrorListener;

public class CommandLineErrorPrinter implements LexicalErrorListener,
		SyntaxErrorListener {

	@Override
	public void onSyntaxError(SyntaxError error) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLexicalError(LexicalError error) {
		// TODO Auto-generated method stub

	}

}
