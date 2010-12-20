/*
 * HardcodedCParsingTableTest.java
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

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.Option;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;

/**
 * @author Leo Osvald
 *
 */
@Ignore
public class HardcodedCParsingTableTest {

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

		ParserGenerator parserGenerator = new CParserGenerator();
		parserGenerator.readFromFile(new File(remainingArgs[0]));
		parserGenerator.generate();
	}

	public static void printUsage() {
		System.out.println("HardcodedCParsingTableTest syntax-rules-file"
				+ " [{-o, --output-dir} path]");
	}

}
