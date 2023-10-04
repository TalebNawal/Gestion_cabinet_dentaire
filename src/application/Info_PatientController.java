package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Info_PatientController extends MenuDentiste{
	@FXML
	private Label id;
	@FXML
	private Label cin;
	@FXML
	private Label nom;
	@FXML
	private Label prenom;
	@FXML
	private Label tel;
	@FXML
	private Label sexe;
	@FXML
	private Label dateNaiss;
	@FXML
	private Label lastInter;
	@FXML
	private Label nextInter;
	@FXML
	private Label IDCouverture;
	@FXML
	private Label type;
	@FXML
	private TableView<ActesMedicaux> actesData;
	@FXML
	private TableColumn<ActesMedicaux, Integer> acteID;
	@FXML
	private TableColumn<ActesMedicaux, Date> acteDebut;
	@FXML
	private TableColumn<ActesMedicaux, Float> actePrix;
	@FXML
	private TableColumn<ActesMedicaux, Date> acteFin;
	@FXML
	private TableColumn<ActesMedicaux, String> EtatActe;
	private ObservableList<ActesMedicaux> actesList = FXCollections.observableArrayList();
	public static ActesMedicaux acteClicked;
	
	@FXML
	protected void initialize() {
		super.initialize();
		id.setText(String.valueOf(GestionPatientsController.patientClicked.getIDPatient()));
		cin.setText(String.valueOf(GestionPatientsController.patientClicked.getCIN()));
		nom.setText(String.valueOf(GestionPatientsController.patientClicked.getNom()));
		prenom.setText(String.valueOf(GestionPatientsController.patientClicked.getPrenom()));
		tel.setText(String.valueOf(GestionPatientsController.patientClicked.getTel()));
		sexe.setText(String.valueOf(GestionPatientsController.patientClicked.getSexe()));
		dateNaiss.setText(String.valueOf(GestionPatientsController.patientClicked.getDateNaissance()));
		try {
			if (GestionPatientsController.patientClicked.getlastInter() != null){
				lastInter.setText(String.valueOf(GestionPatientsController.patientClicked.getlastInter()));
			}
			if (GestionPatientsController.patientClicked.getnextInter() != null){
				nextInter.setText(String.valueOf(GestionPatientsController.patientClicked.getnextInter()));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT idCouverture, TypeCouverture FROM couverturemedicale WHERE IDPatient ="+GestionPatientsController.patientClicked.getIDPatient();
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		IDCouverture.setText(String.valueOf(results.getInt("idCouverture")));
	    		type.setText(results.getString("TypeCouverture"));
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		acteID.setCellValueFactory(new PropertyValueFactory<>("IDSoin"));
		acteDebut.setCellValueFactory(new PropertyValueFactory<>("DebutSoin"));
		actePrix.setCellValueFactory(new PropertyValueFactory<>("PrixComtabilise"));
		acteFin.setCellValueFactory(new PropertyValueFactory<>("FinSoin"));
		EtatActe.setCellValueFactory(new PropertyValueFactory<>("StatusActe"));
		
		refreshTable();
	}
	
	private void refreshTable(){
		actesList.clear();
		
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT * FROM actemedical WHERE IDPatient ="+GestionPatientsController.patientClicked.getIDPatient();
	    	ResultSet results = mystmt.executeQuery(query);
	    	while (results.next()) {
	    		if (results.getDate("FinSoin") == null)
	    			actesList.add(new ActesMedicaux(results.getInt("IDSoin"),
	    										results.getDate("DebutSoin").toLocalDate(),
	    										results.getFloat("PrixComptabilise"),
	    										null,
	    										results.getBoolean("EtatActe")));
	    		else
	    			actesList.add(new ActesMedicaux(results.getInt("IDSoin"),
							results.getDate("DebutSoin").toLocalDate(),
							results.getFloat("PrixComptabilise"),
							results.getDate("FinSoin").toLocalDate(),
							results.getBoolean("EtatActe")));
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		actesData.setItems(actesList);		
	}
	
	@FXML
	public void add(ActionEvent event) throws IOException {
		Stage popupwindow=new Stage();	
		popupwindow.setResizable(false);
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("Ajout d'un acte medical");
		Parent popup = FXMLLoader.load(getClass().getResource("AddActesPatient.fxml"));
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
	public void edit(MouseEvent event) throws IOException {
		Stage popupwindow=new Stage();	
		popupwindow.setResizable(false);
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("Couverture medicale");
		Parent popup = FXMLLoader.load(getClass().getResource("EditCouverture.fxml"));
		Scene scene1= new Scene(popup);
		popupwindow.setScene(scene1);
		popupwindow.centerOnScreen();
		popupwindow.showAndWait();
			
		while (true) {
			if (!popupwindow.isShowing()) {
				try {
					String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
					String username = "root";
					String password= "ayoub2001";
					Connection con = DriverManager.getConnection( host, username, password);
					Statement mystmt = con.createStatement();
					String query = "SELECT idCouverture, TypeCouverture FROM couverturemedicale WHERE IDPatient ="+GestionPatientsController.patientClicked.getIDPatient();
			    	ResultSet results = mystmt.executeQuery(query);
			    	if (results.next()) {
			    		IDCouverture.setText(String.valueOf(results.getInt("idCouverture")));
			    		type.setText(results.getString("TypeCouverture"));
			    	}
					con.close();
					mystmt.close();
					results.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	}	
	
	@FXML
	public void infoActe(MouseEvent event) throws IOException {
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
            acteClicked = (ActesMedicaux) row.getItem();
            
            Main main = new Main();
    		main.changeScene("Info_Acte.fxml");
        }
	}
}
