package auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import origin.Location;
import search.SearchFramework;

/**
 * @author ChenJie
 *
 * 根据发微博的人生成 人*地点 的所有搜索结果
 * 根据已经生成好的friendship文件，locations-whole来组合查询
 * 不用在去索引里搜usersid的好友关系，直接取列表来过滤location的搜索结果
 * 1006662555
 */
public class AutoRunWithFileThread extends SearchFramework implements Runnable{
	
	public AutoRunWithFileThread(String index) {
		super(index);
	}

	static String indexPath ="/Users/omar/project/locationEngine/index-std/";
	static String locationFile = "/Users/omar/project/locationEngine/stat/locations-whole.txt";
	
	String friendshipFile;
	String resultsFile;
	List<Location> locationList = new ArrayList<Location>();
	BufferStore bs = null;
	QueryParser parser = new QueryParser(Version.LUCENE_40, "location", analyzer);
	
	public AutoRunWithFileThread(String friendshipFile,BufferStore bs) {
		super(indexPath);
		this.friendshipFile = friendshipFile;
		this.bs = bs;
		try {
			readFile();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
			if(count>300){//只备份查询次数超过300次的地点
				locationList.add(new Location(i,tmp[0]));
				i ++;
			}else{
				break;
			}
		}
		br.close();
		System.out.println("read locations file over");
	}
	public void autoRun() throws IOException{
		System.out.println("locationList.size: "+locationList.size());
		
		BufferedReader br = new BufferedReader(new FileReader(new File(friendshipFile)));
		String line = "";
		int i = 0;
		while((line = br.readLine())!=null){
//			System.out.println(line);
			String[] tmp=line.split("( |\t)");
			String uid = tmp[0];
//			if(uid.length()<10){
//				continue;
//			}
			System.out.println(uid);
			String frienship_str = tmp[1];
			String[] friendship = frienship_str.split(",");
			if(friendship.length>50){//只对关系人数大于50的人做预处理
				for(Location location: locationList){
					try {
						wideSearch(uid, friendship, location);
					} catch (Exception e) {
						continue;
					}
				}
			}
			i++;
			if(i % 1000 == 0){
				System.out.println(i +" parsed~~");
			}
		}
		bs.close();
		br.close();
	}
	
	public void wideSearch(String uid, String[] friendship, Location location) throws Exception{
	    Query query = parser.parse(location.getLocation());
	    
	    BooleanQuery boolQuery = new BooleanQuery();
	    for(String focusId : friendship){
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
		    String result = uid+"\t"+location.getId()+"\t"+sb.toString();
//		    System.out.println("results: "+result);
		    bs.add(result);
	    }
	}
	
	public void run(){
		try {
			autoRun();
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			ireader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}