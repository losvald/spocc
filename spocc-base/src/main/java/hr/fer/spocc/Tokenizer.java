package hr.fer.spocc;

public interface Tokenizer {
	/**
	 * Vraca sljedeci token ili <code>null</code> ako ga nema
	 * @return
	 */
	Token nextToken();
	
	/**
	 * Resetira tokenizer.
	 */
	void reset();
}
