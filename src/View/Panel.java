package View;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by sukhanovma on 01.04.2017.
 */
public class Panel extends JPanel {
    BufferedImage image;
    BufferedImage newImage;

    boolean start = true;

    ImagePanel imagePanel;
    Image350Panel image350Panel;
    NewImage newImagePanel;


    Panel(){

        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        newImagePanel = new NewImage();
        image350Panel = new Image350Panel();
        imagePanel = new ImagePanel(image350Panel);

        this.add(imagePanel);
        this.add(image350Panel);
        this.add(newImagePanel);

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!start)
            imagePanel.setImage(image);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        start = false;
        revalidate();
        repaint();
    }

    public void setNewImage(BufferedImage newImage) {
        this.newImage = newImage;
        newImagePanel.setNewImage(this.newImage);
    }

    public BufferedImage getImage350() {
        return image350Panel.getImage();
    }

    public void setSel(boolean select) {
        this.imagePanel.setSel(select);
    }

    public boolean getSel(){
        return this.imagePanel.getSel();
    }

}
