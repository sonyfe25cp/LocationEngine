package origin.reader;

import java.io.File;

import origin.NextRowException;
import refined.NewLuyou;

public class OR_STD_Luyou extends OriginReaderFramework{
	
	static String filePath = "/Users/omar/data/sinaweibo/std.lvyou.result.log2";
	static String indexPath = "/Users/omar/project/locationEngine/index-std";

	public OR_STD_Luyou(File file) {
		super(file);
	}
	
	public OR_STD_Luyou(){
		this(new File(filePath));
	}

	public static void main(String[] args) {
		OR_STD_Luyou orstd = new OR_STD_Luyou();
		orstd.run(10000);
	}
	int i = 0 ;
	@Override
	public void operateLine(String line) throws NextRowException {
//		System.out.println(line);
		String[] tmps = line.split("\t");
		if(tmps.length == 6){
			long uid = Long.parseLong(tmps[0]);
			String code = tmps[1];
			String tweet = tmps[2];
			String img = tmps[3];
			long time = Long.parseLong(tmps[4]);
			String device = tmps[5];
			NewLuyou nly = new NewLuyou(uid,tweet,img,null,device,time);
			if(nly.getLocation()==null || nly.getLocation().length()== 0){
				System.out.println(line);
//				System.out.println(nly.toString());
				i++;
			}else{
//				System.out.println(nly.getLocation());
			}
		}
	}

	@Override
	public void close() {
		System.out.println("error count: "+i);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
