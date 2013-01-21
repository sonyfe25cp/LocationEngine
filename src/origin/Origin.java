package origin;

import org.apache.lucene.document.Document;

public abstract class Origin {

	public abstract Document toDocument();
	
	public abstract void fromLine(String line) throws NextRowException;
	
}
