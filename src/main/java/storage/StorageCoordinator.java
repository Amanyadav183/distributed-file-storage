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

    /*
     * Try nodes starting from the preferred primary.
     * If one node is unavailable, automatically try the next node.
     */
    private int findAvailableNode(
            String fileName) throws Exception {

        int primaryIndex = selectPrimary(fileName);

        for (int offset = 0; offset < nodes.length; offset++) {

            int index =
                    (primaryIndex + offset) % nodes.length;

            try {

                // Check whether the node is reachable
                nodes[index].get("__health_check__");

            } catch (Exception e) {

                // Connection failure -> try next node
                continue;
            }

            return index;
        }

        throw new Exception(
                "No storage nodes are currently available"
        );
    }

    public String put(
        String fileName,
        String content) throws Exception {

    int primaryIndex = selectPrimary(fileName);

    System.out.println(
            "Selected primary Node " +
                    (primaryIndex + 1) +
                    " for file: " +
                    fileName
    );

    // Try the selected primary first
    try {

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

    } catch (Exception e) {

        System.out.println(
                "Primary Node " +
                        (primaryIndex + 1) +
                        " unavailable: " +
                        e.getMessage()
        );
    }

    // Primary failed — try another node
    for (int i = 1; i < nodes.length; i++) {

        int index =
                (primaryIndex + i) % nodes.length;

        try {

            System.out.println(
                    "PUT failover: trying Node " +
                            (index + 1)
            );

            StorageClient newPrimary =
                    nodes[index];

            newPrimary.put(
                    fileName,
                    content
            );

            System.out.println(
                    "File written successfully to Node " +
                            (index + 1)
            );

            // Replicate to the remaining available nodes
            for (int j = 0; j < nodes.length; j++) {

                if (j == index) {
                    continue;
                }

                try {

                    nodes[j].put(
                            fileName,
                            content
                    );

                    System.out.println(
                            "Replicated to Node " +
                                    (j + 1)
                    );

                } catch (Exception replicaError) {

                    System.out.println(
                            "Node " +
                                    (j + 1) +
                                    " unavailable during replication: " +
                                    replicaError.getMessage()
                    );
                }
            }

            return "OK|PUT";

        } catch (Exception e) {

            System.out.println(
                    "Node " +
                            (index + 1) +
                            " unavailable: " +
                            e.getMessage()
            );
        }
    }

    throw new Exception(
            "All storage nodes are unavailable"
    );
}
    public String get(String fileName) throws Exception {

        int primaryIndex = selectPrimary(fileName);

        System.out.println(
                "Trying primary Node " +
                (primaryIndex + 1) +
                " for file: " +
                fileName
        );

        // Try primary first
        try {
            return nodes[primaryIndex].get(fileName);

        } catch (Exception e) {

            System.out.println(
                    "Primary Node " +
                    (primaryIndex + 1) +
                    " unavailable: " +
                    e.getMessage()
            );
        }

        // Failover: try the remaining nodes
        for (int i = 1; i < nodes.length; i++) {

            int index =
                    (primaryIndex + i) % nodes.length;

            try {

                System.out.println(
                        "Failover: trying Node " +
                        (index + 1)
                );

                return nodes[index].get(fileName);

            } catch (Exception e) {

                System.out.println(
                        "Node " +
                        (index + 1) +
                        " unavailable: " +
                        e.getMessage()
                );
            }
        }

        throw new Exception(
                "All storage nodes are unavailable"
        );
    }

    public String delete(
            String fileName) throws Exception {

        int primaryIndex =
                selectPrimary(fileName);

        for (int offset = 0;
             offset < nodes.length;
             offset++) {

            int index =
                    (primaryIndex + offset) % nodes.length;

            try {

                System.out.println(
                        "Attempting DELETE on Node " +
                                (index + 1)
                );

                return nodes[index].delete(fileName);

            } catch (Exception e) {

                System.out.println(
                        "Node " +
                                (index + 1) +
                                " unavailable: " +
                                e.getMessage()
                );
            }
        }

        throw new Exception(
                "No storage nodes are currently available"
        );
    }

    public String increment(
            String fileName) throws Exception {

        int primaryIndex =
                selectPrimary(fileName);

        for (int offset = 0;
             offset < nodes.length;
             offset++) {

            int index =
                    (primaryIndex + offset) % nodes.length;

            try {

                System.out.println(
                        "Attempting INCREMENT on Node " +
                                (index + 1)
                );

                return nodes[index].increment(fileName);

            } catch (Exception e) {

                System.out.println(
                        "Node " +
                                (index + 1) +
                                " unavailable: " +
                                e.getMessage()
                );
            }
        }

        throw new Exception(
                "No storage nodes are currently available"
        );
    }
}