package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HomeDentisteController extends MenuDentiste{
	@FXML
	private PieChart sexe;
	@FXML
	private PieChart soins;
	private int nbreHomme;
	private int nbreFemme;
	private int nbreSoinsTerminé;
	private int nbreSoinsEnCours;
	@FXML
	private BarChart<String, Number> patient;
	
	@FXML
	protected void initialize() {
		super.initialize();
		
		try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT COUNT(*) FROM patients WHERE Sexe ='M'";
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		nbreHomme = results.getInt("COUNT(*)");
	    	}
			mystmt.close();
			results.close();
			
			Statement mystmt1 = con.createStatement();
			String query1 = "SELECT COUNT(*) FROM patients WHERE Sexe ='F'";
	    	ResultSet results1 = mystmt1.executeQuery(query1);
	    	if (results1.next()) {
	    		nbreFemme = results1.getInt("COUNT(*)");
	    	}
			mystmt1.close();
			results1.close();
			
			con.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		PieChart.Data slice1 = new PieChart.Data("Homme", nbreHomme);
	    PieChart.Data slice2 = new PieChart.Data("Femme", nbreFemme);
	    sexe.getData().add(slice1);
	    sexe.getData().add(slice2);
	    sexe.setLegendSide(Side.LEFT);
	    
	    try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT COUNT(*) FROM actemedical WHERE EtatActe = 0";
	    	ResultSet results = mystmt.executeQuery(query);
	    	if (results.next()) {
	    		nbreSoinsTerminé = results.getInt("COUNT(*)");
	    	}
			mystmt.close();
			results.close();
			
			Statement mystmt1 = con.createStatement();
			String query1 = "SELECT COUNT(*) FROM actemedical WHERE EtatActe = 1";
	    	ResultSet results1 = mystmt1.executeQuery(query1);
	    	if (results1.next()) {
	    		nbreSoinsEnCours = results1.getInt("COUNT(*)");
	    	}
			mystmt1.close();
			results1.close();
			
			con.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	    
	    PieChart.Data slice3 = new PieChart.Data("Soins Termin�", nbreSoinsTerminé);
	    PieChart.Data slice4 = new PieChart.Data("Soins En cours", nbreSoinsEnCours);
	    soins.getData().add(slice3);
	    soins.getData().add(slice4);
	    soins.setLegendSide(Side.LEFT);
	    
	    CategoryAxis xAxis = new CategoryAxis();
	    xAxis.setLabel("Temps");

	    NumberAxis yAxis = new NumberAxis();
	    yAxis.setLabel("Nombre de patients");
	    XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<String, Number>();
	    try {
			String host = "jdbc:mysql://localhost:3306/cabinetdentaire";
			String username = "root";
			String password= "ayoub2001";
			Connection con = DriverManager.getConnection( host, username, password);
			Statement mystmt = con.createStatement();
			String query = "SELECT COUNT(*), DateReelle FROM patients p JOIN actemedical a ON a.IDPatient = p.IDPatients JOIN intervention i ON i.IDSoin = a.IDSoin WHERE DateReelle != NULL GROUP BY DateReelle";
	    	ResultSet results = mystmt.executeQuery(query);
	    	while (results.next()) {
	    		dataSeries1.getData().add(new XYChart.Data<String, Number>(results.getDate("DateReelle").toString(), results.getInt("COUNT(*)")));
	    	}
			mystmt.close();
			results.close();
			con.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	    patient.getData().add(dataSeries1);
	}
}
