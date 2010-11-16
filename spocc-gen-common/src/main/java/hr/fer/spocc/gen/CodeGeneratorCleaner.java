/*
 * CodeGeneratorCleaner.java
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
package hr.fer.spocc.gen;

import java.io.File;
import java.util.Arrays;

/*
 * @author Leo Osvald
 *
 */
public class CodeGeneratorCleaner {

	public static void cleanup(String directoryPath,
			String... packageWhitelist) {
		File genDir = new File(directoryPath);
		
		// precaution measures
		if (!genDir.getAbsolutePath().endsWith("src/generated/java"))
			return ;
		
		convertDotsToSlashes(packageWhitelist); 
		
		System.out.println("Deleting following directories inside "
				+ directoryPath + ": ");
		System.out.println(Arrays.toString(packageWhitelist));
		
		File[] toDelete = genDir.listFiles();
		
		for (File f : toDelete) {
			if (f.getName().equals("hr")) {
				deleteRecursive(f, packageWhitelist);
				return ;
			}
		}
	}
	
	private static void deleteRecursive(File f, String... suffixWhitelist) {
		if (f.isDirectory()) {
			File[] children = f.listFiles();
			for (File c : children)
				deleteRecursive(c, suffixWhitelist);
		} else {
			if (isMatch(f.getParentFile(), suffixWhitelist)) {
				System.out.println("Deleting: "+f);
				f.delete();
			}
		}
	}
	
	private static boolean isMatch(File dir, String... suffixes) {
		for (String suffix : suffixes) {
			if (dir.getAbsolutePath().endsWith(suffix))
				return true;
		}
		return false;
	}
	
	private static void convertDotsToSlashes(String[] packageNames) {
		for (int i = 0; i < packageNames.length; ++i) {
			packageNames[i] = packageNames[i].replaceAll("\\.", "/");
		}
	}
}
