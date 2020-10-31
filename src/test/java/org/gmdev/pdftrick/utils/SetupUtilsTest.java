package org.gmdev.pdftrick.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

class SetupUtilsTest {

    @ParameterizedTest
    @CsvSource({
            "Windows 10, win",
            "Mac OS X, mac"
    })
    void itShouldReturnTheCorrectOsLabel(String osProperty, String expected) {
        // Given
        String property = "os.name";

        MockedStatic<SystemProperty> systemPropertyMock = Mockito.mockStatic(SystemProperty.class);
        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(property)).thenReturn(osProperty);

        // When
        String os = SetupUtils.getOs();

        // Then
        assertThat(os).isEqualTo(expected);

        // Finally
        systemPropertyMock.close();
    }

    @Test
    void itShouldThrowIfOsIsNotWindowsOrMac() {
        // Given
        String property = "os.name";
        String unknownOs = "unknown";

        MockedStatic<SystemProperty> systemPropertyMock = Mockito.mockStatic(SystemProperty.class);
        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(property)).thenReturn(unknownOs);

        // When
        assertThatThrownBy(SetupUtils::getOs)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unknown Operating system");

        // Finally
        systemPropertyMock.close();
    }

    @ParameterizedTest
    @CsvSource({
            "64, true",
            "32, false"
    })
    void itShouldReturnTrueIfJvmArchIs64BitAndFalseIfNot(String jvmArch, boolean expected) {
        // Given
        String property = "sun.arch.data.model";

        MockedStatic<SystemProperty> systemPropertyMock = Mockito.mockStatic(SystemProperty.class);
        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(property)).thenReturn(jvmArch);

        // When
        boolean isJvm64 = SetupUtils.isJvm64();

        // Then
        assertThat(isJvm64).isEqualTo(expected);

        // Finally
        systemPropertyMock.close();
    }

    @ParameterizedTest
    @CsvSource({
            "mac"
    })
    void itShouldCreatePdfTrickHomeFolder(String os) {
        // Given
        String property = "user.home";
        String fakeHome = System.getProperty("user.dir") + File.separator + "src/test/resources";

        MockedStatic<SystemProperty> systemPropertyMock = Mockito.mockStatic(SystemProperty.class);
        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(property)).thenReturn(fakeHome);

        // When
        SetupUtils.getOrCreateHomeFolder(os);

        // Then
        File expectedFakeHome = new File(fakeHome + File.separator + Constants.PDFTRICK_FOLDER);
        assertThat(expectedFakeHome.exists()).isTrue();

        // Finally
        assertThat(expectedFakeHome.delete()).isTrue();
        systemPropertyMock.close();
    }

    @ParameterizedTest
    @CsvSource({
            "mac"
    })
    void itShouldReturnThePdfTrickHomeFolder(String os) {
        // Given
        String property = "user.home";
        String fakeHome = System.getProperty("user.dir") + File.separator + "src/test/resources";

        MockedStatic<SystemProperty> systemPropertyMock = Mockito.mockStatic(SystemProperty.class);
        systemPropertyMock.when(() -> SystemProperty.getSystemProperty(property)).thenReturn(fakeHome);

        File fakeHomeFolder = new File(fakeHome + File.separator + Constants.PDFTRICK_FOLDER);
        if(!fakeHomeFolder.mkdir())
            fail();

        // When
        String pdfTrickHome = SetupUtils.getOrCreateHomeFolder(os);

        // Then
        assertThat(pdfTrickHome).isEqualTo(fakeHomeFolder.getPath());

        // Finally
        assertThat(fakeHomeFolder.delete()).isTrue();
        systemPropertyMock.close();
    }

}