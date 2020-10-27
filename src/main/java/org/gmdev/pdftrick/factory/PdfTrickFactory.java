package org.gmdev.pdftrick.factory;

import org.gmdev.pdftrick.validation.SingleInstanceValidator;

public class PdfTrickFactory {

    private static SingleInstanceValidator singleInstanceValidator;

    public static SingleInstanceValidator getSingleInstanceValidator() {
        if (singleInstanceValidator == null)
            singleInstanceValidator = new SingleInstanceValidator();

        return singleInstanceValidator;
    }
}
