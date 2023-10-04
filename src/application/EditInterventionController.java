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

public class EditInterventionController {
	@FXML
	private Label msg;
	@FXML
	private Label id;
	@FXML
	private ComboBox<String> cat;
	@FXML
	private DatePicker DatePrevue;
	@FXML
	private DatePicker DateReelle;
	
	@FXML
	private void initialize() {
		id.setText(String.valueOf(Info_ActeController.intervSelected.getIDIntervention()));
		cat.setValue(Info_ActeController.intervSelected.getCategorie());
		DatePrevue.setValue(Info_ActeController.intervSelected.getDatePrevue());
		DateReelle.setValue(Info_ActeController.intervSelected.getDateReelle());

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
		cat.setItems(FXCollections.observableArrayList(optionsCat));
	}
	
	// Event Listener on Button.onAction
	@FXML
	public void save(ActionEvent event) {
		boolean changed = false;
		if (DateReelle.getValue() != null && DatePrevue.getValue().isAfter(DateReelle.getValue())) {
			msg.setText("Informations incorrectes");
		}
		else {
			try {
				String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
				String username = "root";
				String password= "ayoub2001";
				Connection con = DriverManager.getConnection( host, username, password);
				CategorieIntervention categ = new CategorieIntervention(cat.getValue());
				if (DateReelle.getValue() != null && Info_ActeController.intervSelected.getDateReelle() == null) {
					Statement mystmt = con.createStatement();
					String query = "SELECT PrixComptabilise FROM actemedical WHERE IDSoin ="+Info_PatientController.acteClicked.getIDSoin();
			    	ResultSet results = mystmt.executeQuery(query);
			    	float cout = 0;
			    	if (results.next()) {
			    		cout = results.getFloat("PrixComptabilise");
			    	}
					mystmt.close();
					results.close();
					cout+=Info_ActeController.intervSelected.getPrix();
					String query2 ="UPDATE actemedical SET PrixComptabilise = "+cout+" WHERE IDSoin = "+Info_PatientController.acteClicked.getIDSoin();
					PreparedStatement preparedStmt1 = con.prepareStatement(query2);
					preparedStmt1.execute();
			    	preparedStmt1.close();
					changed = true;
					Info_PatientController.acteClicked.setPrixComtabilise(cout);
				}
				if (DateReelle.getValue() != Info_ActeController.intervSelected.getDateReelle()) {
					String query2 ="UPDATE intervention SET DateReelle = '"+DateReelle.getValue()+"', EtatRV = 'passée' WHERE IDIntervention = "+Info_ActeController.intervSelected.getIDIntervention();
					PreparedStatement preparedStmt1 = con.prepareStatement(query2);
					preparedStmt1.execute();
			    	preparedStmt1.close();
					con.close();
					changed = true;
				}
				if (DatePrevue.getValue() != Info_ActeController.intervSelected.getDatePrevue()) {
					String query2 ="UPDATE intervention SET DatePrevue = '"+DatePrevue.getValue()+"' WHERE IDIntervention = "+Info_ActeController.intervSelected.getIDIntervention();
					PreparedStatement preparedStmt1 = con.prepareStatement(query2);
					preparedStmt1.execute();
			    	preparedStmt1.close();
					con.close();
					changed = true;
				}
				if (cat.getValue() != Info_ActeController.intervSelected.getCategorie()) {
					String query2 ="UPDATE intervention SET IDCategorie = '"+categ.getIDCategorie()+"' WHERE IDIntervention = "+Info_ActeController.intervSelected.getIDIntervention();
					PreparedStatement preparedStmt1 = con.prepareStatement(query2);
					preparedStmt1.execute();
			    	preparedStmt1.close();
					con.close();
					changed = true;
				}
				
				if (changed) {
					Alert confirm = new Alert(Alert.AlertType.INFORMATION);
			    	confirm.setHeaderText(null);
			    	confirm.setTitle("Confirmation");
			    	confirm.setContentText("Cette intervention a été ajouté avec succès");
			    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			    	confirm.initOwner(stage);
			    	stage.close();
			    	confirm.show();
			    	con.close();
				}
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
				cat.setItems(FXCollections.observableArrayList(optionsCat));
				break;
			}
		}
	}
}
