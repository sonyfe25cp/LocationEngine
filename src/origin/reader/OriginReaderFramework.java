package origin.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import origin.NextRowException;

/**
 * @author ChenJie
 *
 */
public abstract class OriginReaderFramework {
	private BufferedReader br;
	String line=null;
	
	public OriginReaderFramework(File file){
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public abstract void operateLine(String line) throws NextRowException;
	public abstract void close();
	public void run(){
		try {
			while((line = br.readLine())!=null){
				try{
					operateLine(line);
				}catch(NextRowException e){
					System.out.println("error! :"+line);
					continue;
				}
			}
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeOperation(){
		close();
		if(br!=null){
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
