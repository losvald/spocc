/*
 * TokenTypeCodeGenerator.java
 *
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 * Copyright (C) 2010 Marin Pranjic <marin.pranjic@gmail.com>
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

import hr.fer.spocc.TokenType;
import hr.fer.spocc.gen.CodeGenerator;
import hr.fer.spocc.gen.TokenTypeDescriptor;
import hr.fer.spocc.lexer.gen.naming.TokenTypeNameFactory;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

/**
 * Generator koda koji mora izgenerirati kod za implementaciju
 * sucelja {@link TokenType} ciji opisnik dobiva preko konstruktora.
 * 
 * @author Marin Pranjic
 * @author Leo Osvald
 *
 */
class TokenTypeCodeGenerator implements CodeGenerator {

	private TokenTypeDescriptor typeTokenDescriptor;
	private Set<String> tokentypes;
	
	public TokenTypeCodeGenerator(TokenTypeDescriptor tokenTypeDescriptor) {
		this.typeTokenDescriptor = tokenTypeDescriptor;
		
		}

	@Override
	public void generateSourceFile() throws JClassAlreadyExistsException, IOException {
		tokentypes = this.typeTokenDescriptor.getTypeTokens();
		JCodeModel cm = new JCodeModel();
		JDefinedClass dc = cm._class(TokenTypeNameFactory.getInstance().getFullEnumName(), ClassType.ENUM);
		dc._implements(TokenType.class);
		
		for (String tokentype : tokentypes) {
			
			dc.enumConstant(tokentype);
		}
		
		String dir = GeneratorProperties.getProperty(
				CodeGenerationPropertyConstants.
				DEFAULT_DIRECTORY_PROPERTY);
		
		File file = new File(dir);
		file.mkdirs();
		cm.build(file);
	}

}
