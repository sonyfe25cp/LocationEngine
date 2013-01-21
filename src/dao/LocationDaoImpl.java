package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import origin.Location;

public class LocationDaoImpl extends BaseDaoImpl {

	public List<Location> getLocations() {
		String sql = "select * from locations;";
		List<Location> locations = new ArrayList<Location>();
		try {
			PreparedStatement psmt = conn.prepareStatement(sql);
			ResultSet rs = psmt.executeQuery();
			Location location=null;
			while(rs.next()){
				location = new Location();
				long id = rs.getLong("id");
				String locationValue = rs.getString("location");
				location.setId(id);
				location.setLocation(locationValue);
				locations.add(location);
			}
			rs.close();
			psmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locations;
	}

	public static void main(String[] args) {

	}

}
