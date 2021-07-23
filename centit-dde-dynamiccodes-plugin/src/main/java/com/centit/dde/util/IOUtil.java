package com.centit.dde.util;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class IOUtil {
	public static final String UTF8 = "utf-8";
	public static final String GBK = "gbk";
	public static final String TABLE = "\t";
	public static final String LINE = "\n";
	public static final byte[] TABBYTE = TABLE.getBytes();
	public static final byte[] LINEBYTE = LINE.getBytes();

	public static InputStream getInputStream(String path) {
		try {
			return new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedReader getReader(String path, String charEncoding) throws FileNotFoundException, UnsupportedEncodingException {
		return getReader(new File(path), charEncoding);
	}

	public static BufferedReader getReader(File file, String charEncoding) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		InputStream is = new FileInputStream(file);
		return new BufferedReader(new InputStreamReader(is, charEncoding));
	}

	public static RandomAccessFile getRandomAccessFile(String path, String charEncoding) throws FileNotFoundException {
		InputStream is = getInputStream(path);
		if (is != null) {
			return new RandomAccessFile(new File(path), "r");
		}
		return null;
	}

	public static void Writer(String path, String charEncoding, String content) {
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(path));
			fos.write(content.getBytes(charEncoding));
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(fos);
		}
	}

	public static BufferedReader getReader(InputStream inputStream, String charEncoding) throws UnsupportedEncodingException {
		return new BufferedReader(new InputStreamReader(inputStream, charEncoding));
	}

	public static String getContent(String path, String charEncoding) {
		return getContent(new File(path), charEncoding);
	}

	public static String getContent(InputStream is, String charEncoding) {
		BufferedReader reader = null;
		try {
			reader = IOUtil.getReader(is, charEncoding);
			return getContent(reader);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	public static String getContent(File file, String charEncoding) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			return getContent(is, charEncoding);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			close(is);
		}
		return "";
	}

	public static String getContent(BufferedReader reader) throws IOException {
		StringBuilder sb = new StringBuilder();
		try {
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				sb.append(temp);
				sb.append("\n");
			}
		} finally {
			close(reader);
		}
		return sb.toString();
	}

	/**
	 * 灏嗕竴涓璞″簭鍒楀寲鍒扮‖鐩樹腑
	 *
	 * @param string
	 * @param hm
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void WriterObj(String path, Serializable hm) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(new FileOutputStream(path));
			objectOutputStream.writeObject(hm);
		} finally {
			if (objectOutputStream != null) {
				objectOutputStream.close();
			}
		}
	}

	/**
	 * 鍏抽棴瀛楃娴�
	 *
	 * @param reader
	 */
	public static void close(Reader reader) {
		try {
			if (reader != null)
				reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 鍏抽棴瀛楄妭娴�
	 *
	 * @param is
	 */
	public static void close(InputStream is) {
		try {
			if (is != null)
				is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鍏抽棴瀛楄妭娴�
	 *
	 * @param is
	 */
	public static void close(OutputStream os) {
		try {
			if (os != null) {
				os.flush();
				os.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static FileIterator instanceFileIterator(String path, String charEncoding) {
		try {
			return instanceFileIterator(IOUtil.getInputStream(path), charEncoding);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static FileIterator instanceFileIterator(InputStream is, String charEncoding) {
		try {
			return new FileIterator(is, charEncoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static <K, V> HashMap<K, V> loadMap(String path, String charEncoding, Class<K> key, Class<V> value) throws UnsupportedEncodingException {

		FileIterator iteartor = null;
		HashMap<K, V> hm = null;
		try {
			iteartor = instanceFileIterator(path, charEncoding);
			hm = new HashMap<K, V>();
			String[] split = null;
			int index = 0;
			while (iteartor.hasNext()) {
				index++;
				String readLine = iteartor.next();
				split = readLine.split("\t");
				if (split.length < 2) {
					System.err.println(path + " line:" + index + " has err :" + readLine + " err to load !");
					continue;
				}
				hm.put((K) ObjConver.conversion(split[0], key), (V) ObjConver.conversion(split[1], value));
			}
		} finally {
			iteartor.close();
		}
		return hm;
	}

	public static <K, V> void writeMap(Map<K, V> hm, String path, String charEncoding) throws IOException {
		Iterator<Entry<K, V>> iterator = hm.entrySet().iterator();
		FileOutputStream fos = null;
		Entry<K, V> next = null;
		try {
			fos = new FileOutputStream(path);
			while (iterator.hasNext()) {
				next = iterator.next();
				fos.write(next.getKey().toString().getBytes(charEncoding));
				fos.write(TABBYTE);
				fos.write(next.getValue().toString().getBytes(charEncoding));
				fos.write(LINEBYTE);
			}
			fos.flush();
		} finally {
			fos.close();
		}
	}
	public static <T> void writeList(List<T> list, String path, String charEncoding) throws IOException {
		Iterator<T> iterator = list.iterator();
		FileOutputStream fos = null;
		T next = null;
		try {
			fos = new FileOutputStream(path);
			while (iterator.hasNext()) {
				next = iterator.next();
				fos.write(next.toString().getBytes(charEncoding));
				fos.write(LINEBYTE);
			}
			fos.flush();
		} finally {
			fos.close();
		}
	}

	public static List<String> readFile2List(String path, String charEncoding) throws UnsupportedEncodingException, FileNotFoundException {
		return readFile2List(getReader(path, charEncoding));
	}

	public static List<String> readFile2List(File file, String charEncoding) throws FileNotFoundException, UnsupportedEncodingException {
		return readFile2List(getReader(file, charEncoding));
	}

	public static List<String> readFile2List(InputStream inputStream, String charEncoding) throws UnsupportedEncodingException {
		return readFile2List(getReader(inputStream, charEncoding));
	}

	public static List<String> readFile2List(BufferedReader br) {
		List<String> all = new ArrayList<String>();
		String temp = null;
		try {
			while ((temp = br.readLine()) != null) {
				all.add(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return all;
	}

}
