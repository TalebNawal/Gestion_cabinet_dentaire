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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Info_ActeController extends MenuDentiste{
	@FXML
	private Label id;
	@FXML
	private Label nomPatient;
	@FXML
	private Label debutSoin;
	@FXML
	private Label FinSoin;
	@FXML
	private Label prixComp;
	@FXML
	private Label EtatActe;
	@FXML
	private Label lastInter;
	@FXML
	private Label nextInter;
	@FXML
	private ImageView editacte;
	@FXML
	private ImageView delete;
	@FXML
	private ImageView save;
	@FXML
	private ComboBox<String> EtatCombo;
	@FXML
	private TableView<Radio> radioData;
	@FXML
	private TableColumn<Radio, Integer> radioID;
	@FXML
	private TableColumn<Radio, String> radioType;
	@FXML
	private TableColumn<Radio, Date> radioDate;
	private ObservableList<Radio> radiosList = FXCollections.observableArrayList();
	@FXML
	private TableView<Intervention> intervsData;
	@FXML
	private TableColumn<Intervention, Integer> IntervID;
	@FXML
	private TableColumn<Intervention, Date> IntervCat;
	@FXML
	private TableColumn<Intervention, String> IntervPrevue;
	@FXML
	private TableColumn<Intervention, String> IntervReelle;
	@FXML
	private TableColumn<Intervention, String> IntervPrix;
	@FXML
	private TableColumn<Intervention, String> IntervEtat;
	private ObservableList<Intervention> intervsList = FXCollections.observableArrayList();
	public static Intervention intervSelected;
	public static Radio radioClicked;
	
	@FXML
	protected void initialize() {
		super.initialize();
		id.setText(String.valueOf(Info_PatientController.acteClicked.getIDSoin()));
		nomPatient.setText(String.valueOf(GestionPatientsController.patientClicked.getNom())+" "+String.valueOf(GestionPatientsController.patientClicked.getPrenom()));
		debutSoin.setText(String.valueOf(Info_PatientController.acteClicked.getDebutSoin()));
		if (Info_PatientController.acteClicked.getFinSoin() != null){
			FinSoin.setText(String.valueOf(Info_PatientController.acteClicked.getFinSoin()));
		}
		prixComp.setText(String.valueOf(Info_PatientController.acteClicked.getPrixComtabilise())+" DH");
		EtatActe.setText(String.valueOf(Info_PatientController.acteClicked.getStatusActe()));
		try {
			if (Info_PatientController.acteClicked.getlastInter() != null){
				lastInter.setText(String.valueOf(Info_PatientController.acteClicked.getlastInter()));
			}
			if (Info_PatientController.acteClicked.getnextInter() != null){
				nextInter.setText(String.valueOf(Info_PatientController.acteClicked.getnextInter()));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		EtatCombo.getItems().clear();
		EtatCombo.getItems().addAll(
	            "En Cours",
	            "Terminé"
	    );
		
		radioID.setCellValueFactory(new PropertyValueFactory<>("IDRadio"));
		radioDate.setCellValueFactory(new PropertyValueFactory<>("DateRadio"));
		radioType.setCellValueFactory(new PropertyValueFactory<>("TypeR"));
		
		refreshTableRadio();
		
		IntervID.setCellValueFactory(new PropertyValueFactory<>("IDIntervention"));
		IntervCat.setCellValueFactory(new PropertyValueFactory<>("Categorie"));
		IntervPrevue.setCellValueFactory(new PropertyValueFactory<>("DatePrevue"));
		IntervReelle.setCellValueFactory(new PropertyValueFactory<>("DateReelle"));
		IntervPrix.setCellValueFactory(new PropertyValueFactory<>("Prix"));
		IntervEtat.setCellValueFactory(new PropertyValueFactory<>("EtatRV"));
		
		refreshTableInterv();
	}
	
	private void refreshTableRadio(){
		radiosList.clear();
		
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT * FROM radio WHERE IDSoin ="+Info_PatientController.acteClicked.getIDSoin();
	    	ResultSet results = mystmt.executeQuery(query);
	    	while (results.next()) {
	    			radiosList.add(new Radio(results.getInt("IDRadio"),
	    									results.getString("RemarquesPositives"),
	    									results.getString("RemarquesGenerales"),
	    									results.getString("RemarquesNegatives"),
	    									results.getDate("DateRadio").toLocalDate(),
	    									results.getInt("type")));
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		radioData.setItems(radiosList);		
	}
	
	private void refreshTableInterv(){
		intervsList.clear();
		
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT * FROM intervention WHERE IDSoin ="+Info_PatientController.acteClicked.getIDSoin();
	    	ResultSet results = mystmt.executeQuery(query);
	    	while (results.next()) {
	    		if (results.getDate("DateReelle") != null)
	    			intervsList.add(new Intervention(results.getInt("IDIntervention"),
		    									results.getDate("DateReelle").toLocalDate(),
		    									results.getDate("DatePrevue").toLocalDate(),
		    									results.getString("EtatRV"),
		    									results.getInt("IDCategorie")));
	    		else
	    			intervsList.add(new Intervention(results.getInt("IDIntervention"),
							null,
							results.getDate("DatePrevue").toLocalDate(),
							results.getString("EtatRV"),
							results.getInt("IDCategorie")));
	    	}
			con.close();
			mystmt.close();
			results.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		intervsData.setItems(intervsList);		
	}
	
	@FXML
	public void back(MouseEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("Info_Patient.fxml");
	}
	
	@FXML
	public void editActe(MouseEvent event) throws IOException {
		editacte.setVisible(false);
		delete.setVisible(false);
		EtatActe.setVisible(false);
		save.setVisible(true);
		EtatCombo.setVisible(true);
		if (Info_PatientController.acteClicked.isEtatActe())
			EtatCombo.getSelectionModel().select(0);
		else
			EtatCombo.getSelectionModel().select(1);
	}
	
	@FXML
	public void saveEdit(MouseEvent event) throws IOException, SQLException {
		if (EtatCombo.getValue() != Info_PatientController.acteClicked.getStatusActe()) {
			if (Info_PatientController.acteClicked.isEtatActe()) {
				try {
					String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
					String username = "root";
					String password= "ayoub2001";
					Connection con = DriverManager.getConnection( host, username, password);
					String query;
					if (Info_PatientController.acteClicked.getlastInter() != null)
						query = "UPDATE actemedical SET EtatActe = 0 , FinSoin = '"+Info_PatientController.acteClicked.getlastInter()+"' WHERE IDSoin = "+Info_PatientController.acteClicked.getIDSoin();
		    		else
						query = "UPDATE actemedical SET EtatActe = 0 WHERE IDSoin = "+Info_PatientController.acteClicked.getIDSoin();
					PreparedStatement preparedStmt = con.prepareStatement(query);
			    	preparedStmt.execute();
					preparedStmt.close();
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Info_PatientController.acteClicked.setEtatActe(false);
				Info_PatientController.acteClicked.setStatusActe("Terminé");
				Info_PatientController.acteClicked.setFinSoin(Info_PatientController.acteClicked.getlastInter());
				
			}
			else {
				try {
					String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
					String username = "root";
					String password= "ayoub2001";
					Connection con = DriverManager.getConnection( host, username, password);
		    		String query = "UPDATE actemedical SET EtatActe = 1 , FinSoin = NULL WHERE IDSoin = "+Info_PatientController.acteClicked.getIDSoin();
		    		PreparedStatement preparedStmt = con.prepareStatement(query);
			    	preparedStmt.execute();
					preparedStmt.close();
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Info_PatientController.acteClicked.setEtatActe(true);
				Info_PatientController.acteClicked.setStatusActe("En cours");
				Info_PatientController.acteClicked.setFinSoin(null);

			}
			EtatActe.setText(String.valueOf(Info_PatientController.acteClicked.getStatusActe()));
			if (Info_PatientController.acteClicked.getFinSoin() != null){
				FinSoin.setText(String.valueOf(Info_PatientController.acteClicked.getFinSoin()));
			}
			else {
				FinSoin.setText("_____________");
			}
		}
		editacte.setVisible(true);
		delete.setVisible(true);
		EtatActe.setVisible(true);
		save.setVisible(false);
		EtatCombo.setVisible(false);
	}

	@FXML
	public void deleteActe(MouseEvent event) throws IOException {
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
    		String query = "DELETE FROM intervention WHERE IDSoin = "+Info_PatientController.acteClicked.getIDSoin();
    		String query0 = "DELETE FROM radio WHERE IDSoin = "+Info_PatientController.acteClicked.getIDSoin();
    		String query1 = "DELETE FROM actemedical WHERE IDSoin = "+Info_PatientController.acteClicked.getIDSoin();
    		PreparedStatement preparedStmt = con.prepareStatement(query);
	    	preparedStmt.execute();
			preparedStmt.close();
			PreparedStatement preparedStmt0 = con.prepareStatement(query0);
	    	preparedStmt0.execute();
			preparedStmt0.close();
			PreparedStatement preparedStmt1 = con.prepareStatement(query1);
	    	preparedStmt1.execute();
			preparedStmt1.close();
			con.close();
			
			Alert confirm = new Alert(Alert.AlertType.INFORMATION);
	    	confirm.setHeaderText(null);
	    	confirm.setTitle("Confirmation");
	    	confirm.setContentText("Cette acte a été supprimé avec succès");
	    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	confirm.initOwner(stage);
	    	confirm.show();
	    	
	    	Main main = new Main();
    		main.changeScene("Info_Patient.fxml");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void addRadio (ActionEvent event) throws IOException {
		Stage popupwindow=new Stage();	
		popupwindow.setResizable(false);
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("Radio");
		Parent popup = FXMLLoader.load(getClass().getResource("AddRadio.fxml"));
		Scene scene1= new Scene(popup);
		popupwindow.setScene(scene1);
		popupwindow.centerOnScreen();
		popupwindow.showAndWait();
			
		while (true) {
			if (!popupwindow.isShowing()) {
				refreshTableRadio();
				break;
			}
		}
	}

	@FXML
	public void addInterv (ActionEvent event) throws IOException {
		Stage popupwindow=new Stage();	
		popupwindow.setResizable(false);
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("Intervention");
		Parent popup = FXMLLoader.load(getClass().getResource("AddIntervention.fxml"));
		Scene scene1= new Scene(popup);
		popupwindow.setScene(scene1);
		popupwindow.centerOnScreen();
		popupwindow.showAndWait();
			
		while (true) {
			if (!popupwindow.isShowing()) {
				refreshTableInterv();
				this.initialize();
				break;
			}
		}
	}
	
	@FXML
	public void deleteInterv (ActionEvent event) throws IOException {
		intervSelected = null;
		intervSelected = intervsData.getSelectionModel().getSelectedItem();
		if (intervSelected != null) {			
			try {
				String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
				String username = "root";
				String password= "ayoub2001";
				Connection con = DriverManager.getConnection( host, username, password);
				Statement mystmt = con.createStatement();
				String query = "DELETE FROM intervention WHERE IDIntervention ="+intervSelected.getIDIntervention();
		    	PreparedStatement preparedStmt = con.prepareStatement(query);
		    	preparedStmt.execute();
				con.close();
				mystmt.close();
				preparedStmt.close();
							
				Alert confirm = new Alert(Alert.AlertType.INFORMATION);
		    	confirm.setHeaderText(null);
		    	confirm.setTitle("Confirmation");
		    	confirm.setContentText("Cette intervention a été supprimé avec succès");
		    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    	confirm.initOwner(stage);
		    	confirm.show();
				refreshTableInterv();
				this.initialize();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Erreur");
			alert.setContentText("sélectionner l'intervention à supprimer");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			alert.initOwner(stage);
			alert.show();
		}
	}
	
	@FXML
	public void editInterv (ActionEvent event) throws IOException {
		intervSelected = null;
		intervSelected = intervsData.getSelectionModel().getSelectedItem();
		if (intervSelected != null) {			
			Stage popupwindow=new Stage();	
			popupwindow.setResizable(false);
			popupwindow.initModality(Modality.APPLICATION_MODAL);
			popupwindow.setTitle("Intervention");
			Parent popup = FXMLLoader.load(getClass().getResource("EditIntervention.fxml"));
			Scene scene1= new Scene(popup);
			popupwindow.setScene(scene1);
			popupwindow.centerOnScreen();
			popupwindow.showAndWait();
							
			while (true) {
				if (!popupwindow.isShowing()) {
					refreshTableInterv();
					this.initialize();
					break;
				}
			}
		}
		else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Erreur");
			alert.setContentText("sélectionner l'intervention à modifier");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			alert.initOwner(stage);
			alert.show();
		}
	}
	
	@FXML
	public void infoRadio(MouseEvent event) throws IOException {
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
            radioClicked = (Radio) row.getItem();
            
            Main main = new Main();
    		main.changeScene("Info_Radio.fxml");
        }
	}
}
