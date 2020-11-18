package org.gmdev.pdftrick.manager;

import org.gmdev.pdftrick.swingmanager.UserInterfaceBuilder;
import org.gmdev.pdftrick.ui.UserInterface;
import org.gmdev.pdftrick.utils.Messages;

import java.nio.file.Path;

import static org.gmdev.pdftrick.utils.FileUtils.*;

public class PdfTrickStarter {

    public static void start(String operatingSystem, Path homeFolderPath, Path nativeLibraryPath) {
        PdfTrickBag bag = PdfTrickBag.getBuilder()
                .os(operatingSystem)
                .homeFolderPath(homeFolderPath)
                .nativeLibraryPath(nativeLibraryPath)
                .build();

        cleanUpPdfTrickHome(bag.getThumbnailsFolderPath(), bag.getPdfFilePath());

        UserInterface userInterface = UserInterfaceBuilder.build();
        bag.setUserInterface(userInterface);

        Messages.printWelcomeMessage();
    }


}
