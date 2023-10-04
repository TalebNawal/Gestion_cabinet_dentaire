package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
public class Radio {
	private int IDRadio;
	private String RemarquesPositives;
	private String RemarquesGenerales;
	private String RemarquesNegatives;
	private LocalDate DateRadio;
    private String CheminImage;
    private String TypeR;
    
    public Radio(int IDRadio, String RemarquesPositives, String RemarquesGenerales, String RemarquesNegatives, LocalDate DateRadio, int IDTypeRadio) {
    	setIDRadio(IDRadio);
    	setRemarquesPositives(RemarquesPositives);
    	setRemarquesGenerales(RemarquesGenerales);
    	setRemarquesNegatives(RemarquesNegatives);
    	setDateRadio(DateRadio);
    	setTypeR(IDTypeRadio);
    }
    public Radio(LocalDate DateRadio, int IDTypeRadio, String CheminImage, int IDSoin) {
    	setDateRadio(DateRadio);
    	setTypeR(IDTypeRadio);
    	setCheminImage(CheminImage);
    	setIDRadio(IDSoin, IDTypeRadio);
    }
	public int getIDRadio() {
		return IDRadio;
	}
	public void setIDRadio(int iDRadio) {
		IDRadio = iDRadio;
	}
	public void setIDRadio(int IDSoin, int IDTypeRadio) {
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT IDRadio FROM radio WHERE DateRadio = '"+DateRadio+"' AND CheminImage ='"+CheminImage+"' AND IDSoin ="+IDSoin+" AND type ="+IDTypeRadio;
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		IDRadio = results.getInt("IDRadio");
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public String getRemarquesPositives() {
		return RemarquesPositives;
	}
	public void setRemarquesPositives(String remarquesPositives) {
		RemarquesPositives = remarquesPositives;
	}
	public String getRemarquesGenerales() {
		return RemarquesGenerales;
	}
	public void setRemarquesGenerales(String remarquesGenerales) {
		RemarquesGenerales = remarquesGenerales;
	}
	public String getRemarquesNegatives() {
		return RemarquesNegatives;
	}
	public void setRemarquesNegatives(String remarquesNegatives) {
		RemarquesNegatives = remarquesNegatives;
	}
	public LocalDate getDateRadio() {
		return DateRadio;
	}
	public void setDateRadio(LocalDate dateRadio) {
		DateRadio = dateRadio;
	}
	public String getCheminImage() {
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT CheminImage FROM radio WHERE IDRadio ="+this.IDRadio;
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		return results.getString("CheminImage");
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void setCheminImage(String cheminImage) {
		CheminImage = cheminImage;
	}
	public String getTypeR() {
		return TypeR;
	}
	public void setTypeR(int IDType) {
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT Description FROM typeradio WHERE IDTypeRadio ="+IDType;
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		TypeR = results.getString("Description");
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
