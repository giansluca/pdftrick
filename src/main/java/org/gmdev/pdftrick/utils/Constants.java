package org.gmdev.pdftrick.utils;

public final class Constants {

	private Constants() {
		throw new AssertionError("Constants class should never be instantiated");
	}

	// general
	public static final String APP_NAME = "PdfTrick";
	public static final String PDFTRICK_HOME_FOLDER = ".pdftrick";
	public static final String PDF_FILE_NAME = "file.pdf";
	public static final String PAGES_THUMBNAIL_FOLDER = "pages-thumbnails";
	public static final String LICENSE_TITLE = "PdfTrick Licence";
	public static final String JFC_EXTRACT_TITLE = "Folder to save";
	public static final String JFC_OPEN_TITLE = "Open files";
	public static final String PWD_DIALOG = "Password is required";

	// pdf page zoom
	public static final int ZOOM_THUMBNAIL = 30;
	public static final int ZOOM_FULL = 100;

	// native library
	public static final String NATIVE_LIB_PATH = "nativelib";
	public static final String NATIVE_LIB_VERSION="1.0.2";
	public static final String NATIVE_LIB_WIN_FILE ="pdftrick-native_" + NATIVE_LIB_VERSION + ".dll";
	public static final String NATIVE_LIB_MAC_FILE ="pdftrick-native_" + NATIVE_LIB_VERSION + ".jnilib";

	// resource files
	public static final String MESSAGES_PROPERTY_FILE = "properties/messages.properties";
	public static final String GENERIC_ICC_FILE = "Generic_CMYK_Profile.icc";
	public static final String LICENSE_FILE = "license.txt";

	// icons
	public static final String PDFTRICK_ICO = "icons/pdftrick_icon.png";
	public static final String WARNING_ICO = "icons/warning.png";
	public static final String ABOUT_ICO = "icons/about_icon.png";
	public static final String EXIT_ICO = "icons/exit_icon.png";
	public static final String OPEN_FILE_ICO = "icons/open_icon.png";
	public static final String LICENSE_ICO = "icons/license_icon.png";
	public static final String GET_IMG_ICO = "icons/getigm_icon.png";
	public static final String CANCEL_ICO = "icons/cancel_icon.png";
	public static final String CLEAN_SEL_ICO = "icons/clean_icon.png";
	public static final String GPL3_ICO = "icons/gpl_3.png";

	// custom file chooser for mac
	public static final String UP_ICO = "icons/filechooser/up_icon.png";
	public static final String HOME_ICO = "icons/filechooser/home_icon.png";
	public static final String DESKTOP_ICO = "icons/filechooser/desktop_icon.png";

	// gifs
	public static final String WAIT = "icons/wait.gif";
	public static final String WAIT_IMG = "icons/wait_img.gif";
	public static final String WAIT_EXT = "icons/wait_ext.gif";

	// thumbnails size
	public static final int THUMBNAIL_MAX_SIZE_WIDTH = 170;
	public static final int THUMBNAIL_MAX_SIZE_HEIGHT = 170;
	
}
