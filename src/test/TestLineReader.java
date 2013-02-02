package test;

import java.io.IOException;

import utils.DLDELineReader;

public class TestLineReader extends DLDELineReader{
	static String friendshipFile = "/Users/omar/project/locationEngine/stat/usersId-useful.txt";
	public TestLineReader(String filePath) {
		super(filePath);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestLineReader tlr = new TestLineReader(friendshipFile);
		try {
			tlr.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	int i = 0;
	
	@Override
	public void opterate(String line) {
		if(i < 100){
			System.out.println(line);
			System.out.println("---");
		}else{
			System.exit(0);
		}
		i ++ ;
	}

}
