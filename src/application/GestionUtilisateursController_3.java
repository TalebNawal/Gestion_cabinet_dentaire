package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.ListIterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class GestionUtilisateursController_3 extends GestionUtilisateurs{
	@FXML
	private TableView<Assistant> assistantsData;
	@FXML
	private TableColumn<Assistant, Integer> assistantID;
	@FXML
	private TableColumn<Assistant, String> assistantNom;
	@FXML
	private TableColumn<Assistant, String> assistantCIN;
	@FXML
	private TableColumn<Assistant, String> assistantTel;
	@FXML
	private TableColumn<Assistant, String> assistantDate;
	private ObservableList<Assistant> assistantList = FXCollections.observableArrayList();
	@FXML
	private TextField newName;
	@FXML
	private TextField newLogin;
	@FXML
	private PasswordField newPass;
	@FXML
	private PasswordField newPass2;
	@FXML
	private TextField newCIN;
	@FXML
	private TextField newTel;
	@FXML
	private PasswordField adminPass;
	@FXML
	private Label addMsg;
	private static Assistant assistantSel;
	private static boolean deleteSel;
	private static boolean estAssist;
	@FXML
	protected void initialize() {
		super.initialize();
		
		assistantID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		assistantNom.setCellValueFactory(new PropertyValueFactory<>("name"));
		assistantCIN.setCellValueFactory(new PropertyValueFactory<>("CIN"));
		assistantTel.setCellValueFactory(new PropertyValueFactory<>("Tel"));
		assistantDate.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
		
		refreshTable();
	}
	
	private void refreshTable() {
		assistantList.clear();
		File assistantFile = new File("assistants"); 
		if (assistantFile.length() != 0) {
			try{
				FileInputStream fis = new FileInputStream(assistantFile);
				while (fis.available() > 0) {
					ObjectInputStream ois = new ObjectInputStream(fis);
					Assistant assistant = (Assistant) ois.readObject();
					assistant.setName(assistant.getName().toUpperCase());
					assistant.setCIN(assistant.getCIN().toUpperCase());
					assistantList.add(assistant);
				}
				fis.close();
			} catch (IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		
		assistantsData.setItems(assistantList);
		//dentistesData.getSortOrder().addAll(dentisteID);
	}
	
	public Assistant assistantSelected() {
		return assistantSel;
	}
	
	public boolean existCIN(String cin) {
		File assistantFile = new File("assistants"); 
		if (assistantFile.length() != 0) {
			try (FileInputStream fis = new FileInputStream(assistantFile)) {
					while (fis.available() > 0) {
						ObjectInputStream ois = new ObjectInputStream(fis);
						Assistant assistant = (Assistant) ois.readObject();
						if (assistant.getCIN().equals(cin)) {
							return true;
						}
					}
			} catch (IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean existNom(String nom) {
		File assistantFile = new File("assistants"); 
		if (assistantFile.length() != 0) {
			try (FileInputStream fis = new FileInputStream(assistantFile)) {
					while (fis.available() > 0) {
						ObjectInputStream ois = new ObjectInputStream(fis);
						Assistant assistant = (Assistant) ois.readObject();
						if (assistant.getName().equals(nom)) {
							return true;
						}
					}
			} catch (IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean existLogin(String log) {
		File assistantFile = new File("assistants"); 
		if (assistantFile.length() != 0) {
			try (FileInputStream fis = new FileInputStream(assistantFile)) {
					while (fis.available() > 0) {
						ObjectInputStream ois = new ObjectInputStream(fis);
						Assistant assistant = (Assistant) ois.readObject();
						if (assistant.getLogin().equals(log)) {
							return true;
						}
					}
			} catch (IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean isDel() {
		return deleteSel;
	}
	
	public boolean isAsst() {
		return estAssist;
	}
	
	public void estAsst(boolean a) {
		estAssist = a;
	}
	
	public void add(ActionEvent event) throws IOException {
		Sign_in_Controller m = new Sign_in_Controller();
		if (newName.getText().isBlank() || newLogin.getText().isBlank() || newPass.getText().isBlank() || newPass2.getText().isBlank() || newCIN.getText().isBlank() || newTel.getText().isBlank() || adminPass.getText().isBlank()) {
			addMsg.setText("Tous les champs sont obligatoires");
		}
		else if (!newPass.getText().equals(newPass2.getText())) {
			addMsg.setText("Veuillez confirmer votre password");
		}
		else if (!adminPass.getText().equals(m.getUserPass())) {
			addMsg.setText("Le password de l'administrateur est invalide");
		}
		else if (existCIN(newCIN.getText())) {
			addMsg.setText("La CIN existe déjà");
		}
		else if (existNom(newName.getText())) {
			addMsg.setText("Le nom existe déjà");
		}
		else if (existLogin(newLogin.getText())) {
			addMsg.setText("Le login existe déjà");
		}
		else {
			try (FileOutputStream fos = new FileOutputStream("assistants",true);
				 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				    Assistant assistant = new Assistant(newLogin.getText(),newPass.getText(),newName.getText(),newCIN.getText(),newTel.getText());
				    oos.writeObject(assistant);
				} catch (IOException ex) {
				    ex.printStackTrace();
				}
			
			Alert confirm = new Alert(Alert.AlertType.INFORMATION);
	    	confirm.setHeaderText(null);
	    	confirm.setTitle("Confirmation");
	    	confirm.setContentText("le nouvel utilisateur a été créé avec succès");
	    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	confirm.initOwner(stage);
	    	confirm.show();
			
	    	refreshTable();
	    	
	    	newName.clear();
	    	newLogin.clear();
	    	newPass.clear();
	    	newPass2.clear();
	    	newCIN.clear();
	    	newTel.clear();
	    	adminPass.clear();
		}
	}
	
	public void delete(ActionEvent event) throws IOException {
		Assistant assistantSelected = assistantsData.getSelectionModel().getSelectedItem();
		if (assistantSelected != null) {			
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
							FileInputStream fis = new FileInputStream("assistants");
							ArrayList<Assistant> assistantListe = new ArrayList<>();
							while (fis.available() > 0) {
								ObjectInputStream ois = new ObjectInputStream(fis);
								Assistant asst = (Assistant) ois.readObject();
								if (!assistantSelected.getLogin().equals(asst.getLogin()))
									assistantListe.add(asst);
							}
							fis.close();
							
							new FileOutputStream("assistants").close();
							
							ListIterator<Assistant> iterator = assistantListe.listIterator();
							FileOutputStream fos = new FileOutputStream("assistants", true);
							while (iterator.hasNext()) {
							    ObjectOutputStream oos = new ObjectOutputStream(fos);   
							    oos.writeObject(iterator.next());
							} 
							fos.close();
							
						    Alert confirm = new Alert(Alert.AlertType.INFORMATION);
					    	confirm.setHeaderText(null);
					    	confirm.setTitle("Confirmation");
					    	confirm.setContentText("Cet assistant a été supprimé avec succès");
					    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					    	confirm.initOwner(stage);
					    	confirm.show();
					    	
					    	refreshTable();
						} catch (IOException | ClassNotFoundException ex) {
							ex.printStackTrace();
						}
						break;
					}
					else {
						refreshTable();
						break;
					}
				}
			}
		}	
		else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Erreur");
			alert.setContentText("sélectionner l'assistant à supprimer");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			alert.initOwner(stage);
			alert.show();
		}
	}

	public void edit(ActionEvent event) throws IOException {
		assistantSel = assistantsData.getSelectionModel().getSelectedItem();
		if (assistantSel != null) {			
			AuthentificationController.isDel = false;
			estAssist = true;
			AuthentificationController.src = "EditUser.fxml";
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
			alert.setContentText("sélectionner le dentiste à supprimer");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			alert.initOwner(stage);
			alert.show();
		}
	}
}
