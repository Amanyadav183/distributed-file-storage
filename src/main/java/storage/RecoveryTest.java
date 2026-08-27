package storage;

public class RecoveryTest {

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

        String fileName = "recovery.txt";

        System.out.println("=== RECOVERY TEST ===");

        // Step 1: Store the file on all nodes
        replication.put(
                fileName,
                "This file will be recovered on Node 2."
        );

        System.out.println();

        // Step 2: Simulate Node 2 losing the file
        System.out.println(
                "Deleting file from Node 2..."
        );

        node2.delete(fileName);

        // Verify Node 2 no longer has it
        try {

            System.out.println(
                    "Node 2: " +
                    node2.get(fileName)
            );

        } catch (Exception e) {

            System.out.println(
                    "Node 2 missing file: " +
                    e.getMessage()
            );
        }

        // Step 3: Recover Node 2 from Node 1
        System.out.println();

        replication.resyncReplica(
                0,
                fileName
        );

        // Step 4: Verify recovered data
        System.out.println();

        System.out.println(
                "Node 2 after recovery: " +
                node2.get(fileName)
        );

        replication.shutdown();

        System.out.println();
        System.out.println("=== RECOVERY TEST COMPLETED ===");
    }
}