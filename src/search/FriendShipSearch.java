package search;


import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;

import origin.FriendShipOrigin;

public class FriendShipSearch extends SearchFramework{
	public FriendShipSearch(String index) {
		super(index);
	}
	public FriendShipSearch(){
		super(indexPath);
	}
	static String indexPath="/Users/omar/project/locationEngine/index-friendship";
	boolean focusFlag = false;
	@Override
	public Query createQuery(String args) {
		if(!focusFlag){
			setMaxNum(10000);
			QueryParser parser = new QueryParser(Version.LUCENE_40, "focus", analyzer);
		    Query query=null;
			try {
				query = parser.parse(args);
			} catch (ParseException e) {
				e.printStackTrace();
			}	
		    return query;
		}else{
			TermQuery query = new TermQuery(new Term("uid",args));
			return query;
		}
	}

	@Override
	public void handleDocument(Document hitDoc) {
		origins.add(FriendShipOrigin.fromDocument(hitDoc));
		if(debug)
			System.out.println(FriendShipOrigin.fromDocument(hitDoc).toString());		
	}
    
	public void friends(String id){
		focusFlag=false;
		search(id);
	}
	public void focuses(String id ){
		focusFlag=true;
		search(id);
	}
	
	/**
	 * @param args
	 * Dec 24, 2012
	 */
	public static void main(String[] args) {
		String indexPath="/Users/omar/project/locationEngine/index-friendship";
		String uid = "1869786551";
		FriendShipSearch ls = new FriendShipSearch(indexPath);
		ls.debug = true;
		ls.friends(uid);
		System.out.println("***************");
		ls.focuses(uid);
		ls.close();
	}
}
