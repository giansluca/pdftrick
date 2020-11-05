package org.gmdev.pdftrick.manager;

import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.swingmanager.UserInterfaceBuilder;
import org.gmdev.pdftrick.ui.UserInterface;
import org.gmdev.pdftrick.utils.Utils;

import java.nio.file.Path;
import java.util.Properties;

public class PdfTrickStarter {

    public static void start(String operatingSystem, Path homeFolderPath, Path nativeLibraryPath) {
        var pdfTrickBag = PdfTrickBag.getBag();
        pdfTrickBag.build(operatingSystem, homeFolderPath, nativeLibraryPath);

        Utils.deleteImgFolderAnDFiles();
        Utils.deletePdfFile();

        Properties messages = Utils.loadMessageProperties();
        pdfTrickBag.setMessages(messages);

        NativeObjectManager nativeObjectManager = new NativeObjectManager();
        pdfTrickBag.setNativeObjectManager(nativeObjectManager);

        UserInterface userInterface = UserInterfaceBuilder.build();
        pdfTrickBag.setUserInterface(userInterface);

        Utils.welcomeMessage();
    }


}
