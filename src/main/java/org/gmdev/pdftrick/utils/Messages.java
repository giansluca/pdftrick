package org.gmdev.pdftrick.utils;

import java.text.*;
import java.util.Calendar;
import javax.swing.*;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.SwingInvoker;

import static org.gmdev.pdftrick.utils.SystemProperty.getSystemProperty;

public class Messages {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

	private Messages() {
		throw new AssertionError("Messages class should never be instantiated");
	}

	public static void printWelcomeMessage() {
		String message = bag.getMessagesProps().getProperty("d_msg_09");
		append("INFO", MessageFormat.format(message,
				getSystemProperty("os.name"),
				getSystemProperty("sun.arch.data.model"),
				getSystemProperty("java.version")));
	}

	public static void append(String level, String message) {
		JTextArea txtArea = bag.getUserInterface().getBottom().getTextArea();
		StringBuilder builder = new StringBuilder(300);
		Calendar cal = Calendar.getInstance();
		
        builder.append(df.format(cal.getTimeInMillis()) );
        builder.append(" [");
        builder.append(level);
        builder.append("]: ");
		builder.append(message);
		builder.append("\n");

		if (!SwingUtilities.isEventDispatchThread())
			SwingInvoker.invokeAndWait(() -> txtArea.append(builder.toString()));
		else
			txtArea.append(builder.toString());
	}

	public static void appendLater(String level, String message) {
		JTextArea txtArea = bag.getUserInterface().getBottom().getTextArea();
		StringBuilder builder = new StringBuilder(300);
		Calendar cal = Calendar.getInstance();
		
        builder.append(df.format(cal.getTimeInMillis()) );
        builder.append(" [");
        builder.append(level);
        builder.append("]: ");
		builder.append(message);
		builder.append("\n");
		
		if (!SwingUtilities.isEventDispatchThread())
			SwingUtilities.invokeLater(() -> txtArea.append(builder.toString()));
	}

	public static void appendNoNewLine(String level, String message) {
		JTextArea txtArea = bag.getUserInterface().getBottom().getTextArea();
		StringBuilder builder = new StringBuilder(300);
		Calendar cal = Calendar.getInstance();
		
        builder.append(df.format(cal.getTimeInMillis()) );
        builder.append(" [");
        builder.append(level);
        builder.append("]: ");
		builder.append(message);

		if (!SwingUtilities.isEventDispatchThread())
			SwingInvoker.invokeAndWait(() -> txtArea.append(builder.toString()));
		else
			txtArea.append(builder.toString());
	}

	public static void appendInline(String message) {
		JTextArea txtArea = bag.getUserInterface().getBottom().getTextArea();
		if (!SwingUtilities.isEventDispatchThread())
			SwingInvoker.invokeAndWait(() -> txtArea.append(message));
		else
			txtArea.append(message);
	}

	public static void appendNewLine() {
		JTextArea txtArea = bag.getUserInterface().getBottom().getTextArea();
		if (!SwingUtilities.isEventDispatchThread())
			SwingInvoker.invokeAndWait(() -> txtArea.append("\n"));
		else
			txtArea.append("\n");
	}


}
