package auto;

import java.util.ArrayList;

public class BufferStore {

	static String resultFile = "/Users/omar/project/locationEngine/stat/results-with-friendship-multi.txt";
	private static ArrayList<String> store;
	private BufferStore(){
	}
	
	public static BufferStore getInstance(){
		if(store == null){
			store = new ArrayList<String>();
		}
		return new BufferStore();
	}
	
	public void add(String str){
		store.add(str);
		if(store.size() == 10000){
			clean();
		}
	}
	
	private synchronized void clean(){
		ArrayList<String> tmpList = (ArrayList<String>) store.clone();
		new Thread(new Writer(resultFile,tmpList)).start();
		store.clear();
	}
	public void close(){
		if(store.size()>0){
			clean();
		}
	}
}
