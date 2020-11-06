package org.gmdev.pdftrick.engine;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.engine.ImageAttr.InlineImage;
import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.external.CustomExtraImgReader;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.Utils;

import com.itextpdf.text.exceptions.UnsupportedPdfException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.parser.PdfImageObject;

public class ImagesExtractor {
	
	private static final Logger logger = Logger.getLogger(ImagesExtractor.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	/**
	 * Prepare for the extraction and call images extractor
	 */
	public void getImages() {
		final Properties messages = BAG.getMessages();
		final HashMap<String, RenderedImageAttributes> inlineImgSelected = BAG.getInlineImgSelected();
		final File resultFile = new File(BAG.getPdfFilePath());
		boolean getImgCheck = false;
	
		Messages.append("INFO", messages.getProperty("tmsg_17"));
			
		String finalFolderTosave = BAG.getFolderToSave()+ Utils.getTimedDirResult();
		File fileFinalFolderTosave = new File(finalFolderTosave);
		fileFinalFolderTosave.mkdir();
			
		getImgCheck = extractImgSel(finalFolderTosave, resultFile.getPath(), BAG.getImageSelected(), inlineImgSelected);
		
		// if extraction breaks ...
		if (!getImgCheck) { 
			Messages.append("WARNING", messages.getProperty("tmsg_18"));
			Utils.deleteSelectedFolderToSave(finalFolderTosave);
		}			
	}
	
	/**
	 * Extract in line images after selected after normal images extraction 
	 */
	private void extractInlineImgSel(String timeDirResult, HashMap<String, RenderedImageAttributes> inlineImgSelected, int counter) {
		String result = timeDirResult+ "/"+ "Img_%s.%s";
		Set<String> keys = inlineImgSelected.keySet();
		Iterator<String> i = keys.iterator();
		
		int z = counter;
		while (i.hasNext()) {
			String key = i.next();
			RenderedImageAttributes imgAttr = inlineImgSelected.get(key);
			InlineImage inImg = imgAttr.getInlineImage();
			
			String type = inImg.getFileType();
			String filename = String.format(result, z, type);
			
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
			
			try {
				ImageIO.write(inImg.getImage(), encode, outputfile);
			} catch (IOException e) {
				logger.error("Exception", e);
			}
			
			z++;
		}
	}
	
	private boolean extractImgSel (String timeDirResult, String resultFilePath, HashMap<String, RenderedImageAttributes> imageSelected, 
			HashMap<String, RenderedImageAttributes> inlineImgSelected) {
		
		final Properties messages = BAG.getMessages();
		String result = timeDirResult+ "/"+ "Img_%s.%s";
		PdfReader reader = null;
		boolean retExtract = true;
		
		try {
			reader = new PdfReader(resultFilePath);
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
						buff = CustomExtraImgReader.readIndexedPNG(ref, resultFilePath);
						buff = Utils.adjustImage(buff, flip, rotate);
						String type = "png";
						String filename = String.format(result, z, type);
						
						File outputFile = new File(filename);
						ImageIO.write(buff, type, outputFile);
					} catch (Exception e) {
						logger.error("Exception", e);
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
							logger.error("Exception", e);
						}
					}
					
					// check if image contains a mask image
					BufferedImage buffMask = null;
					PdfDictionary imageDictionary = io.getDictionary(); 
				    PRStream maskStream = (PRStream) imageDictionary.getAsStream(PdfName.SMASK); 
					
				    if (maskStream != null) {
				    	// i have an smask object i check that is not a jpeg format, because this may cause some problem on offscreen rendering
				    	// usually all imges with mask are png ... and there aren't problem, if image is jpg i discard the mask :)
				    	if (!type.equalsIgnoreCase("jpg")) {
				    		PdfImageObject maskImage = new PdfImageObject(maskStream);
				    		buffMask = maskImage.getBufferedImage();
				    
				    		Image img = Utils.TransformGrayToTransparency(buffMask);
				    		buff = Utils.ApplyTransparency(buffPic, img);
				    	} else {
				        	buff = buffPic;
				        }
				    } else {
				    	buff = buffPic;
				    }	
				    
					if (buff != null) {
						buff = Utils.adjustImage(buff, flip, rotate);
						
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
				extractInlineImgSel(timeDirResult, inlineImgSelected, z);
			}
			
			Messages.append("INFO", messages.getProperty("tmsg_19"));
		} catch (Exception e) {
			logger.error("Exception", e);
			retExtract = false;
		}
		return retExtract;
	}

}
