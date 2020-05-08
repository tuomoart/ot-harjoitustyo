package domain;

import dao.HistoryDao;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class for creating Julia-fractals
 * 
 * @author tuomoart
 */
public class Fractal {
    final HistoryDao history;
    final Properties properties;
    
    private int width;
    private int height;
    
    private double x;
    private double y;
    private double areaWidth;
    private double areaHeight;

    private ComplexNumber c;

    private boolean[][] values;

    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    private double threshold;
    private int iterations;

    /**
     * 
     * @param history DAO managing event history
     * @param properties Properties-object for default settings
     */
    public Fractal(HistoryDao history, Properties properties) {
        this.minX = -1.5;
        this.maxX = 1.5;
        this.minY = -1.5;
        this.maxY = 1.5;
        this.history = history;
        this.properties = properties;
    }
    
    /**
     * Method for resetting all parameters back to default settings
     * @throws Exception 
     */
    public void loadToDefaults() throws Exception {
        this.x = Double.valueOf(properties.getProperty("x"));
        this.y = Double.valueOf(properties.getProperty("y"));
        
        int size = Integer.valueOf(properties.getProperty("drawAreaSize"));
        this.width = size;
        this.height = size;
        this.areaWidth = (double) size;
        this.areaHeight = (double) size;
        
        double real = Double.valueOf(properties.getProperty("real"));
        double img = Double.valueOf(properties.getProperty("img"));
        this.c = new ComplexNumber(real, img);
        
        this.threshold = Double.valueOf(properties.getProperty("threshold"));
        this.iterations = Integer.valueOf(properties.getProperty("iterations"));
        
        this.history.empty();
        saveModifications();
    }
    
    public int getIterations() {
        return this.iterations;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getReal() {
        return this.c.getReal();
    }
    
    public double getImg() {
        return this.c.getImg();
    }
    
    public int getWidth() {
        return this.width;
    }
    
    /**
     * Method for setting the number of iterations. Only values greater than 0 will be accepted
     * 
     * @param iterations 
     */
    public void setIterations(int iterations) {
        if (iterations > 0) {
            this.iterations = iterations;
        }
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public void setAreaWidth(double areaWidth) {
        this.areaWidth = areaWidth;
    }
    
    public void setAreaHeight(double areaHeight) {
        this.areaHeight = areaHeight;
    }
    
    /**
     * Method for setting a new base complex number
     * 
     * @param r
     * @param i 
     */
    public void setNumber(double r, double i) {
        this.c = new ComplexNumber(r, i);
    }
    
    /**
     * Method for saving new event to history. Creates events for undo
     * 
     * @throws Exception 
     */
    public void saveModifications() throws Exception {
        String settings = iterations + "," + c.getReal() + "," + c.getImg();
        
        this.history.saveModification(settings);
    }
    
    /**
     * Loads previous settings or loads defaults if there are no previous
     * 
     * @return 
     * @throws Exception
     */
    public boolean[][] undo() throws Exception {
        try {
            unpackSettings(history.undo());
        } catch (SQLException e) {
            loadToDefaults();
        }
        getValues();
        return this.values;
    }
    
    private void unpackSettings(String settings) {
        String[] s = settings.split(",");
        
        iterations = Integer.valueOf(s[0]);
        c = new ComplexNumber(Double.valueOf(s[1]), Double.valueOf(s[2]));
    }
    
    /**
     * Method for getting values representing the Julia set
     * 
     * @param w width of the area
     * @param h height of the area
     * @return Boolean table where values are true if pixel belongs to the set
     */
    public boolean[][] generateJuliaSet(int w, int h) {
        this.width = w;
        this.height = h;
        
        getValues();
        return this.values;
    }
    
    private void getValues() {
        values = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            double it = (double) y + (double) i * areaWidth / width;
            for (int j = 0; j < height; j++) {
                double jt = (double) x + (double) j * areaHeight / height;
                double a = it * (maxX - minX) / (double) width + minX;
                double b = jt * (maxY - minY) / (double) height + minY;
                values[i][j] = isInSet(new ComplexNumber(a, b));
            }
        }
    }
    
    private boolean isInSet(ComplexNumber cn) {
        for (int i = 0; i < iterations; i++) {
            cn = cn.square().add(c);
        }
        return cn.magnitude() < threshold * threshold;
    }
}
