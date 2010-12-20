/*
 * DefaultCompiler.java
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

import hr.fer.spocc.export.DotExporter;
import hr.fer.spocc.lexer.LexerDescriptorFactory;
import hr.fer.spocc.lexer.TokenListWriter;
import hr.fer.spocc.lexer._LexerDescriptorFactory;
import hr.fer.spocc.parser.ParseTree;
import hr.fer.spocc.parser.ParsingTableFactory;
import hr.fer.spocc.parser._ParsingTableFactory;
import hr.fer.spocc.util.AbstractEntityWriter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Default implementacija kompajlera (koristi kod izgeneriran
 * od strane generatora leksickog analizatora i generatora sintaksnog 
 * analizatora).
 * 
 * @author Leo Osvald
 *
 */
class DefaultCompiler extends AbstractCompiler {

	@Override
	protected LexerDescriptorFactory createLexerDescriptorFactory() {
		return new _LexerDescriptorFactory();
	}
	
	@Override
	protected ParsingTableFactory createParsingTableFactory() {
		return new _ParsingTableFactory();
	}
	
	@Override
	protected boolean outputSymbolTable(File outputFile) {
		try {
			SymbolTableWriter.getInstance().print(getSymbolTable(), outputFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected void outputSymbolTable(PrintStream printStream) {
		SymbolTableWriter.getInstance().print(getSymbolTable(), printStream);
	}
	
	@Override
	protected boolean outputTokenList(File outputFile) {
		try {
			TokenListWriter writer = new TokenListWriter(getSymbolTable());
			writer.print(getTokenList(), outputFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected void outputTokenList(PrintStream printStream) {
		TokenListWriter writer = new TokenListWriter(getSymbolTable());
		writer.print(getTokenList(), printStream);
	}
	
	@Override
	protected boolean checkSemantic(ParseTree parseTree) {
		// TODO semanticka analiza
		return true;
	}
	
	@Override
	protected boolean outputSyntaxTree(File outputFile) {
		try {
			switch (getParseTreeRepresentation()) {
			case CLASSIC:
				ParseTreeFileWriter.INSTANCE.print(getParseTree(), outputFile);
				break;
			default:
				DotExporter.exportToFile(outputFile, getParser().getParseTree());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	protected void outputSyntaxTree(PrintStream printStream) {
		switch (getParseTreeRepresentation()) {
		case CLASSIC:
			printParseTreeClassically(getParseTree(), printStream);
			break;
		default:
			printStream.append(DotExporter.toDotString(getParseTree()));
			printStream.append("\n");
		}
	}
	
	private static boolean printParseTreeClassically(ParseTree parseTree,
			PrintStream printStream) {
		printStream.append(parseTree.toString());
		printStream.append("\n");
		return true;
	}
	
	private static class ParseTreeFileWriter 
	extends AbstractEntityWriter<ParseTree> {

		private static final ParseTreeFileWriter INSTANCE 
		= new ParseTreeFileWriter();
		
		@Override
		public boolean print(ParseTree entity, PrintStream printStream) {
			return printParseTreeClassically(entity, printStream);
		} 
	}

}
