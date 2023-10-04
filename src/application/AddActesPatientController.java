package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddActesPatientController {
	@FXML
	private DatePicker DateDebut;
	@FXML
	private ComboBox<String> categorie;
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
		categorie.setItems(FXCollections.observableArrayList(optionsCat));
	}
	
	@FXML
	public void save(ActionEvent event) {

			if (DateDebut.getValue() == null || categorie.getValue() == null) {
				msg.setText("Tous les champs sont obligatoires");
			}
			else {
				try {
					String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
					String username = "root";
					String password= "ayoub2001";
					Connection con = DriverManager.getConnection( host, username, password);
					Statement mystmt3 = con.createStatement();
					String query3 = "SELECT IDSoin FROM actemedical WHERE DebutSoin = '"+DateDebut.getValue()+"' AND IDPatient ="+GestionPatientsController.patientClicked.getIDPatient();
			    	ResultSet results3 = mystmt3.executeQuery(query3);
			    	if (results3.next()) {
			    		Alert confirm = new Alert(Alert.AlertType.INFORMATION);
				    	confirm.setHeaderText(null);
				    	confirm.setTitle("Erreur");
				    	confirm.setContentText("Cet acte existe déjà");
				    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				    	confirm.initOwner(stage);
				    	stage.close();
				    	confirm.show();
			    	}
			    	else {
			    		CategorieIntervention cat = new CategorieIntervention(categorie.getValue());
						Intervention interv = new Intervention(0,null,DateDebut.getValue(),"prévue",cat.getIDCategorie());
						ActesMedicaux acte = new ActesMedicaux(DateDebut.getValue(), "", null);
						String query;
						if (acte.isEtatActe())
							query = "INSERT INTO actemedical (DebutSoin, PrixComptabilise, IDPatient, EtatActe) VALUES ('"+acte.getDebutSoin()+"', NULL, "+GestionPatientsController.patientClicked.getIDPatient()+", 1)";
						else
							query = "INSERT INTO actemedical (DebutSoin, PrixComptabilise, IDPatient, EtatActe) VALUES ('"+acte.getDebutSoin()+"', NULL, "+GestionPatientsController.patientClicked.getIDPatient()+", 0)";
						PreparedStatement preparedStmt = con.prepareStatement(query);
				    	preparedStmt.execute();
						preparedStmt.close();
					
						Statement mystmt1 = con.createStatement();
						String query1 = "SELECT IDSoin FROM actemedical WHERE DebutSoin = '"+acte.getDebutSoin()+"' AND IDPatient ="+GestionPatientsController.patientClicked.getIDPatient();
				    	ResultSet results = mystmt1.executeQuery(query1);
				    	if (results.next()) {
				    		acte.setIDSoin(results.getInt("IDSoin"));
				    	}
						mystmt1.close();
						results.close();
						
						String query2 ="INSERT INTO intervention (DatePrevue, EtatRV, IDSoin, IDCategorie) VALUES ('"+interv.getDatePrevue()+"', '"+interv.getEtatRV()+"', "+acte.getIDSoin()+", "+cat.getIDCategorie()+")";
						PreparedStatement preparedStmt1 = con.prepareStatement(query2);
						preparedStmt1.execute();
				    	preparedStmt1.close();
						con.close();
						
						Alert confirm = new Alert(Alert.AlertType.INFORMATION);
				    	confirm.setHeaderText(null);
				    	confirm.setTitle("Confirmation");
				    	confirm.setContentText("Cet acte a été ajouté avec succès");
				    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				    	confirm.initOwner(stage);
				    	stage.close();
				    	confirm.show();
			    	}
			    	mystmt3.close();
			    	results3.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
	}
	
	@FXML
	public void cancel(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}
	
	@FXML
	public void addCat(ActionEvent event) throws IOException {
		Stage popupwindow=new Stage();	
		popupwindow.setResizable(false);
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("Ajout d'une categorie");
		Parent popup = FXMLLoader.load(getClass().getResource("AddCategorie.fxml"));
		Scene scene1= new Scene(popup);
		popupwindow.setScene(scene1);
		popupwindow.centerOnScreen();
		popupwindow.showAndWait();
			
		while (true) {
			if (!popupwindow.isShowing()) {
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
				categorie.setItems(FXCollections.observableArrayList(optionsCat));
				break;
			}
		}
	}
}
