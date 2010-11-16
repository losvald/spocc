/*
 * ActionDescriptor.java
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

import hr.fer.spocc.lexer.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;


/**
 * Opisnik akcije koju treba generirati.<br>
 * 
 * Generator na temelju ovog opisnika treba izgenerirati kod koji
 * stvara instancu klase {@link Action}.
 * 
 * @author Leo Osvald
 *
 */
public final class ActionDescriptor {
	private final String state;
	private final List<SubactionDescriptor> subactionDescriptors;
	
	/**
	 * Stvara opisnik na temelju parametara. Predana lista
	 * bit ce kopirana.
	 * 
	 * @param state ime stanje
	 * @param subactionDescriptors opisnici podakcija
	 */
	public ActionDescriptor(String state, 
			List<SubactionDescriptor> subactionDescriptors) {
		this.state = state;
		
		// XXX staviti
		Validate.isTrue(!subactionDescriptors.isEmpty());
		
		// copy list
		this.subactionDescriptors = Collections.unmodifiableList(
				new ArrayList<SubactionDescriptor>(
						subactionDescriptors));
	}

	public String getState() {
		return state;
	}

	public List<SubactionDescriptor> getSubactionDescriptors() {
		return subactionDescriptors;
	}

	@Override
	public String toString() {
		return "ActionDescriptor [state=" + state + ", subactionDescriptors="
				+ subactionDescriptors + "]";
	}
	
}
