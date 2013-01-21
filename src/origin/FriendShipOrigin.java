package origin;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import refined.Indexable;

public class FriendShipOrigin extends Origin implements Indexable{
	
	private long uid;
	private String focus;
	
	@Override
	public Document toDocument() {
		Document document = new Document();
		document.add(new StringField("uid",uid+"",Store.YES));
		document.add(new TextField("focus",focus,Store.YES));
		return document;
	}
	
	public static FriendShipOrigin fromDocument(Document document){
		long uid = Long.parseLong(document.get("uid"));
		String focus = document.get("focus");
		return new FriendShipOrigin(uid,focus);
	}
	public String toString(){
		return "uid:"+uid+"\nfocus:"+focus+"\n";
	}
	@Override
	public void fromLine(String line) {
//		System.out.println(line);
		String[] parts = line.split("\t");
		this.uid = Long.parseLong(parts[0]);
		if(parts.length == 2){
			this.focus = parts[1];
		}else{
			throw new NextRowException();
		}
	}
	
	public FriendShipOrigin() {
		super();
	}

	public FriendShipOrigin(long uid, String focus) {
		super();
		this.uid = uid;
		this.focus = focus;
	}

	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getFocus() {
		return focus;
	}
	public void setFocus(String focus) {
		this.focus = focus;
	}
	
	
	

}
