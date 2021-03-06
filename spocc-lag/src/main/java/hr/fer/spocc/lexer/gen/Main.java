/*
 * Main.java
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
package hr.fer.spocc.lexer.gen;

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.Option;

import java.io.File;
import java.io.IOException;

/**
 * Glavni program generatora leksickog analizatora (LGA). 
 * 
 * @author Leo Osvald
 *
 */
public class Main {

	/**
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		CmdLineParser parser = new CmdLineParser();
		Option outputDir = parser.addStringOption('o', "--output-dir");
		try {
            parser.parse(args);
        } catch ( CmdLineParser.OptionException e ) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(2);
        }
        
        String outputDirFilename = (String) parser.getOptionValue(outputDir);
        if (outputDirFilename != null) {
        	File outputDirFile = new File(outputDirFilename);
        	GeneratorProperties.setProperty(CodeGenerationPropertyConstants
        			.DEFAULT_DIRECTORY_PROPERTY, 
        			outputDirFile.getAbsolutePath());
        }
        
        String[] remainingArgs = parser.getRemainingArgs();
        
        if (remainingArgs.length < 1) {
        	printUsage();
            System.exit(2);
        }
        
        LexerGenerator lexerGenerator = LexerGeneratorFactory.getInstance();
		lexerGenerator.readFromFile(new File(args[0]));
		lexerGenerator.generate();
	}
	
	public static void printUsage() {
		System.err.println("Usage: Main lexical-rules-file"
				+ " [{-o, --output-dir} path]");
	}

}
