package auto;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Writer implements Runnable{
	
	private List<String> list;
	FileWriter fw;
	public Writer(String output,List<String> list){
		this.list=list;
		try {
			fw = new FileWriter(output,true);
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
