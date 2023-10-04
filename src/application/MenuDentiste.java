package application;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class MenuDentiste {
	@FXML
	private Label time;
	@FXML
	private Circle circle;
	@FXML
	private ToggleButton Accueil_button;
	@FXML
	private ToggleButton users_button;
	@FXML
	private ToggleButton patients_button;
	@FXML
	private Label name;

	@FXML
	protected void initialize() {
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
		
		// Afficher la photo par defaut
		try {
			circle.setFill(new ImagePattern(new Image(getClass().getResource("profile.jpg").toURI().toString(),false)));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		// Afficher le nom du dentiste
		Sign_in_Controller m = new Sign_in_Controller();
		if (Sign_in_Controller.isD) {
			name.setText("D. "+m.getUserName().toUpperCase());
		}
		
		if(!m.isAdmin() && m.isD) {
			users_button.setText("Informations personnelles");
		}
	}
	
	public void SeDeconnecter(ActionEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("Sign_in.fxml");
	}
	
	public void Accueil(ActionEvent event) throws IOException {
        if(!Accueil_button.isSelected()){
        	Accueil_button.setSelected(true);
        }
        else {
        	Main main = new Main();
    		main.changeScene("HomeDentiste.fxml");
        }
	}
	
	public void users(ActionEvent event) throws IOException {
        if(!users_button.isSelected()){
        	users_button.setSelected(true);
        }
        else {
        	Main main = new Main();
    		main.changeScene("GestionUtilisateurs_1.fxml");
        }
	}
	
	public void patients(ActionEvent event) throws IOException {
        if(!patients_button.isSelected()){
        	patients_button.setSelected(true);
        }
        else {
        	Main main = new Main();
    		main.changeScene("GestionPatients.fxml");
        }
	}
	
}
