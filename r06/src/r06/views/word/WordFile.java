package r06.views.word;

import java.io.*;
import java.util.*;

public class WordFile {

	private File file;
	private ArrayList list = new ArrayList();
	private Listener listener;
	
	public interface Listener {
		public void added(Word w);
		public void removed(Word w);
	}
	
	/**
	 * Constructor for FileList
	 */
	public WordFile(File file) {
		this.file = file;
		if (file.exists()) {
			readFile();
		} else {
			writeFile();
		}
	}
	
	public void setListener(Listener l) {
		listener = l;
	}
	
	public void add(Word word) {
		list.add(word);
		writeFile();
		if (listener != null)
			listener.added(word);
	}
	
	public void remove(Word word) {
		list.remove(word);
		writeFile();
		if (listener != null)
			listener.removed(word);
	}
	
	public Word find(String str) {
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Word word = (Word)iter.next();
			if (str.equals(word.toString()))
				return word;
		}
		return null;
	}
	
	public List elements() {
		return list;
	}
	
	private void writeFile() {
		try {
			OutputStream os = new FileOutputStream(file);
			DataOutputStream data = new DataOutputStream(os);
			data.writeInt(list.size());
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Word word = (Word)iter.next();
				data.writeUTF(word.toString());
			}
			data.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void readFile() {
		try {
			InputStream is = new FileInputStream(file);
			DataInputStream data = new DataInputStream(is);
			int size = data.readInt();
			for (int nX = 0; nX < size; nX ++) {
				String str = data.readUTF();
				list.add(new Word(str));
			}
			data.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

