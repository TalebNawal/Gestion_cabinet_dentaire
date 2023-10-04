package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class ActesMedicaux {

	private int IDSoin;
	private LocalDate DebutSoin;
	private float PrixComtabilise;
	private LocalDate FinSoin;
    private boolean EtatActe = true;
    private String StatusActe = "En cours";
	
    public ActesMedicaux(int IDSoin, LocalDate DebutSoin, float PrixComtabilise, LocalDate FinSoin, boolean EtatActe) {
    	setIDSoin(IDSoin);
    	setDebutSoin(DebutSoin);
    	setPrixComtabilise(PrixComtabilise);
    	setFinSoin(FinSoin);
    	setEtatActe(EtatActe);
    	if (isEtatActe()) {
    		setStatusActe("En cours");
    	}
    	else {
    		setStatusActe("Terminé");
    	}
    }
    
    public ActesMedicaux(LocalDate DebutSoin, String PrixComtabilise, LocalDate FinSoin) {
    	setDebutSoin(DebutSoin);
    	if (PrixComtabilise != "")
    		setPrixComtabilise(Float.parseFloat(PrixComtabilise));
    	setFinSoin(FinSoin);
    	setEtatActe();
    }
    
	public void setIDSoin(int IDSoin) {
		this.IDSoin = IDSoin;
	}
	public int getIDSoin() {
		return IDSoin;
	}
	public LocalDate getDebutSoin() {
		return DebutSoin;
	}
	public void setDebutSoin(LocalDate debutSoin) {
		DebutSoin = debutSoin;
	}
	public float getPrixComtabilise() {
		return PrixComtabilise;
	}
	public void setPrixComtabilise(float PrixComtabilise) {
		this.PrixComtabilise = PrixComtabilise;
	}
	public void setFinSoin(LocalDate finSoin) {
		FinSoin = finSoin;
	}
	public LocalDate getFinSoin() {
		return FinSoin;
	}
	public boolean isEtatActe() {
		return EtatActe;
	}
	public void setEtatActe(boolean etatActe) {
		EtatActe = etatActe;
	}
	public void setEtatActe() {
		if (FinSoin != null && FinSoin.isBefore(LocalDate.now())) {
			EtatActe = false;
		}
	}
	public String getStatusActe() {
		return StatusActe;
	}
	public void setStatusActe(String StatusActe) {
		this.StatusActe = StatusActe;
	}
	public LocalDate getlastInter() throws SQLException {
		String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
		String username = "root";
		String password= "ayoub2001";
		Connection con = DriverManager.getConnection( host, username, password);
		Statement mystmt = con.createStatement();
    	String query = "SELECT MAX(DateReelle) FROM Intervention WHERE IDSoin = "+IDSoin+" AND EtatRV = 'passée'";
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
    	String query = "SELECT MIN(DatePrevue) FROM Intervention WHERE IDSoin = "+IDSoin+" AND EtatRV = 'prévue' AND DatePrevue >= "+LocalDate.now();
        ResultSet results = mystmt.executeQuery(query);
    	if (results.next() && results.getDate("MIN(DatePrevue)") != null) {
    		return results.getDate("MIN(DatePrevue)").toLocalDate();
    	}
    	else {
    		results.close();
    	}
		con.close();
		mystmt.close();
		return null;
	}
}