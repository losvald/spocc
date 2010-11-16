/*
 * AbstractEntityReader.java
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Apstraktna implementacija sucelja {@link EntityReader}
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public abstract class AbstractEntityReader<T> implements EntityReader<T> {

	protected AbstractEntityReader() {
	}
	
	@Override
	public T read(InputStream inputStream) throws IOException {
		return read(new Scanner(inputStream));
	}

	@Override
	public T read(File inputFile) throws IOException {
		Scanner sc = new Scanner(new BufferedInputStream(
				new FileInputStream(inputFile)));
		try {
			T entity = read(sc);
			if (entity == null)
				throw new IOException();
			return entity;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			sc.close();
			if (sc.ioException() != null)
				throw sc.ioException();
		}
	}

	@Override
	public T read() throws IOException {
		return read(System.in);
	}
	
	/**
	 * Cita iz zadanog scannera
	 * 
	 * @param sc
	 * @return
	 */
	protected abstract T read(Scanner sc);

}
