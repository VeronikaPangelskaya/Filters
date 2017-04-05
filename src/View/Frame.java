package View;

/**
 * Created by sukhanovma on 28.03.2017.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import Controller.Controller;

public class Frame extends JFrame
{
    private Controller controller;
    private JToolBar toolbar;
    private BufferedImage newImage;
    Panel panel;

    public File openFile() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.PNG","*.BMP",  "*.*");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try{
                File f = fileChooser.getSelectedFile();
                f.createNewFile();
                return f;}
            catch (IOException e)
            {

            }
        }
        return null;
    }
    private JToolBar createToolBar(){

        toolbar = new JToolBar();

        JButton open = new JButton();
        open.setToolTipText("Открыть...");
        open.setIcon(new ImageIcon("res/open.png"));
        open.addActionListener(e -> {
            try {
                File img = openFile();
                BufferedImage image = ImageIO.read(img);
                controller.openImage(image);
                panel.setImage(image);
            }catch (IOException err){

            }

        });
        toolbar.add(open);

        JButton save = new JButton();
        save.setToolTipText("Сохранить");
        save.setIcon(new ImageIcon("res/save.png"));
        toolbar.add(save);

        toolbar.add(new JToolBar.Separator());

        JToggleButton sel = new JToggleButton(new ImageIcon("res/sel.png"), false);
        sel.setToolTipText("Выбор 350*350");
        sel.addActionListener(e -> {
            if(panel.getSel()) {
                panel.setSel(false);
                sel.setEnabled(false);
            }
            else {
                panel.setSel(true);
                sel.setEnabled(true);
            }
        });
        toolbar.add(sel);

        JButton blackAndWhite = new JButton();
        blackAndWhite.setToolTipText("Черно-белое изображение");
        blackAndWhite.setIcon(new ImageIcon("res/b&w.png"));
        blackAndWhite.addActionListener(e -> {
            controller.setImage(panel.getImage350());
            newImage = controller.setBlackAndWhite();
            panel.setNewImage(newImage);
        });
        toolbar.add(blackAndWhite);

        JButton negative = new JButton();
        negative.setToolTipText("Негатив");
        negative.setIcon(new ImageIcon("res/neg.png"));
        negative.addActionListener(e -> {
            controller.setImage(panel.getImage350());
            newImage = controller.setNegative();
            panel.setNewImage(newImage);

        });
        toolbar.add(negative);

        JButton gamma = new JButton();
        gamma.setToolTipText("Гамма коррекция");
        gamma.setIcon(new ImageIcon("res/gamma.png"));
        gamma.addActionListener(e -> dialogFromGam());
        toolbar.add(gamma);

        toolbar.add(new JToolBar.Separator());

        JButton sharpness = new JButton();
        sharpness.setToolTipText("Увеличение резкости");
        sharpness.setIcon(new ImageIcon("res/sharp.png"));
        sharpness.addActionListener(e -> {
            controller.setImage(panel.getImage350());
            newImage = controller.setSharpness();
            panel.setNewImage(newImage);

        });
        toolbar.add(sharpness);

        JButton blur = new JButton();
        blur.setToolTipText("Размытие");
        blur.setIcon(new ImageIcon("res/blur.png"));
        blur.addActionListener(e -> {
            controller.setImage(panel.getImage350());
            newImage = controller.setBlur();
            panel.setNewImage(newImage);
        });
        toolbar.add(blur);
        toolbar.add(new JToolBar.Separator());

        JButton emboss = new JButton();
        emboss.setToolTipText("Тиснение");
        emboss.setIcon(new ImageIcon("res/emboss.png"));
        emboss.addActionListener(e -> {
            controller.setImage(panel.getImage350());
            newImage = controller.setEmboss();
            panel.setNewImage(newImage);
        });
        toolbar.add(emboss);

        JButton watercolor = new JButton();
        watercolor.setToolTipText("Акварелизация");
        watercolor.setIcon(new ImageIcon("res/waterpool.png"));
        watercolor.addActionListener(e -> {
            controller.setImage(panel.getImage350());
            newImage = controller.setWatercolor();
            panel.setNewImage(newImage);
        });
        toolbar.add(watercolor);
        toolbar.add(new JToolBar.Separator());

        
        JButton doubl = new JButton();
        doubl.setToolTipText("Удвоение");
        doubl.setIcon(new ImageIcon("res/doubl.png"));
        doubl.addActionListener(e -> {
            controller.setImage(panel.getImage350());
            newImage = controller.setDouble();
            panel.setNewImage(newImage);
        });
        toolbar.add(doubl);

        JButton rotation = new JButton();
        rotation.setToolTipText("Поворот");
        rotation.setIcon(new ImageIcon("res/rotate.png"));
        rotation.addActionListener(e -> dialogFromRotation());
        toolbar.add(rotation);
        toolbar.add(new JToolBar.Separator());

        JButton contour = new JButton();
        contour.setToolTipText("Выделение контуров");
        contour.setIcon(new ImageIcon("res/countour.png"));
        contour.addActionListener(e -> {
            controller.setImage(panel.getImage350());
            newImage = controller.setContour();
            panel.setNewImage(newImage);
        });
        toolbar.add(contour);



        JButton roberts = new JButton();
        roberts.setToolTipText("Дифференциальный оператор Робертса");
        roberts.setIcon(new ImageIcon("res/rob.png"));
        roberts.addActionListener(e -> {
            controller.setImage(panel.getImage350());
            newImage = controller.setRoberts();
            panel.setNewImage(newImage);
        });
        toolbar.add(roberts);

        JButton sobel = new JButton();
        sobel.setToolTipText("Дифференциальный оператор Собеля");
        sobel.setIcon(new ImageIcon("res/sharp.png"));
        sobel.addActionListener(e -> {
            controller.setImage(panel.getImage350());
            newImage = controller.setSobel();
            panel.setNewImage(newImage);
        });
        toolbar.add(sobel);



        JButton orderedDither = new JButton();
        orderedDither.setToolTipText("Упорядоченный дизеринг");
        orderedDither.setIcon(new ImageIcon("res/ord.png"));
        orderedDither.addActionListener(e -> {
            controller.setImage(panel.getImage350());
            newImage = controller.setOrderedDither();
            panel.setNewImage(newImage);
        });
        toolbar.add(orderedDither);

        JButton steinbergDither = new JButton();
        steinbergDither.setToolTipText("Дизеринг Штейнберга");
        steinbergDither.setIcon(new ImageIcon("res/fs.png"));
        steinbergDither.addActionListener(e -> {
            controller.setImage(panel.getImage350());
            newImage = controller.setDither();
            panel.setNewImage(newImage);
        });
        toolbar.add(steinbergDither);

        toolbar.setVisible(true);


        return toolbar;
    }

    private void dialogFromRotation() {
        JDialog dialog = new JDialog(this, "Угол поворота", true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(300, 150);
        dialog.setLocation(500,400);

        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField fieldCellSize = new JTextField(String.valueOf(1),4);


        JButton ok = new JButton("ОК");
        ok.addActionListener(e -> {
            double gam = Double.parseDouble(fieldCellSize.getText());
            controller.setImage(panel.getImage350());
            newImage = controller.setRotation(gam);
            panel.setNewImage(newImage);
            dialog.dispose();
        });
        jPanel.add(ok);
        JButton cancel = new JButton("Отмена");
        cancel.addActionListener(e -> dialog.dispose());
        jPanel.add(fieldCellSize);
//        jPanel.add(sliderCellSize);
        jPanel.add(ok);
        jPanel.add(cancel);

        dialog.add(jPanel);
        dialog.setVisible(true);
    }

    public Frame(Controller controller) {
        super("Фильтры");
        this.controller = controller;
        setIconImage(new ImageIcon("res/icon.png").getImage());
        setLocation(200,200);
        setDefaultCloseOperation( EXIT_ON_CLOSE );

        panel = new Panel();

        add(createToolBar(), BorderLayout.NORTH);
        add(panel);
        setSize(1110, 500);
        setVisible(true);
    }

    public void dialogFromGam() {

        JDialog dialog = new JDialog(this, "Гамма коррекция", true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(300, 150);
        dialog.setLocation(500,400);

        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField fieldCellSize = new JTextField(String.valueOf(1),2);

        JButton ok = new JButton("ОК");
        ok.addActionListener(e -> {
            double gam = Double.parseDouble(fieldCellSize.getText());
            controller.setImage(panel.getImage350());
            newImage = controller.setGamma(gam);
            panel.setNewImage(newImage);
            dialog.dispose();
        });
        jPanel.add(ok);
        JButton cancel = new JButton("Отмена");
        cancel.addActionListener(e -> dialog.dispose());
        jPanel.add(fieldCellSize);
        jPanel.add(ok);
        jPanel.add(cancel);

        dialog.add(jPanel);
        dialog.setVisible(true);
    }

}
