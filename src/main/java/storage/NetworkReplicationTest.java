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

        System.out.println("=== NETWORK FAILURE TEST ===");
        System.out.println();

        // Step 1: Write file
        replication.put(
                "replicated.txt",
                "This file is replicated across network nodes."
        );

        System.out.println();

        // Step 2: Check Node 1
        try {

            System.out.println(
                    "Node 1: " +
                    node1.get("replicated.txt")
            );

        } catch (Exception e) {

            System.out.println(
                    "Node 1 unavailable: " +
                    e.getMessage()
            );
        }

        // Step 3: Check Node 2
        try {

            System.out.println(
                    "Node 2: " +
                    node2.get("replicated.txt")
            );

        } catch (Exception e) {

            System.out.println(
                    "Node 2 unavailable: " +
                    e.getMessage()
            );
        }

        // Step 4: Check Node 3
        try {

            System.out.println(
                    "Node 3: " +
                    node3.get("replicated.txt")
            );

        } catch (Exception e) {

            System.out.println(
                    "Node 3 unavailable: " +
                    e.getMessage()
            );
        }

        System.out.println();
        System.out.println("=== FAILURE TEST COMPLETED ===");

        replication.shutdown();
    }
}