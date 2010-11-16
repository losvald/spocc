package hr.fer.spocc.lexer;


public interface Subaction {
	SubactionType getType();
	void perform(Lexer lexer, Object... args);
}
