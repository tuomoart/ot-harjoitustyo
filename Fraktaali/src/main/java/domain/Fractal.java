package domain;

import java.util.Arrays;

public class Fractal {
    private int width = 350;
    private int height = 350;

    private static ComplexNumber c = new ComplexNumber(-0.223, 0.745);

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
    
    public void setIterations(int iterations) {
        if (iterations>0) {
            this.iterations=iterations;
        }
    }
    
    public boolean[][] generateJuliaSet(int w, int h) {
        this.width=w;
        this.height=h;
        getValues();
        return this.values;
    }
    
    private void getValues() {
        values = new boolean[width][height];
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
		double a = (double)i*(maxX-minX)/(double)width + minX;
		double b = (double)j*(maxY-minY)/(double)height + minY;
                values[i][j] = isInSet(new ComplexNumber(a,b));
            }
        }
    }
    
    private boolean isInSet(ComplexNumber cn) {
        for(int i=0;i<iterations;i++){
            cn = cn.square().add(c);
        }
        return cn.magnitude()<threshold*threshold;
    }
}
