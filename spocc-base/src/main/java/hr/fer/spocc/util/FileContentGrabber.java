/*
 * FileContentGrabber.java
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

/**
 * Sucelje koje pruza apstrakciju izmedju dohvacanja znakova iz file-a.
 * 
 * @author Leo Osvald
 *
 */
public interface FileContentGrabber {
	
	void open(File file) throws IOException;
	
	void close() throws IOException;
	
	/**
	 * Vraca znak na odgovarajućem mjestu u fileu. 
	 * 
	 * @param index redni broj znaka koji se dohvaća
	 * @return
	 */
	Character getCharAt(int index);
	
	void mark(int index);
	
}
