package org.gmdev.pdftrick.rendering.imagereader;

import java.awt.image.BufferedImage;

public class ImageAttributes {

    private final BufferedImage bufferedImage;
    private final String extension;
    private final String key;

    public ImageAttributes(BufferedImage bufferedImage, String extension, String key) {
        this.bufferedImage = bufferedImage;
        this.extension = extension;
        this.key = key;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public String getExtension() {
        return extension;
    }

    public String getKey() {
        return key;
    }
}
