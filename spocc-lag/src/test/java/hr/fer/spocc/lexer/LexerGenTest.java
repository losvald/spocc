/*
 * LexerGenTest.java
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

import hr.fer.spocc.lexer.gen.ActionDescriptor;
import hr.fer.spocc.lexer.gen.DefaultLexerGenerator;
import hr.fer.spocc.lexer.gen.LexerGeneratorEscaper;
import hr.fer.spocc.lexer.gen.LexicalRuleDescriptor;
import hr.fer.spocc.lexer.gen.SubactionDescriptor;
import hr.fer.spocc.regex.DefaultRegularExpression;
import hr.fer.spocc.regex.RegularExpression;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;

/**
 * @author Leo Osvald
 *
 */
@Ignore
public class LexerGenTest {

	private static final String TEST_INPUT_FILES_DIR = "src/test/resources";

	protected static String getRelativePath(String shortFileName) {
		return TEST_INPUT_FILES_DIR + "/" + shortFileName;
	}

	protected static File toFile(String shortFileName) {
		return new File(getRelativePath(shortFileName));
	}

	public static void main(String[] args) throws IOException {
		File lexRules = toFile("minusLang.def");
		DefaultLexerGenerator dlg = new DefaultLexerGenerator();
		dlg.readFromStream(new FileInputStream(lexRules));
		for (String elem: dlg.regularDefinitions.keySet()) {
			System.out.println(elem + " -> " + dlg.regularDefinitions.get(elem));
		}
		for (String elem: dlg.states.getStates()) {
			System.out.println(elem);
		}
		for (String elem: dlg.tokenTypes.getTypeTokens()) {
			System.out.println(elem);
		}
		RegularExpression<Character> regex = new DefaultRegularExpression("\\t|\\_", LexerGeneratorEscaper.getInstance());;
		List<SubactionDescriptor> subactions = new ArrayList<SubactionDescriptor>();
		subactions.add(new SubactionDescriptor(SubactionType.SKIP));
		ActionDescriptor action = new ActionDescriptor("S_pocetno", subactions);
		LexicalRuleDescriptor lrd = new LexicalRuleDescriptor(1, regex, action);
		Assert.assertEquals(dlg.lexicalRuleDescriptors.get(0), lrd);
	}
	
}
