package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;

import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(Map<Integer,Food> idMap){
		String sql = "SELECT * FROM food" ;
		List<Food> cibi = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				
					if(!idMap.containsKey(res.getInt("food_code"))){
					Food f =new Food(res.getInt("food_code"),
							res.getString("display_name")
							);
					
					idMap.put(f.getFood_code(), f);
					cibi.add(f);
					
					}	
					else {
						cibi.add(idMap.get(res.getInt("food_code")));
					}
			
			  }
			
			
			conn.close();
		return cibi;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	public List<Food> getVertici(Integer porzioni,Map<Integer,Food> idMap){
		String sql ="SELECT p1.food_code,f1.display_name " + 
				"FROM `portion` p1, `portion` p2, food f1  " + 
				"WHERE p1.portion_id= p2.portion_id  AND p1.food_code= f1.food_code " + 
				"GROUP BY p1.food_code  " + 
				"HAVING COUNT(DISTINCT(p1.portion_id))=?";
		List<Food> vertex = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(!idMap.containsKey(res.getInt("food_code"))) {
					Food f =new Food(res.getInt("food_code"),
							res.getString("display_name")
							);
					
					idMap.put(f.getFood_code(), f);
					vertex.add(f);
					
					}	
					else {
						vertex.add(idMap.get(res.getInt("food_code")));
					}
			}
			conn.close();
			return vertex;
		}catch (SQLException e) {
				e.printStackTrace();
				return null ;
			}
		
	}
	
	public List<Adiacenza> getAdiacenze(Map<Integer,Food> idMap){
		String sql = "SELECT fc1.food_code, fc2.food_code, AVG(c1.condiment_calories) as peso " + 
				"FROM food_condiment AS fc1, food_condiment AS fc2, condiment AS c1 " + 
				"WHERE fc1.condiment_code= fc2.condiment_code AND fc1.id != fc2.id " + 
				"AND c1.condiment_code = fc1.condiment_code AND fc1.food_code > fc2.food_code  " + 
				"GROUP BY fc1.food_code, fc2.food_code" ;
				
		List<Adiacenza> adiacenze = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
			   
				adiacenze.add(new Adiacenza (idMap.get(res.getInt("fc1.food_code")),idMap.get(res.getInt("fc2.food_code")), res.getDouble("peso")));
			}
			conn.close();
			return adiacenze;
			
		}catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
}
