package domain;

public class ComplexNumber {
    private double real, img;

    public ComplexNumber(double real, double img) {
        this.real = real;
        this.img = img;
    }
    
    public double getReal() {
        return this.real;
    }
    
    public double getImg() {
        return this.img;
    }

    public ComplexNumber square() {
        return new ComplexNumber(this.real * this.real - this.img * this.img, 2 * this.real * this.img);
    }
    
    public ComplexNumber add(ComplexNumber cn) {
        return new ComplexNumber(this.real + cn.real, this.img + cn.img);
    }

    public double magnitude() {
        return real * real + img * img;
    }
}
