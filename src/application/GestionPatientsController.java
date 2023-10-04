package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestionPatientsController extends MenuDentiste{
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
	private TextField newNom;
	@FXML
	private TextField newPrenom;
	@FXML
	private TextField newCIN;
	@FXML
	private TextField newTel;
	@FXML
	private DatePicker newDateNaiss;
	@FXML
	private DatePicker DateDebut;
	@FXML
	private ComboBox<String> Categorie;
	@FXML
	private Button AddCat;
	@FXML
	private PasswordField adminPass;
	@FXML
	private ToggleGroup Gr2;
	@FXML
	private RadioButton homme;
	@FXML
	private RadioButton femme;
	@FXML
	private Label addMsg;
	private ObservableList<Patient> patientsList = FXCollections.observableArrayList();
	
	public static Patient patientSel;
	public static Patient patientClicked;

	@FXML
	protected void initialize() {
		super.initialize();

		patientID.setCellValueFactory(new PropertyValueFactory<>("IDPatient"));
		patientNom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
		patientPrenom.setCellValueFactory(new PropertyValueFactory<>("Prenom"));
		patientCIN.setCellValueFactory(new PropertyValueFactory<>("CIN"));
		patientSexe.setCellValueFactory(new PropertyValueFactory<>("Sexe"));
		patientTel.setCellValueFactory(new PropertyValueFactory<>("Tel"));
		patientNaiss.setCellValueFactory(new PropertyValueFactory<>("DateNaissance"));
		
		refreshTable();
		
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
	
	public char sexeSelected() {
		if (homme.isSelected()) {
			return 'M';
		}
		else if (femme.isSelected()) {
			return 'F';
		}
		return ' ';
	}
	
	/*public boolean isDel() {
		return deleteSel;
	}*/
	
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
						    				results.getString("Telephone")));
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		patientsData.setItems(patientsList);
		//progresswindow.close();
		
	}

	@FXML
	public void edit(ActionEvent event) throws IOException {
		patientSel = patientsData.getSelectionModel().getSelectedItem();
		if (patientSel != null) {
			AuthentificationController.isDel = false;
			AuthentificationController.src = "PatientDetails.fxml";
			Stage popupwindow=new Stage();	
			popupwindow.setResizable(false);
			popupwindow.initModality(Modality.APPLICATION_MODAL);
			popupwindow.setTitle("Authentification");
			Parent popup = FXMLLoader.load(getClass().getResource("Authentification.fxml"));
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
		else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Erreur");
			alert.setContentText("sélectionner le patient à supprimer");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			alert.initOwner(stage);
			alert.show();
		}
	}

	@FXML
	public void delete(ActionEvent event) throws IOException {		
		Patient patientSelected = patientsData.getSelectionModel().getSelectedItem();
		if (patientSelected != null) {			
			//deleteSel = true;
		
			AuthentificationController.isDel = true;
			Stage popupwindow=new Stage();	
			popupwindow.setResizable(false);
			popupwindow.initModality(Modality.APPLICATION_MODAL);
			popupwindow.setTitle("Authentification");
			Parent popup = FXMLLoader.load(getClass().getResource("Authentification.fxml"));
			Scene scene1= new Scene(popup);
			popupwindow.setScene(scene1);
			popupwindow.centerOnScreen();
			popupwindow.showAndWait();
				
			AuthentificationController g = new AuthentificationController();
			while (true) {
				if (!popupwindow.isShowing()) {
					if (g.passValide()) {
						g.passDefault();
						try {
							String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
							String username = "root";
							String password= "ayoub2001";
							Connection con = DriverManager.getConnection( host, username, password);
							Statement mystmt = con.createStatement();
							String query = "DELETE FROM patients WHERE IDPatients ="+patientSelected.getIDPatient();
					    	PreparedStatement preparedStmt = con.prepareStatement(query);
					    	preparedStmt.execute();
							con.close();
							mystmt.close();
							preparedStmt.close();
							
							Alert confirm = new Alert(Alert.AlertType.INFORMATION);
					    	confirm.setHeaderText(null);
					    	confirm.setTitle("Confirmation");
					    	confirm.setContentText("Ce patient a été supprimé avec succès");
					    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					    	confirm.initOwner(stage);
					    	confirm.show();
							refreshTable();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
					else {
						break;
					}
				}
			}
		}	
		else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Erreur");
			alert.setContentText("sélectionner le patient à supprimer");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			alert.initOwner(stage);
			alert.show();
		}
	}

	@FXML
	public void add(ActionEvent event) {
		Sign_in_Controller m = new Sign_in_Controller();
		if (newNom.getText().isBlank() || newPrenom.getText().isBlank() || newCIN.getText().isBlank() || newTel.getText().isBlank() || newDateNaiss.getValue() == null || sexeSelected() == ' ' || adminPass.getText().isBlank()) {
			addMsg.setText("Tous les champs (*) sont obligatoires");
		}
		else if (!adminPass.getText().equals(m.getUserPass())) {
			addMsg.setText("Le password de l'administrateur est invalide");
		}
		else if (existCIN(newCIN.getText())) {
			addMsg.setText("La CIN existe déjà");
		}
		else {
			addMsg.setText(null);
			Patient patient = new Patient(newDateNaiss.getValue(), newCIN.getText().toUpperCase(), newNom.getText().toUpperCase(), newPrenom.getText().toUpperCase(), sexeSelected(), newTel.getText());
			
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
						addMsg.setText("L'acte medical n'a pas été créer");
					}
				}
				
				if (DateDebut.getValue() != null) {
					if(Categorie.getValue() == null) {
						addMsg.setText("L'acte medical n'a pas été créer");
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
		    	confirm.show();
				refreshTable();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    	refreshTable();
	    	
	    	newNom.clear();
	    	newPrenom.clear();
	    	newDateNaiss.setValue(null);
	    	homme.setSelected(false);
	    	femme.setSelected(false);
	    	newCIN.clear();
	    	newTel.clear();
	    	adminPass.clear();
		}
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
				Categorie.setItems(FXCollections.observableArrayList(optionsCat));
				break;
			}
		}
	}
	
	@FXML
	public void info(MouseEvent event) throws IOException {
		if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            Node node = ((Node) event.getTarget()).getParent();
            TableRow<?> row;
            if (node instanceof TableRow) {
                row = (TableRow<?>) node;
            } 
            else {
                // clicking on text part
                row = (TableRow<?>) node.getParent();
            }
            patientClicked = (Patient) row.getItem();
            
            Main main = new Main();
    		main.changeScene("Info_Patient.fxml");
        }
	}
}
