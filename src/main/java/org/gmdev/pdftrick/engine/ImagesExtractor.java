package org.gmdev.pdftrick.engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

import javax.imageio.*;

import org.gmdev.pdftrick.engine.ImageAttr.*;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.*;

import com.itextpdf.text.exceptions.UnsupportedPdfException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import org.gmdev.pdftrick.utils.external.CustomExtraImgReader;

public class ImagesExtractor {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
	
	/**
	 * Prepare for the extraction and call images extractor
	 */
	public void getImages() {
		final Properties messages = bag.getMessagesProps();
		final HashMap<String, RenderedImageAttributes> inlineImgSelected = bag.getInlineSelectedImages();
		final Path pdfFile = bag.getPdfFilePath();
		boolean getImgCheck = false;
	
		Messages.append("INFO", messages.getProperty("tmsg_17"));
			
		Path extractionFolderWithTimePath =
				Path.of(bag.getExtractionFolderPath() +
						File.separator +
						FileUtils.getTimeForExtractionFolder());

		File fileFinalFolderToSave = extractionFolderWithTimePath.toFile();
		fileFinalFolderToSave.mkdir();
			
		getImgCheck = extractImgSel(
				extractionFolderWithTimePath.toString(),
				pdfFile,
				bag.getSelectedImages(),
				inlineImgSelected);
		
		// if extraction breaks ...
		if (!getImgCheck) { 
			Messages.append("WARNING", messages.getProperty("tmsg_18"));
			FileUtils.deleteExtractionFolderAndImages(extractionFolderWithTimePath);
		}			
	}
	
	/**
	 * Extract in line images after selected after normal images extraction 
	 */
	private void extractInlineImgSel(String destFolder,
									 HashMap<String, RenderedImageAttributes> inlineImgSelected,
									 int counter) {

		String result = destFolder + "/" + "Img_%s.%s";
		Set<String> keys = inlineImgSelected.keySet();
		Iterator<String> i = keys.iterator();
		
		int z = counter;
		while (i.hasNext()) {
			String key = i.next();
			RenderedImageAttributes imgAttr = inlineImgSelected.get(key);
			InlineImage inImg = imgAttr.getInlineImage();
			
			String type = inImg.getFileType();
			String filename = String.format(result, z, type);
			
			// particular cases of encoding
			String encode ="";
			if (type.equalsIgnoreCase("jp2")) {
				encode = "jpeg 2000";
			}
			else if (type.equalsIgnoreCase("jbig2")) {
				encode = "jpeg";
				filename = String.format(result, z, "jpg");
			}
			else {
				encode = type;
			}
			
			File outputfile = new File(filename);
			
			try {
				ImageIO.write(inImg.getImage(), encode, outputfile);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			
			z++;
		}
	}
	
	private boolean extractImgSel (String destFolder,
								   Path pdfFile,
								   HashMap<String, RenderedImageAttributes> imageSelected,
								   HashMap<String, RenderedImageAttributes> inlineImgSelected) {
		
		final Properties messages = bag.getMessagesProps();
		String result = destFolder + "/" + "Img_%s.%s";
		PdfReader reader = null;
		boolean retExtract = true;
		
		try {
			reader = new PdfReader(pdfFile.toString());
			Set<String> keys = imageSelected.keySet();
			Iterator<String> i = keys.iterator();
			int z = 1;
			while (i.hasNext()) {
				BufferedImage buff = null;
				String key = i.next();
				
				RenderedImageAttributes imgAttr = imageSelected.get(key);
				String flip = imgAttr.getFlip();
				String rotate = imgAttr.getRotate();
				int ref = imgAttr.getReference();
					
				PdfObject o = reader.getPdfObject(ref);
				PdfStream stream = (PdfStream) o;
				PdfImageObject io = null;
				
				try {
					io = new PdfImageObject((PRStream) stream);
				} catch (UnsupportedPdfException updfe) {
					try {
						buff = CustomExtraImgReader.readIndexedPNG(ref, pdfFile);
						buff = ImageUtils.adjustImage(buff, flip, rotate);
						String type = "png";
						String filename = String.format(result, z, type);
						
						File outputFile = new File(filename);
						ImageIO.write(buff, type, outputFile);
					} catch (Exception e) {
						throw new IllegalStateException(e);
					}
				}
				
				if (io != null) {
					String type = io.getFileType();
					String filename = String.format(result, z, type);
					BufferedImage buffPic = null;
					
					try {
						if (type.equalsIgnoreCase("JBIG2")) {
							buffPic = CustomExtraImgReader.readJBIG2(io);
						} else {
							buffPic = io.getBufferedImage();
						}	
					} catch (IIOException e) {
						byte[] imageByteArray = io.getImageAsBytes();
						
						try {
							buffPic = CustomExtraImgReader.readCMYK_JPG(imageByteArray);
						} catch (Exception ex) {
							throw new IllegalStateException(e);
						}
					}
					
					// isValid if image contains a mask image
					BufferedImage buffMask = null;
					PdfDictionary imageDictionary = io.getDictionary(); 
				    PRStream maskStream = (PRStream) imageDictionary.getAsStream(PdfName.SMASK); 
					
				    if (maskStream != null) {
				    	// i have an smask object i isValid that is not a jpeg format, because this may cause some problem on offscreen rendering
				    	// usually all imges with mask are png ... and there aren't problem, if image is jpg i discard the mask :)
				    	if (!type.equalsIgnoreCase("jpg")) {
				    		PdfImageObject maskImage = new PdfImageObject(maskStream);
				    		buffMask = maskImage.getBufferedImage();
				    
				    		Image img = ImageUtils.TransformGrayToTransparency(buffMask);
				    		buff = ImageUtils.ApplyTransparency(buffPic, img);
				    	} else {
				        	buff = buffPic;
				        }
				    } else {
				    	buff = buffPic;
				    }	
				    
					if (buff != null) {
						buff = ImageUtils.adjustImage(buff, flip, rotate);
						
						//particular cases of encoding
						String encode ="";
						if (type.equalsIgnoreCase("jp2")) {
							encode = "jpeg 2000";
						}
						else if (type.equalsIgnoreCase("jbig2")) {
							encode = "jpeg";
							filename = String.format(result, z, "jpg");
						}
						else {
							encode = type;
						}
						
						File outputfile = new File(filename);
						ImageIO.write(buff, encode, outputfile);
					}
				}				
				z++;
			}
			
			reader.close();
			if (inlineImgSelected.size() > 0) {
				extractInlineImgSel(destFolder, inlineImgSelected, z);
			}
			
			Messages.append("INFO", messages.getProperty("tmsg_19"));
		} catch (Exception e) {
			retExtract = false;
		}
		return retExtract;
	}

}
