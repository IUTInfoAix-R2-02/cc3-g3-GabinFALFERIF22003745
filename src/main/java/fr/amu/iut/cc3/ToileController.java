package fr.amu.iut.cc3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class ToileController implements Initializable {

    private static int rayonCercleExterieur = 200;
    private static int angleEnDegre = 60;
    private static int angleDepart = 90;
    private static int noteMaximale = 20;
    private ArrayList<Circle> listCircles;
    private ArrayList<Line> listLines;
    @FXML
    private Pane toilePane;
    @FXML
    private Label erreurLabel;
    @FXML
    private Label erreurLabel2;
    @FXML
    private TextField comp1;
    @FXML
    private TextField comp2;
    @FXML
    private TextField comp3;
    @FXML
    private TextField comp4;
    @FXML
    private TextField comp5;
    @FXML
    private TextField comp6;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listCircles = new ArrayList<>();
        listLines = new ArrayList<>();
    }

    int getXRadarChart(double value, int axe ){
        return (int) (rayonCercleExterieur + Math.cos(Math.toRadians(angleDepart - (axe-1)  * angleEnDegre)) * rayonCercleExterieur
                *  (value / noteMaximale));
    }

    int getYRadarChart(double value, int axe ){
        return (int) (rayonCercleExterieur - Math.sin(Math.toRadians(angleDepart - (axe-1)  * angleEnDegre)) * rayonCercleExterieur
                *  (value / noteMaximale));
    }

    @FXML
    private void addPoint(ActionEvent event) {
        TextField target = (TextField) event.getSource();
        String id = target.getId();
        String noteText = target.getText();
        try {
            double note = Double.parseDouble(noteText);
            int numComp = Integer.parseInt(id.charAt(4) + "");
            update(numComp);
            if (note < 0 || note > 20) {
                erreurLabel.setText("Erreur de saisie :");
                erreurLabel2.setText("Les valeurs doivent être entre 0 et 20");
            }
            else {
                erreurLabel.setText("");
                erreurLabel2.setText("");
                Circle cercle = new Circle();
                cercle.setFill(Color.BLACK);
                cercle.setLayoutX(getXRadarChart(note, numComp));
                cercle.setLayoutY(getYRadarChart(note, numComp));
                cercle.setRadius(5);
                cercle.setId("cercle" + numComp);
                listCircles.add(cercle);
                toilePane.getChildren().add(cercle);
            }

            if (lineIsDraw()) {
                eraseLines();
                tracer();
            }

        } catch (NumberFormatException e) {
            showErrorPopup("Erreur de format", "La population doit être un nombre entier.");
        }
    }

    private void showErrorPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void vider() {
        for (Circle cercle : listCircles) {
            toilePane.getChildren().remove(cercle);
        }
        listCircles = new ArrayList<>();
        eraseLines();
        comp1.setText("");
        comp2.setText("");
        comp3.setText("");
        comp4.setText("");
        comp5.setText("");
        comp6.setText("");
    }

    @FXML
    private void tracer() {
        listCircles.sort(new CircleCompartor());
        for (int i = 0; i < listCircles.size(); ++i) {
            Circle premierCercle = listCircles.get(i);
            Circle secondCercle;
            if (i == listCircles.size() - 1) {
                 secondCercle = listCircles.get(0);
            }
            else {
                 secondCercle = listCircles.get(i+1);
            }
            Line line = new Line();
            line.setStartX(premierCercle.getLayoutX());
            line.setStartY(premierCercle.getLayoutY());
            line.setEndX(secondCercle.getLayoutX());
            line.setEndY(secondCercle.getLayoutY());
            line.setStyle("-fx-stroke: \"black\";");
            listLines.add(line);
            toilePane.getChildren().add(line);
        }
    }

    private int getPosition(Shape object) {
        return Integer.parseInt(object.getId().charAt(object.getId().length()-1) + "");
    }

    private void update(int idTextField) {
        int index = -1;
        for (int i = 0; i < listCircles.size(); ++i) {
            if (idTextField == getPosition(listCircles.get(i))) {
                toilePane.getChildren().remove(listCircles.get(i));
                index = i;
                System.out.println(index);
            }
        }
        if (index != -1)
            listCircles.remove(index);
    }

    private boolean lineIsDraw() {
        return listLines.size() > 0;
    }
    private void eraseLines() {
        for (Line line : listLines) {
            toilePane.getChildren().remove(line);
        }
        listLines = new ArrayList<>();
    }
}
