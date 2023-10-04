package application;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.File;
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

import javafx.scene.control.ComboBox;

import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;

public class AddRadioController {
	@FXML
	private ComboBox<String> type;
	@FXML
	private Button chooseFile;
	@FXML
	private Label choosedFile;
	@FXML
	private DatePicker date;
	@FXML
	private TextArea RmqG;
	@FXML
	private TextArea RmqP;
	@FXML
	private TextArea RmqN;
	@FXML
	private Label msg;
	private File file = null;

	@FXML
	private void initialize() {
		type.getItems().addAll(
	            "Radiographie rétro-coronaire (Bite-wing)",
	            "Radiographie périapicale",
	            "Radiographie occlusale",
	            "Radiographie panoramique",
	            "Radiographie céphalométrique",
	            "Radiographie à faisceau conique (Cone Beam)"
	    );
	}
	
	// Event Listener on Button.onAction
	@FXML
	public void save(ActionEvent event) {
		if (type.getValue() == null || date.getValue() == null || file == null) {
			msg.setText("Le type, la date et l'image sont obligatoires");
		}
		else {
			try {
				TypeRadio typeradio = new TypeRadio(type.getValue());
				String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
				String username = "root";
				String password= "ayoub2001";
				Connection con = DriverManager.getConnection( host, username, password);
				String path = file.getAbsolutePath().replace('\\', '/');
				String query = "INSERT INTO radio (DateRadio, CheminImage, type, IDSoin) VALUES ('"+date.getValue()+"', '"+path+"', "+typeradio.getIDTypeRadio()+", "+Info_PatientController.acteClicked.getIDSoin()+")";
				PreparedStatement preparedStmt = con.prepareStatement(query);
			    preparedStmt.execute();
				preparedStmt.close();
				
				Radio r = new Radio(date.getValue(), typeradio.getIDTypeRadio(), path, Info_PatientController.acteClicked.getIDSoin());
				if(!RmqG.getText().isBlank()) {
					String query1 = "UPDATE radio SET RemarquesGenerales = '"+RmqG.getText()+"' WHERE IDRadio = "+r.getIDRadio();
					PreparedStatement preparedStmt1 = con.prepareStatement(query1);
				    preparedStmt1.execute();
					preparedStmt1.close();
				}
				
				if(!RmqP.getText().isBlank()) {
					String query1 = "UPDATE radio SET RemarquesPositives = '"+RmqP.getText()+"' WHERE IDRadio = "+r.getIDRadio();
					PreparedStatement preparedStmt1 = con.prepareStatement(query1);
				    preparedStmt1.execute();
					preparedStmt1.close();
				}
				
				if(!RmqN.getText().isBlank()) {
					String query1 = "UPDATE radio SET RemarquesNegatives = '"+RmqN.getText()+"' WHERE IDRadio = "+r.getIDRadio();
					PreparedStatement preparedStmt1 = con.prepareStatement(query1);
				    preparedStmt1.execute();
					preparedStmt1.close();
				}
				
				con.close();
					
				Alert confirm = new Alert(Alert.AlertType.INFORMATION);
		    	confirm.setHeaderText(null);
		    	confirm.setTitle("Confirmation");
		    	confirm.setContentText("Le radio a été ajouté avec succès");
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
	// Event Listener on Button[#chooseFile].onAction
	@FXML
	public void choose(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choisir une image");
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			choosedFile.setText(file.getName());
        }
		else {
			choosedFile.setText("Aucun fichier choisi");
		}
	}
}
