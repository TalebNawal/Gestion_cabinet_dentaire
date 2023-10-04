package application;

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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestionUtilisateursController_2 extends GestionUtilisateurs{
	@FXML
	private TableView<Dentiste> dentistesData;
	@FXML
	private TableColumn<Dentiste, Integer> dentisteID;
	@FXML
	private TableColumn<Dentiste, String> dentisteNom;
	@FXML
	private TableColumn<Dentiste, String> dentisteCIN;
	@FXML
	private TableColumn<Dentiste, String> dentisteTel;
	@FXML
	private TableColumn<Dentiste, String> dentisteDate;
	private ObservableList<Dentiste> dentistesList = FXCollections.observableArrayList();
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
	private CheckBox isAdmin;
	@FXML
	private Label addMsg;
	private static Dentiste dentisteSel;
	private static boolean deleteSel;
	
	@FXML
	protected void initialize() {
		super.initialize();
		
		dentisteID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		dentisteNom.setCellValueFactory(new PropertyValueFactory<>("name"));
		dentisteCIN.setCellValueFactory(new PropertyValueFactory<>("CIN"));
		dentisteTel.setCellValueFactory(new PropertyValueFactory<>("Tel"));
		dentisteDate.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
		
		refreshTable();
	}
	
	private void refreshTable() {
		dentistesList.clear();
		
		try{
			FileInputStream fis = new FileInputStream("dentistes");
			while (fis.available() > 0) {
				ObjectInputStream ois = new ObjectInputStream(fis);
				Dentiste dentiste = (Dentiste) ois.readObject();
				dentiste.setName(dentiste.getName().toUpperCase());
				dentiste.setCIN(dentiste.getCIN().toUpperCase());
				dentistesList.add(dentiste);
			}
			fis.close();
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		
		dentistesData.setItems(dentistesList);
		//dentistesData.getSortOrder().addAll(dentisteID);
	}
	
	public Dentiste dentisteSelected() {
		return dentisteSel;
	}
	
	public boolean existCIN(String cin) {
		try (FileInputStream fis = new FileInputStream("dentistes")) {
				while (fis.available() > 0) {
					ObjectInputStream ois = new ObjectInputStream(fis);
					Dentiste dentiste = (Dentiste) ois.readObject();
					if (dentiste.getCIN().equals(cin)) {
						return true;
					}
				}
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean existNom(String nom) {
		try (FileInputStream fis = new FileInputStream("dentistes")) {
				while (fis.available() > 0) {
					ObjectInputStream ois = new ObjectInputStream(fis);
					Dentiste dentiste = (Dentiste) ois.readObject();
					if (dentiste.getName().equals(nom)) {
						return true;
					}
				}
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean existLogin(String log) {
		try (FileInputStream fis = new FileInputStream("dentistes")) {
				while (fis.available() > 0) {
					ObjectInputStream ois = new ObjectInputStream(fis);
					Dentiste dentiste = (Dentiste) ois.readObject();
					if (dentiste.getLogin().equals(log)) {
						return true;
					}
				}
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean isDel() {
		return deleteSel;
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
			try (FileOutputStream fos = new FileOutputStream("dentistes",true);
				 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				    Dentiste dentiste = new Dentiste(newLogin.getText(),newPass.getText(),newName.getText(),newCIN.getText(),newTel.getText(),isAdmin.isSelected());
				    oos.writeObject(dentiste);
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
	    	isAdmin.setSelected(false);
		}
	}
	
	public void delete(ActionEvent event) throws IOException {
		Dentiste dentisteSelected = dentistesData.getSelectionModel().getSelectedItem();
		if (dentisteSelected != null) {
			Sign_in_Controller m = new Sign_in_Controller();
			if (dentisteSelected.getLogin().equals(m.getUserLogin())) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setTitle("Erreur");
				alert.setContentText("Impossible de supprimer votre profile");
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				alert.initOwner(stage);
				alert.show();
			}
			else {
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
								FileInputStream fis = new FileInputStream("dentistes");
								ArrayList<Dentiste> dentisteListe = new ArrayList<>();
								while (fis.available() > 0) {
									ObjectInputStream ois = new ObjectInputStream(fis);
									Dentiste dent = (Dentiste) ois.readObject();
									if (!dentisteSelected.getLogin().equals(dent.getLogin()))
										dentisteListe.add(dent);
								}
								fis.close();
								
								new FileOutputStream("dentistes").close();
								
								ListIterator<Dentiste> iterator = dentisteListe.listIterator();
								FileOutputStream fos = new FileOutputStream("dentistes", true);
								while (iterator.hasNext()) {
								    ObjectOutputStream oos = new ObjectOutputStream(fos);   
								    oos.writeObject(iterator.next());
								} 
								fos.close();
								
							    Alert confirm = new Alert(Alert.AlertType.INFORMATION);
						    	confirm.setHeaderText(null);
						    	confirm.setTitle("Confirmation");
						    	confirm.setContentText("Ce dentiste a été supprimé avec succès");
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

	public void edit(ActionEvent event) throws IOException {
		dentisteSel = dentistesData.getSelectionModel().getSelectedItem();
		if (dentisteSel != null) {
			Sign_in_Controller m = new Sign_in_Controller();
			if (dentisteSel.getLogin().equals(m.getUserLogin())) {
				Main main = new Main();
				main.changeScene("GestionUtilisateurs_1.fxml");
			}
			else {
				AuthentificationController.isDel = false;
				GestionUtilisateursController_3 a = new GestionUtilisateursController_3();
				a.estAsst(false);
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

		}
		else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Erreur");
			alert.setContentText("sélectionner le dentiste à modifier");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			alert.initOwner(stage);
			alert.show();
		}
	}
}
