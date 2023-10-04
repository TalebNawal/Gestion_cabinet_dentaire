package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PatientDetailsController {
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
	private RadioButton femme;
	@FXML
	private Label msg;

	@FXML
	private void initialize() {
		nom.setText(GestionPatientsController.patientSel.getNom());
		cin.setText(GestionPatientsController.patientSel.getCIN());
		prenom.setText(GestionPatientsController.patientSel.getPrenom());
		tel.setText(GestionPatientsController.patientSel.getTel());
		date.setValue(GestionPatientsController.patientSel.getDateNaissance());
		if (GestionPatientsController.patientSel.getSexe()=='M')
			homme.setSelected(true);
		else
			femme.setSelected(true);
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
	
	@FXML
	public void save(ActionEvent event) {
		if (nom.getText().toUpperCase().equals(GestionPatientsController.patientSel.getNom()) && 
			prenom.getText().toUpperCase().equals(GestionPatientsController.patientSel.getPrenom()) &&
			cin.getText().toUpperCase().equals(GestionPatientsController.patientSel.getCIN()) &&
			tel.getText().equals(GestionPatientsController.patientSel.getTel()) &&
			date.getValue().equals(GestionPatientsController.patientSel.getDateNaissance()) &&
			GestionPatientsController.patientSel.getSexe()==sexeSelected()) {
			cancel(event);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
		}
		else if (nom.getText().isBlank() || 
				prenom.getText().isBlank() || 
				cin.getText().isBlank() || 
				tel.getText().isBlank() || 
				date.getValue() == null || 
				(!homme.isSelected() &&
				!femme.isSelected())) {
			msg.setText("Tous les champs sont obligatoires");
		}
		else if (GestionPatientsController.existCIN(cin.getText()) && !cin.getText().equals(GestionPatientsController.patientSel.getCIN())) {
			msg.setText("La CIN existe déjà");
		}
		else {
			try {
				String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
				String username = "root";
				String password= "ayoub2001";
				Connection con = DriverManager.getConnection( host, username, password);
				Statement mystmt = con.createStatement();
				String query = "UPDATE patients SET Nom='"+nom.getText()+"', Prenom='"+prenom.getText()+"', CIN='"+cin.getText()+"', Telephone='"+tel.getText()+"', DateNaissance='"+date.getValue()+"', Sexe='"+sexeSelected()+"' WHERE IDPatients="+GestionPatientsController.patientSel.getIDPatient();
		    	PreparedStatement preparedStmt = con.prepareStatement(query);
		    	preparedStmt.execute();
				con.close();
				mystmt.close();
				preparedStmt.close();
				
				Alert confirm = new Alert(Alert.AlertType.INFORMATION);
		    	confirm.setHeaderText(null);
		    	confirm.setTitle("Confirmation");
		    	confirm.setContentText("Ce patient a été modifié avec succès");
		    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    	confirm.initOwner(stage);
		    	confirm.show();
		    	
				stage.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}

	@FXML
	public void cancel(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}
}
