package application;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;

import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import javafx.scene.control.RadioButton;

import javafx.scene.control.DatePicker;

public class addPatientController {
	@FXML
	private TextField nom;
	@FXML
	private TextField cin;
	@FXML
	private TextField prenom;
	@FXML
	private TextField tel;
	@FXML
	private DatePicker date;
	@FXML
	private RadioButton homme;
	@FXML
	private ToggleGroup Gr;
	@FXML
	private RadioButton femme;
	@FXML
	private DatePicker DateDebut;
	@FXML
	private ComboBox<String> Categorie;
	@FXML
	private Label msg;

	@FXML
	private void initialize() {
		List<String> optionsCat = new ArrayList<>();
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT Type FROM categorieintervention WHERE Archivée = 0";
	    	ResultSet results = mystmt.executeQuery(query);
	    	while (results.next()) {
	    		optionsCat.add(results.getString("Type"));
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		Categorie.setItems(FXCollections.observableArrayList(optionsCat));
	}
	
	public char sexeSelected() {
		if (homme.isSelected()) {
			return 'M';
		}
		else if (femme.isSelected()) {
			return 'F';
		}
		return ' ';
	}
	
	public static boolean existCIN(String cin) {
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT CIN FROM patients WHERE UPPER(CIN) ='"+cin.toUpperCase()+"'";
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		return true;
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@FXML
	public void save(ActionEvent event) {
		if (nom.getText().isBlank() || prenom.getText().isBlank() || cin.getText().isBlank() || tel.getText().isBlank() || date.getValue().equals(null) || sexeSelected() == ' ' || DateDebut.getValue().equals(null) || Categorie.getValue() == null) {
			msg.setText("Tous les champs sont obligatoires");
		}
		else if (existCIN(cin.getText())) {
			msg.setText("La CIN existe déjà");
		}
		else {
			msg.setText(null);
			Patient patient = new Patient(date.getValue(), cin.getText().toUpperCase(), nom.getText().toUpperCase(), prenom.getText().toUpperCase(), sexeSelected(), tel.getText());
			
			try {
				String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
				String username = "root";
				String password= "ayoub2001";
				Connection con = DriverManager.getConnection( host, username, password);
				String query = "INSERT INTO patients (DateNaissance, CIN, Nom, Prenom, Sexe, Telephone) VALUES ('"+patient.getDateNaissance()+"', '"+patient.getCIN()+"', '"+patient.getNom()+"', '"+patient.getPrenom()+"', '"+patient.getSexe()+"', '"+patient.getTel()+"')";
		    	PreparedStatement preparedStmt = con.prepareStatement(query);
		    	preparedStmt.execute();
				preparedStmt.close();
				
				if (Categorie.getValue() != null) {
					if(DateDebut.getValue() == null) {
						msg.setText("Il faut entrer une date debut");
					}
				}
				
				if (DateDebut.getValue() != null) {
					if(Categorie.getValue() == null) {
						msg.setText("Il faut choisir une categorie");
					}
					else {
						Statement mystmt1 = con.createStatement();
						String query2 = "SELECT IDPatients FROM patients WHERE CIN = '"+patient.getCIN()+"'";
				    	ResultSet results = mystmt1.executeQuery(query2);
				    	int idPat = 0;
				    	if (results.next()) {
				    		idPat = results.getInt("IDPatients");
				    	}
						mystmt1.close();
						results.close();
						if (idPat != 0) {
							String query1 = "INSERT INTO actemedical (DebutSoin, IDPatient, EtatActe) VALUES ('"+DateDebut.getValue()+"', "+idPat+", 1)";
							PreparedStatement preparedStmt1 = con.prepareStatement(query1);
					    	preparedStmt1.execute();
							preparedStmt1.close();
							
							int idSoin = 0;
							Statement mystmt4 = con.createStatement();
							String query4 = "SELECT IDSoin FROM actemedical WHERE DebutSoin = '"+DateDebut.getValue()+"' AND IDPatient ="+idPat;
					    	ResultSet results4 = mystmt4.executeQuery(query4);
					    	if (results4.next()) {
					    		idSoin = results4.getInt("IDSoin");
					    	}
							mystmt4.close();
							results4.close();
							if (idSoin != 0) {
					    		CategorieIntervention cat = new CategorieIntervention(Categorie.getValue());
								String query3 ="INSERT INTO intervention (DatePrevue, EtatRV, IDSoin, IDCategorie) VALUES ('"+DateDebut.getValue()+"', 'prévue', "+idSoin+", "+cat.getIDCategorie()+")";
						    	PreparedStatement preparedStmt2 = con.prepareStatement(query3);
						    	preparedStmt2.execute();
								con.close();
								preparedStmt2.close();
							}
						}
					}
				}
				
				Alert confirm = new Alert(Alert.AlertType.INFORMATION);
		    	confirm.setHeaderText(null);
		    	confirm.setTitle("Confirmation");
		    	confirm.setContentText("le nouveau patient a été créé avec succès");
		    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    	confirm.initOwner(stage);
		    	stage.close();
		    	confirm.show();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	// Event Listener on Button.onAction
	@FXML
	public void cancel(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}
}
