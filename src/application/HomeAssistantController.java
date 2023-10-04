package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeAssistantController {
	@FXML
	private Label time;
	@FXML
	private Label name;
	@FXML
	private Circle circle;
	@FXML
	private TableView<Patient> patientsData;
	@FXML
	private TableColumn<Patient, Integer> patientID;
	@FXML
	private TableColumn<Patient, String> patientNom;
	@FXML
	private TableColumn<Patient, String> patientPrenom;
	@FXML
	private TableColumn<Patient, String> patientCIN;
	@FXML
	private TableColumn<Patient, Character> patientSexe;
	@FXML
	private TableColumn<Patient, String> patientTel;
	@FXML
	private TableColumn<Patient, Date> patientNaiss;
	@FXML
	private TableColumn<Patient, Date> PatientNextInterv;
	private ObservableList<Patient> patientsList = FXCollections.observableArrayList();
	
	@FXML
	private void initialize() {
		// Afficher et mettre à jour la date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		Thread thread = new Thread(() -> {
			while (true) {
				LocalDateTime timenow = LocalDateTime.now();
				Platform.runLater(() -> {
					time.setText(dtf.format(timenow));
				});
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
		thread.start();
		
		Sign_in_Controller m = new Sign_in_Controller();
		name.setText(m.getUserName().toUpperCase());
		
		try {
			circle.setFill(new ImagePattern(new Image(getClass().getResource("profile.jpg").toURI().toString(),false)));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	
		patientID.setCellValueFactory(new PropertyValueFactory<>("IDPatient"));
		patientNom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
		patientPrenom.setCellValueFactory(new PropertyValueFactory<>("Prenom"));
		patientCIN.setCellValueFactory(new PropertyValueFactory<>("CIN"));
		patientSexe.setCellValueFactory(new PropertyValueFactory<>("Sexe"));
		patientTel.setCellValueFactory(new PropertyValueFactory<>("Tel"));
		patientNaiss.setCellValueFactory(new PropertyValueFactory<>("DateNaissance"));
		PatientNextInterv.setCellValueFactory(new PropertyValueFactory<>("DateInter"));
		
		refreshTable();
	}
	
	public LocalDate getnextInter(int IDPatient) throws SQLException {
		String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
		String username = "root";
		String password= "ayoub2001";
		Connection con = DriverManager.getConnection( host, username, password);
		Statement mystmt = con.createStatement();
    	String query = "SELECT MIN(DatePrevue) FROM Intervention I JOIN ActeMedical A ON I.IDSoin=A.IDSoin WHERE IDPatient = "+IDPatient+" AND EtatRV = 'prévue' AND DatePrevue >= "+LocalDate.now();
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
	
	private void refreshTable(){
		patientsList.clear();
		
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT * FROM patients";
	    	ResultSet results = mystmt.executeQuery(query);
	    	while (results.next()) {
	    		patientsList.add(new Patient(results.getInt("IDPatients"),
	    									results.getDate("DateNaissance").toLocalDate(),
						    				results.getString("CIN"),
						    				results.getString("Nom"),
						    				results.getString("Prenom"),
						    				results.getString("Sexe").charAt(0),
						    				results.getString("Telephone"),
						    				getnextInter(results.getInt("IDPatients"))));
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		patientsData.setItems(patientsList);		
	}
	
	@FXML
	public void add(ActionEvent event) throws IOException {
		Stage popupwindow=new Stage();	
		popupwindow.setResizable(false);
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("Patient");
		Parent popup = FXMLLoader.load(getClass().getResource("addPatient.fxml"));
		Scene scene1= new Scene(popup);
		popupwindow.setScene(scene1);
		popupwindow.centerOnScreen();
		popupwindow.showAndWait();
		
		while (true) {
			if (!popupwindow.isShowing()) {
				refreshTable();
				break;
			}
		}
	}
	
	@FXML
	public void SeDeconnecter(ActionEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("Sign_in.fxml");
	}
}
