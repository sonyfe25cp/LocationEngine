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
import origin.reader.OriginReaderFramework;
import refined.NewLuyou;

public class IndexStdTweets extends OriginReaderFramework {
	public IndexStdTweets(File file) {
		super(file);
	}
	public IndexStdTweets() {
		this(new File(filePath));
	}

	static String filePath = "/Users/omar/data/sinaweibo/std.lvyou.result.log2";
	static String indexPath = "/Users/omar/project/locationEngine/index-std2";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IndexStdTweets  ist= new IndexStdTweets();
		ist.run(10);
	}

	IndexWriter iwriter = null;
	Directory dir;

	@Override
	public void init() {
		try {
			File path = new File(indexPath);
			Analyzer analyzer = new IKAnalyzer(true);
			dir = FSDirectory.open(path);
			IndexWriterConfig iwConfig = new IndexWriterConfig(
					Version.LUCENE_40, analyzer);
			iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			iwriter = new IndexWriter(dir, iwConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void operateLine(String line) throws NextRowException {
		String[] tmps = line.split("\t");
		if (tmps.length == 6) {
			long uid = Long.parseLong(tmps[0]);
			String code = tmps[1];
			String tweet = tmps[2];
			String img = tmps[3];
			long time = Long.parseLong(tmps[4]);
			String device = tmps[5];
			NewLuyou nly = new NewLuyou(uid, tweet, img, null, device, time);
			try {
				if(nly.getLocation()!=null && nly.getLocation().length()>1){
					System.out.println(nly.toString());
					iwriter.addDocument(nly.toDocument());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void close() {
		try {
			iwriter.commit();
			iwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
