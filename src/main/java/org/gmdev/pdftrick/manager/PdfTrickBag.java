package org.gmdev.pdftrick.manager;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import org.gmdev.pdftrick.checking.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.ui.UserInterface;
import org.gmdev.pdftrick.utils.PropertyLoader;

import static org.gmdev.pdftrick.utils.Constants.*;

public enum PdfTrickBag {
	INSTANCE;

	private String os;
	private String version;
	private Path nativeLibraryPath;
	private Path savedFilePath;
	private Path thumbnailsFolderPath;
	private int numberOfPages;
	private int selectedPage;
	private Path extractionFolderPath;
	private HashMap<Integer, String> pagesRotation;
	private String pdfPassword;
	private HashMap<String, RenderedImageAttributes> selectedImages;
	private HashMap<String, RenderedImageAttributes> inlineSelectedImages;
	private TasksContainer tasksContainer;
	private Properties messagesProps;
	private NativeObjectManager nativeObjectManager;
	private UserInterface userInterface;

	public static PdfTrickBag getInstance() {
		return INSTANCE;
	}

	private PdfTrickBag init(Builder builder) {
		this.os = builder.os;
		this.version = builder.version;
		this.nativeLibraryPath = builder.nativeLibraryPath;
		Path homeFolderPath = builder.homeFolderPath;

		savedFilePath = Path.of(homeFolderPath + File.separator + PDF_FILE_NAME);
		thumbnailsFolderPath = Path.of(homeFolderPath + File.separator + PAGES_THUMBNAIL_FOLDER);
		numberOfPages = 0;
		selectedPage = 0;
		pagesRotation = new HashMap<>();
		selectedImages = new HashMap<>();
		inlineSelectedImages = new HashMap<>();
		tasksContainer = new TasksContainer();
		messagesProps = PropertyLoader.loadMessagesPropertyFile();
		nativeObjectManager = new NativeObjectManager();

		return INSTANCE;
	}

	public static Builder getBuilder() {
		return new Builder();
	}

	protected static class Builder {
		private String os;
		private String version;
		private Path homeFolderPath;
		private Path nativeLibraryPath;

		public Builder os(String value) { os = value; return this; }
		public Builder version(String value) { version = value; return this; }
		public Builder homeFolderPath(Path val) { homeFolderPath = val; return this; }
		public Builder nativeLibraryPath(Path val) { nativeLibraryPath = val; return this; }
		public PdfTrickBag build() {
			return INSTANCE.init(this);
		}
	}

	public void cleanUp() {
		cleanSelectedImagesHashMap();
		cleanInlineSelectedImagesHashMap();
		cleanPagesRotationHashMap();
		setSelectedPage(0);
		setExtractionFolderPath(null);
	}

	public void cleanSelectedImagesHashMap() {
		selectedImages.clear();
	}

	public void cleanInlineSelectedImagesHashMap() {
		inlineSelectedImages.clear();
	}

	public void cleanPagesRotationHashMap() {
		pagesRotation.clear();
	}

	public String getOs() {
		return os;
	}

	public String getVersion() {
		return version;
	}

	public Path getNativeLibraryPath() {
		return nativeLibraryPath;
	}

	public Path getSavedFilePath() {
		return savedFilePath;
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

	public HashMap<Integer, String> getPagesRotationPages() {
		return pagesRotation;
	}

	public String getPdfPassword() {
		return pdfPassword;
	}

	public void setPdfPassword(String pdfPassword) {
		this.pdfPassword = pdfPassword;
	}

	public HashMap<String, RenderedImageAttributes> getSelectedImages() {
		return selectedImages;
	}

	public HashMap<String, RenderedImageAttributes> getInlineSelectedImages() {
		return inlineSelectedImages;
	}

	public TasksContainer getTasksContainer() {
		return tasksContainer;
	}

	public Properties getMessagesProps() { return messagesProps; }

	public NativeObjectManager getNativeObjectManager() {
		return nativeObjectManager;
	}

	public UserInterface getUserInterface() {
		return userInterface;
	}

	protected void setUserInterface(UserInterface userInterface) {
		this.userInterface = userInterface;
	}

}
