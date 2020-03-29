package ui;

import domain.Fractal;
import java.util.Random;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Gui extends Application {
    private Label status;
    private Label iterationsLabel;
    private Slider iterationsSlider;
    private Canvas drawing;
    private GraphicsContext drawer;
    private int drawArea=500;
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
        
        this.iterationsLabel = new Label("Iterations: " + this.generator.getIterations());
        leftSide.getChildren().add(this.iterationsLabel);
        this.iterationsSlider = new Slider(0,100,50);
        this.iterationsSlider.setShowTickLabels(true);
        this.iterationsSlider.setShowTickMarks(true);
        this.iterationsSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            sliderChangeAction(new_val);
        });
        leftSide.getChildren().add(this.iterationsSlider);
        
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
    
    public void sliderChangeAction(Number value) {
        int intvalue = value.intValue();
        this.iterationsLabel.setText("Iterations: " + intvalue);
        this.generator.setIterations(intvalue);
        draw();
    }
    
    public void generateButtonAction() {
        this.status.setText("Generating...");
        generator.setIterations((int) this.iterationsSlider.getValue());
        draw();
    }
    
    public void draw() {
        boolean[][] grid = this.generator.generateJuliaSet(this.drawArea, this.drawArea);
        
        this.drawer.clearRect(0, 0, drawing.getWidth(), drawing.getHeight());
        
        for (int y=0;y<grid.length; y++) {
            for (int x=0; x<grid[y].length; x++) {
                if (grid[y][x]) {
                    drawer.fillOval(x, y, 1, 1);
                }
            }
        }
    }
    
    public void begin() {
        launch(Gui.class);
    }
}
