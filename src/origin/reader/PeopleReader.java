package origin.reader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import origin.PeopleOrigin;

public class PeopleReader extends OriginReaderFramework{
	
	

	public PeopleReader(File file) {
		super(file);
	}
	FileWriter fw = null;
	String htmlHead = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body>";
	@Override
	public void init() {
		try {
			fw= new FileWriter(new File("/tmp/beijing.html"));
			fw.write(htmlHead);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void operateLine(String line) {
		
		PeopleOrigin po = new PeopleOrigin();
		po.fromLine(line);
		if(po.getTags().contains("女")&&po.getTags().contains("北京")&&po.getImg()!=null && po.getImg().length()>1){
			try {
				String html = showInHtml(po);
				System.out.println(html);
				fw.write(html);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String showInHtml(PeopleOrigin po){
		String img = po.getImg().replaceAll("/50/", "/180/");
		String block = "<a href='http://weibo.com/"+po.getUid()+"' target='blank'><img src='"+img+"' /></a>";
		block = block+" &nbsp;&nbsp;&nbsp; "+ po.getTags() ;
		block = block +"<br/>";
		return block;
	}
	
	public static void main(String[] args){
		String filePath =  "/Users/omar/data/sinaweibo/userinfo/user_profile.full";
		File file = new File(filePath);
		PeopleReader pr = new PeopleReader(file);
		pr.run(20000);
	}

	@Override
	public void close() {
		try {
			fw.write("</body></html>");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
