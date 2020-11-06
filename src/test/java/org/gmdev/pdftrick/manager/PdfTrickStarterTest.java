package org.gmdev.pdftrick.manager;

import org.gmdev.pdftrick.swingmanager.UserInterfaceBuilder;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.FileLoader;
import org.gmdev.pdftrick.utils.SetupUtils;
import org.gmdev.pdftrick.utils.Utils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gmdev.pdftrick.utils.Constants.NATIVE_LIB_PATH;
import static org.junit.jupiter.api.Assertions.fail;

class PdfTrickStarterTest {

    private static final String HOME_FOR_TEST = "src/test/resources/for-test";

    @Test
    void isShouldBuildPdfBag() {
        // Given

        Path fakeHomeFolderPath = Path.
                of(System.getProperty("user.dir") + File.separator + HOME_FOR_TEST);

        String os = SetupUtils.getOs();
        Path fakeNativeLibraryPath = SetupUtils.setAndGetNativeLibrary(fakeHomeFolderPath, os);

        MockedStatic<Utils> utilsMock = Mockito.mockStatic(Utils.class);
        MockedStatic<UserInterfaceBuilder> userInterfaceBuilderMock = Mockito.mockStatic(UserInterfaceBuilder.class);

        // When
        PdfTrickStarter.start(os, fakeHomeFolderPath, fakeNativeLibraryPath);

        // Then
        PdfTrickBag bag = PdfTrickBag.INSTANCE;
        assertThat(bag.getNativeLibraryPath()).isEqualTo(fakeNativeLibraryPath);
        assertThat(bag.getNativeObjectManager()).isNotNull();

        // Finally
        userInterfaceBuilderMock.close();
        utilsMock.close();

        bag.getNativeObjectManager().unloadNativeLib();
        assertThat(fakeNativeLibraryPath.toFile().delete()).isTrue();
    }

}