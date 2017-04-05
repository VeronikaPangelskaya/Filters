package Controller;

import Model.Filters;
import View.Panel;

import java.awt.image.BufferedImage;

/**
 * Created by sukhanovma on 28.03.2017.
 */
public class Controller {
    Filters filter;

    public Controller(Filters _filter){
        filter = _filter;
    }
    public BufferedImage setBlackAndWhite(){
        return filter.convertToBlackAndWhite();
    }

    public BufferedImage setNegative() {
        return filter.convertToNegative();
    }

    public BufferedImage setDither() {
        return filter.ditherFloydSteinberg();
    }

    public BufferedImage setSharpness() {
        return filter.convertToSharpness();
    }

    public BufferedImage setBlur() {
        return filter.convertToBlurs();
    }

    public BufferedImage setEmboss() {
        return filter.convertToEmboss();
    }

    public BufferedImage setWatercolor() {
        return filter.convertToWatercolor();
    }

    public BufferedImage setDouble() {
        return filter.convertToDouble();
    }

    public BufferedImage setContour() {
        return filter.convertToContour();
    }

    public BufferedImage setRotation(double gam) {
        return filter.convertToRotation(gam);
    }

    public BufferedImage setRoberts() {
        return filter.convertToRoberts();
    }

    public BufferedImage setSobel() {
        return filter.convertToSobel();
    }

    public BufferedImage setGamma(double gam) {
        return filter.convertToGamma(gam);
    }

    public BufferedImage setOrderedDither() {
        return filter.convertToOrderedDither();
    }

    public void openImage(BufferedImage image) {
        filter.setNewImage(image);
    }

    public void setImage(BufferedImage image) {
        filter.setNewImage(image);
    }
}
