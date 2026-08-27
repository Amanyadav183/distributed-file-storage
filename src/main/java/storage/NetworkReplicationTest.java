package storage;

public class NetworkReplicationTest {

    public static void main(String[] args) throws Exception {

        StorageClient node1 =
                new StorageClient("localhost", 5001);

        StorageClient node2 =
                new StorageClient("localhost", 5002);

        StorageClient node3 =
                new StorageClient("localhost", 5003);

        NetworkReplicationManager replication =
                new NetworkReplicationManager(
                        node1,
                        node2,
                        node3
                );

        // Step 1: Replicate file to all nodes
        replication.put(
                "replicated.txt",
                "This file is replicated across network nodes."
        );

        System.out.println();

        System.out.println(
                "Node 1: " +
                node1.get("replicated.txt")
        );

        System.out.println(
                "Node 2: " +
                node2.get("replicated.txt")
        );

        System.out.println(
                "Node 3: " +
                node3.get("replicated.txt")
        );

        // Step 2: Simulate Node 2 losing its replica
        System.out.println();
        System.out.println(
                "Simulating Node 2 data loss..."
        );

        node2.delete("replicated.txt");

        try {

            node2.get("replicated.txt");

        } catch (Exception e) {

            System.out.println(
                    "Node 2 missing file: " +
                    e.getMessage()
            );
        }

        // Step 3: Resynchronize Node 2 from the primary
        System.out.println();

        replication.resyncReplica(
                0,
                "replicated.txt"
        );

        // Step 4: Verify recovered replica
        System.out.println();

        System.out.println(
                "Node 2 after resynchronization: " +
                node2.get("replicated.txt")
        );

        replication.shutdown();
    }
}