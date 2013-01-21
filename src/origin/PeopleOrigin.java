package origin;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import refined.Indexable;

public class PeopleOrigin extends Origin implements Indexable{
	
	private long uid;
	private int tweetsNum;
	private int focusNum;
	private int fansNum;
	
	private String name;
	private String tags;
	private String img;
	
	@Override
	public Document toDocument() {
		Document document = new Document();
		document.add(new StringField("uid",uid+"",Store.YES));
		document.add(new StringField("tweetsNum",tweetsNum+"",Store.YES));
		document.add(new StringField("focusNum",focusNum+"",Store.YES));
		document.add(new StringField("fansNum",fansNum+"",Store.YES));
		document.add(new StringField("name",name,Store.YES));
		document.add(new TextField("tags",tags,Store.YES));
		document.add(new StringField("img",img,Store.YES));
		return document;
	}
	
	public static PeopleOrigin fromDocument(Document document){
		long uid = Long.parseLong(document.get("uid"));
		int tweetsNum = trans(document.get("tweetsNum"));
		int focusNum = trans(document.get("focusNum"));
		int fansNum = trans(document.get("fansNum"));
		String name = document.get("name");
		String tags = document.get("tags");
		String img = document.get("img");
		return new PeopleOrigin(uid,tweetsNum,focusNum,fansNum,name,tags,img);
	}
	
	@Override
	public void fromLine(String line) {
		String[] parts = line.split("\t");
		long uid=0;
		try{
			uid = Long.parseLong(parts[0]);
		}catch(Exception e){
			throw new NextRowException();
		}
		int tweetsNum = trans(parts[1]);
		int focusNum = trans(parts[2]);
		int fansNum = trans(parts[3]);
		String name = parts[4].replaceAll("#", "");
		String tags="";
		if(parts.length>5){
			tags = parts[5];
		}
		String img = "";
		if(parts.length>6){
			img = parts[6];
		}
		this.uid = uid;
		this.tweetsNum = tweetsNum;
		this.focusNum = focusNum;
		this.fansNum = fansNum;
		this.name = name;
		this.tags = tags;
		this.img = img;
	}
	public static int trans(String value){
		try{
			return Integer.parseInt(value);
		}catch(Exception e){
			return 0;
		}
	}
	
	public PeopleOrigin() {
		super();
	}
	public PeopleOrigin(long uid, int tweetsNum, int focusNum, int fansNum,
			String name, String tags, String img) {
		super();
		this.uid = uid;
		this.tweetsNum = tweetsNum;
		this.focusNum = focusNum;
		this.fansNum = fansNum;
		this.name = name;
		this.tags = tags;
		this.img = img;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("uid:"+uid+"\n");
		sb.append("tweetsNum:"+tweetsNum+"\n");
		sb.append("focusNum:"+focusNum+"\n");
		sb.append("fansNum:"+fansNum+"\n");
		sb.append("name:"+name+"\n");
		sb.append("tags:"+tags+"\n");
		sb.append("img:"+img+"\n");
		return sb.toString();
	}
	
	public int getFocusNum() {
		return focusNum;
	}
	public void setFocusNum(int focusNum) {
		this.focusNum = focusNum;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getTweetsNum() {
		return tweetsNum;
	}
	public void setTweetsNum(int tweetsNum) {
		this.tweetsNum = tweetsNum;
	}
	public int getFansNum() {
		return fansNum;
	}
	public void setFansNum(int fansNum) {
		this.fansNum = fansNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	
	

}
