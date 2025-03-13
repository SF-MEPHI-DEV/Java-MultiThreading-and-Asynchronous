package ru.mephi.week4.lesson2.forkjoin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RecursiveActionExample {

    public static class ImageProcessingTask extends RecursiveAction {

        private static final int THRESHOLD = 1_000;
        private BufferedImage image;
        private int startX, startY, endX, endY;
        private int brightnessChange;

        public ImageProcessingTask(BufferedImage image, int startX, int startY, int endX, int endY, int brightnessChange) {
            this.image = image;
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.brightnessChange = brightnessChange;
        }

        @Override
        protected void compute() {
            int area = (endX - startX) * (endY - startY);

            if (area > THRESHOLD) {
                int midX = (startX + endX) / 2;
                int midY = (startY + endY) / 2;

                invokeAll(
                        new ImageProcessingTask(image, startX, startY, midX, midY, brightnessChange),
                        new ImageProcessingTask(image, midX, startY, endX, midY, brightnessChange),
                        new ImageProcessingTask(image, startX, midY, midX, endY, brightnessChange),
                        new ImageProcessingTask(image, midX, midY, endX, endY, brightnessChange)
                );
            } else {
                applyBrightness();
            }
        }

        private void applyBrightness() {
            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    int rgb = image.getRGB(x, y);
                    int r = Math.min(255, Math.max(0, ((rgb >> 16) & 0xFF) + brightnessChange));
                    int g = Math.min(255, Math.max(0, ((rgb >> 8) & 0xFF) + brightnessChange));
                    int b = Math.min(255, Math.max(0, (rgb & 0xFF) + brightnessChange));
                    int newRGB = (r << 16) | (g << 8) | b;
                    image.setRGB(x, y, newRGB);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

        // файл изображения в репозитрии не прикреплён
        BufferedImage image = ImageIO.read(new File("image.png"));

        ForkJoinPool pool = new ForkJoinPool();
        ImageProcessingTask task = new ImageProcessingTask(image, 0, 0, image.getWidth(), image.getHeight(), 50);

        long start = System.currentTimeMillis();
        pool.invoke(task);
        long end = System.currentTimeMillis();

        ImageIO.write(image, "jpg", new File("output.png"));

        System.out.println("Обработка завершена за " + (end - start) + " мс");
    }

}
