/*
 * DefaultFileContentGrabberTest.java
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
package hr.fer.spocc.util;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/*
 * @author Leo Osvald
 *
 */
public class DefaultFileContentGrabberTest {

	private static final String INPUT_FILE_DIR = "src/test/resources";

	private FileContentGrabber fcg = new DefaultFileContentGrabber();
	private FileContentGrabber fcg2 = new DefaultFileContentGrabber();

	@Before
	public void init() throws IOException {
		fcg.open(getFile("filecontent-01.txt"));
//		fcg2.open(getFile("filecontent-02.txt"));
	}

	@After
	public void cleanup() throws IOException {
		fcg.close();
//		fcg2.close();
	}
	
	@Test
	public void test0() {
		Assert.assertEquals((Character)'5', fcg.getCharAt(5));	
		Assert.assertEquals((Character)'6', fcg.getCharAt(6));	
		Assert.assertEquals((Character)'1', fcg.getCharAt(1));	
		Assert.assertEquals((Character)'4', fcg.getCharAt(4));	
		Assert.assertEquals((Character)'3', fcg.getCharAt(3));	
		Assert.assertEquals((Character)'5', fcg.getCharAt(5));	
	}
	
	@Test
	public void test1() {
		Assert.assertEquals(null, fcg.getCharAt(10));	
	}
	
	@Test
	public void test2() {
		for (int i = 0; i < 9; ++i ) {
			System.out.print(fcg.getCharAt(i));
			Assert.assertEquals((Character)(char)('0' + i), fcg.getCharAt(i));
		}
	}
	
	@Test
	public void test3() {
		Assert.assertEquals((Character)(char)('0' + 2), fcg.getCharAt(2));
		Assert.assertEquals((Character)(char)('0' + 5), fcg.getCharAt(5));
		Assert.assertEquals((Character)(char)('0' + 3), fcg.getCharAt(3));
		Assert.assertEquals((Character)(char)('0' + 6), fcg.getCharAt(6));
	}
	
	@Test
	@Ignore("Krivi test ili bug")
	public void test4() {
		System.err.println(fcg2.getCharAt(0));
		System.err.println(fcg2.getCharAt(1));
		System.err.println(fcg2.getCharAt(2));
		//Assert.assertEquals((Character)(char)('0' + 2), fcg2.getCharAt(0));
	}



	static File getFile(String shortName) {
		return new File(INPUT_FILE_DIR+"/"+shortName);
	}
}
