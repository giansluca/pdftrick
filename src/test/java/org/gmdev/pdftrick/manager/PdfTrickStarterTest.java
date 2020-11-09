package org.gmdev.pdftrick.manager;

import org.gmdev.pdftrick.swingmanager.UserInterfaceBuilder;
import org.gmdev.pdftrick.utils.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class PdfTrickStarterTest {

    private static final String HOME_FOR_TEST = "src/test/resources/home-for-test";

    @Test
    void isShouldBuildPdfBag() {
        // Given
        Path fakeHomeFolderPath = Path.
                of(System.getProperty("user.dir") + File.separator + HOME_FOR_TEST);

        String os = SetupUtils.getOs();
        Path fakeNativeLibraryPath = SetupUtils.setAndGetNativeLibrary(fakeHomeFolderPath, os);

        MockedStatic<Utils> utilsMock = Mockito.mockStatic(Utils.class);
        MockedStatic<UserInterfaceBuilder> userInterfaceBuilderMock = Mockito.mockStatic(UserInterfaceBuilder.class);
        MockedStatic<Messages> messagesMock = Mockito.mockStatic(Messages.class);

        // When
        PdfTrickStarter.start(os, fakeHomeFolderPath, fakeNativeLibraryPath);

        // Then
        PdfTrickBag bag = PdfTrickBag.INSTANCE;
        assertThat(bag.getNativeLibraryPath()).isEqualTo(fakeNativeLibraryPath);
        assertThat(bag.getNativeObjectManager()).isNotNull();

        // Finally
        userInterfaceBuilderMock.close();
        utilsMock.close();
        messagesMock.close();

        bag.getNativeObjectManager().unloadNativeLib();
        assertThat(fakeNativeLibraryPath.toFile().delete()).isTrue();
    }


}