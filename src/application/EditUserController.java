package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.ListIterator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;

public class EditUserController {
	@FXML
	private TextField name;
	@FXML
	private TextField login;
	@FXML
	private TextField cin;
	@FXML
	private TextField tel;
	@FXML
	private TextField pass;
	@FXML
	private TextField pass2;
	@FXML
	private CheckBox isAdmin;
	@FXML
	private Label msg;
	private GestionUtilisateursController_2 m =new GestionUtilisateursController_2();
	private GestionUtilisateursController_3 g =new GestionUtilisateursController_3();

	@FXML
	private void initialize() {
		if (!g.isAsst()) {
			name.setText(m.dentisteSelected().getName());
			login.setText(m.dentisteSelected().getLogin());
			cin.setText(m.dentisteSelected().getCIN());
			tel.setText(m.dentisteSelected().getTel());
			pass.setText(m.dentisteSelected().getPasswd());
			pass2.setText(m.dentisteSelected().getPasswd());
			if (m.dentisteSelected().isAdmin()) {
				isAdmin.setSelected(true);
			}
		}
		else {
			name.setText(g.assistantSelected().getName());
			login.setText(g.assistantSelected().getLogin());
			cin.setText(g.assistantSelected().getCIN());
			tel.setText(g.assistantSelected().getTel());
			pass.setText(g.assistantSelected().getPasswd());
			pass2.setText(g.assistantSelected().getPasswd());
			isAdmin.setVisible(false);
		}
	}
	
	public void save(ActionEvent event) throws IOException {
		if (!g.isAsst()) {
			if (name.getText().toUpperCase().equals(m.dentisteSelected().getName()) && 
				login.getText().equals(m.dentisteSelected().getLogin()) &&
				cin.getText().toUpperCase().equals(m.dentisteSelected().getCIN()) &&
				tel.getText().equals(m.dentisteSelected().getTel()) &&
				pass.getText().toUpperCase().equals(m.dentisteSelected().getPasswd()) &&
				pass2.getText().toUpperCase().equals(m.dentisteSelected().getPasswd()) &&
				isAdmin.isSelected() == m.dentisteSelected().isAdmin()) {
				cancel(event);
			}
			else if (!pass.getText().equals(pass2.getText())) {
				msg.setText("Veuillez confirmer votre password");
			}
			else if (name.getText().isBlank() || 
					login.getText().isBlank() || 
					cin.getText().isBlank() || 
					tel.getText().isBlank() || 
					pass.getText().isBlank() || 
					pass2.getText().isBlank()) {
				msg.setText("Tous les champs sont obligatoires");
			}
			else if (m.existCIN(cin.getText()) && !cin.getText().equals(m.dentisteSelected().getCIN())) {
				msg.setText("La CIN existe déjà");
			}
			else if (m.existNom(name.getText()) && !name.getText().equals(m.dentisteSelected().getName())) {
				msg.setText("Le nom existe déjà");
			}
			else if (m.existLogin(login.getText()) && !login.getText().equals(m.dentisteSelected().getLogin())) {
				msg.setText("Le login existe déjà");
			}
			else {
				try {
					FileInputStream fis = new FileInputStream("dentistes");
					ArrayList<Dentiste> dentisteListe = new ArrayList<>();
					while (fis.available() > 0) {
						ObjectInputStream ois = new ObjectInputStream(fis);
						Dentiste dent = (Dentiste) ois.readObject();
						if (!m.dentisteSelected().getLogin().equals(dent.getLogin()))
							dentisteListe.add(dent);
						else {
							dentisteListe.add(new Dentiste(login.getText(),pass.getText(),name.getText(),cin.getText(),tel.getText(),isAdmin.isSelected()));
						}
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
					
					cancel(event);
				    
					Alert confirm = new Alert(Alert.AlertType.INFORMATION);
			    	confirm.setHeaderText(null);
			    	confirm.setTitle("Confirmation");
			    	confirm.setContentText("Les informations de ce dentiste ont été modifiées avec succès");
			    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			    	confirm.initOwner(stage);
			    	confirm.show();
			    	
				} catch (IOException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		else {
			if (name.getText().toUpperCase().equals(g.assistantSelected().getName()) && 
				login.getText().equals(g.assistantSelected().getLogin()) &&
				cin.getText().toUpperCase().equals(g.assistantSelected().getCIN()) &&
				tel.getText().equals(g.assistantSelected().getTel()) &&
				pass.getText().toUpperCase().equals(g.assistantSelected().getPasswd()) &&
				pass2.getText().toUpperCase().equals(g.assistantSelected().getPasswd())) {
					cancel(event);
				}
				else if (!pass.getText().equals(pass2.getText())) {
					msg.setText("Veuillez confirmer votre password");
				}
				else if (name.getText().isBlank() || 
						login.getText().isBlank() || 
						cin.getText().isBlank() || 
						tel.getText().isBlank() || 
						pass.getText().isBlank() || 
						pass2.getText().isBlank()) {
					msg.setText("Tous les champs sont obligatoires");
				}
				else if (g.existCIN(cin.getText()) && !cin.getText().equals(g.assistantSelected().getCIN())) {
					msg.setText("La CIN existe déjà");
				}
				else if (g.existNom(name.getText()) && !name.getText().equals(g.assistantSelected().getName())) {
					msg.setText("Le nom existe déjà");
				}
				else if (g.existLogin(login.getText()) && !login.getText().equals(g.assistantSelected().getLogin())) {
					msg.setText("Le login existe déjà");
				}
				else {
					try {
						FileInputStream fis = new FileInputStream("assistants");
						ArrayList<Assistant> assistantListe = new ArrayList<>();
						while (fis.available() > 0) {
							ObjectInputStream ois = new ObjectInputStream(fis);
							Assistant asst = (Assistant) ois.readObject();
							if (!g.assistantSelected().getLogin().equals(asst.getLogin()))
								assistantListe.add(asst);
							else {
								assistantListe.add(new Assistant(login.getText(),pass.getText(),name.getText(),cin.getText(),tel.getText()));
							}
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
						
						cancel(event);
					    
						Alert confirm = new Alert(Alert.AlertType.INFORMATION);
				    	confirm.setHeaderText(null);
				    	confirm.setTitle("Confirmation");
				    	confirm.setContentText("Les informations de cet assistant ont été modifiées avec succès");
				    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				    	confirm.initOwner(stage);
				    	confirm.show();
				    	
					} catch (IOException | ClassNotFoundException ex) {
						ex.printStackTrace();
					}
				}
		}
	}
	
	public void cancel(ActionEvent event) throws IOException {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}
}
