package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class Sign_in_Controller {
	@FXML
	private Label time;
	@FXML
	private Label loginMsg;
	@FXML
	private ToggleButton button_dentiste;
	@FXML
	private ToggleButton button_assistant;
	@FXML
	private TextField login;
	@FXML
	private PasswordField passwd;
	private static User utilisateur;
	public static boolean isD = false;
	private boolean successful;
	
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
	
	public void Dentiste(ActionEvent event) {
        if(!button_dentiste.isSelected()){
        	button_dentiste.setSelected(true);
        }
        else {
        	login.clear();
        	passwd.clear();
        }
	}
	
	public String getUserName(){
		return utilisateur.getName();
	}
	
	public String getUserPass(){
		return utilisateur.getPasswd();
	}
	
	public String getUserLogin(){
		return utilisateur.getLogin();
	}
	
	public boolean isAdmin() {
		if (isD) {
			return ((Dentiste) utilisateur).isAdmin();
		}
		return false;
	}
	
	public void Assistant(ActionEvent event) {
        if(!button_assistant.isSelected()){
        	button_assistant.setSelected(true);
        }
        else {
        	login.clear();
        	passwd.clear();
        }
	}
	
	public void SeConnecter(ActionEvent event) throws IOException {
		Main main = new Main();
		if (login.getText().isBlank() || passwd.getText().isBlank()){
			loginMsg.setText("Tous les champs sont obligatoires");
		}
		else { 
			if (button_dentiste.isSelected()) {
				
				// Se connecter avec le compte par défaut
				File dentFile = new File("dentistes"); 
				if (dentFile.length() == 0) {
					if (login.getText().equals("admin") && passwd.getText().equals("admin")) {
						main.changeScene("New_admin.fxml");
						successful = true;
					}
				}
				
				// Verifier login & passwd pour le Dentiste
				else {
					try{
						FileInputStream fis = new FileInputStream("dentistes");
						while (fis.available() > 0) {
							ObjectInputStream ois = new ObjectInputStream(fis);
							utilisateur = (Dentiste) ois.readObject();
							if (utilisateur.getLogin().equals(login.getText()) && utilisateur.getPasswd().equals(passwd.getText())) {
								isD = true;
								successful = true;
								main.changeScene("HomeDentiste.fxml");
								break;
							}
						}
						fis.close();
					} catch (IOException | ClassNotFoundException ex) {
						ex.printStackTrace();
					}
				}
				if (!successful) {
					loginMsg.setText("Le login ou le password n'est pas valide");
				}
			}
			else {
				// Verifier login & passwd pour l'Assistant
				File assistantFile = new File("assistants"); 
				if (assistantFile.length() != 0) {
					try{
						FileInputStream fis = new FileInputStream(assistantFile);
						while (fis.available() > 0) {
							ObjectInputStream ois = new ObjectInputStream(fis);
							utilisateur = (Assistant) ois.readObject();
							if (utilisateur.getLogin().equals(login.getText()) && utilisateur.getPasswd().equals(passwd.getText())) {
								isD = false;
								successful = true;
								main.changeScene("HomeAssistant.fxml");
								break;
							}
						}
						fis.close();
					} catch (IOException | ClassNotFoundException ex) {
						ex.printStackTrace();
					}
				}
				if (assistantFile.length() == 0 || !successful) {
					loginMsg.setText("Le login ou le password n'est pas valide");
				}
			}
		}
	}
}
