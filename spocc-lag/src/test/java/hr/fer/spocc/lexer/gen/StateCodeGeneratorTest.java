package hr.fer.spocc.lexer.gen;

import java.io.IOException;

import hr.fer.spocc.lexer.gen.StateCodeGenerator;
import hr.fer.spocc.lexer.gen.StateDescriptor;

import org.junit.Ignore;

import com.sun.codemodel.JClassAlreadyExistsException;

@Ignore("Manualni test")
public class StateCodeGeneratorTest {

	public StateCodeGeneratorTest(String... typeTokenNames) throws JClassAlreadyExistsException, IOException {
		StateDescriptor stateDescriptor = new StateDescriptor();
		for (String name : typeTokenNames)
			stateDescriptor.addState(name);
		new StateCodeGenerator(stateDescriptor).generateSourceFile();
	}
	
	public static void main(String[] args) throws JClassAlreadyExistsException, IOException {
		new StateCodeGeneratorTest("PRVO_STANJE", "DRUGO");
	}
	
}
