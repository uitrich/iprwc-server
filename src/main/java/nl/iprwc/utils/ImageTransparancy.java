package nl.iprwc.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

/**
 * Class containing code for converting an image's white background to be
 * transparent, adapted with minor changes from StackOverflow thread "How to
 * make a color transparent in a BufferedImage and save as PNG"
 * (http://stackoverflow.com/questions/665406/how-to-make-a-color-transparen...).
 */
public class ImageTransparancy
{

    /**
     * Convert Image to BufferedImage.
     *
     * @param image Image to be converted to BufferedImage.
     * @return BufferedImage corresponding to provided Image.
     */
    public static BufferedImage imageToBufferedImage(final Image image)
    {
        final BufferedImage bufferedImage =
                new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }
    public static Image makeColorTransparent(BufferedImage im, final Color color, float threshold) {
        ImageFilter filter = new RGBImageFilter() {
            public float markerAlpha = color.getRGB() | 0xFF000000;
            public final int filterRGB(int x, int y, int rgb) {
                int currentAlpha = rgb | 0xFF000000;           // just to make it clear, stored the value in new variable
                float diff = Math.abs((currentAlpha - markerAlpha) / markerAlpha);  // Now get the difference
                if (diff <= threshold) {                      // Then compare that threshold value
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
}