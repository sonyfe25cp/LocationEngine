package search;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;

import origin.FriendShipOrigin;
import refined.Indexable;

public class FriendShipSearch extends SearchFramework{
	public FriendShipSearch(String index) {
		super(index);
		parser = new QueryParser(Version.LUCENE_40, "focus", analyzer);	
	}
	public FriendShipSearch(){
		this(indexPath);
	}
	static String indexPath="/Users/omar/project/locationEngine/index-friendship";
	boolean focusFlag = false;
	QueryParser parser=null;
	@Override
	public Query createQuery(String args) {
		if(!focusFlag){
			setMaxNum(10000);
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
	 
	public void friendships(String uid,HashSet<String> focusIds){
		clear();
		if(debug)
			System.out.println("搜 "+uid+" 在本系统中关注了多少个好友");
		focuses(uid);
		List<Indexable> focuses = origins;
		if(focuses.size()==1){
			FriendShipOrigin fso = (FriendShipOrigin)focuses.get(0);
			String fsos = fso.getFocus();
			String[] fsoses = fsos.split(",");
			for(String tmp : fsoses){
				focusIds.add(tmp);
			}
		}
		if(debug)
			System.out.println("搜 "+uid+" 在本系统中有多少个粉丝");
		clear();
		friends(uid);
		focuses = origins;
		if(focuses!=null && focuses.size()>0){
			for(Indexable indexable:focuses){
				FriendShipOrigin fso = (FriendShipOrigin)indexable;
				focusIds.add(fso.getUid()+"");
			}
		}
	}
	
	/**
	 * @param args
	 * Dec 24, 2012
	 */
	public static void main(String[] args) {
		String indexPath="/Users/omar/project/locationEngine/index-friendship";
		String uid = "1869786551";
		FriendShipSearch ls = new FriendShipSearch(indexPath);
//		ls.debug = true;
//		ls.friends(uid);
//		System.out.println("***************");
//		ls.focuses(uid);
		
		try {
			ls.readUsersIdFile();
			ls.totalFlush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ls.close();
	}
	@Override
	public Filter createFilter() {
		return null;
	}
	List<String> usersId = new ArrayList<String>();
	static String usersIdFile = "/Users/omar/project/locationEngine/stat/usersId.txt";
	static String friendshipFile = "/Users/omar/project/locationEngine/stat/friendshipFile.txt";
	public void readUsersIdFile() throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(new File(usersIdFile)));
		String line = null;
		while((line =  br.readLine()) !=null){
			usersId.add(line);
		}
		br.close();
		System.out.println("read usersId file over");
	}
	public void totalFlush(){
		try {
			FileWriter fw = new FileWriter(new File(friendshipFile));
			ArrayList<String> results = new ArrayList<String>();
			int i = 0 ; 
			for(String uid : usersId){
				if(i % 10 == 0){
					System.out.println(i + " uid parsed~~");
					for(String result: results){
						fw.write(result);
						fw.write("\n");
						fw.flush();
					}
					results = new ArrayList<String>();
				}
				HashSet<String> uidlist = new HashSet<String>();
				friendships(uid,uidlist);
				StringBuilder sb = new StringBuilder();
				if(uidlist.size()>0){
					for(String id:uidlist){
						sb.append(id+",");
					}
					String line = uid+"\t"+sb.toString();
					results.add(line);
				}
				i ++;
			}
			for(String result: results){
				fw.write(result);
				fw.write("\n");
				fw.flush();
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
