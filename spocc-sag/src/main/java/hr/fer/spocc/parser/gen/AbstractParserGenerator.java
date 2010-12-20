/*
 * AbstractParserGenerator.java
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

import hr.fer.spocc.gen.CodeGeneratorCleaner;
import hr.fer.spocc.gen.TokenTypeDescriptor;
import hr.fer.spocc.parser.gen.naming.FillerNameFactory;
import hr.fer.spocc.parser.gen.naming.ParsingTableDescriptorFactoryNameFactory;
import hr.fer.spocc.parser.gen.naming.TokenTypeNameFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Leo Osvald
 *
 */
public abstract class AbstractParserGenerator implements ParserGenerator {

	private boolean genTokenTypes;
	
	@Override
	public void readFromFile(File inputFile) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(
				new FileInputStream(inputFile));
		try {
			readFromStream(bis);
		} finally {
			bis.close();
		}
	}
	
	@Override
	public int generate() throws IllegalStateException {
		List<String> toClean = new ArrayList<String>(Arrays.asList(
				FillerNameFactory.getInstance().packageName(),
				ParsingTableDescriptorFactoryNameFactory
				.getInstance().packageName()));
		if (genTokenTypes) {
			System.err.println("Package: "+TokenTypeNameFactory.getInstance().packageName());
				toClean.add(TokenTypeNameFactory.getInstance().packageName());
		}
		
		String[] toCleanArr = new String[toClean.size()];
		for (int i = 0; i < toClean.size(); ++i)
			toCleanArr[i] = toClean.get(i);
		
		CodeGeneratorCleaner.cleanup(
				GeneratorProperties.getProperty(CodeGenerationPropertyConstants
						.DEFAULT_DIRECTORY_PROPERTY),
					toCleanArr
						
		);
		
		ParsingTableDescriptorFactory factory 
		= createParsingTableDescriptorFactory();
		
		if (genTokenTypes && tokenTypeDescriptor() != null) {
			TokenTypeCodeGenerator tokenTypeCodegen = 
				new TokenTypeCodeGenerator(tokenTypeDescriptor());
			try {
				tokenTypeCodegen.generateSourceFile();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		
		ParsingTableDescriptor descriptor = factory.createDescriptor();
		ParsingTableFactoryCodeGenerator codegen 
		= new ParsingTableFactoryCodeGenerator(descriptor);
		try {
			codegen.generateSourceFile();
			return descriptor.getStartStateId();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	protected abstract ParsingTableDescriptorFactory 
	createParsingTableDescriptorFactory();
	
	protected abstract TokenTypeDescriptor tokenTypeDescriptor();

	@Override
	public void setGenerateTokenTypes(boolean b) {
		this.genTokenTypes = b;
	}
}
