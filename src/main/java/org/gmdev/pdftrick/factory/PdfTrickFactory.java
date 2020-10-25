package org.gmdev.pdftrick.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.ui.UI2;
import org.gmdev.pdftrick.utils.Consts;
import org.gmdev.pdftrick.utils.NativeObjectManager;
import org.gmdev.pdftrick.utils.PdfTrickUtils;

public class PdfTrickFactory {
	
	private static final Logger logger = Logger.getLogger(PdfTrickFactory.class);
	private static volatile PdfTrickFactory instance = null;
	
	private UI2 userInterface;
	private String os;
	private String hiddenHomeFolder;
	private String resultFile;
	private NativeObjectManager nativemanager;
	private int numPages;
	private ArrayList<File> filesVett;
	private String selected;
	private String folderToSave;
	private HashMap<Integer, String> rotationFromPages;
	private Properties messages;
	private HashMap<String, String> namePwd;
	private HashMap<String, RenderedImageAttributes> imageSelected;
	private HashMap<String, RenderedImageAttributes> inlineImgSelected;
	private volatile ThreadContainer tContainer;
	
	/**
	 * private constructor (Singleton)
	 */
	//private PdfTrickFactory(){}
	
	public static PdfTrickFactory getFactory() {
		if (instance == null) {
			synchronized(PdfTrickFactory.class){
				if (instance == null) {
					instance = new PdfTrickFactory();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Initialize PdfTrick 
	 */
	public void initialize(String hiddenHomeFolder, String os) {
		this.os = os;
		this.hiddenHomeFolder = hiddenHomeFolder;
		resultFile = hiddenHomeFolder+File.separator+Consts.RESULTPDFFILE;
		nativemanager = new NativeObjectManager();
		filesVett = new ArrayList<File>();
		selected = "";
		folderToSave = "";
		rotationFromPages = new HashMap<Integer, String>();
		messages = PdfTrickUtils.loadProperties();
		namePwd = new HashMap<String, String>();
		imageSelected = new HashMap<String, RenderedImageAttributes>();
		inlineImgSelected = new HashMap<String, RenderedImageAttributes>();
		tContainer = new ThreadContainer();
		
		// if result file exist (for example due to a previous exception and not deleted by PdfTrick), 
		// for security i call the clean here on startUp
		PdfTrickUtils.deleteImgFolderAnDFile();
		PdfTrickUtils.deleteResultFile();
		
		//inizialize UI
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					userInterface = new UI2();
					PdfTrickUtils.welcomeMessage();
					userInterface.setVisible(true);
				} catch (Exception e) {
					logger.error("Exception", e);
				}
			}
		});
	}
	
	public synchronized UI2 getUserInterface() {
		return userInterface;
	}

	public synchronized String getOs() {
		return os;
	}
	
	public synchronized String getHiddenHomeFolder() {
		return hiddenHomeFolder;
	}
	
	public synchronized String getResultFile() {
		return resultFile;
	}
	
	public synchronized NativeObjectManager getNativemanager() {
		return nativemanager;
	}
	
	public synchronized int getNumPages() {
		return numPages;
	}
	
	public synchronized void setNumPages(int numPages) {
		this.numPages = numPages;
	}
	
	public synchronized ArrayList<File> getFilesVett() {
		return filesVett;
	}
	
	public synchronized String getSelected() {
		return selected;
	}
	
	public synchronized void setSelected(String selected) {
		this.selected = selected;
	}
	
	public synchronized String getFolderToSave() {
		return folderToSave;
	}
	
	public synchronized void setFolderToSave(String folderToSave) {
		this.folderToSave = folderToSave;
	}
	
	public synchronized HashMap<Integer, String> getRotationFromPages() {
		return rotationFromPages;
	}
	
	public synchronized Properties getMessages() {
		return messages;
	}
	
	public synchronized HashMap<String, String> getNamePwd() {
		return namePwd;
	}
	
	public synchronized HashMap<String, RenderedImageAttributes> getImageSelected() {
		return imageSelected;
	}
	
	public synchronized HashMap<String, RenderedImageAttributes> getInlineImgSelected() {
		return inlineImgSelected;
	}
	
	public synchronized ThreadContainer gettContainer() {
		return tContainer;
	}
	
	
}
