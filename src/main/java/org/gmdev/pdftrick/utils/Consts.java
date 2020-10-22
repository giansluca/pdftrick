package org.gmdev.pdftrick.utils;

public final class Consts {
	// general
	public static final String APPNAME = "PdfTrick";
	public static final String HOME_FOLDER = ".pdftrick";
	public static final String LICENCETITLE = "PdfTrick Licence";
	public static final String RESULTPDFFILE = "resultfile.pdf";
	public static final String JFCEXTRACTTITLE = "Folder to save";
	public static final String JFCOPENTITLE = "Open files";
	public static final String PWDDIALOG = "Password is required";
	
	// engine
	public static final int ZOOM_THUMB = 30;
	public static final int ZOOM_COVER = 100;
	
	// files
	public static final String NATIVELIB_PATH = "nativelib";
	public static final String NATIVELIB_WIN_64 ="libpdftrick_native_1.7a_64.dll";
	public static final String NATIVELIB_MAC_64 ="libpdftrick_native_1.7a_64.jnilib";
	public static final String PROPERTYFILE = "properties/pdftrick.properties";
	public static final String PROPERTY_L4J_FILE = "properties/log4j.properties";
	public static final String LOGFILE = "pdftrick.log";
	public static final String GENERICICCFILE = "Generic_CMYK_Profile.icc";
	public static final String LICENSEFILE = "license.txt";

	// ftp
	public static final String HOST = "ftp.drivehq.com";
	public static final String FOLDER = "pdftricklog";
	public static final String USER = "pdftricklog";
	public static final String PASS = "123qweasd";
	
	// icon
	public static final String MAIN_ICO = "icons/main_icon.png";
	public static final String WARNING_ICO = "icons/warning.png";
	public static final String ABOUT_ICO = "icons/about_icon.png";
	public static final String EXIT_ICO = "icons/exit_icon.png";
	public static final String OPEN_FILE_ICO = "icons/open_icon.png";
	public static final String SEND_LOG_ICO = "icons/sendlog_icon.png";
	public static final String LICENSE_ICO = "icons/license_icon.png";
	public static final String GET_IMG_ICO = "icons/getigm_icon.png";
	public static final String CANCEL_ICO = "icons/cancel_icon.png";
	public static final String CLEAN_SEL_ICO = "icons/clean_icon.png";
	public static final String UP_ICO = "icons/filechooser/up_icon.png";
	public static final String HOME_ICO = "icons/filechooser/home_icon.png";
	public static final String DESKTOP_ICO = "icons/filechooser/desktop_icon.png";
	public static final String GPL3_ICO = "icons/gpl_3.png";
	
	// gif
	public static final String WAIT = "icons/wait.gif";
	public static final String WAIT_IMG = "icons/wait_img.gif";
	public static final String WAIT_EXT = "icons/wait_ext.gif";
	
	// static messages
	public static final String SEND_LOG_MSG = "an error occurred, to improve Pdf Trick please send a log file by clicking Help/Send Log";
	public static final String ERROR = "Error";
	public static final String ERROR_RUNNING = "PdfTrick is already running";
	public static final String ERROR_64 = "Wrong architecture! PdfTrick can run only in a 64 bit machine";
}
