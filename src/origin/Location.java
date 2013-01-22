package origin;

import index.VecTextField;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import refined.Indexable;

public class Location extends Origin implements Indexable{
	
	private long id;
	private String location;
	
	public Location() {
		super();
	}
	public Location(long id, String location) {
		super();
		this.id = id;
		this.location = location;
	}
	@Override
	public Document toDocument() {
		Document document = new Document();
		document.add(new StringField("id",id+"",Store.YES));
		document.add(new VecTextField("location",location,Store.YES));
		document.add(new TextField("location2",location,Store.YES));
		return document;
	}
	@Override
	public void fromLine(String line) throws NextRowException {
		
	}
	
	public static Location fromDocument(Document document){
		long id = Long.parseLong(document.get("id"));
		String location = document.get("location");
		Location l = new Location();
		l.setId(id);
		l.setLocation(location);
		return l;
	}
	public String toString(){
		return "id:"+id+"\nlocation:"+location;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	

}
