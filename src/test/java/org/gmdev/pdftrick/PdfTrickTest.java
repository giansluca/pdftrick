package org.gmdev.pdftrick;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.swingmanager.SwingInvoker;
import org.gmdev.pdftrick.utils.SetupUtils;
import org.gmdev.pdftrick.validation.SingleInstanceValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;

class PdfTrickTest {

    @Mock
    SingleInstanceValidator singleInstanceValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isShouldThrowIfJvmArchitectureIsNot64() {
        // Given
        String os = "mac";
        String[] args = {"-os", os};

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
    void isShouldThrowIfOsArgumentIsEmptyOrNull() {
        // Given
        String[] args1 = {};

        MockedStatic<SingleInstanceValidator> singleInstanceValidatorMock =
                Mockito.mockStatic(SingleInstanceValidator.class);
        singleInstanceValidatorMock.when(SingleInstanceValidator::getInstance)
                .thenReturn(singleInstanceValidator);
        doNothing().when(singleInstanceValidator).checkAlreadyRunning();

        // When
        // Then
        assertThatThrownBy(() -> PdfTrick.main(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Argument object cannot be null");

        assertThatThrownBy(() -> PdfTrick.main(args1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Os argument is missing");

        // Finally
        singleInstanceValidatorMock.close();
    }

    @ParameterizedTest
    @CsvSource({
            "win", "mac"
    })
    void isShouldThrowIfOsAndOsArgumentDoNotMatch(String os) {
        // Given
        String[] args = {"-os", "wrong", "-version", "test-version"};

        MockedStatic<SingleInstanceValidator> singleInstanceValidatorMock =
                Mockito.mockStatic(SingleInstanceValidator.class);
        singleInstanceValidatorMock.when(SingleInstanceValidator::getInstance)
                .thenReturn(singleInstanceValidator);
        doNothing().when(singleInstanceValidator).checkAlreadyRunning();

        MockedStatic<SetupUtils> setupUtilsMock = Mockito.mockStatic(SetupUtils.class);
        setupUtilsMock.when(SetupUtils::getOs).thenReturn(os);
        setupUtilsMock.when(SetupUtils::isJvm64).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> PdfTrick.main(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Os argument should be 'win' or 'mac'");

        // Finally
        singleInstanceValidatorMock.close();
        setupUtilsMock.close();
    }

    @Test
    void isShouldThrowIfOsArgumentNameIsMisspelled() {
        // Given
        String[] args = {"-wrong", "win", "-version", "test-version"};
        String os = "win";

        MockedStatic<SingleInstanceValidator> singleInstanceValidatorMock =
                Mockito.mockStatic(SingleInstanceValidator.class);
        singleInstanceValidatorMock.when(SingleInstanceValidator::getInstance)
                .thenReturn(singleInstanceValidator);
        doNothing().when(singleInstanceValidator).checkAlreadyRunning();

        MockedStatic<SetupUtils> setupUtilsMock = Mockito.mockStatic(SetupUtils.class);
            setupUtilsMock.when(SetupUtils::getOs).thenReturn(os);
            setupUtilsMock.when(SetupUtils::isJvm64).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> PdfTrick.main(args))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Argument name -wrong unexpected.");

        // Finally
        singleInstanceValidatorMock.close();
        setupUtilsMock.close();
    }

    @Test
    void isShouldTRunWithNoErrors() {
        // Given
        String os = "mac";
        String version = "test-version";
        String[] args = {"-os", os, "-version", version};

        MockedStatic<SetupUtils> setupUtilsMock = Mockito.mockStatic(SetupUtils.class);
        setupUtilsMock.when(SetupUtils::getOs).thenReturn(os);
        setupUtilsMock.when(SetupUtils::isJvm64).thenReturn(true);

        MockedStatic<SingleInstanceValidator> singleInstanceValidatorMock =
                Mockito.mockStatic(SingleInstanceValidator.class);
        singleInstanceValidatorMock.when(SingleInstanceValidator::getInstance)
                .thenReturn(singleInstanceValidator);
        doNothing().when(singleInstanceValidator).checkAlreadyRunning();

        MockedStatic<PdfTrickStarter> pdfTrickStarterMock = Mockito.mockStatic(PdfTrickStarter.class);

        // When
        // Then
        PdfTrick.main(args);

        // Finally
        setupUtilsMock.close();
        singleInstanceValidatorMock.close();
        pdfTrickStarterMock.close();
    }
}