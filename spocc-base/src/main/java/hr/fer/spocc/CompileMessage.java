/*
 * CompileMessage.java
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
package hr.fer.spocc;

/**
 * @author Leo Osvald
 *
 */
public class CompileMessage {
	private final int line;
	private final String message;
	private final Level level;
	
	public CompileMessage(int line, String message, Level level) {
		this.line = line;
		this.message = message;
		this.level = level;
	}
	
	public int getLine() {
		return line;
	}
	
	public String getMessage() {
		return message;
	}

	public Level getLevel() {
		return level;
	}
	
	@Override
	public String toString() {
		return line + " : " + level + " : " + message;
	}
	
	public enum Level {
		INFO, WARNING, ERROR;
		
		public boolean isWarning() {
			return this == WARNING;
		}
		
		public boolean isError() {
			return this == ERROR;
		}
		
		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}
}
