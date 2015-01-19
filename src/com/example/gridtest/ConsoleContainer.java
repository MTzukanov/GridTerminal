package com.example.gridtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class ConsoleContainer extends IndexedContainer {
	private final int SCREEN_WIDTH;
	private Item lineItem;
	int lineCursorPos = 0;
	
	private In in = new In();
	private Out out = new Out();
	
	public ConsoleContainer(int screenwidth) {
		SCREEN_WIDTH = screenwidth;
		
		for (int i = 0; i < SCREEN_WIDTH; i++)
			addContainerProperty(i, String.class, null);
		
		createNewLine();
	}

	private void createNewLine() {
		lineCursorPos = 0;
		lineItem = getItem(addItem());
	}
	
	class Out extends OutputStream {
		@Override
		synchronized public void write(byte[] b) throws IOException {
			super.write(b);
		}
		
		@Override
		synchronized public void write(byte[] b, int off, int len) throws IOException {
			super.write(b, off, len);
		}
		
		@Override
		public synchronized void write(int b) throws IOException {
			if (UI.getCurrent() != null)
				UI.getCurrent().access(() -> writeInternal(b));
//			else
//				writeInternal(b);
		}
		
		@SuppressWarnings("unchecked")
		private void writeInternal(int b) {
			if (b == '\n')
			{
				createNewLine();
				return;
			}
				
			if (lineCursorPos >= SCREEN_WIDTH)
				createNewLine();
		
			String value = "";
			if (b < 32)
				value = b + "";
			else
				value = ((char) b)+"";
			
			lineItem.getItemProperty(lineCursorPos++).setValue(value);
		}
	}

	public Out getOutputStream() {
		return out;
	}

	class In extends InputStream {
		int next = -1;
		
		@Override
		public int read(byte[] b, int off, int len) throws IOException {
	        int c = read();
	        if (c == -1) {
	            return -1;
	        }
	        b[0] = (byte)c;
			return 1;
		}
		
		@Override
		public synchronized int read() throws IOException {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return next;
		}
		
		public synchronized void input(int b) {
			next = b;
			notify();
		}		
	}
	
	public InputStream getInputStream() {
		return in;
	}
	
	public void input(int b) {
		in.input(b);
	}
}
