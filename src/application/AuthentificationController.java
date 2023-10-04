package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class AuthentificationController {
	@FXML
	private Label msg;
	@FXML
	private PasswordField passwd;
	public static boolean isDel;
	//private GestionUtilisateursController_2 g = new GestionUtilisateursController_2();
	//private GestionUtilisateursController_3 n = new GestionUtilisateursController_3();
	//private GestionPatientsController a = new GestionPatientsController();
	private static boolean passValide;
	public static String src;
	public boolean passValide() {
		return passValide;
	}
	
	public void passDefault() {
		passValide = false;
	}
	
	// Event Listener on Button.onAction
	@FXML
	public void confirm(ActionEvent event) throws IOException {
		Sign_in_Controller m = new Sign_in_Controller();
		if (passwd.getText().equals(m.getUserPass())) {
			//if (g.isDel() || n.isDel() || a.isDel()) {
			if (isDel) {
				passValide = true;
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.close();
			}
			else {
				Parent pane = FXMLLoader.load(getClass().getResource(src));
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.getScene().setRoot(pane);
				stage.sizeToScene();
				stage.centerOnScreen();
			}
		}
		else if (passwd.getText().isBlank()) {
			msg.setText("Ce champ est obligatoire");
		}
		else {
			msg.setText("Le password est incorrect");
		}
	}
}
