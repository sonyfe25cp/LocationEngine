package refined;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
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
		doc.add(new TextField("location",location,Store.YES));
		doc.add(new TextField("tweet",tweet,Store.YES));
		doc.add(new StringField("img",img,Store.YES));
		doc.add(new TextField("device",device,Store.YES));
		return doc;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("uid: "+uid +" \n");
		sb.append("location: "+location +" \n");
		sb.append("tweet: "+tweet +" \n");
		sb.append("img: "+img +" \n");
		sb.append("device: "+device +" \n");
		return sb.toString();
	}
	
	public static NewLuyou fromDocument(Document document){
		NewLuyou luyou = new NewLuyou();
		String location = document.get("location");
		String tweet = document.get("tweet");
		String img = document.get("img");
		String device = document.get("device");
		
		long uid = document.get("uid") == null ? 0 :Long.parseLong(document.get("uid"));
		luyou.setUid(uid);
		luyou.setLocation(location);
		luyou.setDevice(device);
		luyou.setImg(img);
		luyou.setTweet(tweet);
//		luyou.setAtlist(atlist);
		return luyou;
		
	}
	
	private List<String> atFilter(String tweet){
		if(tweet!=null && tweet.length()>0){
//			return dao.save("pic", "img_url", imgUrl);
		}else{
			
		}
		return null;
	}
	
	
	private String locationFilter(String tweet){
		String regex = "我在这里：#.*?#";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(tweet);
		String location = "";
		if (matcher.find()){
		      location = matcher.group();
		      location =  location.substring(location.indexOf("#")+1, location.length()-1);
		}
		return location;
	}
	
	private String tweetFilter(String body){
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

}
