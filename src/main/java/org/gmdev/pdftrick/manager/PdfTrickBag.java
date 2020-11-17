package org.gmdev.pdftrick.manager;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.ui.UserInterface;

import static org.gmdev.pdftrick.utils.Constants.*;

public enum PdfTrickBag {
	INSTANCE;

	String os;
	Path homeFolderPath;
	Path nativeLibraryPath;
	Path pdfFilePath;
	Path thumbnailsFolderPath;
	int numberOfPages;
	ArrayList<File> pdfFilesArray;
	int selectedPage;
	Path extractionFolderPath;
	HashMap<Integer, String> rotationFromPages;
	HashMap<String, String> namePwd;
	HashMap<String, RenderedImageAttributes> selectedImages;
	HashMap<String, RenderedImageAttributes> inlineSelectedImages;
	ThreadContainer threadContainer;
	Properties messagesProps;
	NativeObjectManager nativeObjectManager;
	UserInterface userInterface;

	@CanIgnoreReturnValue
	public static PdfTrickBag getInstance() {
		return INSTANCE;
	}

	protected void init(String os, Path homeFolderPath, Path nativeLibraryPath) {
		this.os = os;
		this.homeFolderPath = homeFolderPath;
		this.nativeLibraryPath = nativeLibraryPath;
		pdfFilePath = Path.of(homeFolderPath + File.separator + PDF_FILE_NAME);
		thumbnailsFolderPath = Path.of(homeFolderPath + File.separator + PAGES_THUMBNAIL_FOLDER);
		numberOfPages = 0;
		pdfFilesArray = new ArrayList<>();
		selectedPage = 0;
		rotationFromPages = new HashMap<>();
		namePwd = new HashMap<>();
		selectedImages = new HashMap<>();
		inlineSelectedImages = new HashMap<>();
		threadContainer = new ThreadContainer();
	}

	public void cleanPdfFilesArray(){
		pdfFilesArray.clear();
	}

	public void cleanSelectedImagesHashMap() {
		selectedImages.clear();
	}

	public void cleanInlineSelectedImagesHashMap() {
		inlineSelectedImages.clear();
	}

	public void cleanRotationFromPagesHashMap() {
		rotationFromPages.clear();
	}

	public String getOs() {
		return os;
	}

	public Path getNativeLibraryPath() {
		return nativeLibraryPath;
	}

	public Path getPdfFilePath() {
		return pdfFilePath;
	}

	public Path getThumbnailsFolderPath() {
		return thumbnailsFolderPath;
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

	public int getSelectedPage() {
		return selectedPage;
	}

	public void setSelectedPage(int selectedPage) {
		this.selectedPage = selectedPage;
	}

	public Path getExtractionFolderPath() {
		return extractionFolderPath;
	}

	public void setExtractionFolderPath(Path extractionFolderPath) {
		this.extractionFolderPath = extractionFolderPath;
	}

	public HashMap<Integer, String> getRotationFromPages() {
		return rotationFromPages;
	}

	public HashMap<String, String> getNamePwd() {
		return namePwd;
	}

	public HashMap<String, RenderedImageAttributes> getSelectedImages() {
		return selectedImages;
	}

	public HashMap<String, RenderedImageAttributes> getInlineSelectedImages() {
		return inlineSelectedImages;
	}

	public ThreadContainer getThreadContainer() {
		return threadContainer;
	}

	public Properties getMessagesProps() { return messagesProps; }

	protected void setMessagesProps(Properties messagesProps) {
		this.messagesProps = messagesProps;
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
