package org.gmdev.pdftrick.manager;

import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.swingmanager.UserInterfaceBuilder;
import org.gmdev.pdftrick.ui.UserInterface;
import java.nio.file.Path;
import java.util.Properties;

import static org.gmdev.pdftrick.utils.Utils.*;

public class PdfTrickStarter {

    public static void start(String operatingSystem, Path homeFolderPath, Path nativeLibraryPath) {
        PdfTrickBag pdfTrickBag = PdfTrickBag.getBag();
        pdfTrickBag.build(operatingSystem, homeFolderPath, nativeLibraryPath);

        deleteImgFolderAnDFiles();
        deletePdfFile();

        Properties messages = loadMessageProperties();
        pdfTrickBag.setMessages(messages);

        NativeObjectManager nativeObjectManager = new NativeObjectManager();
        pdfTrickBag.setNativeObjectManager(nativeObjectManager);

        UserInterface userInterface = UserInterfaceBuilder.build();
        pdfTrickBag.setUserInterface(userInterface);

        printWelcomeMessage();
    }


}
