package index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import origin.LuyouOrigin;
import origin.reader.OR_Luyou;
import refined.NewLuyou;

public class IndexTweets {
	
	public static void main(String[] args){
		IndexTweets it =new IndexTweets();
		it.buildIndex();
	}
	
	public void buildIndex(){
		String filePath="/Users/omar/project/locationEngine/index";
		File path = new File(filePath);
		try {
			Directory dir = FSDirectory.open(path);
			Analyzer analyzer = new IKAnalyzer();
			
			IndexWriter iwriter = null;
			IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_40 , analyzer);
			iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			iwriter = new IndexWriter(dir , iwConfig);
			
			File file = new File("/Users/omar/data/sinaweibo/lvyou.result.2");
			OR_Luyou or = new OR_Luyou(file);
			LuyouOrigin luyou = null;
			while((luyou = or.getNext())!=null){//用null来判断文件到头
				if(luyou.getA()!=null){//部分json会报错，返回为空对象
					Document doc = new NewLuyou(luyou).toDocument();
					iwriter.addDocument(doc);
				}
			}
			iwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
