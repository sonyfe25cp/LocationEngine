package origin;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import refined.Indexable;

/**
 * @author ChenJie
 *
 */
public class FriendShipOrigin extends Origin implements Indexable{
	
	private long uid;
	private String friendship;
	
	@Override
	public Document toDocument() {
		Document document = new Document();
		document.add(new StringField("uid",uid+"",Store.YES));
		document.add(new StringField("friendship",friendship,Store.YES));
		return document;
	}
	
	public static FriendShipOrigin fromDocument(Document document){
		long uid = Long.parseLong(document.get("uid"));
		String focus = document.get("focus");
		return new FriendShipOrigin(uid,focus);
	}
	public String toString(){
		return "uid:"+uid+"\nfocus:"+friendship+"\n";
	}
	@Override
	public void fromLine(String line) {
//		System.out.println(line);
		String[] parts = line.split("( |\t)");
		if(parts.length == 2){
			this.friendship = parts[1];
			this.uid = Long.parseLong(parts[0]);
		}else{
			throw new NextRowException();
		}
	}
	
	public FriendShipOrigin() {
		super();
	}

	public FriendShipOrigin(long uid, String friendship) {
		super();
		this.uid = uid;
		this.friendship = friendship;
	}

	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getFriendship() {
		return friendship;
	}

	public void setFriendship(String friendship) {
		this.friendship = friendship;
	}

}
