/*
 * FenwickTreeTest.java
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
package hr.fer.spocc.grammar.cfg;

import hr.fer.spocc.grammar.cfg.CfgGrammar.FenwickTree;
import junit.framework.Assert;

import org.junit.Test;
import org.sglj.util.ArrayToStringUtils;

/*
 * @author Leo Osvald
 *
 */
public class FenwickTreeTest {

	@Test
	public void test1() {
		FenwickTree ft = new FenwickTree(5);
		assertEquals(ft, 0, 0, 0, 0, 0);
		ft.set(2, true);
		assertEquals(ft, 0, 0, 1, 0, 0);
		ft.set(2, false);
		assertEquals(ft, 0, 0, 0, 0, 0);
		
		ft.set(2, false);
		assertEquals(ft, 0, 0, 0, 0, 0);
		ft.set(2, true);
		assertEquals(ft, 0, 0, 1, 0, 0);
	}
	
	@Test
	public void test2() {
		FenwickTree ft = new FenwickTree(5);
		ft.set(1, true);
		assertEquals(ft, 0, 1, 0, 0, 0);
		ft.set(4, true);
		assertEquals(ft, 0, 1, 0, 0, 1);
		ft.set(2, true);
		assertEquals(ft, 0, 1, 1, 0, 1);
		Assert.assertTrue(ft.isAllSet(1, 3));
	}
	
	@Test
	public void test3() {
		FenwickTree ft = new FenwickTree(16);
		assertEquals(ft, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		ft.set(0, true);
		assertEquals(ft, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		ft.set(6, true);
		assertEquals(ft, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		ft.set(7, true);
		assertEquals(ft, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0);
		ft.set(11, true);
		assertEquals(ft, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0);
		ft.set(12, true);
		assertEquals(ft, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0);
		ft.set(4, true);
		assertEquals(ft, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0);
		
		Assert.assertTrue(ft.isAllUnset(1, 4));
		Assert.assertTrue(ft.isAllUnset(5, 6));
		Assert.assertTrue(ft.isAllUnset(8, 10));
		Assert.assertTrue(ft.isAllUnset(9, 11));
		Assert.assertTrue(ft.isAllUnset(8, 11));
		Assert.assertTrue(ft.isAllSet(6, 8));
		
		Assert.assertFalse(ft.isAllSet(4, 8));
		
		ft.set(5, true);
		assertEquals(ft, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0);
		
		Assert.assertTrue(ft.isAllSet(4, 8));
	}
	
	static void assertEquals(FenwickTree ft, Integer... arr) {
		Assert.assertEquals(arr.length, ft.size());
		System.out.println("Assert: "+ArrayToStringUtils.toString(arr));
		for (int i = 0; i < arr.length; ++i) {
			System.out.println(i);
			Assert.assertEquals(arr[i] != 0 ? true : false, ft.isSet(i));
		}
	}
}
