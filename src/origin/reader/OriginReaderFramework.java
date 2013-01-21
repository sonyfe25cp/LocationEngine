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
	public abstract void init();
	public void run(){
		try {
			int i = 0;
			init();
			while((line = br.readLine())!=null){
				try{
					if(i%1000 == 0){
						System.out.println(i + " lines over~");
					}
					operateLine(line);
					i ++;
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
	public void run(int num){
		try {
			int i = 0;
			init();
			while((line = br.readLine())!=null){
				if(i>num){
					break;
				}
				try{
					operateLine(line);
					i++;
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
