package org.gmdev.pdftrick.extraction;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.rendering.imagereader.ImageAttributes;
import org.gmdev.pdftrick.utils.FileUtils;
import org.gmdev.pdftrick.utils.Messages;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

public class ImageExtractor {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    private final Properties messages;
    private final Map<String, ImageAttributes> selectedImages;

    public ImageExtractor() {
        messages = bag.getMessagesProps();
        selectedImages = bag.getSelectedImages();
    }

    public void extract() {
        Messages.append("INFO", messages.getProperty("t_msg_17"));

        Path extractionFolderWithTimePath =
                Path.of(bag.getExtractionFolderPath() +
                        File.separator +
                        FileUtils.getTimeForExtractionFolder());

        File destFolder = extractionFolderWithTimePath.toFile();
        if (!destFolder.mkdir())
            throw new IllegalStateException("error creating extraction folder");

        doExtraction(destFolder.toString());
    }

    private void doExtraction(String destFolder) {
        int i = 1;
        for (ImageAttributes imageAttributes : selectedImages.values()) {
            BufferedImage bufferedImage = imageAttributes.getBufferedImage();

            String extension = imageAttributes.getExtension();
            String filePath = String.format("%s/image-%s.%s", destFolder, i, extension);

            saveImage(bufferedImage, getImageEncode(extension), filePath);
            i++;
        }
    }

    private String getImageEncode(String extension) {
        if (extension.equalsIgnoreCase("jp2")) return "jpeg 2000";
        if (extension.equalsIgnoreCase("jbig2")) return "jpeg";
        return extension;
    }

    private void saveImage(BufferedImage bufferedImage, String imageType, String filePath) {
        try {
            File outputFile = new File(filePath);
            ImageIO.write(bufferedImage, imageType, outputFile);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Error saving image: %s", filePath));
        }
    }


}
