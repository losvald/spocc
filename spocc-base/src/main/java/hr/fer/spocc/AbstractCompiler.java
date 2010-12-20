/*
 * AbstractCompiler.java
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

import hr.fer.spocc.lexer.DefaultLexer;
import hr.fer.spocc.lexer.Lexer;
import hr.fer.spocc.lexer.LexerDescriptorFactory;
import hr.fer.spocc.lexer.LexicalErrorListener;
import hr.fer.spocc.lexer.TokenList;
import hr.fer.spocc.parser.LR1Parser;
import hr.fer.spocc.parser.ParseTree;
import hr.fer.spocc.parser.ParseTreeRepresentation;
import hr.fer.spocc.parser.Parser;
import hr.fer.spocc.parser.ParsingTable;
import hr.fer.spocc.parser.ParsingTableFactory;
import hr.fer.spocc.parser.SyntaxErrorListener;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * Apstraktna implementacija kompajlera (bez semanticke analize).
 * 
 * @author Leo Osvald
 *
 */
abstract class AbstractCompiler implements Compiler {

	private final Lexer lexer;
	private final Parser parser;
	private File lastCompiledFile;
	
	private boolean compiledSuccessfully;
	
	private final List<CompilationListener> listeners
	= new ArrayList<CompilationListener>();
	
	private File syntaxTreeOutputFile;
	private PrintStream syntaxTreePrintStream;
	private ParseTreeRepresentation parseTreeRepresentation
	= ParseTreeRepresentation.DOT;
	
	private File tokenListOutputFile;
	private PrintStream tokenListPrintStream;
	
	private File symbolTableOutputFile;
	private PrintStream symbolTablePrintStream;
	
	private boolean lexOk;
	private boolean syntaxOk;
	
	private TokenList tokenList;
	
	private int compileFlags = 
		LEXICAL_ANALYSIS | SYNTAX_ANALYSIS; 
	
	public AbstractCompiler() {
		lexer = new DefaultLexer(createLexerDescriptorFactory()
		.createLexerDescriptor());
		
		ParsingTable<TokenType> parsingTable
		= createParsingTableFactory().createParsingTable();
		parser = new LR1Parser(lexer, parsingTable);
		parser.setSymbolTable(lexer.getSymbolTable());
	}
	
	@Override
	public synchronized boolean compile(File sourceFile) {
		Validate.notNull(sourceFile);
		
		getLexer().reset();
		getParser().reset();
		
		setLastCompiledFile(sourceFile);
		setCompiledSuccessfully(false);
		
		boolean ret = true;
		syntaxOk = false;
		try {
			// do lexical analysis
			tokenList = lexer.tokenizeAll(sourceFile).second();
			
			// check for lexical errors
			if (!isCompileFlagsSet(IGNORE_LEXICAL_ERRORS)) {
				lexOk = !lexer.isError();
				if (!(ret &= lexOk))
					return ret;
				
				if (isCompileFlagsSet(PRINT_SYMBOL_TABLE)) {
					File file = getSymbolTableOutputFile();
					if (file != null && !(ret &= outputSymbolTable(file)))
						return ret;
					
					PrintStream printStream = getSymbolTablePrintStream();
					if (printStream != null)
						outputSymbolTable(printStream);
				}
				
				if (isCompileFlagsSet(PRINT_TOKEN_LIST)) {
					File file = getTokenListOutputFile();
					if (file != null && !(ret &= outputTokenList(file)))
						return ret;
					
					PrintStream printStream = getTokenListPrintStream();
					if (printStream != null)
						outputTokenList(printStream);
				}
			}
			
			// do parsing and check for errors
			if (isCompileFlagsSet(SYNTAX_ANALYSIS)) {
				parser.setSourceFile(sourceFile);
				syntaxOk = parser.parse();
				if (!(ret &= syntaxOk))
					return ret;
						
				if (isCompileFlagsSet(PRINT_PARSE_TREE)) {
					File file = getParseTreeOutputFile();
					if (file != null && !(ret &= outputSyntaxTree(file)))
						return ret;
					
					PrintStream printStream = getParseTreePrintStream();
					if (printStream != null)
						outputSyntaxTree(printStream);
				}
				
			}
			
			if (isCompileFlagsSet(SEMANTIC_ANALYSIS))
				if (!(ret &= checkSemantic(parser.getParseTree())))
					return ret;
		} finally {
			// set compile status and fire necessary events
			setCompiledSuccessfully(ret);
			if (ret)
				fireSourceCompiled(sourceFile);
			else
				fireSourceCompilationFailed(sourceFile);
		}
		
		return ret;
	}
	
	@Override
	public synchronized boolean isCompiledSuccessfully() {
		return compiledSuccessfully;
	}
	
	@Override
	public synchronized void setCompileFlagMask(int bitmask) {
		compileFlags = bitmask;
	}

	@Override
	public synchronized void setCompileFlags(int setMask) {
		compileFlags |= setMask;
	}
	
	@Override
	public synchronized void clearCompileFlags(int clearMask) {
		compileFlags &= ~clearMask;
	}
	
	@Override
	public synchronized boolean isCompileFlagsSet(int mask) {
		return (compileFlags & mask) == mask;
	}
	
	@Override
	public synchronized int getCompileFlags() {
		return compileFlags;
	}
	
	@Override
	public synchronized void setParseTreeOutputFile(File dotOutputSourceFile) {
		this.syntaxTreeOutputFile = dotOutputSourceFile;
	}
	
	@Override
	public synchronized File getParseTreeOutputFile() {
		return syntaxTreeOutputFile;
	}
	
	@Override
	public synchronized void setParseTreePrintStream(PrintStream printStream) {
		this.syntaxTreePrintStream = printStream;
	}
	
	@Override
	public synchronized PrintStream getParseTreePrintStream() {
		return syntaxTreePrintStream;
	}
	
	public synchronized void setParseTreeRepresentation(
			ParseTreeRepresentation parseTreeRepresentation) {
		this.parseTreeRepresentation = parseTreeRepresentation;
	}
	
	public synchronized ParseTreeRepresentation getParseTreeRepresentation() {
		return parseTreeRepresentation;
	}
	
	@Override
	public synchronized ParseTree getParseTree() {
		if (!syntaxOk)
			return null;
		return getParser().getParseTree();
	}
	
	@Override
	public synchronized TokenList getTokenList() {
		if (!lexOk)
			return null;
		return tokenList;
	}
	
	@Override
	public synchronized File getTokenListOutputFile() {
		return tokenListOutputFile;
	}
	
	@Override
	public synchronized void setTokenListOutputFile(File outputFile) {
		this.tokenListOutputFile = outputFile;
	}
	
	@Override
	public synchronized PrintStream getTokenListPrintStream() {
		return tokenListPrintStream;
	}
	
	@Override
	public synchronized void setTokenListPrintStream(PrintStream printStream) {
		this.tokenListPrintStream = printStream;
	}
	
	@Override
	public synchronized SymbolTable getSymbolTable() {
		if (!lexOk)
			return null;
		return getLexer().getSymbolTable();
	}
	
	@Override
	public synchronized File getSymbolTableOutputFile() {
		return symbolTableOutputFile;
	}
	
	@Override
	public synchronized void setSymbolTableOutputFile(File outputFile) {
		this.symbolTableOutputFile = outputFile;
	}
	
	@Override
	public synchronized PrintStream getSymbolTablePrintStream() {
		return symbolTablePrintStream;
	}
	
	@Override
	public synchronized void setSymbolTablePrintStream(PrintStream printStream) {
		this.symbolTablePrintStream = printStream;
	}
	
	
	
	@Override
	public synchronized void addCompilationListener(CompilationListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public synchronized void removeCompilationListener(CompilationListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public synchronized void addLexicalErrorListener(LexicalErrorListener listener) {
		lexer.addLexicalErrorListener(listener);
	}
	
	@Override
	public synchronized void removeLexicalErrorListener(LexicalErrorListener listener) {
		lexer.removeLexicalErrorListener(listener);
	}
	
	@Override
	public synchronized void addSyntaxErrorListener(SyntaxErrorListener listener) {
		parser.addSyntaxErrorListener(listener);
	}
	
	@Override
	public synchronized void removeSyntaxErrorListener(SyntaxErrorListener listener) {
		parser.removeSyntaxErrorListener(listener);
	}
	
	public synchronized File getLastCompiledFile() {
		return lastCompiledFile;
	}
	
	protected synchronized Lexer getLexer() {
		return lexer;
	}
	
	protected synchronized Parser getParser() {
		return parser;
	}
	
	protected abstract LexerDescriptorFactory createLexerDescriptorFactory();
	
	protected abstract ParsingTableFactory createParsingTableFactory();
	
	protected abstract boolean checkSemantic(ParseTree parseTree);
	
	protected abstract boolean outputSyntaxTree(File outputFile);
	protected abstract void outputSyntaxTree(PrintStream printStream);
	
	protected abstract boolean outputSymbolTable(File outputFile);
	protected abstract void outputSymbolTable(PrintStream printStream);
	
	protected abstract boolean outputTokenList(File outputFile);
	protected abstract void outputTokenList(PrintStream printStream);
	
	protected void setCompiledSuccessfully(boolean compiledSuccessfully) {
		this.compiledSuccessfully = compiledSuccessfully;
	}
	
	protected void setLastCompiledFile(File lastCompiledFile) {
		this.lastCompiledFile = lastCompiledFile;
	}
	
	protected void fireSourceCompiled(File sourceFile) {
		for (CompilationListener l : listeners)
			l.sourceFileCompiled(sourceFile);
	}
	
	protected void fireSourceCompilationFailed(File sourceFile) {
		for (CompilationListener l : listeners)
			l.sourceFileCompilationFailed(sourceFile);
	}
	
}
