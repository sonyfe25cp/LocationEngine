package auto;

import java.io.File;

/**
 * @author ChenJie
 * 多线程版本没什么效果..全卡住了
 */
public class AutoRunThreadVersion {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dir = "/Users/omar/project/locationEngine/stat/test";
		File fileDir = new File(dir);
		BufferStore bs = BufferStore.getInstance();
		for(File tmp : fileDir.listFiles()){
//			System.out.println(tmp.getName());
			System.out.println(tmp.getAbsolutePath());
			AutoRunWithFileThread arwf = new AutoRunWithFileThread(tmp.getAbsolutePath(),bs);
			new Thread(arwf).start();
		}
	}

}