package org.gmdev.pdftrick.utils;

import java.lang.reflect.InvocationTargetException;
import java.text.*;
import java.util.Calendar;
import javax.swing.*;
import org.gmdev.pdftrick.manager.PdfTrickBag;

import static org.gmdev.pdftrick.utils.SystemProperty.getSystemProperty;

public class Messages {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

	private Messages() {
		throw new AssertionError("Messages class should never be instantiated");
	}

	public static void printWelcomeMessage() {
		String message = BAG.getMessagesProps().getProperty("dmsg_09");
		append("INFO", MessageFormat.format(message,
				getSystemProperty("os.name"),
				getSystemProperty("sun.arch.data.model"),
				getSystemProperty("java.version")));
	}

	public static void cleanTextArea() {
		JTextArea textArea = BAG.getUserInterface().getBottom().getTextArea();
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
		JTextArea txtArea = BAG.getUserInterface().getBottom().getTextArea();
		StringBuilder builder = new StringBuilder(300);
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
				throw new IllegalStateException(e);
			}
		} else {
			txtArea.append(builder.toString());
		}
	}
	
	/**
	 * Append messages to text area using invokeLater
	 */
	public static void appendLater(String level, String message) {
		JTextArea txtArea = BAG.getUserInterface().getBottom().getTextArea();
		StringBuilder builder = new StringBuilder(300);
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
		JTextArea txtArea = BAG.getUserInterface().getBottom().getTextArea();
		StringBuilder builder = new StringBuilder(300);
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
				throw new IllegalStateException(e);
			}
		} else {
			txtArea.append(builder.toString());
		}
	}
	
	/**
	 * Append in line message without date and level
	 */
	public static void appendInline(String message) {
		JTextArea txtArea = BAG.getUserInterface().getBottom().getTextArea();
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				SwingUtilities.invokeAndWait(() -> txtArea.append(message));
			} catch (InterruptedException | InvocationTargetException e) {
				throw new IllegalStateException(e);
			}
		} else {
			txtArea.append(message);
		}
	}
	
	/**
	 * Append new line character
	 */
	public static void appendNewLine() {
		JTextArea txtArea = BAG.getUserInterface().getBottom().getTextArea();
		if (!SwingUtilities.isEventDispatchThread()) {
			try {
				SwingUtilities.invokeAndWait(() -> txtArea.append("\n"));
			} catch (InterruptedException | InvocationTargetException e) {
				throw new IllegalStateException(e);
			}
		} else {
			txtArea.append("\n");
		}
	}

}
