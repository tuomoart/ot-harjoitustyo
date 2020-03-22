/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author tuomoart
 */
public class Gui extends Application {
    private Label status;
    private Canvas drawing;
    private GraphicsContext drawer;
    private int drawArea=500;
    
    @Override
    public void start(Stage window) {
        window.setTitle("Fractal generator");
        
        BorderPane layout = new BorderPane();
        VBox leftSide = new VBox();
        layout.setLeft(leftSide);
        
        this.status = new Label("");
        leftSide.getChildren().add(this.status);
        
        Button generateButton = new Button("Generate");
        leftSide.getChildren().add(generateButton);
        generateButton.setOnAction((event) -> {
            generateButtonAction();
        });
        
        this.drawing = new Canvas(drawArea,drawArea);
        this.drawer = this.drawing.getGraphicsContext2D();
        layout.setCenter(this.drawing);
        
        Scene scene = new Scene(layout);
        
        window.setScene(scene);
        window.show();
    }
    
    public void generateButtonAction() {
        status.setText("Generating...");
        Random r = new Random();
        int i=0;
        while (i<100) {
            this.drawer.fillOval(r.nextDouble()*this.drawArea, r.nextDouble()*this.drawArea, 4, 4);
            i++;
        }
    }
    
    public void draw() {
        
    }
    
    public void begin() {
        launch(Gui.class);
    }
}
