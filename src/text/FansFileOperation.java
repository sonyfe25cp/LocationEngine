package text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import utils.DLDEConfiguration;

/**
 * @author ChenJie
 * 
 * 将 id fid
 *    id2 fid
 * 这种格式的中间文件改成标准型
 * id fid1,fid2,fid3
 * id2 fid2,fid3
 */
public class FansFileOperation {

	String inputFile = DLDEConfiguration.getInstance("config.properties").getValue("fansFile");
	String outputFile = DLDEConfiguration.getInstance("config.properties").getValue("fansResult");
	
	public void operate(){
		File input = new File(inputFile);
		File output = new File(outputFile);
		try {
			BufferedReader br = new BufferedReader(new FileReader(input));
//			BufferedWriter bw = new BufferedWriter(new FileWriter(output));
			FileWriter bw = new FileWriter(output);
			String line = br.readLine();//每一行的内容
			String tmp = line.split(" ")[0];//用户标注
			StringBuilder sb = new StringBuilder();
			String res = "";//用于处理最后一行
			while(line != null){
				String[] array = line.split(" ");
				if(array.length == 2){ //有fans
					String id = array[0];
					if(tmp.equals(id)){
						
					}else{
						res = merge(tmp,sb);
//						System.out.println(res);
						bw.write(res);
						bw.write("\n");
						tmp = id;
						sb = new StringBuilder();//置空
					}
						sb.append(array[1]);
						sb.append(",");
						
				}
				line = br.readLine();
			}
			res = merge(tmp,sb);
//			System.out.println(res);
			bw.write(res);
			bw.write("\n");
			bw.flush();
			bw.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String merge(String id,StringBuilder sb){
		sb.deleteCharAt(sb.length()-1);
		String res = id + " " + sb.toString();
		return res;
	}
	
	public static void main(String[] args){
		FansFileOperation ffo = new FansFileOperation();
		ffo.operate();
	}
}
