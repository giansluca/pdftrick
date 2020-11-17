package org.gmdev.pdftrick.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FileUtilsTest {

    private static final String HOME_FOR_TEST = "src/test/resources/home-for-test";

    @Test
    void isShouldCreateTheThumbnailsFolder() {
        // Given
        Path fakeThumbnailsFolder = Path.of(
                HOME_FOR_TEST + File.separator + Constants.PAGES_THUMBNAIL_FOLDER);

        assertThat(fakeThumbnailsFolder.toFile().exists()).isFalse();

        // When
        boolean created = FileUtils.createIfNotExistsThumbnailsFolder(fakeThumbnailsFolder);

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

        // create home folder
        FileUtils.createIfNotExistsThumbnailsFolder(fakeThumbnailsFolder);
        assertThat(fakeThumbnailsFolder.toFile().exists()).isTrue();

        // When
        boolean created = FileUtils.createIfNotExistsThumbnailsFolder(fakeThumbnailsFolder);

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

        // create home folder
        FileUtils.createIfNotExistsThumbnailsFolder(fakeThumbnailsFolderPath);
        File fakeThumbnailsFolder = fakeThumbnailsFolderPath.toFile();

        // create some fake thumbnail files
        int numberOfFiles = 3;
        createSomeFakeFiles(fakeThumbnailsFolderPath, numberOfFiles);

        File[] fakeFiles = fakeThumbnailsFolder.listFiles();
        assertThat(fakeFiles).isNotNull();
        assertThat(fakeFiles.length).isEqualTo(numberOfFiles);

        // When
        FileUtils.deleteThumbnailFiles(fakeThumbnailsFolderPath);

        // Then
        fakeFiles = fakeThumbnailsFolder.listFiles();
        assertThat(fakeFiles).isNotNull();
        assertThat(fakeFiles.length).isEqualTo(0);

        // Finally
        assertThat(fakeThumbnailsFolderPath.toFile().delete()).isTrue();
    }

    @Test
    void isShouldDeleteExtractionFolderAndImages() throws IOException {
        // Given
        Path fakeExtractionFolderPath = Path.of(
                HOME_FOR_TEST + File.separator + FileUtils.getTimeForExtractionFolder());

        // create home folder
        FileUtils.createIfNotExistsThumbnailsFolder(fakeExtractionFolderPath);
        File fakeExtractionFolder = fakeExtractionFolderPath.toFile();

        // create some fake thumbnail files
        int numberOfFiles = 5;
        createSomeFakeFiles(fakeExtractionFolderPath, numberOfFiles);

        File[] fakeFiles = fakeExtractionFolder.listFiles();
        assertThat(fakeFiles).isNotNull();
        assertThat(fakeFiles.length).isEqualTo(numberOfFiles);

        // When
        FileUtils.deleteExtractionFolderAndImages(fakeExtractionFolderPath);

        // Then
        fakeFiles = fakeExtractionFolder.listFiles();
        assertThat(fakeFiles).isNull();
    }

    @Test
    void isShouldDeletePdfFile() throws IOException {
        // Given
        Path fakePdfPath = Path.of(
                HOME_FOR_TEST + File.separator + "fake.pdf");

        // create fake pdf file
        File fakePdfFile = fakePdfPath.toFile();
        assertThat(fakePdfFile.createNewFile()).isTrue();
        assertThat(fakePdfFile.exists()).isTrue();

        // When
        FileUtils.deletePdfFile(fakePdfPath);

        // Then
        assertThat(fakePdfFile.exists()).isFalse();
    }

    public void createSomeFakeFiles(Path fakeFolder,
                                    int numberOfFiles) throws IOException {
        
        for (int i = 0; i < numberOfFiles; i++)
            if (!new File(fakeFolder + File.separator + "img-" + i).createNewFile())
                throw new IllegalStateException("Error creating fake thumbnail file");
    }
}