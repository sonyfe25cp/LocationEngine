package refined;

import index.VecTextField;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import origin.LuyouOrigin;

public class NewLuyou implements Indexable{
	private long uid;
	private String tweet;
	private String img;
	private String location;
	private List<String> atlist;
	private String device;
	private long time=0;
	
	
	public NewLuyou(long uid, String tweet, String img, String location,
			List<String> atlist, String device, long time) {
		super();
		this.uid = uid;
		this.tweet = tweet;
		this.img = img;
		this.location = location;
		this.atlist = atlist;
		this.device = device;
		this.time = time;
	}
	
	public NewLuyou(long uid, String tweet, String img, List<String> atlist,
			String device, long time) {
		super();
		this.uid = uid;
		this.tweet = tweet;
		this.img = img;
		this.location= locationFilter(tweet);
		this.atlist = atlist;
		this.device = device;
		this.time = time;
	}



	public NewLuyou (LuyouOrigin origin){
		
		long uid = Long.parseLong(origin.getA());
		String tweet = origin.getC();
		String body = tweetFilter(tweet);
		
		String dev = origin.getF();
		
		String img = origin.getD();
		
		List<String> alist = atFilter(tweet);//待做
		
		String location = locationFilter(tweet);
		
		this.uid = uid;
		this.tweet= body;
		this.img=img==null?"":img;
		this.location=location==null?"":location;
		this.device=dev==null?"":dev;
		this.atlist=alist;
	}
	public NewLuyou(){
		
	}
	public Document toDocument(){
		Document doc = new Document();
		doc.add(new StringField("uid",uid+"",Store.YES));
		doc.add(new VecTextField("location",location,Store.YES));
		doc.add(new VecTextField("tweet",tweet,Store.YES));
		doc.add(new StringField("img",img,Store.YES));
		doc.add(new TextField("device",device,Store.YES));
		doc.add(new LongField("time",time,Store.YES));
		return doc;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("uid: "+uid +" \n");
		sb.append("location: "+location +" \n");
		sb.append("tweet: "+tweet +" \n");
		sb.append("img: "+img +" \n");
		sb.append("device: "+device +" \n");
		sb.append("time: "+time+" \n");
		return sb.toString();
	}
	
	public static NewLuyou fromDocument(Document document){
		NewLuyou luyou = new NewLuyou();
		String location = document.get("location");
		String tweet = document.get("tweet");
		String img = document.get("img");
		String device = document.get("device");
		long time = Long.parseLong(document.get("time")==null?"0":document.get("time"));
		
		long uid = document.get("uid") == null ? 0 :Long.parseLong(document.get("uid"));
		luyou.setUid(uid);
		luyou.setLocation(location);
		luyou.setDevice(device);
		luyou.setImg(img);
		luyou.setTweet(tweet);
		luyou.setTime(time);
//		luyou.setAtlist(atlist);
		return luyou;
		
	}
	
	private List<String> atFilter(String tweet){
		if(tweet!=null && tweet.length()>0){
		}else{
		}
		return null;
	}
	
	
	public static String locationFilter(String tweet){
		String location = "";
		String regex = "(我在这里：#.*?#|我在#.*?#|我在这里: #.*?#|我在这里:#.*?#|我在這裏: #.*?#|我在這裏:#.*?#)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(tweet);
		if (matcher.find()){
		      location = matcher.group();
		      location =  location.substring(location.indexOf("#")+1, location.length()-1);
		}
		if(location.length()>0){
			return location;
		}
		String regex2 ="我在.*?。";
		Pattern pattern2 = Pattern.compile(regex2);
		Matcher matcher2 = pattern2.matcher(tweet);
		location = "";
		if (matcher2.find()){
		      location = matcher2.group();
		      location =  location.replaceAll("我在", "").replaceAll("。", "");
		}
		if(location.length()<5){
			return location;
		}else{
			return "";
		}
	}
	
	public static void main(String[] args){
		NewLuyou nly = new NewLuyou();
		String test = "我在这里：#上海浦东国际机场#我们出发咯 http://t.cn/S4b0W3";
		String t2 = "我在#家乐福#，^V^ http://t.cn/SbKXkN";
		String t3 = "我在家乐福看电影。^V^ http://t.cn/SbKXkN";
		
		System.out.println(locationFilter(test));
		System.out.println(locationFilter(t2));
		System.out.println(locationFilter(t3));
	}
	
	
	public static String tweetFilter(String body){
		if(body!=null && body.length()>0){
			body = body.replaceAll("[\uafff-\uefff]+"," ");
			body = body.replaceAll("[\u1000-\u3fff]+"," ");
			return body;
		}else
			return null;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<String> getAtlist() {
		return atlist;
	}

	public void setAtlist(List<String> atlist) {
		this.atlist = atlist;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

}
