package index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import origin.FriendShipOrigin;
import origin.NextRowException;
import origin.reader.OriginReaderFramework;
import utils.DLDEConfiguration;

public class IndexFriendShip extends OriginReaderFramework{
	
	static String friendshipFile=DLDEConfiguration.getInstance("config.properties").getValue("friendshipResult");
	static String friendshipIndex=DLDEConfiguration.getInstance("config.properties").getValue("friendshipIndex");
	
	public IndexFriendShip(File file) {
		super(file);
		init();
	}
	
	public IndexFriendShip() {
		this(new File(friendshipFile));
	}

	IndexWriter iwriter;
	Directory dir;
	public void init(){
		File path = new File(friendshipIndex);
		try {
			dir = FSDirectory.open(path);
			IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_40 , null);
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
			e.printStackTrace();
		}
	}
	

}
