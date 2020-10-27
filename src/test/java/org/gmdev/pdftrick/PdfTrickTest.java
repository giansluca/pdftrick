package org.gmdev.pdftrick;

import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.swingmanager.SwingInvoker;
import org.gmdev.pdftrick.utils.SetupUtils;
import org.gmdev.pdftrick.validation.SingleInstanceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

class PdfTrickTest {

    @Mock
    PdfTrickBag pdfTrickBag;

    @Mock
    SingleInstanceValidator singleInstanceValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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

    @Test
    void isShouldTRunWithNoErrors() {
        // Given
        String os = "mac";
        String[] args = {os};

        MockedStatic<SetupUtils> setupUtilsMock = Mockito.mockStatic(SetupUtils.class);
        setupUtilsMock.when(SetupUtils::getOs).thenReturn(os);
        setupUtilsMock.when(SetupUtils::isJvm64).thenReturn(true);

        MockedStatic<PdfTrickFactory> pdfTrickFactoryMock = Mockito.mockStatic(PdfTrickFactory.class);
        pdfTrickFactoryMock.when(PdfTrickFactory::getSingleInstanceValidator)
                .thenReturn(singleInstanceValidator);
        doNothing().when(singleInstanceValidator).checkPdfTrickAlreadyRunning();

        MockedStatic<PdfTrickBag> pdfTrickBagMock = Mockito.mockStatic(PdfTrickBag.class);
        pdfTrickBagMock.when(PdfTrickBag::getPdfTrickBag).thenReturn(pdfTrickBag);
        doNothing().when(pdfTrickBag).initialize(anyString(), anyString());

        // When
        // Then
        PdfTrick.main(args);

        // Finally
        setupUtilsMock.close();
        pdfTrickBagMock.close();
    }
}