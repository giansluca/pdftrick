package org.gmdev.pdftrick.manager;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.ui.UserInterface;
import org.gmdev.pdftrick.utils.PropertyLoader;

import static org.gmdev.pdftrick.utils.Constants.*;

public enum PdfTrickBag {
	INSTANCE;

	private String os;
	private Path nativeLibraryPath;
	private Path pdfFilePath;
	private Path thumbnailsFolderPath;
	private int numberOfPages;
	private ArrayList<File> pdfFilesArray;
	private int selectedPage;
	private Path extractionFolderPath;
	private HashMap<Integer, String> pagesRotation;
	private HashMap<String, String> pfdPasswords;
	private HashMap<String, RenderedImageAttributes> selectedImages;
	private HashMap<String, RenderedImageAttributes> inlineSelectedImages;
	private TasksContainer tasksContainer;
	private Properties messagesProps;
	private NativeObjectManager nativeObjectManager;
	private UserInterface userInterface;

	@CanIgnoreReturnValue
	public static PdfTrickBag getInstance() {
		return INSTANCE;
	}

	private PdfTrickBag init(Builder builder) {
		this.os = builder.os;
		this.nativeLibraryPath = builder.nativeLibraryPath;
		Path homeFolderPath = builder.homeFolderPath;

		pdfFilePath = Path.of(homeFolderPath + File.separator + PDF_FILE_NAME);
		thumbnailsFolderPath = Path.of(homeFolderPath + File.separator + PAGES_THUMBNAIL_FOLDER);
		numberOfPages = 0;
		pdfFilesArray = new ArrayList<>();
		selectedPage = 0;
		pagesRotation = new HashMap<>();
		pfdPasswords = new HashMap<>();
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
		private Path homeFolderPath;
		private Path nativeLibraryPath;

		public Builder os(String val) { os = val; return this; }
		public Builder homeFolderPath(Path val) { homeFolderPath = val; return this; }
		public Builder nativeLibraryPath(Path val) { nativeLibraryPath = val; return this; }

		public PdfTrickBag build() {
			return INSTANCE.init(this);
		}
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

	public void cleanPagesRotationHashMap() {
		pagesRotation.clear();
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

	public HashMap<Integer, String> getPagesRotationPages() {
		return pagesRotation;
	}

	public HashMap<String, String> getPfdPasswords() {
		return pfdPasswords;
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
