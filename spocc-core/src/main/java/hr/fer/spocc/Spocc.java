/*
 * Spocc.java
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
package hr.fer.spocc;

import hr.fer.spocc.parser.ParseTreeRepresentation;
import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.Option;

import java.io.File;
import java.util.Arrays;

/**
 * @author Leo Osvald
 *
 */
public class Spocc {

	public static void printUsage() {
		System.err.println("Usage: Spocc source-file"
				+ " [{-s, --symbol-table} file]"
				+ " [{-t, --token-list} file]"
				+ " [{-p, --parse-tree} file]"
				+ " [{-h, --parse-tree-human-readable}]"
				+ " [{-i, --ignore-lexical-errors}]"
				+ " [{-v, --verbose}]");
		System.err.println("Note: instead of file \"-\" (without quotes)"
				+ " can be put to print to standard out");
	}

	public static void main(String[] args) {
		Environment.setEnvironment(Environment.RELEASE);
		
		CmdLineParser clParser = new CmdLineParser();
//		Option assembly = clParser.addStringOption('a', "assembly-file");
		Option parseTree = clParser.addStringOption('p', "parse-tree");
		Option symbolTable = clParser.addStringOption('s', "symbol-table");
		Option tokenList = clParser.addStringOption('t', "token-list");
		Option parseTreeHR = clParser.addBooleanOption('h',
				"parse-tree-human-readable");
		Option ignoreLexicalErrors = clParser.addBooleanOption('i',
				"ignore-lexical-errors");
		Option verbose = clParser.addBooleanOption('v', "verbose");
		
		try {
			clParser.parse(args);
		} catch ( CmdLineParser.OptionException e ) {
			System.err.println(Arrays.toString(args));
			System.err.println(e.getMessage());
			printUsage();
			System.exit(2);
		}
		
		String[] sourceFilenames = clParser.getRemainingArgs();
		if (sourceFilenames.length < 1) {
			printUsage();
			System.exit(2);
		}
		
		// check for verbose optoin
		if ((Boolean) clParser.getOptionValue(verbose, false))
			Environment.setEnvironment(Environment.DEVELOPMENT);
		
		// TODO
//		String assemblyValue = (String) clParser.getOptionValue(assembly,
//		"out.asm");
		
		String symbolTableValue = (String) clParser.getOptionValue(symbolTable);
		File symbolTableFile = null;
		boolean printSymbolTableToStdout = false;
		if (symbolTableValue != null) {
			if (symbolTableValue.equals("-")) {
				printSymbolTableToStdout = true;
			} else {
				symbolTableFile = new File(symbolTableValue);
			}
		}
		
		String tokenListValue = (String) clParser.getOptionValue(tokenList);
		File tokenListFile = null;
		boolean printTokenListToStdout = false;
		if (tokenListValue != null) {
			if (tokenListValue.equals("-")) {
				printTokenListToStdout = true;
			} else {
				tokenListFile = new File(tokenListValue);
			}
		}
		
		String parseTreeValue = (String) clParser.getOptionValue(parseTree);
		File parseTreeFile = null;
		boolean printParseTreeToStream = false;
		if (parseTreeValue != null) {
			if (parseTreeValue.equals("-")) {
				printParseTreeToStream = true;
			} else {
				parseTreeFile = new File(parseTreeValue);
			}
		}
		
		boolean parseTreeHRValue = (Boolean) clParser.getOptionValue(
				parseTreeHR, false);
		
		boolean ignoreLexicalErrorsValue = (Boolean) clParser.getOptionValue(
		ignoreLexicalErrors, false);

		for (String sourceFilename : sourceFilenames) {
			Compiler compiler = CompilerFactory.getInstance();
			attachListeners(compiler);

			if (ignoreLexicalErrorsValue)
				compiler.setCompileFlags(Compiler.IGNORE_LEXICAL_ERRORS);
			
			// postavi za parametre za printanje tablice znakova
			if (symbolTableFile != null) {
				compiler.setSymbolTableOutputFile(symbolTableFile);
				compiler.setCompileFlags(Compiler.PRINT_SYMBOL_TABLE);
			}
			if (printSymbolTableToStdout) {
				compiler.setSymbolTablePrintStream(System.out);
				compiler.setCompileFlags(Compiler.PRINT_SYMBOL_TABLE);
			}
			
			// postavi za parametre za printanje niza uniformnih znakova
			if (tokenListFile != null) {
				compiler.setTokenListOutputFile(tokenListFile);
				compiler.setCompileFlags(Compiler.PRINT_TOKEN_LIST);
			}
			if (printTokenListToStdout) {
				compiler.setTokenListPrintStream(System.out);
				compiler.setCompileFlags(Compiler.PRINT_TOKEN_LIST);
			}
			
			// postavi za parametre za printanje sintaksnog stable
			if (parseTreeFile != null) {
				compiler.setParseTreeOutputFile(parseTreeFile);
				compiler.setCompileFlags(Compiler.PRINT_PARSE_TREE);
			}
			if (printParseTreeToStream) {
				compiler.setParseTreePrintStream(System.out);
				compiler.setCompileFlags(Compiler.PRINT_PARSE_TREE);
			}
			if (compiler.isCompileFlagsSet(Compiler.PRINT_PARSE_TREE)) {
				compiler.setParseTreeRepresentation(parseTreeHRValue
						? ParseTreeRepresentation.CLASSIC
								: ParseTreeRepresentation.DOT);
			}

			File sourceFile = new File(sourceFilename);
			boolean status = compiler.compile(sourceFile);
			
			if (!status) {
				System.err.println("Compilation failed");
			}
		}

	}
	
	private static void attachListeners(Compiler compiler) {
		CommandLineErrorListener listener 
		= CommandLineErrorListener.getInstance();
		compiler.addLexicalErrorListener(listener);
		compiler.addSyntaxErrorListener(listener);
	}
	
}
