module cabinet_dentaire {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.fxml;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
}
