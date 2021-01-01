package org.gmdev.pdftrick.tasks;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.engine.ImageListenerShowThumb;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.WaitPanel;
import org.gmdev.pdftrick.utils.Messages;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

public class PageThumbnailsDisplayTask implements Runnable {
	
	private static final Logger logger = Logger.getLogger(PageThumbnailsDisplayTask.class);
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
	
	private final int numberPage;
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public PageThumbnailsDisplayTask(int numberPage) {
		this.numberPage = numberPage;
	}

	public boolean isRunning() {
		return running.get();
	}

	@Override
	public void run () {
		running.set(true);
		Properties messages = bag.getMessagesProps();
		JPanel centerPanel = bag.getUserInterface().getCenter().getCenterPanel();
		
		WaitPanel.setLoadingThumbnailsWaitPanel();
		
		try {
			PdfReader reader;
			ImageListenerShowThumb listener;
			
			reader = new PdfReader(bag.getPdfFilePath().toString());
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			listener = new ImageListenerShowThumb(numberPage);

			parser.processContent(numberPage, listener);			
			reader.close();
			
			String infoUnsupported = "";
			String infoAvailable;
			
			if (listener.getUnsupportedImage() > 0)
				infoUnsupported = MessageFormat.format(messages.getProperty("dmsg_02"), numberPage);
			
			if (listener.getNumImg() == 0) {
				String noImgTitle = messages.getProperty("tmsg_07");
				SwingUtilities.invokeLater(() -> {
					JLabel noImageLabel = new JLabel(noImgTitle);
					noImageLabel.setHorizontalAlignment(JLabel.CENTER);
					noImageLabel.setVerticalAlignment(JLabel.CENTER);
					noImageLabel.setFont(new Font("Verdana", Font.BOLD,20));
					noImageLabel.setName("NoPicsImg");
					centerPanel.setLayout(new GridBagLayout());
					centerPanel.add(noImageLabel);
					centerPanel.revalidate();
					centerPanel.repaint();
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
		running.set(false);
	}

}
