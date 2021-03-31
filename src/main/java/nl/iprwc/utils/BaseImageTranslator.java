package nl.iprwc.utils;

import nl.iprwc.exception.InvalidOperationException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class BaseImageTranslator {
    public static String convert(String baseImage) {
        try{
            // tokenize the data
            String base64Image;
            base64Image = baseImage.split(",")[1];
            byte[] imageByte = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

            // create a buffered image
            BufferedImage image;
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();

            image = ImageTransparancy.imageToBufferedImage(
                    ImageTransparancy.makeColorTransparent(image, Color.white)
            );

            // write the image to a file
            ByteArrayOutputStream outputfile = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputfile);
            String result = "data:image/png;base64," + Base64.getEncoder().encodeToString(outputfile.toByteArray());

            return result;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBase64URL(String url) throws IOException {

        try {
            return Base64.getEncoder().encodeToString(translateURL(url));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] translateURL(String url) throws IOException {
        URL imageUrl = new URL(url);
        URLConnection ucon = imageUrl.openConnection();
        InputStream is = ucon.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = is.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, read);
        }
        baos.flush();
        return baos.toByteArray();
    }


}
