package org.gmdev.pdftrick.manager;

import io.github.giansluca.jargs.Jargs;
import org.gmdev.pdftrick.swingmanager.UserInterfaceBuilder;
import org.gmdev.pdftrick.ui.UserInterface;
import org.gmdev.pdftrick.utils.Messages;

import java.nio.file.Path;

import static org.gmdev.pdftrick.PdfTrick.*;
import static org.gmdev.pdftrick.utils.FileUtils.*;

public class PdfTrickStarter {

    public static void start(Jargs arguments, Path homeFolderPath, Path nativeLibraryPath) {
        PdfTrickBag bag = PdfTrickBag.getBuilder()
                .os(arguments.getString(OS))
                .version(arguments.getString(VERSION))
                .homeFolderPath(homeFolderPath)
                .nativeLibraryPath(nativeLibraryPath)
                .build();

        cleanUpPdfTrickHome(bag.getThumbnailsFolderPath(), bag.getSavedFilePath());

        UserInterface userInterface = UserInterfaceBuilder.build();
        bag.setUserInterface(userInterface);

        Messages.printWelcomeMessage();
    }


}
