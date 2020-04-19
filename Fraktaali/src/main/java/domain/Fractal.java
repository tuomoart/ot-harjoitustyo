package domain;

import dao.HistoryDao;
import dao.SQLiteHistoryDao;
import java.sql.SQLException;
import java.util.Properties;

public class Fractal {
    private HistoryDao history;
    private Properties properties;
    
    private int width;
    private int height;
    
    private double x;
    private double y;
    private double areaWidth;
    private double areaHeight;

    private ComplexNumber c;

    private boolean[][] values;

    private double minX = -1.5;
    private double maxX = 1.5;
    private double minY = -1.5;
    private double maxY = 1.5;

    private double threshold;
    private int iterations;

    public Fractal(HistoryDao history, Properties properties) {
        this.history = history;
        this.properties = properties;
        
        loadToDefaults();
    }
    
    public void loadToDefaults() {
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
    
    public void setNumber(double r, double i) {
        this.c = new ComplexNumber(r, i);
    }
    
    public void saveModifications() {
        String settings = iterations + "," + c.getReal() + "," + c.getImg();
        try {
            this.history.saveModification(settings);
        } catch (SQLException e) {
            
        }
        
    }
    
    public boolean[][] undo() {
        try {
            unpackSettings(history.undo());
        } catch (SQLException e) {
            loadToDefaults();
        }
        getValues();
        return this.values;
    }
    
    public void unpackSettings(String settings) {
        String[] s = settings.split(",");
        
        iterations = Integer.valueOf(s[0]);
        c = new ComplexNumber(Double.valueOf(s[1]), Double.valueOf(s[2]));
    }
    
    public boolean[][] generateJuliaSet(int w, int h) {
        this.width = w;
        this.height = h;
        
        saveModifications();
        
        getValues();
        return this.values;
    }
    
    private void getValues() {
        values = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            double it = 1.0 * y + 1.0 * i * areaWidth / width;
            for (int j = 0; j < height; j++) {
                double jt = 1.0 * x + 1.0 * j * areaHeight / height;
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
