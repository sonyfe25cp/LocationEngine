package test;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FieldValueFilter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import refined.NewLuyou;

public class TestStd2 {

	public TestStd2() {
		init();
	}

	static String indexPath="/Users/omar/project/locationEngine/index-std2";
	Directory dir = null;
	DirectoryReader ireader = null;
    IndexSearcher isearcher = null;
    protected Analyzer analyzer = null;
    private int maxNum = 100;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestStd2 tstd2 =new TestStd2();
		tstd2.search("大学");
	    
	    
	}

	public Query createQuery(String args) {
		Query query = null;
		
		try {
			QueryParser parser = new QueryParser(Version.LUCENE_40, "tweet",
					analyzer);
			query = parser.parse(args);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return query;
	}
	public void handleDocument(Document hitDoc) {
		System.out.println(NewLuyou.fromDocument(hitDoc).toString());
	}
	public void init(){
    	try {
			dir = NIOFSDirectory.open(new File(indexPath));
			ireader = DirectoryReader.open(dir);
			isearcher = new IndexSearcher(ireader);
			analyzer = new IKAnalyzer(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	 public void search(String args){
			try {
			    Query query = createQuery(args);
			    
			    String[] ids = new String[] { "1802173541", "1804577451" ,"1806956105"};
			    BooleanQuery boolQuery = new BooleanQuery();
				for (String focusId : ids) {
					TermQuery uidQuery = new TermQuery(new Term("uid", focusId));
					boolQuery.add(uidQuery, Occur.SHOULD);
				}
				QueryWrapperFilter qwf = new QueryWrapperFilter(boolQuery);
			    ScoreDoc[] hits = isearcher.search(query, qwf, maxNum).scoreDocs;
//			    ScoreDoc[] hits = isearcher.search(query, null, maxNum).scoreDocs;
			    for (int i = 0; i < hits.length; i++) {
			      Document hitDoc = isearcher.doc(hits[i].doc);
			      handleDocument(hitDoc);
			    }
			    System.out.println("本次搜索一共找到："+hits.length+" 个结果.");
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}

}
