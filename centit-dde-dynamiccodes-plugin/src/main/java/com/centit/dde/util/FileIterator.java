package com.centit.dde.util;

import java.io.*;
import java.util.Iterator;

public class FileIterator implements Iterator<String>, Closeable {
	String temp = null;
	private BufferedReader br = null;

	protected FileIterator(String path, String charEncoding) throws UnsupportedEncodingException, FileNotFoundException {
		br = IOUtil.getReader(path, charEncoding);
	}

	protected FileIterator(InputStream is, String charEncoding) throws UnsupportedEncodingException, FileNotFoundException {
		br = IOUtil.getReader(is, charEncoding);
	}

	@Override
	public boolean hasNext() {
		if (temp == null) {
			try {
				temp = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (temp == null) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	public String readLine() {
		try {
			if (temp == null) {
				temp = br.readLine();
			}
			return temp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			temp = null;
		}
	}

	@Override
	public void close() {
		if (br != null)
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public String next() {
		return readLine();
	}

	@Override
	public void remove() {
		throw new RuntimeException("file iteartor can not remove ");
	}
}

