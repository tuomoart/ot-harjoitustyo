package domain;

/**
 * Class for representing and making calculations with complex numbers
 * 
 * @author tuomoart
 */
public class ComplexNumber {
    private double real, img;

    /**
     * @param real  Real part of the complex number
     * @param img   Imaginary part of the complex number
     */
    public ComplexNumber(double real, double img) {
        this.real = real;
        this.img = img;
    }
    
    /**
     * getter for the real component of the imaginary number
     * 
     * @return 
     */
    public double getReal() {
        return this.real;
    }
    
    /**
     * setter for the real component of the imaginary number
     * 
     * @return 
     */
    public double getImg() {
        return this.img;
    }
    
    /**
     * calculate the square of this complex number
     * @return  squared complex number
     */
    public ComplexNumber square() {
        return new ComplexNumber(this.real * this.real - this.img * this.img, 2 * this.real * this.img);
    }
    
    /**
     * method for adding this complex number to another
     * 
     * @param cn the other complex number
     * 
     * @return  sum as a complex number
     */
    public ComplexNumber add(ComplexNumber cn) {
        return new ComplexNumber(this.real + cn.real, this.img + cn.img);
    }

    /**
     * method for calculating the magnitude of a complex number
     * 
     * @return  magnitude
     */
    public double magnitude() {
        return real * real + img * img;
    }
}
