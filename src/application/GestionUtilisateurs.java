package application;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

public class GestionUtilisateurs extends MenuDentiste{
	@FXML
	protected ToggleButton info;
	@FXML
	protected ToggleButton dentistes;
	@FXML
	protected ToggleButton assistants;
	
	public void infoPerso(ActionEvent event) throws IOException {
        if(!info.isSelected()){
        	info.setSelected(true);
        }
        else {
        	Main main = new Main();
    		main.changeScene("GestionUtilisateurs_1.fxml");
        }
	}
	
	public void dentistes_butt(ActionEvent event) throws IOException {
        if(!dentistes.isSelected()){
        	dentistes.setSelected(true);
        }
        else {
        	Main main = new Main();
    		main.changeScene("GestionUtilisateurs_2.fxml");
        }
	}
	
	public void assistantsButt(ActionEvent event) throws IOException {
        if(!assistants.isSelected()){
        	assistants.setSelected(true);
        }
        else {
        	Main main = new Main();
    		main.changeScene("GestionUtilisateurs_3.fxml");
        }
	}
}
