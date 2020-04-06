package domain;

import java.util.Arrays;

public class Fractal {
    private int width = 350;
    private int height = 350;
    
    private double x = 0;
    private double y = 0;
    private double areaWidth = 350;
    private double areaHeight = 350;

    private ComplexNumber c = new ComplexNumber(-0.223, 0.745);

    private boolean[][] values = null;

    private double minX = -1.5;
    private double maxX = 1.5;
    private double minY = -1.5;
    private double maxY = 1.5;

    private double threshold = 1;
    private int iterations = 50;

    public Fractal() {

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
    
    public boolean[][] generateJuliaSet(int w, int h) {
        this.width = w;
        this.height = h;
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
