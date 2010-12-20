package hr.fer.spocc.parser.gen;

import hr.fer.spocc.TokenType;
import hr.fer.spocc.gen.CodeGenerator;
import hr.fer.spocc.gen.TokenTypeDescriptor;
import hr.fer.spocc.parser.gen.naming.TokenTypeNameFactory;

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
 * @author Marin Pranjić
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
