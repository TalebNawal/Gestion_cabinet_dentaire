package application;

import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

public class EditCouvertureController {
	@FXML
	private Label msg;
	@FXML
	private ComboBox<String> type;
	private boolean existe = false;
	private String Couv = null;
	
	@FXML
	private void initialize() {
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT TypeCouverture FROM couverturemedicale WHERE IDPatient ="+GestionPatientsController.patientClicked.getIDPatient();
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		existe = true;
	    		Couv = results.getString("TypeCouverture");
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Couv != null) {
			type.getSelectionModel().select(Couv);
		}
		type.getItems().addAll(
	            "FAR",
	            "CNOPS",
	            "CNSS",
	            "RAMED",
	            "AUTRE"
	        );
		
	}

	@FXML
	public void save(ActionEvent event) throws SQLException {
		if (type.getValue() == null) {
			msg.setText("Veuillez choisir le type");
		}
		else {
			try {
				if (!existe) {
					String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
					String username = "root";
					String password= "ayoub2001";
					Connection con = DriverManager.getConnection( host, username, password);
					Statement mystmt = con.createStatement();
					String query = "INSERT INTO couverturemedicale (TypeCouverture, IDPatient) VALUES ('"+type.getValue()+"', "+GestionPatientsController.patientClicked.getIDPatient()+")";
			    	PreparedStatement preparedStmt = con.prepareStatement(query);
			    	preparedStmt.execute();
					con.close();
					mystmt.close();
					preparedStmt.close();
				}
				else {
					String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
					String username = "root";
					String password= "ayoub2001";
					Connection con = DriverManager.getConnection( host, username, password);
					Statement mystmt = con.createStatement();
					String query = "UPDATE couverturemedicale SET TypeCouverture ='"+type.getValue()+"' WHERE IDPatient = "+GestionPatientsController.patientClicked.getIDPatient();
			    	PreparedStatement preparedStmt = con.prepareStatement(query);
			    	preparedStmt.execute();
					con.close();
					mystmt.close();
					preparedStmt.close();
				}
				Alert confirm = new Alert(Alert.AlertType.INFORMATION);
		    	confirm.setHeaderText(null);
		    	confirm.setTitle("Confirmation");
		    	confirm.setContentText("La mise à jour a été effectuée avec succès");
		    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    	confirm.initOwner(stage);
		    	stage.close();
		    	confirm.show();		    	
			} catch (SQLException e) {
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
