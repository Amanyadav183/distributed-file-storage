package storage;

public class FinalRecoveryTest {

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

        String fileName = "put-failover.txt";

        System.out.println("=== FINAL RECOVERY TEST ===");
        System.out.println();

        // Check the file created during PUT failover
        System.out.println("Checking existing replicas:");

        try {
            System.out.println(
                    "Node 1: " +
                    node1.get(fileName)
            );
        } catch (Exception e) {
            System.out.println(
                    "Node 1 unavailable: " +
                    e.getMessage()
            );
        }

        try {
            System.out.println(
                    "Node 2: " +
                    node2.get(fileName)
            );
        } catch (Exception e) {
            System.out.println(
                    "Node 2 unavailable: " +
                    e.getMessage()
            );
        }

        try {
            System.out.println(
                    "Node 3: " +
                    node3.get(fileName)
            );
        } catch (Exception e) {
            System.out.println(
                    "Node 3 unavailable: " +
                    e.getMessage()
            );
        }

        System.out.println();
        System.out.println("Simulating Node 2 data loss...");

        // Delete only Node 2's copy
        try {
            node2.delete(fileName);
        } catch (Exception e) {
            System.out.println(
                    "Could not delete Node 2 copy: " +
                    e.getMessage()
            );
        }

        System.out.println();

        System.out.println("Node 2 after data loss:");

        try {
            System.out.println(
                    node2.get(fileName)
            );
        } catch (Exception e) {
            System.out.println(
                    "Node 2 missing file: " +
                    e.getMessage()
            );
        }

        System.out.println();

        // Resynchronize Node 2 from Node 1
        System.out.println(
                "Resynchronizing Node 2 from Node 1..."
        );

        replication.resyncReplica(
                0,
                fileName
        );

        System.out.println();

        // Verify recovered copy
        System.out.println(
                "Node 2 after recovery:"
        );

        System.out.println(
                node2.get(fileName)
        );

        System.out.println();
        System.out.println(
                "=== FINAL RECOVERY TEST COMPLETED ==="
        );

        replication.shutdown();
    }
}