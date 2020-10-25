package org.gmdev.pdftrick.validation;

import org.gmdev.pdftrick.swingmanager.SwingInvoker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SingleInstanceValidatorTest {

    SingleInstanceValidator underTest;

    @BeforeEach
    void setUp() {
      MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        underTest.stopFlagServerSocket();
    }

    @Test
    void isShouldThrowIfServerSocketIsAlreadyRunning() {
        // Given
        MockedStatic<SwingInvoker> swingInvokerMock = Mockito.mockStatic(SwingInvoker.class);

        underTest = new SingleInstanceValidator();
        underTest.checkPdfTrickAlreadyRunning();

        // When
        // Then
        assertThatThrownBy(() -> underTest.startFlagServerSocket())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PdfTrick is already running");

        // Finally
        swingInvokerMock.close();
    }

}