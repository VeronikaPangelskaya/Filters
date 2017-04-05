import Controller.Controller;
import Model.Filters;
import View.Frame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by sukhanovma on 28.03.2017.
 */
public class Filter {
    public static void main(String[] args) {

////        Field field = new Field(15, 10);
        BufferedImage image = new BufferedImage(5,5, BufferedImage.TYPE_INT_ARGB);
        try {
            image = ImageIO.read(new File("res/test.bmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Filters filters = new Filters(image);
        Controller controller = new Controller(filters);
        Frame frame = new Frame(controller);
    }
}
