package origin.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import origin.LuyouOrigin;
import parse.handler.TweetBodyHandler;

import com.google.gson.Gson;

import database.MySqlDataBase;

public class OR_Luyou {
	
	private BufferedReader br;
	String line=null;
	
	public OR_Luyou(File file){
		try {
			br = new BufferedReader(new FileReader(file));
			line = br.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public LuyouOrigin getNext(){
		if(line == null){
			return null;
		}
		LuyouOrigin luyou = null;
		try {
			String json = "";
			json = line.substring(line.indexOf(":")+1, line.length());
			luyou = transfromString(json);
			line = br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			return new LuyouOrigin();
		}
		return luyou;
	}
	
	public void close(){
		if(br!=null){
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	static Gson gson = new Gson();
	private static  LuyouOrigin transfromString(String json){
		try{
			LuyouOrigin luyou = gson.fromJson(json, LuyouOrigin.class);
			return luyou;
		}catch(Exception e){
			System.out.println("error: "+json);
			return new LuyouOrigin();
		}
		
	}

	public static void main(String[] args){
		String str="{\"a\":\"1771201181\",\"b\":\"xkjCWrcH3\",\"c\":\"@思文迪 我在这里：#小榄人家酒楼#过来啦！！ http://t.cn/aYIGCP  \",\"d\":\"http://wp2.sina.cn/wap240/6992629djw1dkbtujhvjxj.jpg\",\"e\":1313809027,\"f\":\"iPhone客户端\",\"g\":3,\"h\":0}";
		LuyouOrigin luyou = transfromString(str);
//		System.out.println(luyou);
		
		File file = new File("/Users/omar/data/sinaweibo/lvyou.result.2");
		OR_Luyou or = new OR_Luyou(file);
		luyou = or.getNext();
		int i = 1;
		TweetBodyHandler th = new TweetBodyHandler();
		while(luyou!=null){
//			System.out.println(luyou.toString());
			if(luyou.getA()!=null){
				th.handle(luyou);
			}
			luyou = or.getNext();
			i++;
//			if (i > 10)
//				break;
		}
		MySqlDataBase.release();
		System.out.println(i);
	}
}
