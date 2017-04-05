package Model;

import java.awt.image.BufferedImage;

/**
 * Created by sukhanovma on 28.03.2017.
 */
public class Filters {
    private int height;
    private int width;
    private int[] pixels;
    private int[] newPixels;

    private int[][] sharpness = {{0, (-1), 0}, {(-1), 5, (-1)}, {0, (-1), 0}};
    private double[][] blur = {{0, 0.17, 0}, {0.17, 0.3, 0.17}, {0, 0.17, 0}};
    private int[][] emboss = {{-1, -1, 0}, {-1, 0, 1}, {0, 1, 1}};
    private double[][] wsharpness = {{-0.1, -0.1, -0.1}, {-0.1, 1.8, -0.1}, {-0.1, -0.1, -0.1}};
    private int[][] contour = {{1, 0, 1}, {0, -4, 0}, {1, 0, 1}};
    private double[][] rotation;
    private int[][] orderedDither = {{0,8,2,10},{12,4,14,6},{3,11,1,9},{15,7,13,5}};

    private int roberts = 80;
    private int sobel = 200;
    double gamma = 1.3;
    int Nr = 5, Ng = 5, Nb = 5;

    public int getPixel(int x, int y){
        return pixels[y*width+x];
    }
    public int getRed(int color){
        return color >> 16;
    }
    public int getGreen(int color){
        return (color >> 8) & 0xFF;
    }
    public int getBlue(int color){
        return color  & 0xFF;
    }

    public Filters(BufferedImage image){
        BufferedImage img = image;
        this.height = img.getHeight();
        this.width = img.getWidth();
        this.pixels = copyFromBufferedImage(img);
        this.newPixels = copyFromBufferedImage(img);
    }

    private BufferedImage copyToBufferedImage()  {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                bi.setRGB(j, i, newPixels[i*width +j]);
        return bi;
    }

    private int[] copyFromBufferedImage(BufferedImage bi)  {
        int[] pict = new int[height*width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                pict[i*width + j] = bi.getRGB(j, i) & 0xFFFFFF;
        return pict;
    }

    public BufferedImage convertToBlackAndWhite() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int intens = (getRed(pixels[i * width + j]) +
                        getGreen(pixels[i * width + j]) +
                        getBlue(pixels[i * width + j])) / 3;
                newPixels[i * width + j] = intens + (intens << 8) + (intens << 16);
            }
        return copyToBufferedImage();
    }

    public BufferedImage  convertToNegative() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                newPixels[i*width + j] = ~pixels[i*width + j] & 0xFFFFFF;
        return copyToBufferedImage();
    }

    public BufferedImage ditherFloydSteinberg(){

        int error[][] = new int[width*height][3];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++) {
                newPixels[i * width + j] = 255 + (255 << 8) + (255 << 16);
                error[i * width + j][0] = getRed(getPixel(i,j));
                error[i * width + j][1] = getGreen(getPixel(i,j));
                error[i * width + j][2] = getBlue(getPixel(i,j));
            }
        }

        for (int i = 0; i < height; ++i)
        {
            for (int j = 0; j < width; ++j)
            {
                int newR = convertErrorToColor(error[i * width + j][0], 256, Nr);
                int newG = convertErrorToColor(error[i * width + j][1], 256, Ng);
                int newB = convertErrorToColor(error[i * width + j][2], 256, Nb);

                newPixels[j * width + i] = newB + (newG << 8) + (newR << 16);

                double rE = error[i * width + j][0] - newR;
                double gE = error[i * width + j][1] - newG;
                double bE = error[i * width + j][2] - newB;

                if (j < width - 1)
                {
                    error[i * width + j+1][0] += rE * (7.0/16.0);
                    error[i * width + j+1][1] += gE * (7.0/16.0);
                    error[i * width + j+1][2] += bE * (7.0/16.0);
                }

                if (i < height - 1)
                {
                    if (j > 0)
                    {
                        error[(i+1) * width + j-1][0] += rE * (3.0/16.0);
                        error[(i+1) * width + j-1][1] += gE * (3.0/16.0);
                        error[(i+1) * width + j-1][2] += bE * (3.0/16.0);
                    }

                    error[(i+1) * width + j][0] += rE * (5.0/16.0);
                    error[(i+1) * width + j][1] += gE * (5.0/16.0);
                    error[(i+1) * width + j][2] += bE * (5.0/16.0);

                    if (j < width - 1)
                    {
                        error[(i+1) * width + j+1][0] += rE * (1.0/16.0);
                        error[(i+1) * width + j+1][1] += gE * (1.0/16.0);
                        error[(i+1) * width + j+1][2] += bE * (1.0/16.0);
                    }
                }
            }
        }
        return copyToBufferedImage();
    }

    public BufferedImage convertToSharpness() {
        for (int i = 1; i < height-1; i++) {
            for (int j = 1; j < width - 1; j++) {
                int red = 0;
                int green = 0;
                int blue = 0;
                for (int k = 0; k < 3; ++k) {
                    for (int l = 0; l < 3; ++l) {

                        red += getRed(pixels[(i - 1 + k) * width + (j - 1 + l)]) * sharpness[l][k];
                        green += getGreen(pixels[(i - 1 + k) * width + (j - 1 + l)]) * sharpness[l][k];
                        blue += getBlue(pixels[(i - 1 + k) * width + (j - 1 + l)]) * sharpness[l][k];
                    }
                }
                if (red < 0)
                    red = 0;
                if (red > 255)
                    red = 255;
                if (green < 0)
                    green = 0;
                if (green > 255)
                    green = 255;
                if (blue < 0)
                    blue = 0;
                if (blue > 255)
                    blue = 255;
                newPixels[i * width + j] = blue + (green << 8) + (red << 16);
            }
        }
        return copyToBufferedImage();
    }

    public BufferedImage convertToBlurs() {
        for (int i = 1; i < height-1; i++) {
            newPixels[i * width] = 0 + (0 << 8) + (0 << 16);
            newPixels[i * width + width-1] = 0 + (0 << 8) + (0 << 16);
            for (int j = 1; j < width - 1; j++) {
                int red = 0;
                int green = 0;
                int blue = 0;
                for (int k = 0; k < 3; ++k) {
                    for (int l = 0; l < 3; ++l) {

                        red += getRed(pixels[(i - 1 + k) * width + (j - 1 + l)]) * blur[l][k];
                        green += getGreen(pixels[(i - 1 + k) * width + (j - 1 + l)]) * blur[l][k];
                        blue += getBlue(pixels[(i - 1 + k) * width + (j - 1 + l)]) * blur[l][k];
                    }
                }
                if (red < 0)
                    red = 0;
                if (red > 255)
                    red = 255;
                if (green < 0)
                    green = 0;
                if (green > 255)
                    green = 255;
                if (blue < 0)
                    blue = 0;
                if (blue > 255)
                    blue = 255;
                newPixels[i * width + j] = blue + (green << 8) + (red << 16);
            }
        }
        return copyToBufferedImage();
    }

    public BufferedImage convertToEmboss() {
        for (int i = 1; i < height-1; i++) {
            newPixels[i * width] = 0 + (0 << 8) + (0 << 16);
            newPixels[i * width + width-1] = 0 + (0 << 8) + (0 << 16);
            for (int j = 1; j < width - 1; j++) {
                int red = 0;
                int green = 0;
                int blue = 0;
                for (int k = 0; k < 3; ++k) {
                    for (int l = 0; l < 3; ++l) {

                        red += getRed(pixels[(i - 1 + k) * width + (j - 1 + l)]) * emboss[l][k];
                        green += getGreen(pixels[(i - 1 + k) * width + (j - 1 + l)]) * emboss[l][k];
                        blue += getBlue(pixels[(i - 1 + k) * width + (j - 1 + l)]) * emboss[l][k];
                    }
                }
                int intens = (red+ blue+ green + 384)/3;
                if (intens > 255)
                    intens = 255;
                if(intens < 0)
                    intens = 0;
                newPixels[i * width + j] = intens + (intens << 8) + (intens << 16);
            }
        }
        return copyToBufferedImage();
    }

    public BufferedImage convertToWatercolor() {
        int[] aa = newPixels;
        for (int i = 2; i < height-2; i++) {
            for (int j = 2; j < width - 2; j++) {
                int redL[] = new int[25];
                int greenL[] = new int[25];
                int blueL[] = new int[25];
                for (int k = 0; k < 5; ++k) {
                    for (int l = 0; l < 5; ++l) {
                        redL[k*5 + l] = getRed(pixels[(i - 2 + k) * width + (j - 2 + l)]);
                        greenL[k*5 + l] = getGreen(pixels[(i - 2 + k) * width + (j - 2 + l)]);
                        blueL[k*5 + l] = getBlue(pixels[(i - 2 + k) * width + (j - 2 + l)]);
                    }
                }
                bubbleSort(redL);
                bubbleSort(greenL);
                bubbleSort(blueL);

                aa[i * width + j] = blueL[13] + (greenL[13] << 8) + (redL[13] << 16);
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int k = 0; k < 3; ++k) {
                    for (int l = 0; l < 3; ++l) {

                        red += getRed(aa[(i - 1 + k) * width + (j - 1 + l)]) * wsharpness[l][k];
                        green += getGreen(aa[(i - 1 + k) * width + (j - 1 + l)]) * wsharpness[l][k];
                        blue += getBlue(aa[(i - 1 + k) * width + (j - 1 + l)]) *wsharpness[l][k];
                    }
                }
                if (red < 0)
                    red = 0;
                if (red > 255)
                    red = 255;
                if (green < 0)
                    green = 0;
                if (green > 255)
                    green = 255;
                if (blue < 0)
                    blue = 0;
                if (blue > 255)
                    blue = 255;
                newPixels[i * width + j] = (int)blue + ((int)green << 8) + ((int)red << 16);
            }
        }
        return copyToBufferedImage();
    }

    public BufferedImage convertToDouble() {
        int x = 1;
        for (int i = height/4+1; i < 3*height/4; i++) {
            int y = 1;
            for (int j = width/4+1; j < 3*width/4; j++) {
                int pixL[] = new int[9];
                for (int k = 0; k < 3; ++k) {
                    for (int l = 0; l < 3; ++l) {
                        pixL[k*3 + l] = pixels[(i - 1 + k) * width + (j - 1 + l)];
                    }
                }
                int E0, E1, E2, E3;
                if (pixL[1] != pixL[7] && pixL[3] != pixL[5]) {
                    E0 = pixL[3] == pixL[1] ? pixL[3] : pixL[4];
                    E1 = pixL[1] == pixL[5] ? pixL[5] : pixL[4];
                    E2 = pixL[3] == pixL[7] ? pixL[3] : pixL[4];
                    E3 = pixL[7] == pixL[5] ? pixL[5] : pixL[4];
                } else {
                    E0 = pixL[4];
                    E1 = pixL[4];
                    E2 = pixL[4];
                    E3 = pixL[4];
                }
                newPixels[(x*2-1) * width + y*2-1] = getBlue(E0) + (getGreen(E0) << 8) + (getRed(E0) << 16);
                newPixels[(x*2-1) * width + y*2] = getBlue(E1) + (getGreen(E1) << 8) + (getRed(E1) << 16);
                newPixels[x*2 * width + y*2-1] = getBlue(E2) + (getGreen(E2) << 8) + (getRed(E2) << 16);
                newPixels[x*2 * width + y*2] = getBlue(E3) + (getGreen(E3) << 8) + (getRed(E3) << 16);
                y++;
            }
            x++;
        }
        return copyToBufferedImage();
    }

    public BufferedImage convertToContour() {
        for (int i = 1; i < height-1; i++) {
            newPixels[i * width] = 0 + (0 << 8) + (0 << 16);
            newPixels[i * width + width-1] = 0 + (0 << 8) + (0 << 16);
            for (int j = 1; j < width - 1; j++) {
                int red = 0;
                int green = 0;
                int blue = 0;
                for (int k = 0; k < 3; ++k) {
                    for (int l = 0; l < 3; ++l) {

                        red += getRed(pixels[(i - 1 + k) * width + (j - 1 + l)]) *contour[l][k];
                        green += getGreen(pixels[(i - 1 + k) * width + (j - 1 + l)]) * contour[l][k];
                        blue += getBlue(pixels[(i - 1 + k) * width + (j - 1 + l)]) * contour[l][k];
                    }
                }
                if (red < 0)
                    red = 0;
                if (red > 255)
                    red = 255;
                if (green < 0)
                    green = 0;
                if (green > 255)
                    green = 255;
                if (blue < 0)
                    blue = 0;
                if (blue > 255)
                    blue = 255;
                newPixels[i * width + j] = blue + (green << 8) + (red << 16);
            }
        }
        return copyToBufferedImage();
    }

    public BufferedImage convertToRotation(double gam) {

        setRotation(gam);
        for (int row = 0; row < height; ++row)
        {
            for (int col = 0; col < width; ++col)
            {
                newPixels[row * width + col] = 255 + (255 << 8) + (255 << 16);
            }
        }

        double centerRow = (height - 1) / 2.0;
        double centerCol = (width - 1) / 2.0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                int x = (int)Math.round(rotation[0][0] * (j - centerCol) + rotation[0][1] * (i - centerRow) + centerCol);
                int y = (int)Math.round(rotation[1][0] * (j - centerCol) + rotation[1][1] * (i - centerRow) + centerRow);

                if ((x >= 0) && (x < width) && (y >= 0) && (y < height))
                {
                    newPixels[i * width + j] = getBlue(getPixel(x,y)) + (getGreen(getPixel(x,y)) << 8) + (getRed(getPixel(x,y)) << 16);
                }
            }
        }
        return copyToBufferedImage();
    }

    public BufferedImage convertToRoberts() {

        int aa[] = newPixels;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int intens = (getRed(pixels[i * width + j]) +
                        getGreen(pixels[i * width + j]) +
                        getBlue(pixels[i * width + j])) / 3;
                aa[i * width + j] = intens;
            }

        for (int i = 1; i < height-1; i++) {
            for (int j = 1; j < width - 1; j++) {
                int inten = 0;
                inten = Math.abs(aa[(i)* width + (j)] - aa[(i+1)* width + (j+1)]) +
                        Math.abs(aa[(i)* width + (j+1)] - aa[(i+1)* width + (j)]);

                if (inten > roberts)
                    inten = 255;
                newPixels[i * width + j] = inten + (inten << 8) + (inten << 16);
            }
        }
        return copyToBufferedImage();
    }

    public BufferedImage convertToSobel() {

        int aa[] = new int[512*512];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int intens = (getRed(pixels[i * width + j]) +
                        getGreen(pixels[i * width + j]) +
                        getBlue(pixels[i * width + j])) / 3;
                aa[i * width + j] = intens;
            }


        for (int i = 1; i < height-1; i++) {
            for (int j = 1; j < width - 1; j++) {

                int aA = aa[(i - 1) * width + (j - 1)];
                int bB = aa[(i - 1) * width + (j)];
                int cC = aa[(i - 1) * width + (j + 1)];
                int dD = aa[(i) * width + (j - 1)];
                int eE = aa[(i) * width + (j)];
                int fF = aa[(i) * width + (j + 1)];
                int gG = aa[(i + 1) * width + (j - 1)];
                int hH = aa[(i + 1) * width + (j)];
                int kK = aa[(i + 1) * width + (j + 1)];

                int sx = cC + 2*fF + kK - aA - 2*dD - gG;
                int sy = gG + 2*hH + kK - aA - 2*bB - cC;

                int s = Math.abs(sx) + Math.abs(sy);
                if (s > sobel)
                    s = 255;
                else
                    s = 0;
                newPixels[i * width + j] = s + (s << 8) + (s << 16);
            }
        }
        return copyToBufferedImage();
    }

    public BufferedImage convertToGamma(double gam) {

        gamma = gam;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int red =  (int) (Math.pow(getRed(pixels[i * width + j])/256.0, gamma)*256);
                int green = (int) (Math.pow(getGreen(pixels[i * width + j])/256.0, gamma)*256);
                int blue = (int) (Math.pow(getBlue(pixels[i * width + j])/256.0, gamma)*256);
                if (red > 255)
                    red = 255;
                if (green > 255)
                    green = 255;
                if (blue > 255)
                    blue = 255;
                newPixels[i * width + j] = blue + (green << 8) + (red << 16);
            }
        return copyToBufferedImage();
    }

    public BufferedImage convertToOrderedDither() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int red = getRed(pixels[(i) * width + (j)]);
                int green = getGreen(pixels[(i) * width + (j)]);
                int blue = getBlue(pixels[(i) * width + (j)]);

                if(red > orderedDither[i%4][j%4]*16)
                    red = 215;
                else
                    red = 40;
                if(green > orderedDither[i%4][j%4]*16)
                    green = 215;
                else
                    green = 40;
                if(blue > orderedDither[i%4][j%4]*16)
                    blue = 215;
                else
                    blue = 40;

                newPixels[i * width + j] = blue + (green << 8) + (red << 16);
            }
        }
        return copyToBufferedImage();
    }

    private void bubbleSort(int[] arr) {
        for (int i = arr.length - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int t = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = t;
                }
            }
        }
    }

    public void setNewImage(BufferedImage image) {
        BufferedImage img = image;
        this.height = img.getHeight();
        this.width = img.getWidth();
        this.pixels = copyFromBufferedImage(img);
        this.newPixels = copyFromBufferedImage(img);
    }

    public void setRotation(double angle){
        angle = Math.toRadians(angle);
        rotation = new double[][] { {Math.cos(-angle), -Math.sin(-angle)}, {Math.sin(-angle), Math.cos(-angle)} };
    }

    private int convertErrorToColor(double error, int old, int newCount)
    {
        double delta = (old - 1.0) / (newCount - 1.0);

        return (int)(delta * (int)(error / delta + 0.5));
    }

}
