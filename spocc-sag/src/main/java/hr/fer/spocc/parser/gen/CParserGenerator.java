package hr.fer.spocc.parser.gen;

import hr.fer.spocc.gen.TokenTypeDescriptor;

public class CParserGenerator extends DefaultParserGenerator {
	
	private static final TokenTypeDescriptor TOKEN_TYPE_DESCRIPTOR
	= new TokenTypeDescriptor();
	
	static {
		TOKEN_TYPE_DESCRIPTOR.addTokenTypes("IDN", "BROJ", "ZNAK", "NIZ_ZNAKOVA", "KR_BREAK", "KR_CHAR", "KR_CONST", "KR_CONTINUE", "KR_ELSE", "KR_FLOAT", "KR_FOR", "KR_IF", "KR_INT", "KR_RETURN", "KR_STRUCT", "KR_VOID", "KR_WHILE", "PLUS", "OP_INC", "MINUS", "OP_DEC", "ASTERISK", "OP_DIJELI", "OP_MOD", "OP_PRIDRUZI", "OP_LT", "OP_LTE", "OP_GT", "OP_GTE", "OP_EQ", "OP_NEQ", "OP_NEG", "OP_TILDA", "OP_I", "OP_ILI", "AMPERSAND", "OP_BIN_ILI", "OP_BIN_XILI", "ZAREZ", "TOCKAZAREZ", "TOCKA", "L_ZAGRADA", "D_ZAGRADA", "L_UGL_ZAGRADA", "D_UGL_ZAGRADA", "L_VIT_ZAGRADA", "D_VIT_ZAGRADA");
	}
	
	@Override
	protected ParsingTableDescriptorFactory createParsingTableDescriptorFactory() {
		return new HardcodedCParsingTableDescriptorFactory();
	}
	
	@Override
	protected TokenTypeDescriptor tokenTypeDescriptor() {
		return TOKEN_TYPE_DESCRIPTOR;
	}
	
}
