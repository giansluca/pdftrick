package org.gmdev.pdftrick.engine.ImageAttr;

import java.awt.image.BufferedImage;

/**
 * @author Gian Luca Mori
 */
public class InlineImage {
	
	private BufferedImage image;
	private String fileType;
	
	public InlineImage (BufferedImage image, String fileType) {
		this.image = image;
		this.fileType = fileType;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public String getFileType() {
		return fileType;
	}
	
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	

}
