package search;


import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import origin.PeopleOrigin;

public class PeopleSearch extends SearchFramework{
	public PeopleSearch(String index) {
		super(index);
	}
	static String indexPath ="/Users/omar/project/locationEngine/index-people";
	public PeopleSearch(){
		super(indexPath);
	}
	private boolean idFlag=true;
	
	@Override
	public Query createQuery(String args) {
		Query query=null;
		if(idFlag){
			query = new TermQuery(new Term("uid",args));
		}else{
			query = new TermQuery(new Term("name",args));

		}
		return query;
	}

	@Override
	public void handleDocument(Document hitDoc) {
		origins.add(PeopleOrigin.fromDocument(hitDoc));
		if(debug)
			System.out.println(PeopleOrigin.fromDocument(hitDoc).toString());		
	}
    
	public void searchId(String uid){
		search(uid);
	}
	public void searchName(String name){
		idFlag=false;
		search(name);
	}
	
	/**
	 * @param args
	 * Dec 24, 2012
	 */
	public static void main(String[] args) {
		String indexPath="/Users/omar/project/locationEngine/index-people";
		String uid = "1781085583";
		PeopleSearch ls = new PeopleSearch(indexPath);
		ls.debug=true;
		ls.searchId(uid);
		System.out.println("**************************");
		ls.searchName("梁斌penny");
		ls.close();
	}

	@Override
	public Filter createFilter() {
		return null;
	}
}
