package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;

public class Patient {
	private int IDPatient;
	private LocalDate DateNaissance;
	private String CIN;
	private String Nom;
	private String Prenom;
	private char Sexe;
	private String Tel;
	private LocalDate NextInter;
	private LocalDate DateInter;
	
	public Patient(int ID ,LocalDate DateNaissance, String cin, String nom, String prenom, char sexe, String tel) {
		setIDPatient(ID);
		setDateNaissance(DateNaissance);
		setCIN(cin);
		setNom(nom);
		setPrenom(prenom);
		setSexe(sexe);
		setTel(tel);
	}
	
	public Patient(int ID ,LocalDate DateNaissance, String cin, String nom, String prenom, char sexe, String tel, LocalDate DateInter) {
		setIDPatient(ID);
		setDateNaissance(DateNaissance);
		setCIN(cin);
		setNom(nom);
		setPrenom(prenom);
		setSexe(sexe);
		setTel(tel);
		setDateInter(DateInter);
	}
	
	public Patient(LocalDate DateNaissance, String cin, String nom, String prenom, char sexe, String tel) {
		setDateNaissance(DateNaissance);
		setCIN(cin);
		setNom(nom);
		setPrenom(prenom);
		setSexe(sexe);
		setTel(tel);
		
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
	    	String query = "SELECT IDPatients FROM patients WHERE CIN = '"+cin.toUpperCase()+"'";
	        ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		setIDPatient(results.getInt("IDPatients"));
	    		results.close();
	    	}
	    	else {
	    		results.close();
	    	}
			con.close();
			mystmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getIDPatient() {
		return IDPatient;
	}
	public void setIDPatient(int ID){
		this.IDPatient = ID;
	}
	public LocalDate getDateNaissance() {
		return DateNaissance;
	}
	public void setDateNaissance(LocalDate dateNaissance) {
		DateNaissance = dateNaissance;
	}
	public LocalDate getDateInter() {
		return DateInter;
	}
	public void setDateInter(LocalDate DateInter) {
		this.DateInter = DateInter;
	}
	public String getCIN() {
		return CIN;
	}
	public void setCIN(String CIN) {
		this.CIN = CIN;
	}
	public String getNom() {
		return Nom;
	}
	public void setNom(String nom) {
		Nom = nom;
	}
	public String getPrenom() {
		return Prenom;
	}
	public void setPrenom(String prenom) {
		Prenom = prenom;
	}
	public char getSexe() {
		return Sexe;
	}
	public void setSexe(char sexe) {
		Sexe = sexe;
	}
	public String getTel() {
		return Tel;
	}
	public void setTel(String tel) {
		Tel = tel;
	}
	public LocalDate getlastInter() throws SQLException {
		String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
		String username = "root";
		String password= "ayoub2001";
		Connection con = DriverManager.getConnection( host, username, password);
		Statement mystmt = con.createStatement();
    	String query = "SELECT MAX(DateReelle) FROM Intervention I JOIN ActeMedical A ON I.IDSoin=A.IDSoin WHERE IDPatient = "+IDPatient+" AND EtatRV = 'passée'";
        ResultSet results = mystmt.executeQuery(query);
    	if (results.next() && results.getDate("MAX(DateReelle)") != null) {
    		return results.getDate("MAX(DateReelle)").toLocalDate();
    	}
    	else {
    		results.close();
    	}
		con.close();
		mystmt.close();
		return null;
	}
	public LocalDate getnextInter() throws SQLException {
		String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
		String username = "root";
		String password= "ayoub2001";
		Connection con = DriverManager.getConnection( host, username, password);
		Statement mystmt = con.createStatement();
    	String query = "SELECT MIN(DatePrevue) FROM Intervention I JOIN ActeMedical A ON I.IDSoin=A.IDSoin WHERE IDPatient = "+IDPatient+" AND EtatRV = 'prévue' AND DatePrevue >= "+LocalDate.now();
        ResultSet results = mystmt.executeQuery(query);
    	if (results.next() && results.getDate("MIN(DatePrevue)") != null) {
    		NextInter = results.getDate("MIN(DatePrevue)").toLocalDate();
    	}
    	else {
    		results.close();
    	}
		con.close();
		mystmt.close();
		return NextInter;
	}
	
}
