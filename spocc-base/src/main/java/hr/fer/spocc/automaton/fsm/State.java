/*
 * State.java
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
package hr.fer.spocc.automaton.fsm;

/**
 * Stanje konacnog automata. Dva stanja su razlicita ako su
 * im identifikatori razliciti.<br>
 * Ova klasa je immutable.
 * 
 * @author Leo Osvald
 *
 */
public final class State {

    private final int id;
    private boolean accept;
    private final String name;
    
    public State(int id, boolean accept, String name) {
    	this.id = id;
    	this.accept = accept;
    	this.name = name;
	}
    
    public State(int id, boolean accept) {
    	this(id, accept, "q"+id);
	}
    
	public int getId() {
		return id;
	}
	
	public boolean isAccept() {
		return accept;
	}
	
	public void setAccept(boolean accept) {
		this.accept = accept;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "State [id=" + id + ", accept=" + accept + ", name=" + name
				+ "]";
	}
	
	
	
}
