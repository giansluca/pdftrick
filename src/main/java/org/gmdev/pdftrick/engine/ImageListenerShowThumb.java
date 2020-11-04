package org.gmdev.pdftrick.engine;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.imageio.IIOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.engine.ImageAttr.InlineImage;
import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageInline;
import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageNormal;
import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.render.ImageAction;
import org.gmdev.pdftrick.utils.external.CustomExtraImgReader;
import org.gmdev.pdftrick.utils.Utils;

import com.itextpdf.text.exceptions.UnsupportedPdfException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class ImageListenerShowThumb implements RenderListener {
	
	private static final Logger logger = Logger.getLogger(ImageListenerShowThumb.class);
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	
	private int numImg;
	private int unsupportedImage;
	private final int numPage;
	private final UpdatePanelCenter upPcenter;
	private int inlineImageCounter;
	
	public ImageListenerShowThumb(int numPage) {
		this.numImg = 0;
		this.unsupportedImage = 0;
		this.numPage = numPage;
		this.upPcenter = new UpdatePanelCenter();
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
		final HashMap<Integer, String> rotationFromPages = factory.getRotationFromPages();
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
						buffImg = CustomExtraImgReader.readIndexedPNG(renderInfo.getRef().getNumber(), factory.getPdfFilePath());
					}
				} catch (Exception e) {
					logger.error("Exception", e);
					unsupportedImage++;
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
						logger.error("Exception", e);
						unsupportedImage++;
						return;
					}
				} catch (Exception e) {
					logger.error("Exception", e);
					unsupportedImage++;
					return;
				}
				
				// check if image contains a mask image
				BufferedImage buffMask = null;
				PdfDictionary imageDictionary = image.getDictionary(); 
			    PRStream maskStream = (PRStream) imageDictionary.getAsStream(PdfName.SMASK); 
			    
			    if (maskStream != null) { 
			    	// if i have an mask object i check that is not a jpeg format, because this may cause some problem on off screen rendering
			    	// usually all images with mask are png ... and there aren't problems, if image is jpg i discard the mask :)
			        if (!image.getFileType().equalsIgnoreCase("jpg") && buffPic != null) {
			        	PdfImageObject maskImage = new PdfImageObject(maskStream);
			        	buffMask = maskImage.getBufferedImage();
			        	Image img = Utils.TransformGrayToTransparency(buffMask);
			        	buffImg = Utils.ApplyTransparency(buffPic, img);
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
				buffImg = Utils.adjustImage(buffImg, flip, rotate);
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
						buffImg = Utils.getScaledImagWithScalr(buffImg, scaledW, scaledH);
					
					} else {
						faktor = 160 / (double)h;
						int scaledW = (int) Math.round(faktor * w);
						int scaledH = (int) Math.round(faktor * h);
						buffImg = Utils.getScaledImagWithScalr(buffImg, scaledW, scaledH);
					}
				}
				
				numImg ++;
				// dynamic update on the center panel (under EDT thread). UpdatePanelCenter upPcenter = this.upPcenter;
				upPcenter.setInlineImg(isInline);
				upPcenter.setBuffImg(buffImg);
				upPcenter.setImageAttrs(imageAttrs);
				
				try {
					SwingUtilities.invokeAndWait(upPcenter);
				} catch (InterruptedException e) {
					logger.error("Exception", e);
				} catch (InvocationTargetException e) {
					logger.error("Exception", e);
				}
				
				buffImg.flush();
			} else {
				unsupportedImage++;
			}
		} catch (IOException e) {
			logger.error("Exception", e);
			unsupportedImage++;
		} catch (Exception e) {
			logger.error("Exception", e);
			unsupportedImage++;
		}
	}
	
	public int getNumImg() {
		return numImg;
	}
	
	public void setNumImg(int numImg) {
		this.numImg = numImg;
	}

	public int getUnsupportedImage() {
		return unsupportedImage;
	}
	
	public void setUnsupportedImage(int unsupportedImage) {
		this.unsupportedImage = unsupportedImage;
	}
	
	public class UpdatePanelCenter implements Runnable {
		private final Border borderGray = BorderFactory.createLineBorder(Color.gray);
		private final Border borderOrange = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.orange);
		
		private final HashMap<String, RenderedImageAttributes> imageSelected;
		private final HashMap<String, RenderedImageAttributes> inlineImgSelected;
		private final JPanel centerPanel;
		
		private boolean isInlineImg;
		private BufferedImage buffImg;
		private RenderedImageAttributes imageAttrs;
		
		public UpdatePanelCenter() {
			this.imageSelected = factory.getImageSelected();
			this.inlineImgSelected = factory.getInlineImgSelected();
			this.centerPanel = factory.getUserInterface().getCenter().getCenterPanel();
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

