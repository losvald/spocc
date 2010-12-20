package hr.fer.spocc.parser.gen;


import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class DefaultParsingTableDescriptorFactoryTest {

	HardcodedCParsingTableDescriptorFactory builder;
	
	@Before
	public void init() {
		builder = new HardcodedCParsingTableDescriptorFactory();
	}
	
	@Test
	public void test1() throws IOException {
		builder.createDescriptor();
	}
}
