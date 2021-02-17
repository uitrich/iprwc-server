package nl.iprwc.utils;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;

public class BaseImageTranslator {
    private static BaseImageTranslator instance;

    static {
        instance = new BaseImageTranslator();
    }
    public static BaseImageTranslator getInstance() {
        return instance;
    }

    private static final int WORKER_THREADS = 2;

    private Thread[] workers = new Thread[WORKER_THREADS];
    private Queue<String> queue = new ConcurrentLinkedQueue<>();

    private BaseImageTranslator() {
        for (int i = 0; i < WORKER_THREADS; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    public void addFile(String baseImage) {
        //System.out.println("Attempting to convert files to Images");
        queue.add(baseImage);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            Worker.stopWorkers = true;
        } finally {
            super.finalize();
        }
    }

    private static class Worker extends Thread {
        static boolean stopWorkers = false;

        @Override
        public void run() {
            try {
                nextFile();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

        private void nextFile() throws InterruptedException {
            String queueItem;
            do {
                queueItem = BaseImageTranslator.getInstance().queue.poll();
                if (null == queueItem){
                    sleep(250);
                } else{
                    convert(queueItem, false);
                }
            } while (!stopWorkers);

            if (stopWorkers) {
                return;
            }


        }

    }
    public static File convert(String baseImage, boolean url) {
        try{
            // tokenize the data
            String base64Image;
            if (!url) base64Image = baseImage.split(",")[1];
            else base64Image = baseImage;
            byte[] imageByte = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

            // create a buffered image
            BufferedImage image = null;

            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(base64Image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();

            image = ImageTransparancy.imageToBufferedImage(ImageTransparancy.makeColorTransparent(image, new Color(Color.white.getRGB())));
            image = ImageTransparancy.imageToBufferedImage(ImageTransparancy.makeColorTransparent(image, new Color(Color.lightGray.getRGB())));

            // write the image to a file
            File outputfile = new File("image.png");
            ImageIO.write(image, "png", outputfile);
            return outputfile;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static File getByteArrayFromImageURL(String url) throws IOException {
        return convert(getBase64URL(url), false);
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
        System.out.println("Ive got an url");
        return baos.toByteArray();
    }


}
