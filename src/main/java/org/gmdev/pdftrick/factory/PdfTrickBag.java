package org.gmdev.pdftrick.factory;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import javax.swing.SwingUtilities;

import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.ui.UserInterface;
import org.gmdev.pdftrick.utils.*;

public class PdfTrickBag {
	
	private static PdfTrickBag instance;
	
	private UserInterface userInterface;
	private String os;
	private Path homeFolderPath;
	private Path nativeLibraryPath;
	private String pdfFile;
	private NativeObjectManager nativeManager;
	private int numberOfPages;
	private ArrayList<File> pdfFilesArray;
	private String selected;
	private String folderToSave;
	private HashMap<Integer, String> rotationFromPages;
	private Properties messages;
	private HashMap<String, String> namePwd;
	private HashMap<String, RenderedImageAttributes> imageSelected;
	private HashMap<String, RenderedImageAttributes> inlineImgSelected;
	private ThreadContainer threadContainer;

	private PdfTrickBag() {}
	
	public static PdfTrickBag getPdfTrickBag() {
		if (instance == null) {
			synchronized(PdfTrickBag.class) {
				if (instance == null)
					instance = new PdfTrickBag();
			}
		}
		return instance;
	}

	public void initialize(String os, Path homeFolderPath, Path nativeLibraryPath) {
		this.os = os;
		this.homeFolderPath = homeFolderPath;
		this.nativeLibraryPath = nativeLibraryPath;
		pdfFile = homeFolderPath + File.separator + Constants.RESULT_PDF_FILE;
		nativeManager = new NativeObjectManager();
		pdfFilesArray = new ArrayList<>();
		selected = "";
		folderToSave = "";
		rotationFromPages = new HashMap<>();
		messages = Utils.loadProperties();
		namePwd = new HashMap<>();
		imageSelected = new HashMap<>();
		inlineImgSelected = new HashMap<>();
		threadContainer = new ThreadContainer();

		Utils.deleteImgFolderAnDFile();
		Utils.deleteResultFile();
		
		//initialize UI
		SwingUtilities.invokeLater(() -> {
			try {
				userInterface = new UserInterface();
				Utils.welcomeMessage();
				userInterface.setVisible(true);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		});
	}
	
	public synchronized UserInterface getUserInterface() {
		return userInterface;
	}

	public synchronized String getOs() {
		return os;
	}
	
	public synchronized Path getHomeFolderPath() {
		return homeFolderPath;
	}

	public synchronized Path getNativeLibraryPath() {
		return nativeLibraryPath;
	}

	public synchronized String getResultFile() {
		return pdfFile;
	}
	
	public synchronized NativeObjectManager getNativeManager() {
		return nativeManager;
	}
	
	public synchronized int getNumberOfPages() {
		return numberOfPages;
	}
	
	public synchronized void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	
	public synchronized ArrayList<File> getPdfFilesArray() {
		return pdfFilesArray;
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
	
	public synchronized ThreadContainer getThreadContainer() {
		return threadContainer;
	}
	
	
}
