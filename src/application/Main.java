package application;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
//import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
public class Main extends Application {
	private static Stage stg;
	@Override
	public void start(Stage primaryStage) {
		try {
			stg = primaryStage;
			Parent root = FXMLLoader.load(getClass().getResource("Sign_in.fxml"));
			Scene scene = new Scene(root,400,400);
			primaryStage.setTitle("Gestion d�un cabinet dentaire");
			//primaryStage.setFullScreen(true);
			primaryStage.getIcons().add(new Image(getClass().getResource("icon.png").toURI().toString()));
			primaryStage.setMaximized(true);
			primaryStage.setResizable(false);
			primaryStage.centerOnScreen();
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }
}
