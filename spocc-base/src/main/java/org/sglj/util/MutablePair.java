/*
 * MutablePair.java
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
package org.sglj.util;

/**
 * An extension of the {@link Pair} utility class which allows its contents
 * to be mutable and provides two additional methods for setting them, 
 * {@link #setFirst(Object)} and {@link #setSecond(Object)}.
 *  
 * @author Leo Osvald
 *
 * @param <T1>
 * @param <T2>
 */
public class MutablePair<T1, T2> extends Pair<T1, T2> {

	public MutablePair() {
	}
	
	public MutablePair(T1 first, T2 second) {
		super(first, second);
	}
	
	public void setFirst(T1 first) {
		super.setFirst(first);
	}
	
	public void setSecond(T2 second) {
		super.setSecond(second);
	}

}
