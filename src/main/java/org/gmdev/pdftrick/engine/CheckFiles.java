package org.gmdev.pdftrick.engine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.PdfTrickMessages;

import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;

public class CheckFiles {
	
	private static final Logger logger = Logger.getLogger(CheckFiles.class);
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	
	private boolean checkEncryption = false;
	private final HashMap<String, String> namePwd;
	private boolean userProtection = false;
	private boolean ownerProtection = false;
	
	public CheckFiles() {
		this.namePwd = factory.getNamePwd();
	}
	
	/**
	 * Check files selected (pdf integrity, images inside, protection).
	 */
	public boolean check() {
		final Properties messages = factory.getMessages();
		final ArrayList<File> filesVett = factory.getFilesVett();
		
		boolean check = false;
		boolean checkPdf = false;
		boolean checkNumImg = false;
		
		checkPdf = checkPdf(filesVett, messages);
		
		if (checkPdf) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						checkEncryption = checkEncryption(filesVett, messages);
					}
				});
			} catch (InterruptedException e) {
				logger.error("Exception", e);
			} catch (InvocationTargetException e) {
				logger.error("Exception", e);
			}
			
			if (checkEncryption) {
				checkNumImg = checkNumberImages(filesVett, messages);
				if (checkNumImg) {
					check = true;
				}
			}
		}
		return check;
	}
	
	/**
	 * Check if pdf files selected are valid and are actually pdf files and max size is 256 MB.
	 */
	private boolean checkPdf (ArrayList<File> filesVett, Properties messages) {
		boolean check = true;
		long sizeResultFile = 0;
		
		Iterator<File> ite = filesVett.iterator();
		while (ite.hasNext()) {
			File item = ite.next();
			sizeResultFile += item.length();
			
			String nameFile = item.getName();
			String fileString = this.readFile(item);
			
			// check if the file uploaded is actually a pdf file
			if (!fileString.substring(0, 4).equalsIgnoreCase("%PDF")) {
				check = false;
				PdfTrickMessages.append("WARNING", MessageFormat.format(messages.getProperty("dmsg_04"), nameFile));
			}
		}
		long sizeResultFileKB = sizeResultFile / 1024;
		long sizeResultFileMB = sizeResultFileKB / 1024;
		
		if (sizeResultFileMB > 256) {
			check = false;
			PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_20"));
		}
		
		return check;
	}
	
	/**
	 * Read a pdf file and return first line as String representation (for checkPdf method).
	 */
	private String readFile(File file) {
        String line = null;
        
        try {
            FileReader in = new FileReader(file);
            BufferedReader brd = new BufferedReader(in);
            line = brd.readLine();
            in.close();
        } catch (IOException e) {
        	logger.error("Exception", e);
        }
        
        return line;
    }
	
	/**
	 * Check the number of images contained in the file's selection.
	 */
	private boolean checkNumberImages(ArrayList<File> filesVett, Properties messages) {
		boolean checkNumImg = false;
		Iterator<File> ite = filesVett.iterator();
		while (ite.hasNext()) {
			File item = ite.next();
			PdfReader reader = null;
			
			try {
				if (namePwd.containsKey(item.getName())) {
					reader = new PdfReader(item.getPath(),namePwd.get(item.getName()).getBytes());
				} else {
					reader = new PdfReader(item.getPath());
				}
				
				for (int i = 0; i < reader.getXrefSize(); i++) {
					PdfObject pdfobj = reader.getPdfObject(i);
			        
					if (pdfobj == null || !pdfobj.isStream()) {
			            continue;
			        }
			        
			        PdfStream stream = (PdfStream) pdfobj;
			        PdfObject pdfsubtype = stream.get(PdfName.SUBTYPE);
			        
			        if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
			        	checkNumImg = true;
			        	break;
			        }
				}
				
				if (checkNumImg) {
					break;
				}
			} catch (IOException e) {
				logger.error("Exception", e);
			}
		}
		
		if (!checkNumImg) {
			PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_21"));
		}
		
		return checkNumImg;
	}
	
	/**
	 * True if everiting if OK with PDF files protection (correct password if the PDF require it).
	 */
	private boolean checkEncryption(ArrayList<File> filesVett, Properties messages) {
		boolean check = true;
		
		Iterator<File> ite = filesVett.iterator();
		while (ite.hasNext()) {
			userProtection = false;
			ownerProtection = false;
			
			final File item = ite.next();
			hasProtection(item);
			
			if (userProtection || ownerProtection) {
				int i=1;
				String _ck = askAndChekPwd(item, messages, i);
				
				if (_ck.equalsIgnoreCase("no")) {
					while (_ck.equalsIgnoreCase("no") && i<3) {
						i++;
						_ck = askAndChekPwd(item, messages, i);
						check = false;
					}
				}
				
				if (_ck.equalsIgnoreCase("abort")) {
					PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_22"));
					check = false;
				}
				
				if (_ck.equalsIgnoreCase("ok")) {
					check = true;
				}
			}
			
			if (!check)
				break;
		}
		
		return check;
	}
	
	/**
	 * Check is single pdf has a User password protected, set userProtection = true if BadPasswordException.
	 */
	private void hasProtection(File file) {
		PdfReader reader = null;
		
		try {
			reader = new PdfReader(file.getPath());
			if (reader != null) {
				if (reader.isEncrypted()) {
					ownerProtection = true;
				}
				reader.close();
			}
		} catch (BadPasswordException e) {
			userProtection = true;
		} catch (IOException e) {
			logger.error("Exception", e);
		}
	}
	
	/**
	 * Show dialog to get the User password and check it.
	 */
	private String askAndChekPwd(File file, Properties messages, int n) {
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(Constants.MAIN_ICO));
		
		final JDialog userPwdDialog = new JDialog((JDialog)null, true);
		userPwdDialog.setTitle(Constants.PWD_DIALOG);
		userPwdDialog.setIconImage(imageIcon.getImage());
		userPwdDialog.setSize(620, 80);
		userPwdDialog.setResizable(false);
		userPwdDialog.setLocationRelativeTo(null);
		userPwdDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		userPwdDialog.setLayout(null);
		
		final JPasswordField userPwdField = new JPasswordField();
		userPwdField.setBounds(25, 25, 500, 20);
		userPwdField.setEchoChar('*');
		
		final JLabel userPwdLabel = new JLabel();
		userPwdLabel.setBounds(30, 5, 500, 18);
		userPwdLabel.setText(MessageFormat.format(messages.getProperty("dmsg_06"), file.getName()));
		
		final JButton okButton = new JButton("OK");
		okButton.setBounds(530, 25, 70, 20);
		okButton.setOpaque(true);
		 
		final OkCloseAction ok = new OkCloseAction(messages, file, n);
		final Thread t = new Thread(ok);
	    
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				userPwdDialog.setVisible(false);
				userPwdDialog.dispose();
				ok.pwd = String.valueOf(userPwdField.getPassword());
				t.start();
			}
		});
	    
	    userPwdField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {go();}
			@Override
			public void insertUpdate(DocumentEvent e) {go();}
			@Override
			public void changedUpdate(DocumentEvent e) {go();}
			
			public void go() {
				Border borderOrange = BorderFactory.createLineBorder(Color.ORANGE);
				if (userPwdField.getPassword().length > 0) {
					okButton.setBorder(borderOrange);
				} else {
					okButton.setBorder(new JButton().getBorder());
				}
			}
		});
	    
	    userPwdDialog.getRootPane().setDefaultButton(okButton);
		userPwdDialog.add(userPwdField);
		userPwdDialog.add(userPwdLabel);
		userPwdDialog.add(okButton);
		userPwdDialog.setVisible(true);
		
		if (t.isAlive()){
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.error("Exception", e);
			}
		}
		
		return ok.check;
	}
	
	public class OkCloseAction implements Runnable  {
		private volatile String check = "abort";
		private volatile String pwd;
		private final Properties messages;
		private final File file;
		private final int n;
		
		public OkCloseAction(Properties messages, File file, int n) {
			this.messages = messages;
			this.file = file;
			this.n = n;
		}
		
		@Override
		public void run() {
			PdfReader reader = null;
			try {
				if (userProtection || ownerProtection) {
					reader = new PdfReader(file.getPath(), pwd.getBytes());
					if (reader != null) {
						if (reader.isEncrypted()) { 
							if (reader.isOpenedWithFullPermissions()) {
								check="ok";
								namePwd.put(file.getName(), pwd);
							} else {
								check="no";
								PdfTrickMessages.appendLater("WARNING", MessageFormat.format(messages.getProperty("dmsg_08"), n, file.getName()));
							}
						}	
						reader.close();
					}
				} 
			} catch (BadPasswordException e) {
				check="no";
				PdfTrickMessages.appendLater("WARNING", MessageFormat.format(messages.getProperty("dmsg_07"), n, file.getName()));
			} catch (IOException e) {
				logger.error("Exception", e);
			}
		}
	}
	

}
