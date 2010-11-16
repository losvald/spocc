/*
 * SimplePropertiesLoader.java
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
import java.util.Properties;

/**
 * Jednostavni property loader koji loada properties fileove
 * koji se nalaze u src/main/resources direktoriju.
 * 
 * @author Leo Osvald
 *
 */
public class SimplePropertiesLoader {

	private static final String DEFAULT_PROPERTIES_DIRECTORY 
	= "src/main/resources";
	
	public static Properties loadProperties(String fileName) 
	throws IOException {
		Properties props = new Properties();
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(
					new File(DEFAULT_PROPERTIES_DIRECTORY + "/" + fileName)));
			props.load(bis);
			return props;
		} finally {
			if (bis != null)
				bis.close();
		}
	}
}
