/*
 * Compiler.java
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

import hr.fer.spocc.lexer.LexicalErrorListener;
import hr.fer.spocc.lexer.TokenList;
import hr.fer.spocc.parser.ParseTree;
import hr.fer.spocc.parser.ParseTreeRepresentation;
import hr.fer.spocc.parser.SyntaxErrorListener;

import java.io.File;
import java.io.PrintStream;

/**
 * @author Leo Osvald
 *
 */
public interface Compiler {
	public static final int LEXICAL_ANALYSIS = 1 << 0;
	public static final int SYNTAX_ANALYSIS =  (1 << 1) | LEXICAL_ANALYSIS;
	public static final int SEMANTIC_ANALYSIS = (1 << 2) | SYNTAX_ANALYSIS;
	public static final int GENERATE_METACODE = 1 << 3;
	public static final int IGNORE_LEXICAL_ERRORS = 1 << 8;
	public static final int PRINT_PARSE_TREE = 1 << 10;
	public static final int PRINT_TOKEN_LIST = 1 << 11;
	public static final int PRINT_SYMBOL_TABLE = 1 << 12;
	
	public boolean compile(File sourceFile);
	public boolean isCompiledSuccessfully();
	public void setCompileFlagMask(int bitmask);
	public void setCompileFlags(int setMask);
	public boolean isCompileFlagsSet(int mask);
	public void clearCompileFlags(int clearMask);
	public int getCompileFlags();
	
	public ParseTree getParseTree();
	public void setParseTreeOutputFile(File dotOutputSourceFile);
	public File getParseTreeOutputFile();
	public void setParseTreePrintStream(PrintStream printStream);
	public void setParseTreeRepresentation(ParseTreeRepresentation type);
	public ParseTreeRepresentation getParseTreeRepresentation();
	public PrintStream getParseTreePrintStream();
	
	public TokenList getTokenList();
	public void setTokenListOutputFile(File outputFile);
	public File getTokenListOutputFile();
	public void setTokenListPrintStream(PrintStream printStream);
	public PrintStream getTokenListPrintStream();
	
	public SymbolTable getSymbolTable();
	public void setSymbolTableOutputFile(File outputFile);
	public File getSymbolTableOutputFile();
	public void setSymbolTablePrintStream(PrintStream printStream);
	public PrintStream getSymbolTablePrintStream();
	
	void addCompilationListener(CompilationListener listener);
	void removeCompilationListener(CompilationListener listener);
	void addLexicalErrorListener(LexicalErrorListener listener);
	void removeLexicalErrorListener(LexicalErrorListener listener);
	void addSyntaxErrorListener(SyntaxErrorListener listener);
	void removeSyntaxErrorListener(SyntaxErrorListener listener);
}
