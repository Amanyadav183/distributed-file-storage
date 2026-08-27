package storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StorageNode {

    private final Path storageDirectory;

    public StorageNode(String directory) throws IOException {
        storageDirectory = Path.of(directory);

        Files.createDirectories(storageDirectory);

        System.out.println(
                "Storage node initialized at: " + storageDirectory.toAbsolutePath()
        );
    }

    public void put(String fileName, byte[] data) throws IOException {

        Path filePath = storageDirectory.resolve(fileName);

        Files.write(filePath, data);

        System.out.println("Stored file: " + fileName);
    }

    public byte[] get(String fileName) throws IOException {

        Path filePath = storageDirectory.resolve(fileName);

        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + fileName);
        }

        System.out.println("Reading file: " + fileName);

        return Files.readAllBytes(filePath);
    }

    public void delete(String fileName) throws IOException {

        Path filePath = storageDirectory.resolve(fileName);

        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + fileName);
        }

        Files.delete(filePath);

        System.out.println("Deleted file: " + fileName);
    }
}