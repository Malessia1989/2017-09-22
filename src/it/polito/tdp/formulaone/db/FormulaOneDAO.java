package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;

import it.polito.tdp.formulaone.model.Adiacenza;
import it.polito.tdp.formulaone.model.Race;
import it.polito.tdp.formulaone.model.Season;

public class FormulaOneDAO {

	public List<Season> getAllSeasons() {
		String sql = "SELECT year, url FROM seasons ORDER BY year";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			List<Season> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Season(rs.getInt("year"), rs.getString("url")));
			}
			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Race> getRacesByYear (Season year ,Map<Integer, Race> idMap ){
		String sql="select distinct r.raceId id, r.name name " + 
				"from races r " + 
				"where r.year =?  ";
		
		List<Race> list = new ArrayList<>();
		
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year.getYear());
			ResultSet rs = st.executeQuery();
		
			while (rs.next()) {
				if(idMap.get(rs.getInt("id")) ==null) {
					
					Race r= new Race(rs.getInt("id") ,rs.getString("name"));
		
					list.add(r);
					idMap.put(r.getRaceId(), r);
				}else {
					list.add(idMap.get(rs.getInt("id")));
				}
				
			}
			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Adiacenza> getArchiePeso(Season annoInput, Map<Integer, Race> idMap) {
		String sql="select ra1.raceId id1, ra1.name name1, ra2.raceId id2, ra2.name name2, count(*) as peso " + 
				"from results r1, results r2, races ra1, races ra2 " + 
				"where r1.statusId =1 and r2.statusId =1 " + 
				"and r1.raceId > r2.raceId " + 
				"and r1.driverId = r2.driverId " + 
				"and ra1.year=? and ra2.year=? " + 
				"and r1.raceId=ra1.raceId and r2.raceId=ra2.raceId " + 
				"group by r1.raceId, r2.raceId ";
		
		List<Adiacenza> adj = new ArrayList<>();
		
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, annoInput.getYear());
			st.setInt(2, annoInput.getYear());
			ResultSet rs = st.executeQuery();
		
			while (rs.next()) {
				Race r1= idMap.get(rs.getInt("id1"));
				Race r2=idMap.get(rs.getInt("id2"));
				double peso=rs.getDouble("peso");
				
			
				
				if( r1!= null && r2 != null) {
					Adiacenza a= new Adiacenza(r1, r2, peso);
					adj.add(a);
				}
			}
			conn.close();
			return adj;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	
	}

}
