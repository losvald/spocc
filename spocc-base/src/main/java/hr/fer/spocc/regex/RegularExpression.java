/*
 * RegularExpression.java
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
package hr.fer.spocc.regex;


/**
 * Regularni izraz.
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
public interface RegularExpression<T> {
	
	/**
	 * Provjerava da li je regularni izraz trivijalan tj. da li
	 * se sastoji od iskljucivo jednog znaka/simbola, bez operatora.
	 * 
	 * @return <code>true</code> ako je regularni izraz trivijalan,
	 * <code>false</code> inace.
	 */
	boolean isTrivial();
	
	/**
	 * Vraca trivijalnu vrijednost izraza tj. znak/simbol koji ga predstavlja.
	 * 
	 * @return znak/simbol abecede ili <code>null</code> ako regularni
	 * izraz nije trivijalan.
	 */
	T getTrivialValue();
	
	/**
	 * Vraca tip regularnog izraza.
	 * 
	 * @return tip regularnog izraza
	 */
	RegularExpressionType getType();
	
	/**
	 * Vraca broj elemenata od kojih se sastoji regularni izraz.
	 * 
	 * @return pozitivni cijeli broj
	 */
	int size();
	
	/**
	 * Vraca element regularnog izraza koji se nalazi na indeksu
	 * <tt>index</tt>.
	 * 
	 * @param index indeks
	 * @return element regularnog izraza
	 */
	RegularExpressionElement get(int index);
	
	/**
	 * Vraca operator koji razdvaja ovaj regularni izraz na 
	 * jedan ili vise podizraza.
	 * 
	 * @return operator ili <code>null</code> ako je ovaj izraz trivijalan
	 */
	RegularExpressionOperator getOperator();
	
	/**
	 * Vraca podizraz 
	 * @param n indeks podizraza (0-bazirani).
	 * @return
	 */
	RegularExpression<T> getSubexpression(int n);
	
//	/**
//	 * Vraca jedan ili vise podizraza na koje se ovaj izraz dijeli,
//	 * a koji su odvojeni operatorom.
//	 * 
//	 * @return operandi predstavljeni podizrazima ili <code>null</code>
//	 * ako je izraz trivijalan.
//	 */
//	RegularExpression<T>[] getSubexpressions();
	
//	/**
//	 * Razdvaja regularni izraz na jedan ili dva regularna izraza (sto
//	 * se sprema u prvi clan para), te ustanovljuje kojim operatorom
//	 * su podizrazi odvojeni.
//	 * 
//	 * @return par koji sadrzi podizraze i operator, a ako je regularni
//	 * izraz trivijalan ova metoda vraca <code>null</code>
//	 */
//	Pair<RegularExpression<T>[], RegularExpressionOperator> split();
}
