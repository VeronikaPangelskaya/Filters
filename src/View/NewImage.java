package View;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by sukhanovma on 04.04.2017.
 */
public class NewImage extends JPanel {

    BufferedImage image;
    int widthImage;
    int heightImage;

    NewImage(){
        this.setPreferredSize(new Dimension(350,350));
        this.setLocation(750,10);
        Border blackline = BorderFactory.createDashedBorder(Color.BLACK);
        this.setBorder(blackline);
    }

    public void setNewImage(BufferedImage image350) {
        image = image350;
        widthImage = image.getWidth();
        heightImage = image.getHeight();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 350, 350, 0, 0, widthImage, heightImage, this);
    }
}
