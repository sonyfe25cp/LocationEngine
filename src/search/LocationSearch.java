package search;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import origin.Location;

public class LocationSearch extends SearchFramework{

	static String indexPath="/Users/omar/project/locationEngine/index-location";
	
	public LocationSearch(String index) {
		super(index);
	}
	public LocationSearch() {
		super(indexPath);
	}
	

	@Override
	public Query createQuery(String args) {
		QueryParser parser = new QueryParser(Version.LUCENE_40,"location",analyzer);
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
		origins.add(Location.fromDocument(hitDoc));
		System.out.println(Location.fromDocument(hitDoc).toString());
	}
	
	public static void main(String[] args){
		String indexPath="/Users/omar/project/locationEngine/index-location";
		LocationSearch ls = new LocationSearch(indexPath);
		String location="清华大学";
		ls.search(location);
		ls.showTermsFreq();
		ls.close();
	}
	
	public void showTermsFreq(){
		try {
			int num = ireader.docFreq(new Term("location","巴黎"));
			for(int doc = 25923 ; doc < 25927; doc ++){
				Terms terms = ireader.getTermVector(doc, "location");
				Fields fields = ireader.getTermVectors(doc);
				
				TermsEnum termsEnum = terms.iterator(null);
				BytesRef term = null;
				while ((term = termsEnum.next()) != null) {
				  System.out.println("== "+termsEnum.term().utf8ToString());
				}
				
				for (String field : fields) {
					  // access the terms for this field
					  Terms tmp = fields.terms(field);
					  System.out.println("--terms.getDocCount(): "+tmp.getDocCount());
					  System.out.println("--terms.getSumDocFreq(): "+tmp.getSumDocFreq());
					}
				
				if(terms == null){
					System.out.println(doc + " terms is null");
				}
				System.out.println(ireader.document(doc).get("location"));
				System.out.println("terms.getDocCount(): "+terms.getDocCount());
				System.out.println("terms.getSumDocFreq(): "+terms.getSumDocFreq());
				System.out.println("terms.getSumTotalTermFreq(): "+terms.getSumTotalTermFreq());
				System.out.println("terms.size(): "+terms.size());
			}
			
			System.out.println(num +" freq");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public Filter createFilter() {
		return null;
	}

}
