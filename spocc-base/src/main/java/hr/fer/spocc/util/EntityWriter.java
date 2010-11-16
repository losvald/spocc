/*
 * EntityWriter.java
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
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Pisac entiteta.
 * 
 * @author Leo Osvald
 *
 */
public interface EntityWriter<T> {
	
	/**
	 * Ispisuje entitet u zadani output stream.
	 * Izlazni stream se pritom ne zatvara.
	 * Ova metoda razlicita je od metode
	 * {@link #print(Object, PrintStream)} po nacinu flushanja.
	 * 
	 * @param entity entitet koji se zeli ispisati
	 * @param outputStream output stream
	 * @throws IOException e ako je doslo do greske tijekom pisanja;
	 */
	void write(T entity, OutputStream outputStream) throws IOException;
	
	/**
	 * Ispisuje entitet u zadani print stream.
	 * Izlazni stream se pritom ne zatvara.
	 * 
	 * @param entity entitet koji se zeli ispisati
	 * @param printStream print stream
	 * @return <code>true</code> ako je ispisavanje uspjelo,
	 * <code>false</code> inace.
	 */
	boolean print(T entity, PrintStream printStream);
	
	/**
	 * Ispisije entitet u zadanu datoteku.
	 * 
	 * @param entity entitet koji se zeli ispisati
	 * @param file datoteka
	 * @throws IOException ako je doslo do greske prilikom pisanja
	 */
	void print(T entity, File file) throws IOException;
	
	/**
	 * Ispisuje entitet na standardni izlaz.
	 * 
	 * @param entity entitet koji se zeli ispisati
	 */
	void print(T entity);
}
