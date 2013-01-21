package product;

import java.util.List;

import origin.PeopleOrigin;
import refined.Indexable;
import search.LocationCombineSearch;
import search.PeopleSearch;

public class MainSearch {

	public static void main(String[] args) {
		String location = "北京理工大学";
		String nikename = "Sonyfe25cp";
		long id = 1835054731;
		
		MainSearch ms = new MainSearch();
		//搜某地
		ms.search(location);
		//搜某人在某地
		ms.search(id,location,false);
		//搜某人好友在某地
		ms.search(id,location,true);
		//搜某人昵称在某地
//		ms.search(nikename, location,false);
		//搜某人昵称好友在某地
//		ms.search(nikename, location,true);
	}
	
	void search(String location){
		System.out.println("**********只搜在 "+location+" 发的微博**********");
		LocationCombineSearch ls = new LocationCombineSearch();
		ls.search(location);
		List<Indexable> locations = ls.origins;
		System.out.println("size:"+locations.size());
		for(Indexable origin : locations ){
			System.out.println(origin);
		}
		ls.close();
	}
	//搜某人在某地
	//false 为自己
	//true 为好友
	void search(long uid, String location,boolean flag){
		LocationCombineSearch ls = new LocationCombineSearch();
		if(!flag){
			System.out.println("**********搜 "+uid+" 在 "+location+" 发的微博**********");
			ls.search(uid+"",location);
			List<Indexable> locations = ls.origins;
			System.out.println("size:"+locations.size());
			for(Indexable origin : locations ){
				System.out.println(origin);
			}
		}else{
			System.out.println("**********搜 "+uid+" 的好友在 "+location+" 发的微博**********");
			ls.clear();
			ls.wideSearch(uid+"",location);
			List<Indexable> locations = ls.origins;
			System.out.println("size:"+locations.size());
			for(Indexable origin : locations ){
				System.out.println(origin);
			}
		}
		ls.close();
	}
	//搜昵称与地点
	//false 为自己
	//true 为好友
	void search(String nikename, String location,boolean flag){
		if(flag){
			System.out.println("**********搜 "+nikename+" 的好友在 "+location+" 发的微博**********");
			System.out.println("在本系统中搜 "+nikename+" 有多少个好友");
		}else{
			System.out.println("**********搜 "+nikename+" 在 "+location+" 发的微博**********");
			System.out.println("在本系统中搜 "+nikename+" 是否存在");
		}
		PeopleSearch ps = new PeopleSearch();
		ps.searchName(nikename);
		
		if(ps.origins.size()==1){
			PeopleOrigin peo = (PeopleOrigin) ps.origins.get(0);
			long uid = peo.getUid();
			search(uid,location,flag);
		}else{
			System.out.println("没有找到"+nikename+"在"+location+"发的微博");
		}
		ps.close();
	}
	

}
