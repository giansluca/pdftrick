package org.gmdev.pdftrick;

import org.gmdev.pdftrick.utils.SetuptUtils;
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
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Os argument is missing");

        assertThatThrownBy(() -> PdfTrick.main(null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Os argument is missing");
    }

    @Test
    void isShouldThrowIfSystemAndOsDoNotMatch() {
        // Given
        String[] args = {"wrong"};

        try (MockedStatic<SetuptUtils> setupUtilsMock = Mockito.mockStatic(SetuptUtils.class)) {
            setupUtilsMock.when(SetuptUtils::isWindows).thenReturn(true);

            // When
            // Then
            assertThatThrownBy(() -> PdfTrick.main(args))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("in Windows system Os argument should be " + "'win'");
        }

        try (MockedStatic<SetuptUtils> setupUtilsMock = Mockito.mockStatic(SetuptUtils.class)) {
            setupUtilsMock.when(SetuptUtils::isWindows).thenReturn(false);
            setupUtilsMock.when(SetuptUtils::isMac).thenReturn(true);

            // When
            // Then
            assertThatThrownBy(() -> PdfTrick.main(args))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("in Mac system Os argument should be " + "'mac'");
        }
    }
}