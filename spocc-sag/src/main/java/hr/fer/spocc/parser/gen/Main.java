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
package hr.fer.spocc.parser.gen;

import hr.fer.spocc.Environment;
import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.Option;

import java.io.File;
import java.io.IOException;

/**
 * @author Leo Osvald
 *
 */
public class Main {

	public static void printUsage() {
		System.err.println("Usage: Main syntax-rules-file"
				+ " [{-o, --output-dir} path]"
				+ " [{-t, --generate-token-types}]"
				+ " [{-c, --use-c-precomputed}]"
				+ " [{-v, --verbose}]");
	}
	
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Environment.setEnvironment(Environment.RELEASE);
		
		GeneratorProperties.getProperty(
				CodeGenerationPropertyConstants
				.PARSING_TABLE_FACTORY_FILLER_CLASSES_BASENAME_PROPERTY);
		
		CmdLineParser clParser = new CmdLineParser();
		Option outputDir = clParser.addStringOption('o', "--output-dir");
		Option genTokenTypes = clParser.addBooleanOption('t', "generate-token-types");
		Option precomputedCParsingTable = clParser.addBooleanOption('c', "use-c-precomputed");
		Option verbose = clParser.addBooleanOption('v', "verbose");
		
		try {
            clParser.parse(args);
        } catch ( CmdLineParser.OptionException e ) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(2);
        }
        
        if ((Boolean) clParser.getOptionValue(verbose, false))
        	Environment.setEnvironment(Environment.DEVELOPMENT);
        
        String outputDirFilename = (String) clParser.getOptionValue(outputDir);
        if (outputDirFilename != null) {
        	File outputDirFile = new File(outputDirFilename);
        	GeneratorProperties.setProperty(CodeGenerationPropertyConstants
        			.DEFAULT_DIRECTORY_PROPERTY, 
        			outputDirFile.getAbsolutePath());
        }
        
        String[] remainingArgs = clParser.getRemainingArgs();
        
        if (remainingArgs.length < 1) {
        	printUsage();
            System.exit(2);
        }
        
        ParserGenerator parserGenerator = 
        	((Boolean) clParser.getOptionValue(precomputedCParsingTable, false)
        			? new CParserGenerator()
        	: new DefaultParserGenerator());
        
        parserGenerator.setGenerateTokenTypes((Boolean) clParser.getOptionValue(
        		genTokenTypes, false));
        
		parserGenerator.readFromFile(new File(remainingArgs[0]));
		parserGenerator.generate();
	}
	
}
