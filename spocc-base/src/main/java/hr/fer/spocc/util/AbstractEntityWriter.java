/*
 * AbstractEntityWriter.java
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


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Apstraktna implementacija sucelja {@link EntityWriter}.
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public abstract class AbstractEntityWriter<T> implements EntityWriter<T> {

	protected AbstractEntityWriter() {
	}
	
	@Override
	public void print(T entity, File file) throws IOException {
		PrintStream ps = null;
		try {
			ps = new PrintStream(new BufferedOutputStream(
					new FileOutputStream(file)));
			print(entity, ps);
		} finally {
			if (ps != null) {
				ps.close();
				if (ps.checkError())
					throw new IOException();
			}
		}
	}

	@Override
	public void write(T entity, OutputStream outputStream)
	throws IOException {
		PrintStream ps = new PrintStream(outputStream);
		if (!print(entity, ps))
			throw new IOException();
		ps.flush();
	}

	@Override
	public void print(T entity) {
		print(entity, System.out);
	}

}
