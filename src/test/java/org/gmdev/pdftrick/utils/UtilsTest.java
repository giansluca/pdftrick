package org.gmdev.pdftrick.utils;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class UtilsTest {

    private static final String HOME_FOR_TEST = "src/test/resources/home-for-test";

    @Test
    void isShouldPass() {
        // Given
        MockedStatic<PdfTrickBag> pdfTrickBagMock = Mockito.mockStatic(PdfTrickBag.class);

        // When

        // Then

        // Finally
        pdfTrickBagMock.close();
    }
}