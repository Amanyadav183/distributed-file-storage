package storage;

public class StorageCoordinator {

    private final StorageClient[] nodes;

    public StorageCoordinator(StorageClient... nodes) {

        if (nodes == null || nodes.length == 0) {
            throw new IllegalArgumentException(
                    "At least one storage node is required"
            );
        }

        this.nodes = nodes;
    }

    public int selectPrimary(String fileName) {

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException(
                    "File name cannot be empty"
            );
        }

        return Math.floorMod(
                fileName.hashCode(),
                nodes.length
        );
    }

    public String put(
            String fileName,
            String content) throws Exception {

        int primaryIndex =
                selectPrimary(fileName);

        StorageClient primary =
                nodes[primaryIndex];

        StorageClient[] replicas =
                new StorageClient[nodes.length - 1];

        int replicaIndex = 0;

        for (int i = 0; i < nodes.length; i++) {

            if (i != primaryIndex) {

                replicas[replicaIndex] =
                        nodes[i];

                replicaIndex++;
            }
        }

        System.out.println(
                "Selected primary Node " +
                        (primaryIndex + 1) +
                        " for file: " +
                        fileName
        );

        NetworkReplicationManager replication =
                new NetworkReplicationManager(
                        primary,
                        replicas
                );

        try {

            replication.put(
                    fileName,
                    content
            );

            return "OK|PUT";

        } finally {

            replication.shutdown();
        }
    }

    public String get(
            String fileName) throws Exception {

        int primaryIndex =
                selectPrimary(fileName);

        System.out.println(
                "Reading file from Node " +
                        (primaryIndex + 1) +
                        ": " +
                        fileName
        );

        return nodes[primaryIndex].get(
                fileName
        );
    }

    public String delete(
            String fileName) throws Exception {

        int primaryIndex =
                selectPrimary(fileName);

        System.out.println(
                "Deleting file from Node " +
                        (primaryIndex + 1) +
                        ": " +
                        fileName
        );

        return nodes[primaryIndex].delete(
                fileName
        );
    }

    public String increment(
            String fileName) throws Exception {

        int primaryIndex =
                selectPrimary(fileName);

        System.out.println(
                "Incrementing file on Node " +
                        (primaryIndex + 1) +
                        ": " +
                        fileName
        );

        return nodes[primaryIndex].increment(
                fileName
        );
    }
}