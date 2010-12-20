/*
 * TestUtils.java
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
package misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Ignore;
import org.sglj.util.Pair;

/**
 * 
 * @author Leo Osvald
 *
 */
@Ignore
public class TestUtils {
	
	public static final Set<Character> LOWERCASE_LETTER_CHARSET;
	public static final Set<Character> DIGIT_CHARSET;
	
	static {
		LOWERCASE_LETTER_CHARSET = new HashSet<Character>(26);
		for(char c = 'a'; c <= 'z'; ++c) {
			LOWERCASE_LETTER_CHARSET.add(c);
		}
		
		DIGIT_CHARSET = new HashSet<Character>(10);
		for(char c = '0'; c <= '9'; ++c) {
			DIGIT_CHARSET.add(c);
		}
		
	}
	
	public static String randomString(Set<Character> availableChars, 
			int maxLength, int minLength) {
		char[] chars = toCharArray(availableChars);
		Random random = new Random();
		int len = minLength + random.nextInt(maxLength-minLength+1);
		return RandomStringUtils.random(len, chars);
	}
	
	public static String randomString(Set<Character> availableChars, 
			int maxLength) {
		return randomString(availableChars, maxLength, 0);
	}
	
	private static char[] toCharArray(Set<Character> set) {
		char[] ret = new char[set.size()];
		int ind = 0;
		for(Character c : set) {
			ret[ind++] = c;
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T cloneSerializable(T obj) {
		ObjectOutputStream out = null;
		ObjectInputStream in = null;

		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bout);

			out.writeObject(obj);
			out.close();

			ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
			in = new ObjectInputStream(bin);			
			Object copy = in.readObject();

			in.close();

			return (T) copy;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(out != null) {
					out.close();
				}

				if(in != null) {
					in.close();
				}
			} catch (IOException ignore) {}
		}

		return null;
	}
	
	public static List< Pair<String, Object> > getEntityFields(Object o) {
		List< Pair<String, Object> > ret = new ArrayList<Pair<String,Object>>();
		for(Class<?> clazz = o.getClass(); clazz != Object.class; 
		clazz = clazz.getSuperclass()) {
			for(Field field : clazz.getDeclaredFields()) {
				//ignore some weird fields with unique values
				if(field.getName().contains("$"))
					continue;
				boolean accessible = field.isAccessible();
				try {
					field.setAccessible(true);
					Object val = field.get(o);
					ret.add(new Pair<String, Object>(field.getName(), val));
//					System.out.println(field.getName()+"="+val);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch(SecurityException e) {
					e.printStackTrace();
				} finally {
					field.setAccessible(accessible);
				}
			}
		}
		return ret;
	}
	
	public static final String fieldsToString(Object o) {
		ToStringBuilder tsb = new ToStringBuilder(o);
		List< Pair<String, Object> > fields = getEntityFields(o);
		for(Pair<String, Object> p : fields) {
			tsb.append(p.first(), p.second());
		}
		return removeHashCodes(tsb.toString());
	}
	
	public static void delay(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void assertEqualFields(Object expected, Object actual) {
		if(expected == actual)
			return ;
		if(expected == null ^ actual == null)
			Assert.fail();
		Assert.assertEquals(fieldsToString(expected),
				fieldsToString(actual));
	}
	
	public static void assertToStringEquals(Object a, Object b) {
		if (a == null ^ b == null)
			Assert.fail();
		if (a == null && b == null)
			return ;
		Assert.assertEquals(a.toString(), b.toString());
	}
	
	private static final String removeHashCodes(String toStringValue) {
		StringBuffer sb = new StringBuffer(toStringValue);
		for(int monkeyInd = 0; monkeyInd < sb.length(); ) {
			int ind = sb.indexOf("@", monkeyInd+1);
			if(ind == -1)
				break;
			int ind2 = sb.indexOf("[", ind+1);
			sb.delete(ind, ind2);
		}
		return sb.toString();
	}

}
