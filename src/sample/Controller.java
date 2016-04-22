package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;

public class Controller {
    @FXML private Button testbutton;
    @FXML private PieChart TestGraph;

    ObservableList<PieChart.Data> pieChartData =
            FXCollections.observableArrayList(
                    new PieChart.Data("Grapefruit", 13),
                    new PieChart.Data("Oranges", 25),
                    new PieChart.Data("Plums", 10),
                    new PieChart.Data("Pears", 22),
                    new PieChart.Data("Apples", 30));

    public void showPieChart(){TestGraph.setData(pieChartData);}

    public void doStuff() {
        testbutton.setText("click!");

    }

}
