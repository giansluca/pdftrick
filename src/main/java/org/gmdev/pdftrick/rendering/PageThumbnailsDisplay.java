package org.gmdev.pdftrick.rendering;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.imageio.IIOException;
import javax.swing.*;
import javax.swing.border.Border;

import org.gmdev.pdftrick.rendering.ImageAttr.*;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.ui.actions.ImageAction;
import org.gmdev.pdftrick.utils.*;
import org.gmdev.pdftrick.utils.external.CustomImageReader;

import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;

public class PageThumbnailsDisplay implements RenderListener {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
	
	private int imageNumber;
	private int unsupportedImages;
	private final int pageNumber;
	private final UpdatePanelCenter updatePanelCenter;
	private int inlineImageCounter;
	
	public PageThumbnailsDisplay(int pageNumber) {
		this.imageNumber = 0;
		this.unsupportedImages = 0;
		this.pageNumber = pageNumber;
		this.updatePanelCenter = new UpdatePanelCenter();
		this.inlineImageCounter = 0;
	}
	
	@Override
	public void beginTextBlock() {
	}
	
	@Override
	public void endTextBlock() {
	}
	
	@Override
	public void renderText(TextRenderInfo renderInfo) {
	}
	
	@Override
	public void renderImage(ImageRenderInfo renderInfo) {
		this.render(renderInfo);
	}

	private Optional<PdfImageObject> getImage(ImageRenderInfo renderInfo) {
		try {
			return Optional.of(renderInfo.getImage());
		} catch (IOException e) {
			return Optional.empty();
		}
	}

	private boolean isInlineImage(ImageRenderInfo renderInfo) {
		return renderInfo.getRef() == null;
	}

	private void render(ImageRenderInfo renderInfo ) {
		HashMap<Integer, String> pagesRotation = bag.getPagesRotation();

		boolean isInline = isInlineImage(renderInfo);
		PdfImageObject image = getImage(renderInfo).orElse(null);

		try {
			BufferedImage bufferedImageImg = null;

			if (isInline) inlineImageCounter += 1;
			if (image == null) {
				try {
					bufferedImageImg = CustomImageReader.readIndexedPNG(
									renderInfo.getRef().getNumber(),
									bag.getSavedFilePath());

				} catch (Exception e) {
					unsupportedImages++;
					return;
				}
			}
			
			if (image != null) {
				BufferedImage buffPic = null;
				
				try {
					// if image is JBIG2 type i need a custom way and using jbig2 ImageIO plugin
					if ( image.getFileType().equalsIgnoreCase("JBIG2") ) {
						buffPic = CustomImageReader.readJBIG2(image);
					} else {
						buffPic = image.getBufferedImage();
					}
				} catch (IIOException iioex) {
					byte[] imageByteArray = image.getImageAsBytes();
					
					try {
						buffPic = CustomImageReader.readCMYK_JPG(imageByteArray);
					} catch (Exception e) {
						unsupportedImages++;
						return;
					}
				} catch (Exception e) {
					unsupportedImages++;
					return;
				}
				
				// isValid if image contains a mask image
				BufferedImage buffMask = null;
				PdfDictionary imageDictionary = image.getDictionary(); 
			    PRStream maskStream = (PRStream) imageDictionary.getAsStream(PdfName.SMASK); 
			    
			    if (maskStream != null) { 
			    	// if i have an mask object i isValid that is not a jpeg format, because this may cause some problem on off screen rendering
			    	// usually all images with mask are png ... and there aren't problems, if image is jpg i discard the mask :)
			        if (!image.getFileType().equalsIgnoreCase("jpg") && buffPic != null) {
			        	PdfImageObject maskImage = new PdfImageObject(maskStream);
			        	buffMask = maskImage.getBufferedImage();
			        	Image img = ImageUtils.transformGrayToTransparency(buffMask);
			        	bufferedImageImg = ImageUtils.applyTransparency(buffPic, img);
			        } else {
			        	bufferedImageImg = buffPic;
			        }
			    } else {
			    	bufferedImageImg = buffPic;
			    }	
			}
			
			String flip = "";
			String rotate = "";
			Matrix matrix = renderInfo.getImageCTM();
			String pageRotation = "" + pagesRotation.get(pageNumber);
			
			// experimental 
			float i11 = matrix.get(Matrix.I11);	// if negative -> horizontal flip
			float i12 = matrix.get(Matrix.I12);	// if negative -> 90 degree rotation
			float i21 = matrix.get(Matrix.I21); // if negative -> 270 degree rotation
			float i22 = matrix.get(Matrix.I22); // if negative -> vertical flip
			
			// flip and rotation ... from matrix if i11 or i22 is negative i have to flip image
			if ( (""+i11).charAt(0) =='-' ) {
				flip = "fh";
			} 
			else if ((""+i22).charAt(0) =='-') {
				flip = "fv";
			}
			
			if (pageRotation.equalsIgnoreCase("270") || (""+i21).charAt(0) =='-' ) {
				rotate = "270";
			}
			else if (pageRotation.equalsIgnoreCase("180")) {
				rotate = "180";
			}
			else if (pageRotation.equalsIgnoreCase("90") || (""+i12).charAt(0) =='-') {
				rotate = "90";
			}
			
			if (bufferedImageImg != null) {
				bufferedImageImg = ImageUtils.adjustImage(bufferedImageImg, flip, rotate);
				RenderedImageAttributes imageAttrs = null;
				
				if (isInline) {
					// set up inline image object attributes and store it in a hashmap
					InlineImage inImg = new InlineImage(bufferedImageImg, image!=null?image.getFileType():"png");
					imageAttrs = new RenderedImageInline(inlineImageCounter, inImg, pageNumber, flip, rotate);
				} else {
					// set up image object for normal images
					imageAttrs = new RenderedImageNormal(pageNumber, renderInfo.getRef().getNumber(), flip, rotate);
				}
				
				// scaling image with original aspect ratio (if image exceded pic box)
				int w = bufferedImageImg.getWidth();
				int h = bufferedImageImg.getHeight();
				
				if (w > 170 || h > 170) {
					double faktor;
					if (w > h) {
						faktor = 160 / (double)w;
						int scaledW = (int) Math.round(faktor * w);
						int scaledH = (int) Math.round(faktor * h);
						bufferedImageImg = ImageUtils.getScaledImageWithScalr(bufferedImageImg, scaledW, scaledH);
					
					} else {
						faktor = 160 / (double)h;
						int scaledW = (int) Math.round(faktor * w);
						int scaledH = (int) Math.round(faktor * h);
						bufferedImageImg = ImageUtils.getScaledImageWithScalr(bufferedImageImg, scaledW, scaledH);
					}
				}

				imageNumber ++;
				// dynamic update on the center panel (under EDT thread). UpdatePanelCenter upPcenter = this.upPcenter;
				updatePanelCenter.setInlineImg(isInline);
				updatePanelCenter.setBuffImg(bufferedImageImg);
				updatePanelCenter.setImageAttrs(imageAttrs);
				
				try {
					SwingUtilities.invokeAndWait(updatePanelCenter);
				} catch (InterruptedException | InvocationTargetException e) {
					throw new IllegalStateException(e);
				}

				bufferedImageImg.flush();
			} else {
				unsupportedImages++;
			}
		} catch (Exception e) {
			unsupportedImages++;
		}
	}
	
	public int getImageNumber() {
		return imageNumber;
	}

	public int getUnsupportedImages() {
		return unsupportedImages;
	}
	
	public static class UpdatePanelCenter implements Runnable {
		private final Border borderGray = BorderFactory.createLineBorder(Color.gray);
		private final Border borderOrange = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.orange);
		
		private final HashMap<String, RenderedImageAttributes> imageSelected;
		private final HashMap<String, RenderedImageAttributes> inlineImgSelected;
		private final JPanel centerPanel;
		
		private boolean isInlineImg;
		private BufferedImage buffImg;
		private RenderedImageAttributes imageAttrs;
		
		public UpdatePanelCenter() {
			this.imageSelected = bag.getSelectedImages();
			this.inlineImgSelected = bag.getInlineSelectedImages();
			this.centerPanel = bag.getUserInterface().getCenter().getCenterPanel();
		}
		
		@Override
		public void run() {
			JLabel picLabel = new JLabel(new ImageIcon(buffImg));
			picLabel.setPreferredSize(new Dimension(170, 170));
			
			boolean selected = false;
			
			if (imageSelected.containsKey(imageAttrs.getKey()) || inlineImgSelected.containsKey(imageAttrs.getKey())) {
				picLabel.setBorder(borderOrange);
				selected = true;
			} else {
				picLabel.setBorder(borderGray);
			}
			
			picLabel.addMouseListener(new ImageAction(picLabel, imageAttrs, isInlineImg, selected));
			picLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			
			centerPanel.add(picLabel);
			centerPanel.revalidate();
			centerPanel.repaint();
		}
		
		public void setInlineImg(boolean isInlineImg) {
			this.isInlineImg = isInlineImg;
		}

		public void setBuffImg(BufferedImage buffImg) {
			this.buffImg = buffImg;
		}
		
		public void setImageAttrs(RenderedImageAttributes imageAttrs) {
			this.imageAttrs = imageAttrs;
		}
		
	}

	
}

