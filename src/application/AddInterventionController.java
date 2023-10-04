package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import javafx.scene.control.DatePicker;

public class AddInterventionController {
	@FXML
	private DatePicker DatePrevue;
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
	// Event Listener on Button.onAction
	@FXML
	public void save(ActionEvent event) {
		if (DatePrevue.getValue() == null || categorie.getValue() == null) {
			msg.setText("Tous les champs sont obligatoires");
		}
		else {
			try {
				String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
				String username = "root";
				String password= "ayoub2001";
				Connection con = DriverManager.getConnection( host, username, password);
				CategorieIntervention cat = new CategorieIntervention(categorie.getValue());
				/*
				Statement mystmt3 = con.createStatement();
				String query3 = "SELECT IDIntervention FROM intervention WHERE DatePrevue = '"+DatePrevue.getValue()+"' AND EtatRV ='prévue' AND IDSoin = "+Info_PatientController.acteClicked.getIDSoin()+" AND IDCategorie ="+cat.getIDCategorie();
		    	ResultSet results3 = mystmt3.executeQuery(query3);
		    	if (results3.next()) {
		    		Alert confirm = new Alert(Alert.AlertType.INFORMATION);
			    	confirm.setHeaderText(null);
			    	confirm.setTitle("Erreur");
			    	confirm.setContentText("Cette intervention existe déjà");
			    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			    	confirm.initOwner(stage);
			    	stage.close();
			    	confirm.show();
		    	}
		    	else {*/
					String query2 ="INSERT INTO intervention (DatePrevue, EtatRV, IDSoin, IDCategorie) VALUES ('"+DatePrevue.getValue()+"', 'prévue', "+Info_PatientController.acteClicked.getIDSoin()+", "+cat.getIDCategorie()+")";
					PreparedStatement preparedStmt1 = con.prepareStatement(query2);
					preparedStmt1.execute();
			    	preparedStmt1.close();
					con.close();
						
					Alert confirm = new Alert(Alert.AlertType.INFORMATION);
			    	confirm.setHeaderText(null);
			    	confirm.setTitle("Confirmation");
			    	confirm.setContentText("Cette intervention a été ajouté avec succès");
			    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			    	confirm.initOwner(stage);
			    	stage.close();
			    	confirm.show();
		    	//}
		    	//mystmt3.close();
		    	//results3.close();
		    	con.close();
			} catch(SQLException e) {
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
	// Event Listener on Button.onAction
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
