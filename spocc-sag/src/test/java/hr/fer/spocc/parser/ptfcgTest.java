package hr.fer.spocc.parser;

import hr.fer.spocc.parser.gen.ParsingTable151GenTest;
import hr.fer.spocc.parser.gen.ParsingTableDescriptor;
import hr.fer.spocc.parser.gen.ParsingTableFactoryCodeGenerator;

import java.io.IOException;

import org.junit.Ignore;

import com.sun.codemodel.JClassAlreadyExistsException;


@Ignore
public class ptfcgTest {

	public static void main(String[] args){

		ParsingTableDescriptor desc = new ParsingTable151GenTest().createDescriptorManually();
		ParsingTableFactoryCodeGenerator generator = new ParsingTableFactoryCodeGenerator(desc);
		try {
			generator.generateSourceFile();
		} catch (JClassAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("gotovo1");

	}

}
