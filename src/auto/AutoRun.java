package auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;

import origin.FriendShipOrigin;
import origin.Location;
import refined.Indexable;
import search.FriendShipSearch;
import search.SearchFramework;

/**
 * @author ChenJie
 *
 * 根据发微博的人生成 人*地点 的所有搜索结果
 */
public class AutoRun extends SearchFramework{
	
	public AutoRun(String index) {
		super(index);
	}

	static String filePath ="/Users/omar/project/locationEngine/index-std/";
	static String locationFile = "/Users/omar/project/locationEngine/stat/locations-whole.txt";
	static String usersIdFile = "/Users/omar/project/locationEngine/stat/usersId.txt";
	List<Location> locationList = new ArrayList<Location>();;
	List<String> usersId = new ArrayList<String>();
	Map<String,String> uidMap = new HashMap<String,String>();
	List<String> resultsList = new ArrayList<String>();
	FriendShipSearch fss = new FriendShipSearch();;
	QueryParser parser = new QueryParser(Version.LUCENE_40, "location", analyzer);
	FileWriter fw = null;
	
	public AutoRun() {
		super(filePath);
		try {
			fw = new FileWriter("/Users/omar/project/locationEngine/stat/results.txt");
			readFile();
			readUsersIdFile();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readFile() throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(new File(locationFile)));
		String line = null;
		int i = 0;
		while((line =  br.readLine()) !=null){
			String[] tmp= line.split(" ");
			int count = Integer.parseInt(tmp[tmp.length-1]);
			if(count>100){
				locationList.add(new Location(i,tmp[0]));
				i ++;
			}else{
				break;
			}
		}
		br.close();
		System.out.println("read locations file over");
	}
	public void readUsersIdFile() throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(new File(usersIdFile)));
		String line = null;
		while((line =  br.readLine()) !=null){
			usersId.add(line);
		}
		br.close();
		System.out.println("read usersId file over");
	}
	public void autoRun() throws IOException{
		System.out.println("usersId.size: "+usersId.size());
		System.out.println("locationList.size: "+locationList.size());
		for(String uid : usersId){
			for(Location location: locationList){
				try {
					wideSearch(uid, location);
				} catch (Exception e) {
					continue;
				}
			}
			if(resultsList.size()>0){
				for(String result: resultsList){
					fw.write(result);
					fw.write("\n");
				}
			}
			fw.flush();
			System.out.println("uid: "+uid+" over~~");
		}
	}
	
	public void generateUsersId() throws IOException{
		HashSet<String> usersId = new HashSet<String>();
		Document doc =null;
		for(int docId = 0 ; docId <  ireader.maxDoc(); docId ++){
			if(docId%1000 ==0){
				System.out.println(docId + "Parsed");
			}
			doc = ireader.document(docId);
			String uid = ireader.document(docId).get("uid");
			usersId.add(uid);
		}
		for(String userId : usersId){
			fw.write(userId);
			fw.write("\n");
		}
	}
	
	public void wideSearch(String uid, Location location) throws Exception{
//		System.out.println("search "+uid +" -- "+location);
		fss.clear();//清空结果
		fss.focuses(uid);
		List<Indexable> focuses = fss.origins;
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
		fss.clear();//清空关注
		fss.friends(uid);
		focuses = fss.origins;
		if(focuses!=null && focuses.size()>0){
			for(Indexable indexable:focuses){
				FriendShipOrigin fso = (FriendShipOrigin)indexable;
				focusIds.add(fso.getUid()+"");
			}
		}
	    Query query = parser.parse(location.getLocation());
	    for(Indexable focus:focuses){
	    	((FriendShipOrigin)focus).getFocus();
	    }
	    
	    BooleanQuery boolQuery = new BooleanQuery();
	    for(String focusId : focusIds){
	    	TermQuery uidQuery = new TermQuery(new Term("uid",focusId));
	    	boolQuery.add(uidQuery, Occur.SHOULD);
	    }
	    QueryWrapperFilter qwf = new QueryWrapperFilter(boolQuery);
	    ScoreDoc[] hits = isearcher.search(query, qwf, 100000).scoreDocs;
	    StringBuilder sb = new StringBuilder();
	    if(hits.length>0){
		    for (int i = 0; i < hits.length; i++) {
		    	sb.append(hits[i].doc+",");
		    }
		    String result = uid+"\t"+location.getId()+"\t"+sb.toString();
//		    System.out.println(result);
		    resultsList.add(result);
	    }
	}
	
	public static void main(String[] args) throws Exception {
		AutoRun ar = new AutoRun();
//		ar.generateUsersId();//生成usersId列表
		
		ar.autoRun();
		ar.close();
	}
	
	
	@Override
	public Query createQuery(String args) {
		return null;
	}

	@Override
	public Filter createFilter() {
		return null;
	}

	@Override
	public void handleDocument(Document hitDoc) {
		
	}
	
	@Override
	public void handleDocId(int docId){
		System.out.println(docId);
	};

	public void close(){
		try {
			fw.flush();
			fw.close();
			fss.close();
			ireader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
