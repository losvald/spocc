/*
 * ParserGenerator.java
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
package hr.fer.spocc.parser.gen;

import hr.fer.spocc.grammar.cfg.CfgGrammar;
import hr.fer.spocc.parser.ParsingTableFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Leo Osvald
 *
 */
public interface ParserGenerator {

	void readFromFile(File inputFile) throws IOException;

	void readFromStream(InputStream inputStream) throws IOException;

	/**
	 * Stvara izvorni kod koji omogucuje da se loada tablica.
	 * Tocnije, stvara instancu {@link ParsingTableFactory}-ja.  
	 *  
	 * @return pocetno stanje parsera
	 * @throws IllegalStateException ako je doslo do greske
	 */
	int generate() throws IllegalStateException;
	
	CfgGrammar<String> getGrammar();
	
	void setGenerateTokenTypes(boolean b);
}
