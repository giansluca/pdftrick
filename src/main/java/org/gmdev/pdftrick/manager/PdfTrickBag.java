package org.gmdev.pdftrick.manager;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.ui.UserInterface;
import org.gmdev.pdftrick.utils.*;

public class PdfTrickBag {
	
	private static PdfTrickBag instance;

	private String os;
	private Path homeFolderPath;
	private Path nativeLibraryPath;
	private String pdfFilePath;
	private int numberOfPages;
	private ArrayList<File> pdfFilesArray;
	private String selected;
	private String folderToSave;
	private HashMap<Integer, String> rotationFromPages;
	private HashMap<String, String> namePwd;
	private HashMap<String, RenderedImageAttributes> imageSelected;
	private HashMap<String, RenderedImageAttributes> inlineImgSelected;
	private ThreadContainer threadContainer;
	private Properties messages;
	private NativeObjectManager nativeObjectManager;
	private UserInterface userInterface;

	private PdfTrickBag() {}
	
	public static PdfTrickBag getBag() {
		if (instance == null) {
			synchronized(PdfTrickBag.class) {
				if (instance == null)
					instance = new PdfTrickBag();
			}
		}
		return instance;
	}

	public void build(String os, Path homeFolderPath, Path nativeLibraryPath) {
		this.os = os;
		this.homeFolderPath = homeFolderPath;
		this.nativeLibraryPath = nativeLibraryPath;
		pdfFilePath = homeFolderPath + File.separator + Constants.PDF_FILE_NAME;
		pdfFilesArray = new ArrayList<>();
		selected = "";
		folderToSave = "";
		rotationFromPages = new HashMap<>();
		namePwd = new HashMap<>();
		imageSelected = new HashMap<>();
		inlineImgSelected = new HashMap<>();
		threadContainer = new ThreadContainer();
	}

	public String getOs() {
		return os;
	}
	
	public Path getHomeFolderPath() {
		return homeFolderPath;
	}

	public Path getNativeLibraryPath() {
		return nativeLibraryPath;
	}

	public String getPdfFilePath() {
		return pdfFilePath;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}
	
	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	
	public ArrayList<File> getPdfFilesArray() {
		return pdfFilesArray;
	}
	
	public String getSelected() {
		return selected;
	}
	
	public void setSelected(String selected) {
		this.selected = selected;
	}
	
	public String getFolderToSave() {
		return folderToSave;
	}
	
	public void setFolderToSave(String folderToSave) {
		this.folderToSave = folderToSave;
	}
	
	public HashMap<Integer, String> getRotationFromPages() {
		return rotationFromPages;
	}

	public HashMap<String, String> getNamePwd() {
		return namePwd;
	}
	
	public HashMap<String, RenderedImageAttributes> getImageSelected() {
		return imageSelected;
	}
	
	public HashMap<String, RenderedImageAttributes> getInlineImgSelected() {
		return inlineImgSelected;
	}
	
	public ThreadContainer getThreadContainer() {
		return threadContainer;
	}

	public Properties getMessages() { return messages; }

	protected void setMessages(Properties messages) {
		this.messages = messages;
	}

	public NativeObjectManager getNativeObjectManager() {
		return nativeObjectManager;
	}

	protected void setNativeObjectManager(NativeObjectManager nativeObjectManager) {
		this.nativeObjectManager = nativeObjectManager;
	}

	public UserInterface getUserInterface() {
		return userInterface;
	}

	protected void setUserInterface(UserInterface userInterface) {
		this.userInterface = userInterface;
	}
	
	
}
