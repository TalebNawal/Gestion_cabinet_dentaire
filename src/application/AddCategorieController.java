package application;

import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;

public class AddCategorieController {
	@FXML
	private Label msg;
	@FXML
	private TextField type;
	@FXML
	private TextField Prix;

	// Event Listener on Button.onAction
	@FXML
	public void save(ActionEvent event) {
		if (type.getText().isBlank() || Prix.getText().isBlank()) {
			msg.setText("Tous les champs sont obligatoires");
		}
		else {
			try {
				String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
				String username = "root";
				String password= "ayoub2001";
				Connection con = DriverManager.getConnection( host, username, password);
				Statement mystmt1 = con.createStatement();
				String query1 = "SELECT * FROM categorieintervention WHERE Type = '"+type.getText().toUpperCase()+"' AND PrixBase ="+Prix.getText();
		    	ResultSet results = mystmt1.executeQuery(query1);
		    	if (results.next()) {
		    		Alert confirm = new Alert(Alert.AlertType.INFORMATION);
			    	confirm.setHeaderText(null);
			    	confirm.setTitle("Erreur");
			    	confirm.setContentText("Cette categorie existe déjà");
			    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			    	confirm.initOwner(stage);
			    	stage.close();
			    	confirm.show();
		    	}
		    	else {
		    		String query = "INSERT INTO categorieintervention (Type, PrixBase, Archivée) VALUES ('"+type.getText().toUpperCase()+"', "+Prix.getText()+", 0)";
		    		String query2 = "UPDATE categorieintervention SET Archivée = 1 WHERE Type='"+type.getText().toUpperCase()+"'";
		    		PreparedStatement preparedStmt = con.prepareStatement(query);
		    		PreparedStatement preparedStmt2 = con.prepareStatement(query2);
			    	preparedStmt2.execute();
			    	preparedStmt.execute();
					preparedStmt.close();
					preparedStmt2.close();
					
					Alert confirm = new Alert(Alert.AlertType.INFORMATION);
			    	confirm.setHeaderText(null);
			    	confirm.setTitle("Confirmation");
			    	confirm.setContentText("Cette categorie a été ajoutée avec succès");
			    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			    	confirm.initOwner(stage);
			    	stage.close();
			    	confirm.show();
		    	}
				mystmt1.close();
				results.close();
				con.close();
			} catch (SQLIntegrityConstraintViolationException e) {
				Alert confirm = new Alert(Alert.AlertType.INFORMATION);
		    	confirm.setHeaderText(null);
		    	confirm.setTitle("Erreur");
		    	confirm.setContentText("Cette categorie existe déjà");
		    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    	confirm.initOwner(stage);
		    	stage.close();
		    	confirm.show();
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
}
