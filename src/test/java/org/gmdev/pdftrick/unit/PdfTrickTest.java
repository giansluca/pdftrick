package org.gmdev.pdftrick.unit;

import org.gmdev.pdftrick.PdfTrick;
import org.gmdev.pdftrick.swingmanager.SwingInvoker;
import org.gmdev.pdftrick.utils.SetupUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PdfTrickTest {

    @Test
    void isShouldThrowIfOsArgumentIsNull() {
        // Given
        String[] args1 = {};

        // When
        // Then
        assertThatThrownBy(() -> PdfTrick.main(args1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Os argument is missing");

        assertThatThrownBy(() -> PdfTrick.main(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Os argument is missing");
    }

    @ParameterizedTest
    @CsvSource({
            "win", "mac"
    })
    void isShouldThrowIfOsAndOsArgumentDoNotMatch(String os) {
        // Given
        String[] args = {"wrong"};

        try (MockedStatic<SetupUtils> setupUtilsMock = Mockito.mockStatic(SetupUtils.class)) {
            setupUtilsMock.when(SetupUtils::getOs).thenReturn(os);

            // When
            // Then
            assertThatThrownBy(() -> PdfTrick.main(args))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Os argument should be 'win' or 'mac'");
        }
    }

    @Test
    void isShouldThrowIfJvmArchitectureIsNot64() {
        // Given
        String os = "mac";
        String[] args = {os};

        MockedStatic<SetupUtils> setupUtilsMock = Mockito.mockStatic(SetupUtils.class);
        setupUtilsMock.when(SetupUtils::getOs).thenReturn(os);
        setupUtilsMock.when(SetupUtils::isJvm64).thenReturn(false);

        MockedStatic<SwingInvoker> swingInvokerMock = Mockito.mockStatic(SwingInvoker.class);

        // When
        // Then
        assertThatThrownBy(() -> PdfTrick.main(args))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PdfTrick can run only on a 64 bit Jvm");

        // Finally
        setupUtilsMock.close();
        swingInvokerMock.close();
    }
}