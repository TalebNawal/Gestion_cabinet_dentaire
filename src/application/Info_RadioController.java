package application;

import javafx.fxml.FXML;

import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;

import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Info_RadioController extends MenuDentiste{
	@FXML
	private Label id;
	@FXML
	private Label dateRadio;
	@FXML
	private ImageView editDate;
	@FXML
	private ImageView delete;
	@FXML
	private ImageView save;
	@FXML
	private ImageView saveG;
	@FXML
	private ImageView saveP;
	@FXML
	private ImageView saveN;
	@FXML
	private DatePicker dateChoice;
	@FXML
	private ImageView imgRadio;
	@FXML
	private ImageView editRmqG;
	@FXML
	private ImageView editRmqP;
	@FXML
	private ImageView editRmqN;
	@FXML
	private Label rmqG;
	@FXML
	private Label rmqP;
	@FXML
	private Label rmqN;
	@FXML
	private TextArea GText;
	@FXML
	private TextArea PText;
	@FXML
	private TextArea NText;

	@FXML
	protected void initialize() {
		super.initialize();
		id.setText(String.valueOf(Info_ActeController.radioClicked.getIDRadio()));
		dateRadio.setText(String.valueOf(Info_ActeController.radioClicked.getDateRadio()));
		dateChoice.setValue(Info_ActeController.radioClicked.getDateRadio());
		GText.setText(Info_ActeController.radioClicked.getRemarquesGenerales());
		PText.setText(Info_ActeController.radioClicked.getRemarquesPositives());
		NText.setText(Info_ActeController.radioClicked.getRemarquesNegatives());
		Image img = new Image(Info_ActeController.radioClicked.getCheminImage());
		imgRadio.setImage(img);
		//imgRadio.minHeight(200);
		imgRadio.setFitHeight(230);
		//imgRadio.maxHeight(560);
		//imgRadio.maxWidth(750);
		imgRadio.setPreserveRatio(true);
		rmqG.setText(Info_ActeController.radioClicked.getRemarquesGenerales());
		rmqP.setText(Info_ActeController.radioClicked.getRemarquesPositives());
		rmqN.setText(Info_ActeController.radioClicked.getRemarquesNegatives());
	}
	@FXML
	public void editDt(MouseEvent event) {
		editDate.setVisible(false);
		delete.setVisible(false);
		dateRadio.setVisible(false);
		save.setVisible(true);
		dateChoice.setVisible(true);
	}

	@FXML
	public void saveEdit(MouseEvent event) throws IOException, SQLException {
		if (dateChoice.getValue() != Info_ActeController.radioClicked.getDateRadio()) {
			try {
				String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
				String username = "root";
				String password= "ayoub2001";
				Connection con = DriverManager.getConnection( host, username, password);
				String query = "UPDATE radio SET DateRadio = '"+dateChoice.getValue()+"' WHERE IDRadio ="+Info_ActeController.radioClicked.getIDRadio();
				PreparedStatement preparedStmt = con.prepareStatement(query);
		    	preparedStmt.execute();
				preparedStmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Info_ActeController.radioClicked.setDateRadio(dateChoice.getValue());				
			dateRadio.setText(String.valueOf(Info_ActeController.radioClicked.getDateRadio()));
		}
		editDate.setVisible(true);
		delete.setVisible(true);
		dateRadio.setVisible(true);
		save.setVisible(false);
		dateChoice.setVisible(false);
	}
	
	@FXML
	public void deleteRadio(MouseEvent event) throws IOException {
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
    		String query0 = "DELETE FROM radio WHERE IDRadio = "+Info_ActeController.radioClicked.getIDRadio();
			PreparedStatement preparedStmt0 = con.prepareStatement(query0);
	    	preparedStmt0.execute();
			preparedStmt0.close();
			con.close();
			
			Alert confirm = new Alert(Alert.AlertType.INFORMATION);
	    	confirm.setHeaderText(null);
	    	confirm.setTitle("Confirmation");
	    	confirm.setContentText("Ce radio a été supprimé avec succès");
	    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	confirm.initOwner(stage);
	    	confirm.show();
	    	
	    	Main main = new Main();
    		main.changeScene("Info_Acte.fxml");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void editG(MouseEvent event) {
		editRmqG.setVisible(false);
		rmqG.setVisible(false);
		saveG.setVisible(true);
		GText.setVisible(true);
	}
	
	@FXML
	public void saveRqG(MouseEvent event) throws IOException, SQLException {
		if (GText.getText() != Info_ActeController.radioClicked.getRemarquesGenerales()) {
			try {
				String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
				String username = "root";
				String password= "ayoub2001";
				Connection con = DriverManager.getConnection( host, username, password);
				String query = "UPDATE radio SET RemarquesGenerales = '"+GText.getText()+"' WHERE IDRadio ="+Info_ActeController.radioClicked.getIDRadio();
				PreparedStatement preparedStmt = con.prepareStatement(query);
		    	preparedStmt.execute();
				preparedStmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Info_ActeController.radioClicked.setRemarquesGenerales(GText.getText());				
			rmqG.setText(String.valueOf(Info_ActeController.radioClicked.getRemarquesGenerales()));
			GText.setText(Info_ActeController.radioClicked.getRemarquesGenerales());
		}
		editRmqG.setVisible(true);
		rmqG.setVisible(true);
		saveG.setVisible(false);
		GText.setVisible(false);
	}
	
	@FXML
	public void editP(MouseEvent event) {
		editRmqP.setVisible(false);
		rmqP.setVisible(false);
		saveP.setVisible(true);
		PText.setVisible(true);
	}
	
	@FXML
	public void saveRqP(MouseEvent event) throws IOException, SQLException {
		if (PText.getText() != Info_ActeController.radioClicked.getRemarquesPositives()) {
			try {
				String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
				String username = "root";
				String password= "ayoub2001";
				Connection con = DriverManager.getConnection( host, username, password);
				String query = "UPDATE radio SET RemarquesPositives = '"+PText.getText()+"' WHERE IDRadio ="+Info_ActeController.radioClicked.getIDRadio();
				PreparedStatement preparedStmt = con.prepareStatement(query);
		    	preparedStmt.execute();
				preparedStmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Info_ActeController.radioClicked.setRemarquesPositives(PText.getText());				
			rmqP.setText(String.valueOf(Info_ActeController.radioClicked.getRemarquesPositives()));
			PText.setText(Info_ActeController.radioClicked.getRemarquesPositives());
		}
		editRmqP.setVisible(true);
		rmqP.setVisible(true);
		saveP.setVisible(false);
		PText.setVisible(false);
	}
	
	@FXML
	public void editN(MouseEvent event) {
		editRmqN.setVisible(false);
		rmqN.setVisible(false);
		saveN.setVisible(true);
		NText.setVisible(true);
	}
	
	@FXML
	public void saveRqN(MouseEvent event) throws IOException, SQLException {
		if (NText.getText() != Info_ActeController.radioClicked.getRemarquesNegatives()) {
			try {
				String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
				String username = "root";
				String password= "ayoub2001";
				Connection con = DriverManager.getConnection( host, username, password);
				String query = "UPDATE radio SET RemarquesNegatives = '"+NText.getText()+"' WHERE IDRadio ="+Info_ActeController.radioClicked.getIDRadio();
				PreparedStatement preparedStmt = con.prepareStatement(query);
		    	preparedStmt.execute();
				preparedStmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Info_ActeController.radioClicked.setRemarquesNegatives(NText.getText());				
			rmqN.setText(String.valueOf(Info_ActeController.radioClicked.getRemarquesNegatives()));
			NText.setText(Info_ActeController.radioClicked.getRemarquesNegatives());
		}
		editRmqN.setVisible(true);
		rmqN.setVisible(true);
		saveN.setVisible(false);
		NText.setVisible(false);
	}
	
	@FXML
	public void image(MouseEvent event) throws IOException, SQLException {
		File file = new File(Info_ActeController.radioClicked.getCheminImage());
		Desktop.getDesktop().open(file);
	}
	
	@FXML
	public void back(MouseEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("Info_Acte.fxml");
	}
}
