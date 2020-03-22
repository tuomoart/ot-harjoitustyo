/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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
        
        this.drawing = new Canvas(500,500);
        layout.setCenter(this.drawing);
        
        Scene scene = new Scene(layout);
        
        window.setScene(scene);
        window.show();
    }
    
    public void generateButtonAction() {
        status.setText("Generating...");
        
    }
    
    public void draw() {
        
    }
    
    public void begin() {
        launch(Gui.class);
    }
}
