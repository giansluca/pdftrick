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

import static org.assertj.core.api.Assertions.assertThat;
import static org.gmdev.pdftrick.utils.Constants.NATIVE_LIB_PATH;

class PdfTrickStarterTest {

    private static final String FOR_TEST_FOLDER = "src/test/resources/for-test";

    @Test
    void isShouldBuildPdfBag() {
        // Given
        String os = SetupUtils.getOs();
        String libraryName = null;
        if (os.equals(SetupUtils.MAC_OS))
            libraryName = Constants.NATIVE_LIB_MAC_64;
        else if(os.equals(SetupUtils.WIN_OS))
            libraryName = Constants.NATIVE_LIB_WIN_64;

        Path fakeHomeFolderPath = Path.
                of(System.getProperty("user.dir") + File.separator + FOR_TEST_FOLDER);
        Path fakeNativeLibraryPath = Path
                .of(System.getProperty("user.dir") +
                        File.separator +
                        FOR_TEST_FOLDER +
                        File.separator +
                        libraryName);

        String libToCopy = NATIVE_LIB_PATH + "/" + libraryName;
        extractNativeLibrary(fakeNativeLibraryPath, libToCopy);

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
        assertThat(fakeNativeLibraryPath.toFile().delete()).isTrue();
    }

    private static void extractNativeLibrary(Path to, String libToCopy) {
        if(to.toFile().exists()) return;
        InputStream from = FileLoader.loadAsStream(libToCopy);
        try {
            Files.copy(from, to);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}