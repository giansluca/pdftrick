package org.gmdev.pdftrick.manager;

import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.swingmanager.UserInterfaceBuilder;
import org.gmdev.pdftrick.ui.UserInterface;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.PropertyLoader;

import java.nio.file.Path;
import java.util.Properties;

import static org.gmdev.pdftrick.utils.Utils.*;

public class PdfTrickStarter {

    public static void start(String operatingSystem, Path homeFolderPath, Path nativeLibraryPath) {
        PdfTrickBag bag = PdfTrickBag.INSTANCE;
        bag.init(operatingSystem, homeFolderPath, nativeLibraryPath);

        cleanUp(bag.getThumbnailsFolderPath(), bag.getPdfFilePath());

        Properties messagesProps = PropertyLoader.loadMessagesPropertyFile();
        bag.setMessagesProps(messagesProps);

        NativeObjectManager nativeObjectManager = new NativeObjectManager();
        bag.setNativeObjectManager(nativeObjectManager);

        UserInterface userInterface = UserInterfaceBuilder.build();
        bag.setUserInterface(userInterface);

        Messages.printWelcomeMessage();
    }


}
