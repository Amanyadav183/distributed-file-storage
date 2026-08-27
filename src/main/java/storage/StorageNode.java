package storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

public class StorageNode {

    private final Path storageDirectory;

    private final ConcurrentHashMap<String, Object> fileLocks =
        new ConcurrentHashMap<>();

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
    public int increment(String fileName) throws IOException {

    Object lock = fileLocks.computeIfAbsent(
            fileName,
            key -> new Object()
    );

    synchronized (lock) {

        Path filePath = storageDirectory.resolve(fileName);

        int currentValue = 0;

        if (Files.exists(filePath)) {

            String content =
                    Files.readString(filePath).trim();

            if (!content.isEmpty()) {
                currentValue = Integer.parseInt(content);
            }
        }

        int newValue = currentValue + 1;

        Files.writeString(
                filePath,
                String.valueOf(newValue)
        );

        System.out.println(
                "Atomically incremented " +
                fileName +
                ": " +
                currentValue +
                " -> " +
                newValue
        );

        return newValue;
    }
}
}