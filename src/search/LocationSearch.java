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
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import origin.Location;
import test.WordsCount;

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
//		ls.search(location);
//		ls.showTermsFreq();
		ls.write2file();
		ls.close();
	}
	public void write2file(){
		try {
			Map<String,Long> wordsbag = new HashMap<String,Long>();
			for(int doc = 0 ; doc < ireader.maxDoc(); doc ++){
//			for(int doc = 0 ; doc < 200; doc ++){
				Terms terms = ireader.getTermVector(doc, "location");
				if(terms == null){
					continue;
				}
				TermsEnum termsEnum = terms.iterator(null);
				BytesRef term = null;
				while ((term = termsEnum.next()) != null) {
					String word = termsEnum.term().utf8ToString();
					System.out.println("== "+word);
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

}
