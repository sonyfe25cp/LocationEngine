package test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestThread {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> tmpList = new ArrayList<String>();
		tmpList.add("hah");
		tmpList.add("beijing");
		ArrayList<String> tmpList2 = (ArrayList<String>) tmpList.clone();
		new Thread(new Writer(tmpList2)).start();
		System.out.println("size: "+tmpList.size());
		tmpList.clear();
		System.out.println("size: "+tmpList.size());
	}

}
class Writer implements Runnable{
	static String resultFile = "/Users/omar/project/locationEngine/stat/results-with-friendship123.txt";
	private List<String> list;
	FileWriter fw;
	public Writer(List<String> list){
		this.list=list;
		try {
			fw = new FileWriter(resultFile,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		for(String tmp:list){
			try {
				fw.write(tmp);
				fw.write("\n");
				fw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}