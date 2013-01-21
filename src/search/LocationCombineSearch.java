package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;

import origin.FriendShipOrigin;
import refined.Indexable;
import refined.NewLuyou;

/**
 * 搜索某地的微博
 * @param location
 * Dec 24, 2012
 */
public class LocationCombineSearch extends SearchFramework{
	static String indexPath="/Users/omar/project/locationEngine/index";
	
	public LocationCombineSearch(){
		super(indexPath);
	}
	
	public LocationCombineSearch(String index) {
		super(index);
	}
	@Override
	public Query createQuery(String args) {
		QueryParser parser = new QueryParser(Version.LUCENE_40, "location", analyzer);
	    Query query=null;
		try {
			query = parser.parse(args);
		} catch (ParseException e) {
			e.printStackTrace();
		}	
	    return query;
	}

	@Override
	public void handleDocument(Document hitDoc) {
		origins.add(NewLuyou.fromDocument(hitDoc));
		if(debug)
			System.out.println(NewLuyou.fromDocument(hitDoc).toString());		
	}
    
	/**
	 * @param args
	 * Dec 24, 2012
	 */
	public static void main(String[] args) {
		String indexPath="/Users/omar/project/locationEngine/index";
		String location = "中央戏剧学院";
		LocationCombineSearch ls = new LocationCombineSearch(indexPath);
		ls.search(location);
		
		System.out.println("**********************************");
		
		String uid = "1904350101";
		ls.search(uid,location);
		System.out.println("**********************************");
		ls.wideSearch(uid, location);
		
		ls.close();
	}
	
	
	
	/**
	 * 搜索某人在某地的微博
	 * Dec 24, 2012
	 */
	public void search(String uid, String location){
		try {
			QueryParser parser = new QueryParser(Version.LUCENE_40, "location", analyzer);
		    Query query = parser.parse(location);
		    TermQuery uidQuery = new TermQuery(new Term("uid",uid));
		    
		    QueryWrapperFilter filter = new QueryWrapperFilter(uidQuery);
		    
		    ScoreDoc[] hits = isearcher.search(query, filter, 100).scoreDocs;
		    for (int i = 0; i < hits.length; i++) {
		      Document hitDoc = isearcher.doc(hits[i].doc);
		      System.out.println(NewLuyou.fromDocument(hitDoc).toString());
		    }
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 搜索某人的好友在某地的微博
	 * Dec 24, 2012
	 */
	public void wideSearch(String uid, String location){
		FriendShipSearch ls = new FriendShipSearch();
		System.out.println("搜 "+uid+" 在本系统中有多少个好友");
		ls.search(uid);
		List<Indexable> focuses = ls.origins;
		ls.close();
		List<String> focusIds;
		if(focuses.size()==1){
			FriendShipOrigin fso = (FriendShipOrigin)focuses.get(1);
			String fsos = fso.getFocus();
			String[] fsoses = fsos.split(",");
			focusIds = new ArrayList<String>();
			for(String tmp : fsoses){
				focusIds.add(tmp);
			}
		}else{
			System.out.println("没有搜到 "+uid+" 的好友在 "+location+" 发的微博");
			return ;
		}
		try {
			QueryParser parser = new QueryParser(Version.LUCENE_40, "location", analyzer);
		    Query query = parser.parse(location);
		    for(Indexable focus:focuses){
		    	((FriendShipOrigin)focus).getFocus();
		    }
		    
		    BooleanQuery boolQuery = new BooleanQuery();
		    boolQuery.add(query, Occur.MUST);
		    for(String focusId : focusIds){
		    	TermQuery uidQuery = new TermQuery(new Term("uid",uid));
		    	boolQuery.add(uidQuery, Occur.SHOULD);
		    }
		    
		    ScoreDoc[] hits = isearcher.search(boolQuery, null, getMaxNum()).scoreDocs;
		    for (int i = 0; i < hits.length; i++) {
		      Document hitDoc = isearcher.doc(hits[i].doc);
		      System.out.println(NewLuyou.fromDocument(hitDoc).toString());
		    }
		    ireader.close();
		    dir.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	public void close(){
		 try {
			ireader.close();
		    dir.close();
		 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
	}



}
