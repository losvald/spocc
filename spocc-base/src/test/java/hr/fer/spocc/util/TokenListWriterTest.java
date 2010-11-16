/*
 * TokenListWriterTest.java
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

import hr.fer.spocc.util.AbstractEntityReader;
import hr.fer.spocc.util.AbstractEntityWriter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * @author Leo Osvald
 *
 */
public class TokenListWriterTest {

	File tmpFile;
	
	static final MyClassWriter WRITER = new MyClassWriter();
	static final MyClassReader READER = new MyClassReader();
	
	@Before
	public void createTmpFile() throws IOException {
		tmpFile = File.createTempFile(
				RandomStringUtils.randomAlphabetic(3), null);
	}
	
	@After
	public void deleteTmpFile() {
		try {
			printTmpFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		tmpFile.delete();
	}
	
	@Test
	public void test() throws IOException {
		MyClass mc = new MyClass(1, "first");
		WRITER.print(mc, System.err);
		WRITER.print(mc);
		WRITER.print(mc, tmpFile);
		
		MyClass read = READER.read(tmpFile);
		
		Assert.assertEquals(mc, read);
	}
	
	public void printTmpFile() throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new BufferedInputStream(
						new FileInputStream(tmpFile))));
		System.out.println("===== "+tmpFile.getAbsolutePath()+" =====");
		for (String line; (line = br.readLine()) != null; ) {
			System.out.println(line);
		}
		br.close();
		System.out.println("==========");
	}
	
	static class MyClass {
		int id;
		String name;
		
		public MyClass() {
		}
		
		public MyClass(int id, String name) {
			this.id = id;
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof MyClass))
				return false;
			MyClass other = (MyClass) obj;
			if (id != other.id)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
		
	}
	
	static class MyClassWriter extends AbstractEntityWriter<MyClass> {

		@Override
		public boolean print(MyClass entity, PrintStream printStream) {
			printStream.println(entity.id+" "+entity.name);
			return true;
		}
	}
	
	static class MyClassReader extends AbstractEntityReader<MyClass> {

		@Override
		protected MyClass read(Scanner sc) {
			MyClass mc = new MyClass();
			mc.id = sc.nextInt();
			mc.name = sc.next();
			return mc;
		}
		
	}
}
