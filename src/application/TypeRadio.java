package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TypeRadio {
	private int IDTypeRadio;
	private String Description;
	
	public TypeRadio(String type) {
		setDescription(type);
		setIDTypeRadio(type);
	}
	public void setIDTypeRadio(int IDTypeRadio) {
		this.IDTypeRadio = IDTypeRadio;
	}
	public void setIDTypeRadio(String type) {
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT IDTypeRadio FROM typeradio WHERE Description = '"+type+"'";
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		this.IDTypeRadio = results.getInt("IDTypeRadio");
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public int getIDTypeRadio() {
		return IDTypeRadio;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getDescription() {
		return Description;
	}
}