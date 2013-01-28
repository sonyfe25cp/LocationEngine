package text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import utils.DLDEConfiguration;

/**
 * @author ChenJie
 * 合并focus文件和fans文件到一个文件
 */
public class MergeFansAndFocus {

	String focusInputFile = DLDEConfiguration.getInstance("config.properties").getValue("focusSortedFile");
	String fansInputFile = DLDEConfiguration.getInstance("config.properties").getValue("fansSortedFile");
	String outputFile = DLDEConfiguration.getInstance("config.properties").getValue("friendShipResult");

	
	public void operate(){
		File focusInput = new File(focusInputFile);
		File fansInput = new File(fansInputFile);
		File output = new File(outputFile);
		
		try {
			BufferedReader focusBr = new BufferedReader(new FileReader(focusInput));
			BufferedReader fansBr = new BufferedReader(new FileReader(fansInput));
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));
			
			String focus = focusBr.readLine();
			String fans = fansBr.readLine();
			String res;
			boolean fans_over = false;
			boolean focus_over = false;
			while(focus!=null){
				String tmp_focus[]=focus.split(" ");
				if(tmp_focus.length != 2){
					focus = focusBr.readLine();
					continue;
				}
				long uid = Long.parseLong(tmp_focus[0]);
				String focus_ids = tmp_focus[1];
				while(fans!=null){
					String tmp_fans[] = fans.split(" ");
					if(tmp_fans.length != 2){
						fans = fansBr.readLine();
						continue;
					}
					long uid_fans = Long.parseLong(tmp_fans[0]);
					String fans_ids = tmp_fans[1];
					if(focus_over){
						bw.write(fans);
						bw.newLine();
						fans = fansBr.readLine();
					}else{
						if(uid < uid_fans){
							res = focus;
							bw.write(res);
							bw.newLine();
							focus = focusBr.readLine();
							if(focus==null){
								focus_over = true;
							}else{
								break;
							}
						}else if(uid == uid_fans){
							res = uid+" "+ mergeString(focus_ids,fans_ids);
							bw.write(res);
							bw.newLine();
							fans = fansBr.readLine();
							if(fans==null){
								fans_over = true;
							}
							focus = focusBr.readLine();
							if(focus==null){
								focus_over = true;
							}else{
								break;
							}
						}else{
							res = fans;
							bw.write(res);
							bw.newLine();
							fans = fansBr.readLine();
							if(fans==null){
								fans_over = true;
							}
							continue;
						}
					}
				};
				if(fans_over){
					bw.write(focus);
					bw.newLine();
					focus = focusBr.readLine();
				}
			}
			bw.flush();
			bw.close();
			fansBr.close();
			focusBr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param focus : id,id2,id3
	 * @param fans : id,id2,id4
	 * @return
	 * Jan 28, 2013
	 */
	public String mergeString(String focus,String fans){
		String total = focus+","+fans;
		String[] tmps = total.split(",");
		HashSet<String> set = new HashSet<String>();
		for(String tmp:tmps){
			set.add(tmp);
		}
		StringBuilder sb = new StringBuilder();
		int size = set.size();
		int i = 0;
		for(String tmp:set){
			sb.append(tmp);
			i ++;
			if(i < size){
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	/**
	 * @param args
	 * Jan 28, 2013
	 */
	public static void main(String[] args) {
		MergeFansAndFocus mfaf = new MergeFansAndFocus();
		mfaf.operate();
	}

}
