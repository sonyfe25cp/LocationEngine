package origin.reader;

import java.io.File;

import origin.PeopleOrigin;

public class PeopleReader extends OriginReaderFramework{
	
	

	public PeopleReader(File file) {
		super(file);
	}

	public void operateLine(String line) {
		
		PeopleOrigin po = new PeopleOrigin();
		po.fromLine(line);
		
	}
	
	
	
	public static void main(String[] args){
		String filePath =  "/home/coder/Desktop/Link to share/zcz/user_profile.full";
		File file = new File(filePath);
		PeopleReader pr = new PeopleReader(file);
		pr.run();
	}

	@Override
	public void close() {
		
	}

}
