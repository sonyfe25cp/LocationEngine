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

import origin.NextRowException;
import origin.PeopleOrigin;
import origin.reader.OriginReaderFramework;

public class IndexPeople extends OriginReaderFramework{
	public IndexPeople(File file) {
		super(file);
		init();
	}
	public IndexPeople() {
		this(new File(filePath));
	}

	IndexWriter iwriter;
	Directory dir;
	private static String filePath="/Users/omar/data/sinaweibo/userinfo/user_profile.full";
	private static String indexPath="/Users/omar/project/locationEngine/index-people";
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
		IndexPeople ip =new IndexPeople();
		ip.run();
		
	}

	@Override
	public void operateLine(String line) throws NextRowException {
		PeopleOrigin po = new PeopleOrigin();
		po.fromLine(line);
		try {
			iwriter.addDocument(po.toDocument());
		} catch (IOException e) {
			e.printStackTrace();
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
