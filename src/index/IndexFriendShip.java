package index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import origin.FriendShipOrigin;
import origin.NextRowException;
import origin.reader.OriginReaderFramework;

public class IndexFriendShip extends OriginReaderFramework{
	
	
	
	public IndexFriendShip(File file) {
		super(file);
		init();
	}
	
	public IndexFriendShip() {
		this(new File(filePath));
	}

	IndexWriter iwriter;
	Directory dir;
	private static String filePath="/Users/omar/data/sinaweibo/user_att.full";
	private static String indexPath="/Users/omar/project/locationEngine/index-friendship";
	public void init(){
		File path = new File(indexPath);
		try {
			dir = FSDirectory.open(path);
			Analyzer analyzer = new IKAnalyzer();
			IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_40 , analyzer);
			iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			iwriter = new IndexWriter(dir , iwConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		IndexFriendShip ip =new IndexFriendShip();
		ip.run();
	}
	@Override
	public void operateLine(String line) throws NextRowException {
		try {
			FriendShipOrigin fso = new FriendShipOrigin();
			fso.fromLine(line);
			iwriter.addDocument(fso.toDocument());
		} catch (Exception e) {
			throw new NextRowException();
		}
	}

	@Override
	public void close() {
		try {
			iwriter.close();
			dir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
