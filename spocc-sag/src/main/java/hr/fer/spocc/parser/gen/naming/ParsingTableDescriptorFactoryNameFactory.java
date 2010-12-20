package hr.fer.spocc.parser.gen.naming;

import hr.fer.spocc.gen.naming.ClassNameFactory;
import hr.fer.spocc.parser.gen.CodeGenerationPropertyConstants;
import hr.fer.spocc.parser.gen.GeneratorProperties;

public class ParsingTableDescriptorFactoryNameFactory
extends ClassNameFactory {

	private static final ParsingTableDescriptorFactoryNameFactory INSTANCE
	= new ParsingTableDescriptorFactoryNameFactory();
	
	private ParsingTableDescriptorFactoryNameFactory() {
		super(GeneratorProperties.getProperty(CodeGenerationPropertyConstants
				.PARSING_TABLE_FACTORY_CLASS_NAME_PROPERTY));
	}

	public String className() {
		return baseName();
	}
	
	public static ParsingTableDescriptorFactoryNameFactory getInstance() {
		return INSTANCE;
	}
	
}
