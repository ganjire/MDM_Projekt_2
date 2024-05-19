package ch.zhaw.deeplearningjava.clothing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class IDXtoImageConverter {
    public static void main(String[] args) throws IOException {
        String basePath = "/Users/rebecca/Desktop/ZHAW/Frühlingssemester2024/MDM/Projekt3/djl_clothing_classification/archive/";
        convert(basePath + "dataset-images-idx3-ubyte", basePath + "dataset-labels-idx1-ubyte", basePath + "images");
    }

    public static void convert(String idxImagesPath, String idxLabelsPath, String outputPath) throws IOException {
        ByteBuffer imgData = loadFile(idxImagesPath);
        ByteBuffer labelData = loadFile(idxLabelsPath);

        int numItems = imgData.getInt(4);
        int numRows = imgData.getInt(8);
        int numColumns = imgData.getInt(12);

        imgData.position(16);
        labelData.position(8);

        for (int i = 0; i < numItems; i++) {
            BufferedImage image = new BufferedImage(numColumns, numRows, BufferedImage.TYPE_BYTE_GRAY);
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numColumns; col++) {
                    int gray = 255 - (imgData.get() & 0xFF);
                    image.setRGB(col, row, (gray << 16) | (gray << 8) | gray);
                }
            }
            int label = labelData.get() & 0xFF;
            File dir = new File(outputPath + "/" + label);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            ImageIO.write(image, "png", new File(dir, "image_" + i + ".png"));
        }
    }

    private static ByteBuffer loadFile(String filePath) throws IOException {
        try (FileChannel fc = new FileInputStream(filePath).getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fc.size());
            fc.read(byteBuffer);
            byteBuffer.flip();
            return byteBuffer;
        }
    }
}
