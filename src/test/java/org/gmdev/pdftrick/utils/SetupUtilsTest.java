package org.gmdev.pdftrick.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

class SetupUtilsTest {

    private static final String HOME_FOR_TEST = "src/test/resources/home-for-test";

    MockedStatic<SystemProperty> systemPropertyMock;

    @BeforeEach
    void setUp() {
        systemPropertyMock = Mockito.mockStatic(SystemProperty.class);
    }

    @AfterEach
    void tearDown() {
        systemPropertyMock.close();
    }

    @ParameterizedTest
    @CsvSource({
            "Windows 10, win",
            "Mac OS X, mac"
    })
    void itShouldReturnTheCorrectOsLabel(String osProperty, String expected) {
        // Given
        String property = "os.name";

        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(property)).thenReturn(osProperty);

        // When
        String os = SetupUtils.getOs();

        // Then
        assertThat(os).isEqualTo(expected);
    }

    @Test
    void itShouldThrowIfOsIsNotWindowsOrMac() {
        // Given
        String property = "os.name";
        String unknownOs = "unknown";

        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(property)).thenReturn(unknownOs);

        // When
        assertThatThrownBy(SetupUtils::getOs)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unknown Operating system");
    }

    @ParameterizedTest
    @CsvSource({
            "64, true",
            "32, false"
    })
    void itShouldReturnTrueIfJvmArchIs64BitAndFalseIfNot(String jvmArch, boolean expected) {
        // Given
        String property = "sun.arch.data.model";

        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(property)).thenReturn(jvmArch);

        // When
        boolean isJvm64 = SetupUtils.isJvm64();

        // Then
        assertThat(isJvm64).isEqualTo(expected);
    }

    @Test
    void itShouldCreatePdfTrickHomeFolder() {
        // Given
        String propertyHome = "user.home";
        String fakeHome = System.getProperty("user.dir") + File.separator + HOME_FOR_TEST;
        String propertyOs = "os.name";

        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(propertyHome)).thenReturn(fakeHome);
        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(propertyOs))
                .thenReturn(System.getProperty(propertyOs));

        String os = SetupUtils.getOs();

        File expectedHomeFolder = new File(
                fakeHome + File.separator + Constants.PDFTRICK_HOME_FOLDER);

        // When
        Path homeFolder = SetupUtils.setAndGetHomeFolder(os);

        // Then
        assertThat(expectedHomeFolder.exists()).isTrue();
        assertThat(homeFolder).isEqualTo(expectedHomeFolder.toPath());

        // Finally
        assertThat(expectedHomeFolder.delete()).isTrue();
    }

    @Test
    void itShouldReturnThePdfTrickHomeFolderPath() {
        // Given
        String property = "user.home";
        String fakeHome = System.getProperty("user.dir") + File.separator + HOME_FOR_TEST;
        String propertyOs = "os.name";

        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(property)).thenReturn(fakeHome);
        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(propertyOs))
                .thenReturn(System.getProperty(propertyOs));

        String os = SetupUtils.getOs();

        File expectedHomeFolder = new File(
                fakeHome + File.separator + Constants.PDFTRICK_HOME_FOLDER);
        if(!expectedHomeFolder.mkdir())
            fail();

        // When
        Path homeFolderPath = SetupUtils.setAndGetHomeFolder(os);

        // Then
        assertThat(homeFolderPath).isEqualTo(expectedHomeFolder.toPath());

        // Finally
        assertThat(expectedHomeFolder.delete()).isTrue();
    }

    @Test
    void itShouldExtractTheNativeLibrary() {
        // Given
        String property = "user.home";
        String fakeHome = System.getProperty("user.dir") + File.separator + HOME_FOR_TEST;
        String propertyOs = "os.name";

        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(property)).thenReturn(fakeHome);
        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(propertyOs))
                .thenReturn(System.getProperty(propertyOs));

        String os = SetupUtils.getOs();
        String libraryName = null;
        if (os.equals(SetupUtils.MAC_OS))
            libraryName = Constants.NATIVE_LIB_MAC_FILE;
        else if(os.equals(SetupUtils.WIN_OS))
            libraryName = Constants.NATIVE_LIB_WIN_FILE;
        Path expectedLibraryPath = Path.of(fakeHome + File.separator + libraryName);

        // When
        Path libraryPath = SetupUtils.setAndGetNativeLibrary(Path.of(fakeHome), os);

        // Then
        assertThat(libraryPath).isEqualTo(expectedLibraryPath);

        // Finally
        assertThat(libraryPath.toFile().delete()).isTrue();
    }

    @Test
    void itShouldReturnTheNativeLibraryPath() {
        // Given
        String property = "user.home";
        String fakeHome = System.getProperty("user.dir") + File.separator + HOME_FOR_TEST;
        String propertyOs = "os.name";

        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(property)).thenReturn(fakeHome);
        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(propertyOs))
                .thenReturn(System.getProperty(propertyOs));

        String os = SetupUtils.getOs();
        String libraryName = null;
        if (os.equals(SetupUtils.MAC_OS))
            libraryName = Constants.NATIVE_LIB_MAC_FILE;
        else if (os.equals(SetupUtils.WIN_OS))
            libraryName = Constants.NATIVE_LIB_WIN_FILE;

        File expectedLibraryFile = createExpectedLibraryFile(fakeHome, libraryName);

        // When
        Path libraryPath = SetupUtils.setAndGetNativeLibrary(Path.of(fakeHome), os);

        // Then
        assertThat(libraryPath).isEqualTo(expectedLibraryFile.toPath());

        // Finally
        assertThat(expectedLibraryFile.delete()).isTrue();
    }

    private File createExpectedLibraryFile(String fakeHome, String libraryName) {
        File expectedLibraryFile = new File(fakeHome + File.separator + libraryName);
        if (!expectedLibraryFile.exists()) {
            try {
                if (!expectedLibraryFile.createNewFile())
                    fail();
            } catch (IOException e) {
                fail();
            }
        }

        return expectedLibraryFile;
    }

}