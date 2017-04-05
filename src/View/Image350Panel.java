package View;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by sukhanovma on 04.04.2017.
 */
public class Image350Panel extends JPanel{

    BufferedImage image;
    int widthImage;
    int heightImage;

    Image350Panel(){
        this.setPreferredSize(new Dimension(350,350));
        this.setLocation(380,10);
        Border blackline = BorderFactory.createDashedBorder(Color.BLACK);
        this.setBorder(blackline);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 350, 350, 0, 0, widthImage, heightImage, this);
    }

    public void setImage350(BufferedImage image350) {
        image = image350;
        widthImage = image.getWidth();
        heightImage = image.getHeight();
        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }
}
