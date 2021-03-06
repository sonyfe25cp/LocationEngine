package search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import refined.Indexable;

public abstract class SearchFramework {
	
	public SearchFramework(String index){
		this.indexPath=index;
		localInit();
	}

	protected boolean debug=false;
	
	private String indexPath;
	Directory dir = null;
	protected DirectoryReader ireader = null;
	protected IndexSearcher isearcher = null;
    protected Analyzer analyzer = null;
    private int maxNum = 100;
    
    public List<Indexable> origins = new ArrayList<Indexable>();
    
    public void clear(){
    	origins = null;
    	origins = new ArrayList<Indexable>();
    }
    
    public void localInit(){
    	try {
			dir = NIOFSDirectory.open(new File(indexPath));
			ireader = DirectoryReader.open(dir);
			isearcher = new IndexSearcher(ireader);
			analyzer = new IKAnalyzer(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public abstract Query createQuery(String args);
    public abstract Filter createFilter();
    
    public void search(String args){
		try {
		    Query query = createQuery(args);
		    Filter filter = createFilter();
		    ScoreDoc[] hits = isearcher.search(query, filter, maxNum).scoreDocs;
		    for (int i = 0; i < hits.length; i++) {
		      Document hitDoc = isearcher.doc(hits[i].doc);
//		      System.out.println("docId: "+hits[i].doc);
		      handleDocument(hitDoc);
		    }
//		    System.out.println("本次搜索一共找到："+hits.length+" 个结果.");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
    public abstract void handleDocument(Document hitDoc);
    
    public void close(){
		 try {
			ireader.close();
		    dir.close();
		 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
	}
	public int getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	
    public void searchForDoc(String args){
		try {
		    Query query = createQuery(args);
		    Filter filter = createFilter();
		    ScoreDoc[] hits = isearcher.search(query, filter, maxNum).scoreDocs;
		    for (int i = 0; i < hits.length; i++) {
		    	handleDocId(hits[i].doc);
		    }
		    System.out.println("本次搜索一共找到："+hits.length+" 个结果.");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
    public void handleDocId(int docId){
    	
    };
}
