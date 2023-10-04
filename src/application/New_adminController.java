package application;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class New_adminController {
	@FXML
	private Label time;
	@FXML
	private TextField login;
	@FXML
	private PasswordField passwd;
	@FXML
	private PasswordField passwd2;
	@FXML
	private TextField name;
	@FXML
	private TextField cin;
	@FXML
	private TextField tel;
	@FXML
	private Label error;
	
	@FXML
	private void initialize() {
		// Afficher et mettre à jour la date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		Thread thread = new Thread(() -> {
			while(true){
				LocalDateTime timenow = LocalDateTime.now();
	            Platform.runLater(() -> {
	            	time.setText(dtf.format(timenow));
	            });
	            try{
	                Thread.sleep(1000);
	            }catch(Exception e){
	                System.out.println(e);
	            }
	        }
		});
		thread.start();
	}
	
	public void newAdmin(ActionEvent event) throws IOException {
		if (name.getText().isBlank() || login.getText().isBlank() || passwd.getText().isBlank() || passwd2.getText().isBlank() || cin.getText().isBlank() || tel.getText().isBlank()) {
			error.setText("Tous les champs sont obligatoires");
		}
		else if (!passwd.getText().equals(passwd2.getText())) {
			error.setText("Veuillez confirmer votre password");
		}
		else {
			try (FileOutputStream fos = new FileOutputStream("dentistes",true);
				 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				    Dentiste dentiste = new Dentiste(login.getText(),passwd.getText(),name.getText(),cin.getText(),tel.getText(),true);
				    oos.writeObject(dentiste);
				} catch (IOException ex) {
				    ex.printStackTrace();
				}
			Main main = new Main();
			main.changeScene("Sign_in.fxml");
		}
	}
}
