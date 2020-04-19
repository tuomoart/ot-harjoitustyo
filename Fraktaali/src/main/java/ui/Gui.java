package ui;

import dao.HistoryDao;
import dao.SQLiteHistoryDao;
import domain.Fractal;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import javafx.embed.swing.SwingFXUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class Gui extends Application {
    private Label status;
    private Label numberLabel;
    private Slider realSlider;
    private Slider imaginarySlider;
    private Label iterationsLabel;
    private Slider iterationsSlider;
    private Label zoomLabel;
    private Slider zoomSlider;
    private Canvas drawing;
    private GraphicsContext drawer;
    private int drawArea;
    private Fractal generator;
    private Stage window;
    
    private double x0, y0, transX, transY;
    private double zoom=1;
    
    private boolean[][] grid;
    
    @Override
    public void init() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("config.properties"));
            
            HistoryDao history = new SQLiteHistoryDao(properties.getProperty("historyDatabase"));
            
            this.generator = new Fractal(history, properties);
            this.drawArea = generator.getWidth();
        } catch (SQLException e) {
            System.out.println("Error connecting to action history");
        } catch (Exception ee) {
            System.out.println(ee);
            Platform.exit();
        }
    }
    
    @Override
    public void start(Stage window) {
        this.generator.setAreaWidth(drawArea);
        this.generator.setAreaHeight(drawArea);
        this.window=window;
        
        window.setTitle("Fractal generator");
        
        //Create the layout
        BorderPane layout = new BorderPane();
        VBox leftSide = new VBox();
        layout.setLeft(leftSide);
        
        //Status-label TODO Delete this
        this.status = new Label("");
        leftSide.getChildren().add(this.status);
        
        //Label and sliders for selecting the imaginary number to base the Julia set off of
        this.numberLabel = new Label("Imaginary number: " + this.generator.getReal()
                + "+" + this.generator.getImg() + "i");
        leftSide.getChildren().add(this.numberLabel);
        this.realSlider = new Slider(-1,1,this.generator.getReal());
        this.realSlider.setMajorTickUnit(1);
        this.realSlider.setShowTickLabels(true);
        this.realSlider.setShowTickMarks(true);
        this.realSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            realSliderChangeAction(new_val);
        });
        leftSide.getChildren().add(this.realSlider);
        this.imaginarySlider = new Slider(-1,1,this.generator.getImg());
        this.imaginarySlider.setMajorTickUnit(1);
        this.imaginarySlider.setShowTickLabels(true);
        this.imaginarySlider.setShowTickMarks(true);
        this.imaginarySlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            imaginarySliderChangeAction(new_val);
        });
        leftSide.getChildren().add(this.imaginarySlider);
        
        //Label and slider for setting iterations
        this.iterationsLabel = new Label("Iterations: " + this.generator.getIterations());
        leftSide.getChildren().add(this.iterationsLabel);
        this.iterationsSlider = new Slider(0,100,this.generator.getIterations());
        this.iterationsSlider.setShowTickLabels(true);
        this.iterationsSlider.setShowTickMarks(true);
        this.iterationsSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            iterationsSliderChangeAction(new_val);
        });
        leftSide.getChildren().add(this.iterationsSlider);
        
        //Label and slider for magnification
        this.zoomSlider = new Slider(1,5,1);
        this.zoomLabel = new Label("Magnification: " + this.zoomSlider.getValue() + "x");
        leftSide.getChildren().add(this.zoomLabel);
        this.zoomSlider.setShowTickLabels(true);
        this.zoomSlider.setShowTickMarks(true);
        this.zoomSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            zoomSliderChangeAction(new_val);
        });
        leftSide.getChildren().add(this.zoomSlider);
        
        //Save-button
        Button saveButton = new Button("Save...");
        leftSide.getChildren().add(saveButton);
        saveButton.setOnAction((event) -> {
            saveButtonAction();
        });
        
        //Undo-button
        Button undoButton = new Button("Undo");
        leftSide.getChildren().add(undoButton);
        undoButton.setOnAction((event) -> {
            undo();
        });
        
        //Reset-button
        Button resetButton = new Button("Reset");
        leftSide.getChildren().add(resetButton);
        resetButton.setOnAction((event) -> {
            loadDefaults();
        });
        
        //Create the drawing area
        this.drawing = new Canvas(drawArea,drawArea);
        this.drawing.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                transX = t.getSceneX() - x0;
                transY = t.getSceneY() - y0;
            }
        });
        this.drawing.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                x0 = -t.getSceneX() + transX;
                y0 = -t.getSceneY() + transY;
                updateSettings();
            }
        });
        this.drawer = this.drawing.getGraphicsContext2D();
        layout.setCenter(this.drawing);
        
        Scene scene = new Scene(layout);
        
        this.window.setScene(scene);
        loadDefaults();
        this.window.show();
    }
    
    public void realSliderChangeAction(Number value) {
        double dvalue = value.doubleValue();
        String formattedRealValue = String.format("%1.3f", dvalue);
        String formattedImaginaryValue = String.format("%1.3f", this.generator.getImg());
        this.numberLabel.setText("Imaginary number: " + formattedRealValue
                + "+" + formattedImaginaryValue + "i");
        this.generator.setNumber(dvalue, this.generator.getImg());
        updateSettings();
    }
    
    public void imaginarySliderChangeAction(Number value) {
        double dvalue = value.doubleValue();
        String formattedRealValue = String.format("%1.3f", this.generator.getReal());
        String formattedImaginaryValue = String.format("%1.3f", dvalue);
        this.numberLabel.setText("Imaginary number: " + formattedRealValue
                + "+" + formattedImaginaryValue + "i");
        this.generator.setNumber(this.generator.getReal(), dvalue);
        updateSettings();
    }
    
    public void iterationsSliderChangeAction(Number value) {
        int intvalue = value.intValue();
        this.iterationsLabel.setText("Iterations: " + intvalue);
        this.generator.setIterations(intvalue);
        updateSettings();
    }
    
    public void zoomSliderChangeAction(Number value) {
        double doubleValue = value.doubleValue();
        String text = String.format("Magnification: %1.1f", doubleValue);
        this.zoomLabel.setText(text + "x");
        this.zoom = doubleValue;
        updateSettings();
    }
    
    public void saveButtonAction() {
        FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        
        File f = fileChooser.showSaveDialog(window);
        
        if (f != null) {
            try {
                WritableImage wi = new WritableImage(drawArea,drawArea);
                drawing.snapshot(null, wi);
                RenderedImage ri = SwingFXUtils.fromFXImage(wi,null);
                ImageIO.write(ri, "png", f);
            } catch (IOException e) {
                //TODO create an error prompt
            }
        }
    }
    
    public void updateSlidersAndTitles() {
        this.iterationsSlider.setValue(generator.getIterations());
        this.iterationsLabel.setText("Iterations: " + generator.getIterations());
        
        this.realSlider.setValue(generator.getReal());
        this.imaginarySlider.setValue(generator.getImg());
        String formattedRealValue = String.format("%1.3f", generator.getReal());
        String formattedImaginaryValue = String.format("%1.3f", generator.getImg());
        this.numberLabel.setText("Imaginary number: " + formattedRealValue
                + "+" + formattedImaginaryValue + "i");
    }
    
    public void undo() {
        this.grid = generator.undo();
        
        updateSlidersAndTitles();
        
        draw();
    }
    
    public void updateSettings() {
        double temp = 1.0*drawArea/zoom;
        this.generator.setAreaHeight(1.0*temp);
        this.generator.setAreaWidth(1.0*temp);
        this.generator.setX(x0+1.0*drawArea/2-temp/2);
        this.generator.setY(y0+1.0*drawArea/2-temp/2);

        this.grid = this.generator.generateJuliaSet(this.drawArea, this.drawArea);
        
        draw();
    }
    
    public void draw() {
        this.drawer.clearRect(0, 0, drawing.getWidth(), drawing.getHeight());
        
        for (int y=0;y<grid.length; y++) {
            for (int x=0; x<grid[y].length; x++) {
                if (grid[y][x]) {
                    drawer.fillOval(x, y, 1, 1);
                }
            }
        }
    }
    
    public void loadDefaults() {
        generator.loadToDefaults();
        
        this.x0 = generator.getX();
        this.y0 = generator.getY();
        
        updateSlidersAndTitles();
        
        this.zoomSlider.setValue(1);
        this.zoomLabel.setText("Magnification: 1.0");
        
        this.grid = this.generator.generateJuliaSet(this.drawArea, this.drawArea);
        
        draw();
    }
    
    public void begin() {
        launch(Gui.class);
    }
}
