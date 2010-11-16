package hr.fer.spocc.lexer.gen.naming;

import hr.fer.spocc.TokenType;
import hr.fer.spocc.lexer.Action;
import hr.fer.spocc.lexer.State;

import org.junit.Assert;
import org.junit.Test;

public class CodegenNamingTest {

	@Test
	public void testActionNaming() {
		Assert.assertEquals("_"+Action.class.getSimpleName()+(1),
				ActionNameFactory.getInstance().getClassName(1));
		Assert.assertEquals("_"+Action.class.getSimpleName()+(-2),
				ActionNameFactory.getInstance().getClassName(-2));
	}
	
	@Test
	public void testStateNaming() {
		Assert.assertEquals("_"+State.class.getSimpleName(),
				StateNameFactory.getInstance().getEnumName());
	}
	
	@Test
	public void testTokenTypeNaming() {
		Assert.assertEquals("_"+TokenType.class.getSimpleName(),
				TokenTypeNameFactory.getInstance().getEnumName());
	}
	
}
