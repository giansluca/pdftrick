package org.gmdev.pdftrick.thread;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.text.MessageFormat;
import java.util.Properties;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.engine.ImageListenerShowThumb;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.WaitPanel;
import org.gmdev.pdftrick.utils.Messages;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

public class ImgThumb implements Runnable {
	
	private static final Logger logger = Logger.getLogger(ImgThumb.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private final int numberPage;
	volatile boolean finished = false;
	
	public ImgThumb(int numberPage) {
		this.numberPage = numberPage;
	}
	
	public void stop() {
	    finished = true;
	 }
	
	@Override
	public void run() {
		renderPageThumbnails();
	}

	public void renderPageThumbnails () {
		Properties messages = BAG.getMessagesProps();
		JPanel centerPanel = BAG.getUserInterface().getCenter().getCenterPanel();
		
		WaitPanel.setLoadingThumbnailsWaitPanel();
		
		try {
			PdfReader reader = null;
			ImageListenerShowThumb listener = null;
			
			reader = new PdfReader(BAG.getPdfFilePath().toString());
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			listener = new ImageListenerShowThumb(numberPage);

			parser.processContent(numberPage, listener);			
			reader.close();
			
			String infoUnsupported = "";
			String infoAvailable = "";
			
			if (listener.getUnsupportedImage() > 0){
				infoUnsupported = MessageFormat.format(messages.getProperty("dmsg_02"), numberPage);
			}
			
			if (listener.getNumImg() == 0) {
				final String noImgTitle = messages.getProperty("jmsg_08");
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						JLabel noImageLabel = new JLabel(noImgTitle);
						noImageLabel.setHorizontalAlignment(JLabel.CENTER);
						noImageLabel.setVerticalAlignment(JLabel.CENTER);
						noImageLabel.setFont(new Font("Verdana",1,20));
						noImageLabel.setName("NoPicsImg");
						centerPanel.setLayout(new GridBagLayout());
						centerPanel.add(noImageLabel);
						centerPanel.revalidate();
						centerPanel.repaint();
					}
				});
				infoAvailable = MessageFormat.format(messages.getProperty("dmsg_03"), numberPage);
			} else {
				String t = listener.getNumImg() > 1 ? messages.getProperty("tmsg_15") : messages.getProperty("tmsg_16");
				infoAvailable = listener.getNumImg() + " " + t;
			}
			Messages.append("INFO", infoUnsupported + " " + infoAvailable);
		} catch(Exception e) {
			logger.error("Exception", e);
		}
		
		WaitPanel.removeWaitPanel();
		finished = true;
	}
	
	public boolean isFinished() {
		return finished;
	}
	

}
