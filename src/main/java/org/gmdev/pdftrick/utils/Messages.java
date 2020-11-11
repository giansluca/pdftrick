package org.gmdev.pdftrick.utils;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.text.*;
import java.util.Calendar;
import java.util.Properties;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.gmdev.pdftrick.manager.PdfTrickBag;

import static org.gmdev.pdftrick.utils.Constants.MESSAGES_PROPERTY_FILE;

public class Messages {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private static final Logger logger = Logger.getLogger(Messages.class);
	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

	private static Properties props;

	public static Properties loadMessageProperties() {
		props = PropertyLoader.loadPropertyFile(MESSAGES_PROPERTY_FILE);
		return props;
	}

	public static void printWelcomeMessage() {
		append("INFO", MessageFormat.format(props.getProperty("dmsg_09"),
				System.getProperty("os.name"),
				System.getProperty("sun.arch.data.model"),
				System.getProperty("java.version")));
	}

	public static void cleanTextArea() {
		final JTextArea textArea = BAG.getUserInterface().getBottom().getTextArea();
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(() -> textArea.setText(""));
		} else {
			textArea.setText("");
		}
	}

	/**
	 * Append messages to text area
	 */
	public static void append(String level, String message) {
		final JTextArea txtArea = BAG.getUserInterface().getBottom().getTextArea();
		final StringBuilder builder = new StringBuilder(300);
		Calendar cal = Calendar.getInstance();
		
        builder.append(df.format(cal.getTimeInMillis()) );
        builder.append(" [");
        builder.append(level);
        builder.append("]: ");
		builder.append(message);
		builder.append("\n");
		
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				SwingUtilities.invokeAndWait(() -> txtArea.append(builder.toString()));
			} catch (InterruptedException | InvocationTargetException e) {
				logger.error("Exception", e);
			}
		} else {
			txtArea.append(builder.toString());
		}
	}
	
	/**
	 * Append messages to text area using invokeLater
	 */
	public static void appendLater(String level, String message) {
		final JTextArea txtArea = BAG.getUserInterface().getBottom().getTextArea();
		final StringBuilder builder = new StringBuilder(300);
		Calendar cal = Calendar.getInstance();
		
        builder.append(df.format(cal.getTimeInMillis()) );
        builder.append(" [");
        builder.append(level);
        builder.append("]: ");
		builder.append(message);
		builder.append("\n");
		
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(() -> txtArea.append(builder.toString()));
		}
	}
	
	/**
	 * Append messages to text area without newline character
	 */
	public static void appendNoNewLine(String level, String message) {
		final JTextArea txtArea = BAG.getUserInterface().getBottom().getTextArea();
		final StringBuilder builder = new StringBuilder(300);
		Calendar cal = Calendar.getInstance();
		
        builder.append(df.format(cal.getTimeInMillis()) );
        builder.append(" [");
        builder.append(level);
        builder.append("]: ");
		builder.append(message);
		
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				SwingUtilities.invokeAndWait(() -> txtArea.append(builder.toString()));
			} catch (InterruptedException | InvocationTargetException e) {
				logger.error("Exception", e);
			}
		} else {
			txtArea.append(builder.toString());
		}
	}
	
	/**
	 * Append in line message without date and level
	 */
	public static void appendInline(String message) {
		final JTextArea txtArea = BAG.getUserInterface().getBottom().getTextArea();
		final String mess = message;
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				SwingUtilities.invokeAndWait(() -> txtArea.append(mess));
			} catch (InterruptedException | InvocationTargetException e) {
				logger.error("Exception", e);
			}
		} else {
			txtArea.append(mess);
		}
	}
	
	/**
	 * Append new line character
	 */
	public static void appendNewLine() {
		final JTextArea txtArea = BAG.getUserInterface().getBottom().getTextArea();
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				SwingUtilities.invokeAndWait(() -> txtArea.append("\n"));
			} catch (InterruptedException | InvocationTargetException e) {
				logger.error("Exception", e);
			}
		} else {
			txtArea.append("\n");
		}
	}
	
	/**
	 * display modal windows message
	 */
	public static void displayMessage(Component parent, String message, String title, int type, ImageIcon icon) {
		try {
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(parent, message, title, type, icon));
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}

}
