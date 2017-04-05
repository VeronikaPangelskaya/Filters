package View;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 * Created by sukhanovma on 04.04.2017.
 */
public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener{
    BufferedImage image;
    int widthImage;
    int heightImage;
    double ratio = 1.0;
    double ratioX;
    double ratioY;
    Image350Panel image350;

    boolean select = false;


    ImagePanel(JPanel _image350Panel){
        this.image350 = (Image350Panel) _image350Panel;
        setPreferredSize(new Dimension(350,350));
        setLocation(10,10);
        Border blackline = BorderFactory.createDashedBorder(Color.BLACK);
        setBorder(blackline);
        setVisible(true);
        repaint();
    }

    public void setImage(BufferedImage image){
        this.image = image;
        widthImage = image.getWidth();
        heightImage = image.getHeight();
        ratioX = (double)widthImage/ 350.0;
        ratioY = (double)heightImage/ 350.0;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        addMouseListener(this);
        if(widthImage < 350){
            if(heightImage < 350)
            {
                if (widthImage >= heightImage) {
                    g.drawImage(image, 0, 0, widthImage, (int) heightImage, 0, 0, widthImage, heightImage, this);
                } else {
                    g.drawImage(image, 0, 0, widthImage, (int) heightImage, 0, 0, widthImage, heightImage, this);
                }
            }
            else{
                ratio =(double) 350 / (double) widthImage;
                g.drawImage(image, 0, 0,  (int) (widthImage/ ratio), (int) 350, 0, 0, widthImage, heightImage, this);
            }
        }
        else {
            if(heightImage < 350) {
                ratio =(double) widthImage / (double) 350;
                g.drawImage(image, 0, 0, widthImage, (int) (heightImage/ratio), 0, 0, widthImage, heightImage, this);
            }
            if (widthImage >= heightImage) {
                ratio = (double) widthImage / (double) heightImage;
                g.drawImage(image, 0, 0, 350, (int) (350 / ratio), 0, 0, widthImage, heightImage, this);
            } else {
                ratio = (double) heightImage / (double) widthImage;
                g.drawImage(image, 0, 0, (int) (350 / ratio), 350, 0, 0, widthImage, heightImage, this);
            }
        }

    }



    @Override
    public void mouseClicked(MouseEvent e) {
        if(select) {
            int x = e.getX();
            int y = e.getY();
            int xX, yY;
            if (widthImage >= heightImage) {
                if (y < 350 / ratio) {
                    xX = (int) (x * ratioX);
                    yY = (int) (y * ratioY * ratio);
                } else
                    return;
            } else {
                if (x < 350 / ratio) {
                    xX = (int) (x * ratioX * ratio);
                    yY = (int) (y * ratioY);
                } else
                    return;
            }

            BufferedImage img;
            if (xX >= 175 && xX < (widthImage - 175)) {
                if (yY >= 175 && yY < (heightImage - 175)) {
                    img = image.getSubimage(xX - 175, yY - 175, 350, 350);
                    image350.setImage350(img);
                } else if (yY < 175) {
                    img = image.getSubimage(xX - 175, 0, 350, 350);
                    image350.setImage350(img);
                } else {
                    img = image.getSubimage(xX - 175, heightImage - 350, 350, 350);
                    image350.setImage350(img);
                }
            } else if (xX < 175) {
                if (yY >= 175 && yY < (heightImage - 175)) {
                    img = image.getSubimage(0, yY - 175, 350, 350);
                    image350.setImage350(img);
                } else if (yY < 175) {
                    img = image.getSubimage(0, 0, 350, 350);
                    image350.setImage350(img);
                } else {
                    img = image.getSubimage(0, heightImage - 350, 350, 350);
                    image350.setImage350(img);
                }
            } else {
                if (yY >= 175 && yY < (heightImage - 175)) {
                    img = image.getSubimage(widthImage - 350, yY - 175, 350, 350);
                    image350.setImage350(img);
                } else if (yY < 175) {
                    img = image.getSubimage(widthImage - 350, 0, 350, 350);
                    image350.setImage350(img);
                } else {
                    img = image.getSubimage(widthImage - 350, heightImage - 350, 350, 350);
                    image350.setImage350(img);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void setSel(boolean select) {
        this.select = select;
    }

    public boolean getSel() {
        return select;
    }
}
