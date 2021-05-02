package org.gmdev.pdftrick.rendering;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.imageio.IIOException;
import javax.swing.*;
import javax.swing.border.Border;

import org.gmdev.pdftrick.rendering.ImageAttr.*;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.ui.actions.ImageAction;
import org.gmdev.pdftrick.utils.*;
import org.gmdev.pdftrick.utils.external.CustomExtraImgReader;

import com.itextpdf.text.exceptions.UnsupportedPdfException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;

public class PageThumbnailsDisplay implements RenderListener {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
	
	private int imageNumber;
	private int unsupportedImages;
	private final int numPage;
	private final UpdatePanelCenter updatePanelCenter;
	private int inlineImageCounter;
	
	public PageThumbnailsDisplay(int numPage) {
		this.imageNumber = 0;
		this.unsupportedImages = 0;
		this.numPage = numPage;
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
	
	/**
	 * extracts the images contained in selected page and convert it in a BufferedImage to add and show to the center panel.
	 */
	private void render(ImageRenderInfo renderInfo ) {
		final HashMap<Integer, String> rotationFromPages = bag.getPagesRotationPages();
		boolean isInline = false;
		
		PdfImageObject image = null;
		try {
			BufferedImage buffImg = null;
			
			if (renderInfo.getRef() == null) {
				isInline = true;
				inlineImageCounter += 1;
			}
			
			try {
				image = renderInfo.getImage();
			} catch (UnsupportedPdfException updfe)  {
				try {
					if (isInline) {
						buffImg = null;
					} else {
						buffImg = CustomExtraImgReader.readIndexedPNG(renderInfo.getRef().getNumber(), bag.getSavedFilePath());
					}
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
						buffPic = CustomExtraImgReader.readJBIG2(image);
					} else {
						buffPic = image.getBufferedImage();
					}
				} catch (IIOException iioex) {
					byte[] imageByteArray = image.getImageAsBytes();
					
					try {
						buffPic = CustomExtraImgReader.readCMYK_JPG(imageByteArray);
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
			        	Image img = ImageUtils.TransformGrayToTransparency(buffMask);
			        	buffImg = ImageUtils.ApplyTransparency(buffPic, img);
			        } else {
			        	buffImg = buffPic;
			        }
			    } else {
			    	buffImg = buffPic;
			    }	
			}
			
			String flip = "";
			String rotate = "";
			Matrix matrix = renderInfo.getImageCTM();
			String angle = ""+rotationFromPages.get(numPage);
			
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
			
			if (angle.equalsIgnoreCase("270") || (""+i21).charAt(0) =='-' ) {
				rotate = "270";
			}
			else if (angle.equalsIgnoreCase("180")) {
				rotate = "180";
			}
			else if (angle.equalsIgnoreCase("90") || (""+i12).charAt(0) =='-') {
				rotate = "90";
			}
			
			if (buffImg != null) {
				buffImg = ImageUtils.adjustImage(buffImg, flip, rotate);
				RenderedImageAttributes imageAttrs = null;
				
				if (isInline) {
					// set up inline image object attributes and store it in a hashmap
					InlineImage inImg = new InlineImage(buffImg, image!=null?image.getFileType():"png");
					imageAttrs = new RenderedImageInline(inlineImageCounter, inImg, numPage, flip, rotate);
				} else {
					// set up image object for normal images
					imageAttrs = new RenderedImageNormal(numPage, renderInfo.getRef().getNumber(), flip, rotate);
				}
				
				// scaling image with original aspect ratio (if image exceded pic box)
				int w = buffImg.getWidth();
				int h = buffImg.getHeight();
				
				if (w > 170 || h > 170) {
					double faktor;
					if (w > h) {
						faktor = 160 / (double)w;
						int scaledW = (int) Math.round(faktor * w);
						int scaledH = (int) Math.round(faktor * h);
						buffImg = ImageUtils.getScaledImageWithScalr(buffImg, scaledW, scaledH);
					
					} else {
						faktor = 160 / (double)h;
						int scaledW = (int) Math.round(faktor * w);
						int scaledH = (int) Math.round(faktor * h);
						buffImg = ImageUtils.getScaledImageWithScalr(buffImg, scaledW, scaledH);
					}
				}

				imageNumber ++;
				// dynamic update on the center panel (under EDT thread). UpdatePanelCenter upPcenter = this.upPcenter;
				updatePanelCenter.setInlineImg(isInline);
				updatePanelCenter.setBuffImg(buffImg);
				updatePanelCenter.setImageAttrs(imageAttrs);
				
				try {
					SwingUtilities.invokeAndWait(updatePanelCenter);
				} catch (InterruptedException | InvocationTargetException e) {
					throw new IllegalStateException(e);
				}

				buffImg.flush();
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

