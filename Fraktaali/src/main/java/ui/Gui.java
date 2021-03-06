package ui;

import dao.HistoryDao;
import dao.SQLiteHistoryDao;
import domain.Fractal;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import javafx.embed.swing.SwingFXUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

/**
 * Creates a JavaFX UI for the fractal generator. 
 * @author tuomoart
 */
public class Gui extends Application {
    private Label numberLabel;
    private Slider realSlider;
    private Slider imaginarySlider;
    private Label iterationsLabel;
    private Slider iterationsSlider;
    private Label zoomLabel;
    private Slider zoomSlider;
    private Button undoButton;
    private Canvas drawing;
    private GraphicsContext drawer;
    private int drawArea;
    private Fractal generator;
    private Stage window;
    
    private double x0, y0, transX, transY;
    private double zoom=1;
    
    private boolean[][] grid;
    
    private boolean historyWorks = true;
    
    /**
     * Initial set up, preparing helper classes. 
     */
    @Override
    public void init() {
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/config.properties"));
            
            HistoryDao history = new SQLiteHistoryDao(properties.getProperty("historyDatabase"));
            
            this.generator = new Fractal(history, properties);
            this.generator.loadToDefaults();
            this.drawArea = generator.getWidth();
        } catch (SQLException e) {
            showError("Could not connect to the action history. Program will be shut down.");
            Platform.exit();
        } catch (Exception ee) {
            showError("Unknown error occurred. Program will be shut down.");
            Platform.exit();
        }
    }
    
    /**
     * Constructs the UI and shows it to the user. 
     * @param window 
     */
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
        this.realSlider.setOnMouseClicked((event) -> {
            undoableChangeAction();
        });
        leftSide.getChildren().add(this.realSlider);
        this.imaginarySlider = new Slider(-1,1,this.generator.getImg());
        this.imaginarySlider.setMajorTickUnit(1);
        this.imaginarySlider.setShowTickLabels(true);
        this.imaginarySlider.setShowTickMarks(true);
        this.imaginarySlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            imaginarySliderChangeAction(new_val);
        });
        this.imaginarySlider.setOnMouseClicked((event) -> {
            undoableChangeAction();
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
        this.iterationsSlider.setOnMouseClicked((event) -> {
            undoableChangeAction();
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
        undoButton = new Button("Undo");
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
        this.drawing.setOnMousePressed((MouseEvent t) -> {
            transX = t.getSceneX() - x0;
            transY = t.getSceneY() - y0;
        });
        this.drawing.setOnMouseDragged((MouseEvent t) -> {
            x0 = -t.getSceneX() + transX;
            y0 = -t.getSceneY() + transY;
            updateSettings();
        });
        this.drawer = this.drawing.getGraphicsContext2D();
        layout.setCenter(this.drawing);
        
        Scene scene = new Scene(layout);
        
        this.window.setScene(scene);
        loadDefaults();
        this.window.show();
    }
    
    /**
     * Event handler method for events that can be undone.
     */
    public void undoableChangeAction() {
        try {
            this.generator.saveModifications();
        } catch (Exception e) {
            if (historyWorks) {
                historyWorks = false;
                showError("Modification history has stopped working. You can still continue using the program.");
                undoButton.setDisable(true);
            }
        }
    }
    
    /**
     * Event handler method for changes to the slider controlling the real component.
     * @param value 
     */
    public void realSliderChangeAction(Number value) {
        double dvalue = value.doubleValue();
        String formattedRealValue = String.format("%1.3f", dvalue);
        String formattedImaginaryValue = String.format("%1.3f", this.generator.getImg());
        this.numberLabel.setText("Imaginary number: " + formattedRealValue
                + "+" + formattedImaginaryValue + "i");
        this.generator.setNumber(dvalue, this.generator.getImg());
        updateSettings();
    }
    
    /**
     * Event handler method for changes to the slider controlling the imaginary component.
     * @param value 
     */
    public void imaginarySliderChangeAction(Number value) {
        double dvalue = value.doubleValue();
        String formattedRealValue = String.format("%1.3f", this.generator.getReal());
        String formattedImaginaryValue = String.format("%1.3f", dvalue);
        this.numberLabel.setText("Imaginary number: " + formattedRealValue
                + "+" + formattedImaginaryValue + "i");
        this.generator.setNumber(this.generator.getReal(), dvalue);
        updateSettings();
    }
    
    /**
     * Event handler method for changes to the slider controlling the number of iterations.
     * @param value 
     */
    public void iterationsSliderChangeAction(Number value) {
        int intvalue = value.intValue();
        this.iterationsLabel.setText("Iterations: " + intvalue);
        this.generator.setIterations(intvalue);
        updateSettings();
    }
    /**
     * Event handler method for changes to the slider controlling the zoom level.
     * @param value 
     */
    public void zoomSliderChangeAction(Number value) {
        double doubleValue = value.doubleValue();
        String text = String.format("Magnification: %1.1f", doubleValue);
        this.zoomLabel.setText(text + "x");
        this.zoom = doubleValue;
        updateSettings();
    }
    
    /**
     * Event handler method for the save button.
     */
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
                showError("Error while saving file, please try again");
            }
        }
    }
    
    /**
     * Creates an error prompt and shows it. Waits for users response before continuing.
     * @param msg Message to be displayed in the prompt
     */
    public void showError(String msg) {
        Alert error = new Alert(AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText(msg);
                error.showAndWait();
    }
    
    /**
     * Updates the slider settings and related titles.
     */
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
    
    /**
     * Undoes the latest change made. Only applicable to imaginary number components and number of iterations.
     */
    public void undo() {
        try {
            this.grid = generator.undo();
        } catch (Exception e) {
            showError("Unknown error. The program will now close.");
            Platform.exit();
        }
        
        updateSlidersAndTitles();
        
        draw();
    }
    
    /**
     * Sends the current parameters to the generator object and draws the updated image. 
     */
    public void updateSettings() {
        double temp = 1.0 * drawArea/zoom;
        this.generator.setAreaHeight(1.0 * temp);
        this.generator.setAreaWidth(1.0 * temp);
        this.generator.setX(x0 + 1.0 * drawArea / 2 - temp / 2);
        this.generator.setY(y0 + 1.0 * drawArea / 2 - temp / 2);

        this.grid = this.generator.generateJuliaSet(this.drawArea, this.drawArea);
        
        draw();
    }
    
    /**
     * Re-renders the image. Does not re-draw, only re-renders from local parameters.
     */
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
    
    /**
     * Resets all parameters back to default settings. Default settings are fetched from the generator object.
     */
    public void loadDefaults() {
        try {
            generator.loadToDefaults();
        } catch (Exception e) {
            if (historyWorks) {
                historyWorks = false;
                showError("Modification history has stopped working. You can still continue using the program.");
                undoButton.setDisable(true);
            }
        }
        
        this.x0 = generator.getX();
        this.y0 = generator.getY();
        
        updateSlidersAndTitles();
        
        this.zoomSlider.setValue(1);
        this.zoomLabel.setText("Magnification: 1.0x");
        
        this.grid = this.generator.generateJuliaSet(this.drawArea, this.drawArea);
        
        draw();
    }
    
    /**
     * JavaFX-method for starting the UI.
     */
    public void begin() {
        launch(Gui.class);
    }
}
