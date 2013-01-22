package search;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import origin.FriendShipOrigin;
import refined.Indexable;
import refined.NewLuyou;
import test.WordsCount;

/**
 * 搜索某地的微博
 * @param location
 * Dec 24, 2012
 */
public class LocationCombineSearch extends SearchFramework{
	static String indexPath="/Users/omar/project/locationEngine/index-std";
	
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
		String location = "中央戏剧学院";
		LocationCombineSearch ls = new LocationCombineSearch();
//		ls.search(location);
//		
//		System.out.println("**********************************");
//		
//		String uid = "1904350101";
//		ls.search(uid,location);
//		System.out.println("**********************************");
//		ls.wideSearch(uid, location);
		
//		ls.write2file();
		ls.writeLocations2file();
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
		System.out.println("搜 "+uid+" 在本系统中关注了多少个好友");
		ls.focuses(uid);
		List<Indexable> focuses = ls.origins;
		
		List<String> focusIds;
		focusIds = new ArrayList<String>();
		if(focuses.size()==1){
			FriendShipOrigin fso = (FriendShipOrigin)focuses.get(0);
			String fsos = fso.getFocus();
			String[] fsoses = fsos.split(",");
			for(String tmp : fsoses){
				focusIds.add(tmp);
			}
		}
		System.out.println("搜 "+uid+" 在本系统中有多少个粉丝");
		ls.clear();
		ls.friends(uid);
		focuses = ls.origins;
		if(focuses!=null && focuses.size()>0){
			for(Indexable indexable:focuses){
				FriendShipOrigin fso = (FriendShipOrigin)indexable;
				focusIds.add(fso.getUid()+"");
			}
		}
		ls.close();
		try {
			QueryParser parser = new QueryParser(Version.LUCENE_40, "location", analyzer);
		    Query query = parser.parse(location);
		    for(Indexable focus:focuses){
		    	((FriendShipOrigin)focus).getFocus();
		    }
		    
		    BooleanQuery boolQuery = new BooleanQuery();
		    for(String focusId : focusIds){
//		    	System.out.println("focusId: "+focusId);
		    	TermQuery uidQuery = new TermQuery(new Term("uid",focusId));
		    	boolQuery.add(uidQuery, Occur.SHOULD);
		    }
		    QueryWrapperFilter qwf = new QueryWrapperFilter(boolQuery);
		    ScoreDoc[] hits = isearcher.search(query, qwf, 100000).scoreDocs;
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
	@Override
	public Filter createFilter() {
		// TODO Auto-generated method stub
		return null;
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

	/**
	 *  统计地点热词
	 */
	public void write2file(){
		try {
			Map<String,Long> wordsbag = new HashMap<String,Long>();
			System.out.println("maxDoc: "+ireader.maxDoc());
			for(int doc = 0 ; doc < ireader.maxDoc(); doc ++){
//			for(int doc = 0 ; doc < 200; doc ++){
				
				if(doc%1000 == 0){
					System.out.println(doc + " parsed~~");
				}
				Terms terms = ireader.getTermVector(doc, "location");
				if(terms == null){
					continue;
				}
				TermsEnum termsEnum = terms.iterator(null);
				BytesRef term = null;
				while ((term = termsEnum.next()) != null) {
					String word = termsEnum.term().utf8ToString();
//					System.out.println("== "+word);
					if(word.length()>1){
						if(wordsbag.containsKey(word)){
							long value = wordsbag.get(word);
							wordsbag.put(word, value+1);
						}else{
							wordsbag.put(word, 1l);
						}
					}
				}
			}
			FileWriter fw = new FileWriter(new File("/Users/omar/project/locationEngine/stat/locations.txt"));
			List<WordsCount> wclist = new ArrayList<WordsCount>();
			for(Entry<String,Long> entry: wordsbag.entrySet()){
				WordsCount wc = new WordsCount(entry.getKey(), entry.getValue());
				wclist.add(wc);
			}
			Collections.sort(wclist);
			for(WordsCount wc : wclist){
				fw.write(wc.getWord()+" "+wc.getCount());
				fw.write("\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeLocations2file(){
		try {
			Map<String,Long> wordsbag = new HashMap<String,Long>();
			System.out.println("maxDoc: "+ireader.maxDoc());
			for(int doc = 0 ; doc < ireader.maxDoc(); doc ++){
//			for(int doc = 0 ; doc < 200; doc ++){
				
				if(doc%1000 == 0){
					System.out.println(doc + " parsed~~");
				}
				String word = ireader.document(doc).get("location");
//					System.out.println("== "+word);
					if(word.length()>1){
						if(wordsbag.containsKey(word)){
							long value = wordsbag.get(word);
							wordsbag.put(word, value+1);
						}else{
							wordsbag.put(word, 1l);
						}
					}
			}
			FileWriter fw = new FileWriter(new File("/Users/omar/project/locationEngine/stat/locations-whole.txt"));
			List<WordsCount> wclist = new ArrayList<WordsCount>();
			for(Entry<String,Long> entry: wordsbag.entrySet()){
				WordsCount wc = new WordsCount(entry.getKey(), entry.getValue());
				wclist.add(wc);
			}
			Collections.sort(wclist);
			for(WordsCount wc : wclist){
				fw.write(wc.getWord()+" "+wc.getCount());
				fw.write("\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
