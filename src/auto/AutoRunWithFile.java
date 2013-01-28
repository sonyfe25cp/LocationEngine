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
import java.util.Map.Entry;

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
import search.SearchFramework;

/**
 * @author ChenJie
 *
 * 根据发微博的人生成 人*地点 的所有搜索结果
 * 根据已经生成好的usersid-friendship，locations-whole来组合查询
 * 不用在去索引里搜usersid的好友关系，直接取列表来过滤location的搜索结果
 */
public class AutoRunWithFile extends SearchFramework{
	
	public AutoRunWithFile(String index) {
		super(index);
	}

	static String filePath ="/Users/omar/project/locationEngine/index-std/";
	static String locationFile = "/Users/omar/project/locationEngine/stat/locations-whole.txt";
	static String friendshipFile = "/Users/omar/project/locationEngine/stat/friendshipFile.txt";
	List<Location> locationList = new ArrayList<Location>();;
	Map<String,String> usersId = new HashMap<String,String>();
	Map<String,String> uidMap = new HashMap<String,String>();
	List<String> resultsList = new ArrayList<String>();
	QueryParser parser = new QueryParser(Version.LUCENE_40, "location", analyzer);
	FileWriter fw = null;
	
	public AutoRunWithFile() {
		super(filePath);
		try {
			fw = new FileWriter("/Users/omar/project/locationEngine/stat/results-with-friendship.txt");
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
		BufferedReader br = new BufferedReader(new FileReader(new File(friendshipFile)));
		String line = null;
		while((line =  br.readLine()) !=null){
			String[] tmp=line.split(" ");
			if(tmp.length == 2){
				usersId.put(tmp[0], tmp[1]);
			}
		}
		br.close();
		System.out.println("read usersId file over");
	}
	public void autoRun() throws IOException{
		System.out.println("usersId.size: "+usersId.size());
		System.out.println("locationList.size: "+locationList.size());
		for(Entry<String,String> entry : usersId.entrySet()){
			for(Location location: locationList){
				try {
					wideSearch(entry, location);
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
			System.out.println("uid: "+entry.getKey()+" over~~");
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
	
	public void wideSearch(Entry<String,String> entry, Location location) throws Exception{
//		System.out.println("search "+uid +" -- "+location);
		List<String> focusIds = new ArrayList<String>();
		String ids = entry.getValue();
		if(ids!=null){
			String[] fsoses = ids.split(",");
			for(String tmp : fsoses){
				focusIds.add(tmp);
			}
		}else{
			return ;
		}
	    Query query = parser.parse(location.getLocation());
	    
	    BooleanQuery boolQuery = new BooleanQuery();
	    for(String focusId : focusIds){
	    	TermQuery uidQuery = new TermQuery(new Term("uid",focusId));
	    	boolQuery.add(uidQuery, Occur.SHOULD);
	    }
	    QueryWrapperFilter qwf = new QueryWrapperFilter(boolQuery);
	    ScoreDoc[] hits = isearcher.search(query, qwf, 1000).scoreDocs;
	    StringBuilder sb = new StringBuilder();
	    if(hits.length>0){
		    for (int i = 0; i < hits.length; i++) {
		    	sb.append(hits[i].doc+",");
		    }
		    String result = entry.getKey()+"\t"+location.getId()+"\t"+sb.toString();
//		    System.out.println(result);
		    resultsList.add(result);
	    }
	}
	
	public static void main(String[] args) throws Exception {
		AutoRunWithFile ar = new AutoRunWithFile();
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
			ireader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
