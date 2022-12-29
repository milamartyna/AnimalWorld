package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;


public class App extends Application {

    private WorldMap map;
    private int xAxisLength;
    private int yAxisLength;
    private VariableManager manager;
    private Vector2d startMap;
    private Vector2d endMap;

    @Override
    public void init() {
        String[] parameters = getParameters().getRaw().toArray(new String[0]);
        try {
            this.manager = new VariableManager();
            this.map = new WorldMap(manager);
            SimulationEngine engine = new SimulationEngine(manager, map);
            engine.run();

            this.startMap = map.startMap;
            this.endMap = map.endMap;
            this.xAxisLength = this.endMap.x() - this.startMap.x() + 1;

            this.yAxisLength = this.endMap.y() - this.startMap.y() + 1;

            System.out.println(map);

        } catch (IllegalArgumentException iae){
            System.out.println(iae.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane gridpane = new GridPane();
        gridpane.setGridLinesVisible(true);

        this.setAxisLabels(gridpane);
        this.setObject(gridpane);
        this.setSizeOfRowsColumns(gridpane);
        gridpane.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(gridpane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void setAxisLabels(GridPane gridpane){
        Label xyLabel = new Label("y/x");
        gridpane.add(xyLabel, 0, 0);
        GridPane.setHalignment(xyLabel, HPos.CENTER);

        for(int i = 0; i < this.xAxisLength; i++){
            Label label = new Label(Integer.toString(i + this.startMap.x()));
            gridpane.add(label, i + 1, 0);
            GridPane.setHalignment(label, HPos.CENTER);
        }

        for(int i = 0; i < this.yAxisLength; i++){
            Label label = new Label(Integer.toString(this.endMap.y() - i));
            gridpane.add(label, 0, i + 1);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }

    private void setObject(GridPane gridPane){

        for(int i = this.startMap.x(); i <= this.endMap.x(); i++){
            for(int j = this.startMap.y(); j <= this.endMap.y(); j++){

                Vector2d position = new Vector2d(i, j);

                if(this.map.isOccupied(position)){
                    Object object = this.map.objectAt(position);
                    Label label = new Label(object.toString());
                    gridPane.add(label, i - this.startMap.x() + 1, this.endMap.y() - j + 1);
                    GridPane.setHalignment(label, HPos.CENTER);
                }
            }
        }

    }

    private void setSizeOfRowsColumns(GridPane gridpane){
        for(int i = 0; i <= this.xAxisLength; i++){
            gridpane.getColumnConstraints().add(new ColumnConstraints(30));
        }

        for(int i = 0; i <= this.yAxisLength; i++){
            gridpane.getRowConstraints().add(new RowConstraints(30));
        }
    }

}
