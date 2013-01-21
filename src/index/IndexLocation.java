package index;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import origin.Location;
import dao.LocationDaoImpl;

/**
 * @author omar
 *
 */
public class IndexLocation {
	IndexWriter iwriter;
	Directory dir;
	private static String indexPath="/Users/omar/project/locationEngine/index-location";
	
	public IndexLocation(){
		init();
	}
	public void init(){
		File path = new File(indexPath);
		try {
			dir = FSDirectory.open(path);
			Analyzer analyzer = new IKAnalyzer(true);
			IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_40 , analyzer);
			iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			iwriter = new IndexWriter(dir , iwConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		List<Location> locations = LocationDB.getLocations();
		for(Location location:locations){
			try {
//				System.out.println(location);
				iwriter.addDocument(location.toDocument());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			iwriter.commit();
			iwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		IndexLocation ip =new IndexLocation();
		ip.run();
	}
}

class LocationDB{
	
	public static List<Location> getLocations(){
	
		LocationDaoImpl locationDao = new LocationDaoImpl();
		List<Location> locations = locationDao.getLocations();
		locationDao.release();
		
		return locations;
		
	}
}
