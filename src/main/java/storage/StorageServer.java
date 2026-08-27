package storage;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StorageServer {

    private final StorageNode storageNode;
    private final ExecutorService executor;

    public StorageServer(String storageDirectory, int numberOfThreads)
            throws Exception {

        storageNode = new StorageNode(storageDirectory);
        executor = Executors.newFixedThreadPool(numberOfThreads);

        System.out.println(
                "Storage server started with "
                        + numberOfThreads
                        + " worker threads."
        );
    }

    public void put(String fileName, String content) {

        executor.submit(() -> {

            String threadName = Thread.currentThread().getName();

            System.out.println(
                    threadName + " processing PUT: " + fileName
            );

            try {
                storageNode.put(
                        fileName,
                        content.getBytes(StandardCharsets.UTF_8)
                );
            } catch (Exception e) {
                System.err.println(
                        "PUT failed for " + fileName + ": "
                                + e.getMessage()
                );
            }
        });
    }

    public void delete(String fileName) {

        executor.submit(() -> {

            String threadName = Thread.currentThread().getName();

            System.out.println(
                    threadName + " processing DELETE: " + fileName
            );

            try {
                storageNode.delete(fileName);
            } catch (Exception e) {
                System.err.println(
                        "DELETE failed for " + fileName + ": "
                                + e.getMessage()
                );
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
        System.out.println("Storage server shutting down.");
    }
}