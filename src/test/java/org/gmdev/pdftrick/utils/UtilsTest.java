package org.gmdev.pdftrick.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {

    private static final String HOME_FOR_TEST = "src/test/resources/home-for-test";

    @Test
    void isShouldCreateTheThumbnailsFolder() {
        // Given
        Path fakeThumbnailsFolder = Path.of(
                HOME_FOR_TEST + File.separator + Constants.PAGES_THUMBNAIL_FOLDER);

        // When
        assertThat(fakeThumbnailsFolder.toFile().exists()).isFalse();
        boolean created = Utils.createIfNotExistsThumbnailsFolder(fakeThumbnailsFolder);

        // Then
        assertThat(created).isTrue();
        assertThat(fakeThumbnailsFolder.toFile().exists()).isTrue();

        // Finally
        assertThat(fakeThumbnailsFolder.toFile().delete()).isTrue();
    }

    @Test
    void isShouldNotCreateTheThumbnailsFolder() {
        // Given
        Path fakeThumbnailsFolder = Path.of(
                HOME_FOR_TEST + File.separator + Constants.PAGES_THUMBNAIL_FOLDER);

        Utils.createIfNotExistsThumbnailsFolder(fakeThumbnailsFolder);

        // When
        assertThat(fakeThumbnailsFolder.toFile().exists()).isTrue();
        boolean created = Utils.createIfNotExistsThumbnailsFolder(fakeThumbnailsFolder);

        // Then
        assertThat(created).isFalse();

        // Finally
        assertThat(fakeThumbnailsFolder.toFile().delete()).isTrue();
    }
}