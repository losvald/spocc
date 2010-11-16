package hr.fer.spocc.lexer;

import java.util.EventListener;

public interface LexicalErrorListener extends EventListener {
	void onLexicalError(LexicalError error);
}
