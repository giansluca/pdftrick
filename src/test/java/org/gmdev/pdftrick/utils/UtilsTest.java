package org.gmdev.pdftrick.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {

    private static final String HOME_FOR_TEST = "src/test/resources/home-for-test";

    @Test
    void isShouldCreateTheThumbnailsFolder() {
        // Given
        Path fakeThumbnailsFolder = Path.of(
                HOME_FOR_TEST + File.separator + Constants.PAGES_THUMBNAIL_FOLDER);

        assertThat(fakeThumbnailsFolder.toFile().exists()).isFalse();

        // When
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
        assertThat(fakeThumbnailsFolder.toFile().exists()).isTrue();

        // When
        boolean created = Utils.createIfNotExistsThumbnailsFolder(fakeThumbnailsFolder);

        // Then
        assertThat(created).isFalse();

        // Finally
        assertThat(fakeThumbnailsFolder.toFile().delete()).isTrue();
    }

    @Test
    void isShouldDeletePdfPagesThumbnails() throws IOException {
        // Given
        Path fakeThumbnailsFolderPath = Path.of(
                HOME_FOR_TEST + File.separator + Constants.PAGES_THUMBNAIL_FOLDER);

        Utils.createIfNotExistsThumbnailsFolder(fakeThumbnailsFolderPath);
        File fakeThumbnailsFolder = fakeThumbnailsFolderPath.toFile();

        int numberOfFiles = 3;
        createSomeFakeThumbnailFileS(fakeThumbnailsFolderPath, numberOfFiles);

        File[] fakeFiles = fakeThumbnailsFolder.listFiles();
        assertThat(fakeFiles).isNotNull();
        assertThat(fakeFiles.length).isEqualTo(numberOfFiles);

        // When
        Utils.deleteThumbnailFiles(fakeThumbnailsFolderPath);

        // Then
        fakeFiles = fakeThumbnailsFolder.listFiles();
        assertThat(fakeFiles).isNotNull();
        assertThat(fakeFiles.length).isEqualTo(0);

        // Finally
        assertThat(fakeThumbnailsFolderPath.toFile().delete()).isTrue();
    }

    public void createSomeFakeThumbnailFileS(Path fakeThumbnailsFolder,
                                             int numberOfFiles) throws IOException {
        for (int i = 0; i < numberOfFiles; i++)
            if (!new File(fakeThumbnailsFolder + File.separator + "img" + i).createNewFile())
                throw new IllegalStateException("Error creating fake thumbnail file");
    }
}