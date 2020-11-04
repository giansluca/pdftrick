package org.gmdev.pdftrick.swingmanager;

import org.gmdev.pdftrick.ui.UserInterface;
import org.gmdev.pdftrick.utils.Utils;

public class UserInterfaceBuilder {

    private static UserInterface userInterface;

    public static UserInterface build() {
        Runnable buildUiTask = () -> {
            userInterface = new UserInterface();
            userInterface.setVisible(true);
        };

        SwingInvoker.invokeAndWait(buildUiTask);
        return userInterface;
    }

}
