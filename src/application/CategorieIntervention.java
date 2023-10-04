package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CategorieIntervention {
	private int IDCategorie;
	private String Type;
	private float PrixBase;
	
	public CategorieIntervention(String type) {
		setType(type);
		setIDCategorie(type);
		setPrixBase(type);
	}
	
	public void setIDCategorie(String type) {
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT IDCategorie FROM categorieintervention WHERE Archivée = 0 AND Type = '"+type+"'";
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		IDCategorie = results.getInt("IDCategorie");
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setIDCategorie(int IDCategorie) {
		this.IDCategorie = IDCategorie;
	}
	public int getIDCategorie() {
		return IDCategorie;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getType() {
		return Type;
	}
	public void setPrixBase(float PrixBase) {
		this.PrixBase = PrixBase;
	}
	public void setPrixBase(String type) {
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT PrixBase FROM categorieintervention WHERE Archivée = 0 AND Type = '"+type+"'";
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		PrixBase = results.getFloat("PrixBase");
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public float getPrixBase() {
		return PrixBase;
	}
}