package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Intervention {

	private int IDIntervention;
	private LocalDate DateReelle;
	private LocalDate DatePrevue;
    private String EtatRV;
    private String Categorie;
    private float Prix;
    
    public Intervention (int IDIntervention, LocalDate DateReelle, LocalDate DatePrevue, String EtatRV, int IDCategorie) {
    	setIDIntervention(IDIntervention);
    	setDateReelle(DateReelle);
    	setDatePrevue(DatePrevue);
    	setEtatRV(EtatRV);   
    	setCategorie(IDCategorie);
    	setPrix(IDCategorie);
    }
    
	public void setIDIntervention(int IDIntervention) {
		this.IDIntervention = IDIntervention;
	}
	public int getIDIntervention() {
		return IDIntervention;
	}
	public LocalDate getDateReelle() {
		return DateReelle;
	}
	public void setDateReelle(LocalDate dateReelle) {
		DateReelle = dateReelle;
	}
	public void setDatePrevue(LocalDate datePrevue) {
		DatePrevue = datePrevue;
	}
	public LocalDate getDatePrevue() {
		return DatePrevue;
	}
	public String getEtatRV() {
		return EtatRV;
	}
	public void setEtatRV(String etatRV) {
		EtatRV = etatRV;
	}
	public void setCategorie(int IDCategorie) {
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT Type FROM categorieintervention WHERE IDCategorie ="+IDCategorie;
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		Categorie = results.getString("Type");
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getCategorie() {
		return Categorie;
	}
	public void setPrix(int IDCategorie) {
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT PrixBase FROM categorieintervention WHERE IDCategorie ="+IDCategorie;
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		Prix = results.getFloat("PrixBase");
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public float getPrix() {
		return Prix;
	}
}