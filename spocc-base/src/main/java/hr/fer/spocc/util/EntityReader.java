/*
 * EntityReader.java
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
import java.io.InputStream;

/**
 * Citac entiteta.
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public interface EntityReader<T> {
	
	/**
	 * Cita iz zadanog streama i gradi entitet na temelju procitanog.
	 * Ulazni stream se ne zatvara.
	 * 
	 * @param inputStream stream iz kojeg se cita
	 * @return
	 * @throws IOException ako se dogodila greska tijekom citanja
	 */
	T read(InputStream inputStream) throws IOException;

	/**
	 * Cita iz zadane datoteke i gradi entitet na temelju procitanog.
	 * 
	 * @param inputFile
	 * @return
	 * @throws IOException
	 */
	T read(File inputFile) throws IOException;
	
	/**
	 * Cita sa standardnog izlaza i gradi entite na temelju procitanog.
	 * 
	 * @return entitet
	 * @throws IOException ako se dogodila greska tijekom citanja
	 */
	T read() throws IOException;
}
