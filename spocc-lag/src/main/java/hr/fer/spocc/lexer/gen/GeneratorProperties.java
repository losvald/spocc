/*
 * GeneratorProperties.java
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
package hr.fer.spocc.lexer.gen;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

/*
 * @author Leo Osvald
 *
 */
public class GeneratorProperties {
    
	private static volatile Properties properties;

    public static Properties getProperties() {
        if (properties == null) {
            try {
                properties = PropertiesLoaderUtils
                .loadAllProperties("generator.properties");
            } catch (IOException e) {
                System.err.println("Could not load generator.properties: "+ e);
                properties = new Properties();
            }
        }

        return properties;
    }

    public static void setProperties(Properties properties) {
        GeneratorProperties.properties = properties;
    }

    public static String getProperty(String key) {
        return getProperties().getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
    	return getProperties().getProperty(key, defaultValue);
    }
    
    public static void setProperty(String key, Object value) {
    	getProperties().setProperty(key, value.toString());
    }
}
