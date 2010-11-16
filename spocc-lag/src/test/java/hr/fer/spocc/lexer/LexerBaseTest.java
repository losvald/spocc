/*
 * LexerBaseTest.java
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
package hr.fer.spocc.lexer;

import static hr.fer.spocc.lexer.gen.CodeGenerationPropertyConstants.LEXER_DESCRIPTOR_FACTORY_CLASS_NAME_PROPERTY;
import static hr.fer.spocc.lexer.gen.CodeGenerationPropertyConstants.TOKEN_TYPES_CLASS_NAME_PROPERTY;
import hr.fer.spocc.SymbolTable;
import hr.fer.spocc.SymbolTable.Key;
import hr.fer.spocc.TokenType;
import hr.fer.spocc.lexer.gen.GeneratorProperties;
import hr.fer.spocc.lexer.gen.LexerGeneratorFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.sglj.util.Pair;

/**
 * @author Leo Osvald
 *
 */
@Ignore
public abstract class LexerBaseTest {

	private static final String LEXER_TEST_FILES_DIR = "../spocc-base/src/test/resources";
	private static final String GEN_INPUT_FILE_DIR = "src/test/resources";
	
	private static volatile LexerDescriptorFactory ldf;
	
	private LexerDescriptor lexerDescriptor;
	private final File generatorInputFile;
	
	public LexerBaseTest() {
		this.generatorInputFile = 
			new File(getGenRelativePath(
					getGeneratorInputShortFilename()));
	}
	
	@Before
	public void init() {
		lexerDescriptor = getLexerDescriptorFactory().createLexerDescriptor();
	}
	
	public File getGeneratorInputFile() {
		return generatorInputFile;
	}
	
	public Lexer createLexer() {
		return LexerFactory.createLexer(getLexerDescriptor());
	}
	
	public LexerDescriptor getLexerDescriptor() {
		return lexerDescriptor;
	}
	
	protected void generateLexerSource() throws IOException {
		LexerGeneratorFactory.getInstance().readFromFile(
				getGeneratorInputFile());
		LexerGeneratorFactory.getInstance().generate();
	}
	
	protected static LexerDescriptorFactory getLexerDescriptorFactory() {
		// lazy load
		if (ldf == null) {
			try {
				Class<?> clazz = Class.forName(GeneratorProperties
						.getProperty(LEXER_DESCRIPTOR_FACTORY_CLASS_NAME_PROPERTY));
				try {
					ldf = (LexerDescriptorFactory) clazz.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return ldf;
	}
	
	protected abstract String getGeneratorInputShortFilename();

	public static TokenType getTokenType(String name) {
		return getEnumType(name, GeneratorProperties
				.getProperty(TOKEN_TYPES_CLASS_NAME_PROPERTY));
	}
	
	protected static String getRelativePath(String shortFileName) {
		return LEXER_TEST_FILES_DIR + "/" + shortFileName;
	}
	
	protected static File toFile(String shortFileName) {
		return new File(getRelativePath(shortFileName));
	}
	
	protected void validateTokanization(
			Pair<SymbolTable, TokenList> expectedTables,
			String sourceFileShortName) {
		Lexer lexer = createLexer();
		Pair<SymbolTable, TokenList> computedTables
		= lexer.tokenizeAll(toFile(sourceFileShortName));
		
		hr.fer.spocc.SymbolTableWriter.getInstance().print(computedTables.first(), System.err);
		System.err.println("----");
		new hr.fer.spocc.lexer.TokenListWriter(computedTables.first()).print(computedTables.second(), System.err);
		
		System.err.println("Expected");
		hr.fer.spocc.SymbolTableWriter.getInstance().print(expectedTables.first(), System.err);
		System.err.println("----");
		new hr.fer.spocc.lexer.TokenListWriter(expectedTables.first()).print(expectedTables.second(), System.err);
		
		Assert.assertEquals(expectedTables.first(), computedTables.first());
		Assert.assertEquals(expectedTables.second(), computedTables.second());
	}
	
	protected void validateTokanization(
			String symbolTableShortFileName,
			String tokenListShortFileName,
			String sourceFileShortName) {
		System.out.println(tokenListShortFileName+"\n"
				+symbolTableShortFileName+"\n"
				+sourceFileShortName);
		Pair<SymbolTable, Map<Integer, Key>> symbolTableData
		= loadSymbolTable(symbolTableShortFileName);
		SymbolTable symbolTable = symbolTableData.first();
		TokenList tokenList = loadTokenList(tokenListShortFileName, symbolTableData);
		validateTokanization(new Pair<SymbolTable, TokenList>(
				symbolTable, tokenList), sourceFileShortName);
	}
	
	protected void validateTokanization(String baseName, String testCaseSuffix) {
		validateTokanization(
				getStandardSymbolTableFileName(baseName, testCaseSuffix),
				getStandardTokenListFileName(baseName, testCaseSuffix),
				getStandardSourceFileName(baseName, testCaseSuffix));
		
	}
	
	protected static void assertSymbolTableEquals(String shortFileName,
			SymbolTable symbolTable) {
		SymbolTable expected = loadSymbolTable(shortFileName).first();
		Assert.assertEquals(expected, symbolTable);
	}
	
	protected static void assertTokenListEquals(String shortFileName,
			TokenList tokenList, Pair<SymbolTable, Map<Integer, Key>> symbolTable) {
		TokenList expected = loadTokenList(shortFileName, symbolTable);
		Assert.assertEquals(expected, tokenList);
	}
	
	protected static void assertTablesEquals(
			String symbolTableShortFileName,
			SymbolTable symbolTable,
			String tokenListShortFileName,
			TokenList tokenList) {
		assertSymbolTableEquals(symbolTableShortFileName, symbolTable);
		try {
			assertTokenListEquals(tokenListShortFileName, tokenList, 
					SymbolTableReader.getInstance().read(toFile(symbolTableShortFileName)));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	protected static Pair<SymbolTable, Map<Integer, Key>> loadSymbolTable(String shortFileName) {
		try {
			return SymbolTableReader.getInstance().read(toFile(shortFileName));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
			return null;
		}
	}
	
	protected static TokenList loadTokenList(String shortFileName,
			Pair<SymbolTable, Map<Integer, Key>> symbolTable) {
		try {
			TokenListReader tlr = new TokenListReader(symbolTable);
			return tlr.read(toFile(shortFileName));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
			return null;
		}
	}
	
	protected static Pair<SymbolTable, TokenList> loadTables(
			String symbolTableShortFileName,
			String tokenListShortFileName) {
		Pair<SymbolTable, Map<Integer, Key>> st 
		= loadSymbolTable(symbolTableShortFileName);
		TokenList tl = loadTokenList(tokenListShortFileName, st);
		return new Pair<SymbolTable, TokenList>(st.first(), tl);
	}
	
	protected static String getStandardSymbolTableFileName(
			String baseName, String testCaseSuffix) {
		return baseName + "-" + testCaseSuffix + ".st";
	}
	
	protected static String getStandardTokenListFileName(
			String baseName, String testCaseSuffix) {
		return baseName + "-" + testCaseSuffix + ".tl";
	}
	
	protected static String getStandardSourceFileName(
			String baseName, String testCaseSuffix) {
		return baseName + "-" + testCaseSuffix + ".source";
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T getEnumType(String stateName, String className) {
		try {
			Class<?> clazz = Class.forName(className);
			Method m = clazz.getMethod("valueOf", String.class);
			return (T) m.invoke(null, stateName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String getGenRelativePath(String genShortFileName) {
		return GEN_INPUT_FILE_DIR + "/" + genShortFileName;
	}
	
}
