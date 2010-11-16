/*
 * DefaultFileContentGrabber.java
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * Defaultna implementacija koja pomocu {@link BufferedReader}-a.
 * 
 * @author Leo Osvald
 *
 */
public class DefaultFileContentGrabber extends AbstractFileContentGrabber {

	private Reader reader;
	
	@Override
	protected Character readNext() throws IOException {
		int r = reader.read();
		++to;
		if (r == -1)
			return null;
		return (char) r;
	}

	@Override
	protected void rewind(int index) {
		throw new UnsupportedOperationException();
//		try {
//			reader.reset();
//			reader.skip(index);
//		} catch (IOException e) {
//			throw new IllegalStateException(e);
//		}
	}
	
	@Override
	protected void openFile(File file) throws IOException {
		reader = new BufferedReader(
			    new InputStreamReader(
			        new FileInputStream(file),
			        Charset.forName("UTF-8")));
	}

	@Override
	protected void closeFile(File file) throws IOException {
		reader.close();
	}

}
