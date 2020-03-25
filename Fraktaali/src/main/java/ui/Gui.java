package ui;

import domain.Fractal;
import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Gui extends Application {
    private Label status;
    private TextField iterationsBox;
    private Canvas drawing;
    private GraphicsContext drawer;
    private int drawArea=1000;
    private Fractal generator;
    private Stage window;
    
    @Override
    public void start(Stage window) {
        this.generator=new Fractal();
        this.window=window;
        
        window.setTitle("Fractal generator");
        
        BorderPane layout = new BorderPane();
        VBox leftSide = new VBox();
        layout.setLeft(leftSide);
        
        this.status = new Label("");
        leftSide.getChildren().add(this.status);
        this.iterationsBox=new TextField("50");
        leftSide.getChildren().add(this.iterationsBox);
        
        Button generateButton = new Button("Generate");
        leftSide.getChildren().add(generateButton);
        generateButton.setOnAction((event) -> {
            generateButtonAction();
        });
        
        this.drawing = new Canvas(drawArea,drawArea);
        this.drawer = this.drawing.getGraphicsContext2D();
        layout.setCenter(this.drawing);
        
        Scene scene = new Scene(layout);
        
        this.window.setScene(scene);
        this.window.show();
    }
    
    public void generateButtonAction() {
        this.status.setText("Generating...");
        generator.setIterations(Integer.valueOf(this.iterationsBox.getText()));
        boolean[][] grid = this.generator.generateJuliaSet(this.drawArea, this.drawArea);
        
        for (int y=0;y<grid.length; y++) {
            for (int x=0; x<grid[y].length; x++) {
                if (grid[y][x]) {
                    drawer.fillOval(x, y, 1, 1);
                }
            }
        }
    }
    
    public void draw() {
        
    }
    
    public void begin() {
        launch(Gui.class);
    }
}
