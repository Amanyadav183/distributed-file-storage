package storage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NetworkReplicationManager {

    private final StorageClient primary;
    private final StorageClient[] replicas;
    private final ExecutorService executor;

    public NetworkReplicationManager(
            StorageClient primary,
            StorageClient... replicas) {

        this.primary = primary;
        this.replicas = replicas;

        this.executor =
                Executors.newFixedThreadPool(replicas.length);
    }

    public void put(
            String fileName,
            String content) throws Exception {

        // Store on primary first
        String primaryResponse =
                primary.put(fileName, content);

        System.out.println(
                "Primary response: " + primaryResponse
        );

        // Replicate to all secondary nodes concurrently
        Future<?>[] tasks = new Future<?>[replicas.length];

        for (int i = 0; i < replicas.length; i++) {

            final int replicaIndex = i;

            tasks[i] = executor.submit(() -> {

                try {

                    String response =
                            replicas[replicaIndex].put(
                                    fileName,
                                    content
                            );

                    System.out.println(
                            "Replica Node " +
                            (replicaIndex + 2) +
                            " response: " +
                            response
                    );

                } catch (Exception e) {

                    System.out.println(
                            "Replica Node " +
                            (replicaIndex + 2) +
                            " failed: " +
                            e.getMessage()
                    );
                }
            });
        }

        // Wait for all replication tasks
        for (Future<?> task : tasks) {
            task.get();
        }

        System.out.println(
            "Replication tasks completed. Some replicas may have failed."
        );
    }

    public void resyncReplica(
        int replicaIndex,
        String fileName) throws Exception {

    if (replicaIndex < 0 || replicaIndex >= replicas.length) {
        throw new IllegalArgumentException(
                "Invalid replica index: " + replicaIndex
        );
    }

    System.out.println(
            "Starting resynchronization for Node "
                    + (replicaIndex + 2)
    );

    // Read the authoritative copy from the primary
    String primaryResponse =
            primary.get(fileName);

    if (primaryResponse == null ||
            !primaryResponse.startsWith("OK|")) {

        throw new Exception(
                "Primary could not provide file: " + fileName
        );
    }

    String content =
            primaryResponse.substring(3);

    // Copy the missing data to the recovered replica
    String replicaResponse =
            replicas[replicaIndex].put(
                    fileName,
                    content
            );

    System.out.println(
            "Resynchronization response from Node "
                    + (replicaIndex + 2)
                    + ": "
                    + replicaResponse
    );

    System.out.println(
            "Node "
                    + (replicaIndex + 2)
                    + " resynchronized successfully."
    );
}

    public void shutdown() {

        executor.shutdown();

        System.out.println(
                "Replication executor shut down."
        );
    }
}