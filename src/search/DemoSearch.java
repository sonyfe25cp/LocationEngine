package search;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class DemoSearch {

	public static void main(String[] args) throws Exception{
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

	    // Store the index in memory:
	    Directory directory = new RAMDirectory();
	    // To store an index on disk, use this instead:
	    //Directory directory = FSDirectory.open("/tmp/testindex");
	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
	    IndexWriter iwriter = new IndexWriter(directory, config);
	    Document doc =null;
	    int k  = 0;
	    	doc = new Document();
		    String text = "This is the text to be indexed.";
		    int int_num = k;
		    long long_num = 123123123123l+k;
		    
		    doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
		    doc.add(new StringField("int",int_num+"",Store.YES));
		    doc.add(new LongDocValuesField("long",long_num));
		    doc.add(new StoredField("int","0"));
		    
		    iwriter.addDocument(doc);
	    iwriter.commit();
	    iwriter.close();
	    
	    // Now search the index:
	    DirectoryReader ireader = DirectoryReader.open(directory);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    
	    // Parse a simple query that searches for "text":
	    QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "fieldname", analyzer);
	    Query pquery = parser.parse("text");
	    TermQuery termQuery = new TermQuery(new Term("int",0+""));
	    TermQuery longtermQuery = new TermQuery(new Term("long",123123123125l+""));
	    TermQuery sftermQuery = new TermQuery(new Term("sf","asdf"));
	    List<Query> queryList = new ArrayList<Query>();
	    queryList.add(pquery);
	    queryList.add(termQuery);
	    queryList.add(longtermQuery);
	    queryList.add(sftermQuery);
	    for(Query query : queryList){
		    ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		    for (int i = 0; i < hits.length; i++) {
		    	System.out.println("hits[i].doc: "+hits[i].doc);
		      Document hitDoc = isearcher.doc(hits[i].doc);
		      String output = hitDoc.get("fieldname");
		      String intoutput = hitDoc.get("int");
		      String longoutput = hitDoc.get("long");
		      String sfoutput = hitDoc.get("int");
		      System.out.println(output+"  ,"+intoutput+"  ,"+longoutput+"  ,sf:"+sfoutput);
		    }
		    System.out.println("***********************");
	    }
	    ireader.close();
	    directory.close();
	}
	
}
