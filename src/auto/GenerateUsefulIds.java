package auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class GenerateUsefulIds {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GenerateUsefulIds guf  = new GenerateUsefulIds();
		guf.go();
	}
	static String usersId = "/Users/omar/project/locationEngine/stat/usersId-sorted.txt";
	static String friendshipFile = "/Users/omar/data/sinaweibo/friendship_results.full";
	static String resultFile = "/Users/omar/project/locationEngine/stat/usersId-useful.txt";
	
	HashSet<String> usersSet = new HashSet<String>();
	
	public GenerateUsefulIds(){
		try {
			readUserIdFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readUserIdFile() throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(new File(usersId)));
		String line = null;
		while((line =  br.readLine()) !=null){
			usersSet.add(line);
		}
		br.close();
		System.out.println("read locations file over");
	}
	
	public void go(){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(friendshipFile)));
			String line = "";
			FileWriter fw = new FileWriter(new File(resultFile));
			int i = 0 ;
			while((line = br.readLine())!=null){
				String[] tmp=line.split("( |\t)");
				String uid = tmp[0];
				if(usersSet.contains(uid)){
					if(i%10000==0){
						System.out.println(i+" parsed");
					}
					fw.write(line);
					fw.write("\n");
					i ++;
				}
			}
			fw.flush();
			fw.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}