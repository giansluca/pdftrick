package org.gmdev.pdftrick.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

}