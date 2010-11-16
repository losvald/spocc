/*
 * AbstractFileContentGrabber.java
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

import java.io.File;
import java.io.IOException;

import org.sglj.util.AmortizedRandomAccessQueue;
import org.sglj.util.QueueList;

/**
 * Apstraktna implementacija sucelja {@link FileContentGrabber}.
 * Pamti u bufferu sve znakove od indeksa koji je zadnje obiljezen
 * pozivom metode {@link #mark(int)}, pa do najveceg indeksa
 * koji je dotak zahvacen metodom {@link #getCharAt(int)}
 * 
 * @author Leo Osvald
 *
 */
public abstract class AbstractFileContentGrabber implements FileContentGrabber {

	private File inputFile;
	
	private int from;
	protected int to;
	
	private QueueList<Character> buffer;
	
	private int endTo;
	private boolean endReached;
	
	@Override
	public void open(File file) throws IOException {
		if (buffer != null)
			buffer.clear();
		
		buffer = new AmortizedRandomAccessQueue<Character>(0x100);
		from = to = 0;
		this.inputFile = file;
		openFile(file);
	}
	
	@Override
	public void close() throws IOException {
		buffer.clear();
		closeFile(getInputFile());
	}
	
	
	@Override
	public Character getCharAt(int index) throws IllegalStateException {
		// TODO Auto-generated method stub
		if (index < from)
			throw new IndexOutOfBoundsException();
		
		if (endReached && index >= endTo)
			return null;
		
		if (index >= to) {
			// ak je izvan buffera
			try {
				for (int i = to; i <= index; ++i) {
					Character c = readNext();
					if (c == null) {
						endReached = true;
						endTo = i; 
						return null;
					}

					buffer.add(c);
				}
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			
		}
		
		return buffer.get(index - from);
	}
	
	@Override
	public void mark(int index) {
		if (index < from) {
			rewind(index);
			buffer.clear();
			endReached = false;
		} else {
			for (int itr = index - from; itr > 0; --itr) {
				buffer.poll();
			}
		}
		from = index;
	}

	protected abstract void rewind(int index);
	
	protected abstract Character readNext() throws IOException;
	
	protected abstract void openFile(File file) throws IOException;
	
	protected abstract void closeFile(File file) throws IOException;

	protected File getInputFile() {
		return inputFile;
	}
	
}
