package org.gmdev.pdftrick.utils;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.factory.PdfTrickBag;

public class PdfTrickMessages {
	
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	private static final Logger logger = Logger.getLogger(PdfTrickMessages.class);
	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	
	/**
	 * Clean the text area
	 */
	public static void cleanTextArea() {
		final JTextArea textArea = factory.getUserInterface().getBottom().getTextArea();
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					textArea.setText("");
				}
			});
		} else {
			textArea.setText("");
		}
	}

	/**
	 * Append messages and errors to text area
	 */
	public static void append(String level, String message) {
		final JTextArea txtArea = factory.getUserInterface().getBottom().getTextArea();
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
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						txtArea.append(builder.toString());
					}
				});
			} catch (InterruptedException | InvocationTargetException e) {
				logger.error("Exception", e);
			}
		} else {
			txtArea.append(builder.toString());
		}
	}
	
	/**
	 * Append messages and errors to text area using invokeLater
	 */
	public static void appendLater(String level, String message) {
		final JTextArea txtArea = factory.getUserInterface().getBottom().getTextArea();
		final StringBuilder builder = new StringBuilder(300);
		Calendar cal = Calendar.getInstance();
		
        builder.append(df.format(cal.getTimeInMillis()) );
        builder.append(" [");
        builder.append(level);
        builder.append("]: ");
		builder.append(message);
		builder.append("\n");
		
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					txtArea.append(builder.toString());
				}
			});
		}
	}
	
	/**
	 * Append messages and errors to text area without newline character
	 */
	public static void appendNoNewLine(String level, String message) {
		final JTextArea txtArea = factory.getUserInterface().getBottom().getTextArea();
		final StringBuilder builder = new StringBuilder(300);
		Calendar cal = Calendar.getInstance();
		
        builder.append(df.format(cal.getTimeInMillis()) );
        builder.append(" [");
        builder.append(level);
        builder.append("]: ");
		builder.append(message);
		
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						txtArea.append(builder.toString());
					}
				});
			} catch (InterruptedException e) {
				logger.error("Exception", e);
			} catch (InvocationTargetException e) {
				logger.error("Exception", e);
			}
		} else {
			txtArea.append(builder.toString());
		}
	}
	
	/**
	 * Append in line message without date and level
	 */
	public static void appendIline(String message) {
		final JTextArea txtArea = factory.getUserInterface().getBottom().getTextArea();
		final String mess = message;
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						txtArea.append(mess);
					}
				});
			} catch (InterruptedException e) {
				logger.error("Exception", e);
			} catch (InvocationTargetException e) {
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
		final JTextArea txtArea = factory.getUserInterface().getBottom().getTextArea();
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						txtArea.append("\n");
					}
				});
			} catch (InterruptedException e) {
				logger.error("Exception", e);
			} catch (InvocationTargetException e) {
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
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(parent, message, title, type, icon);
				}
			});
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}

}
