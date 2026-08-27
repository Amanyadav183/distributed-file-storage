package storage;

public class FailoverTest {

    public static void main(String[] args) throws Exception {

        StorageClient node1 =
                new StorageClient("localhost", 5001);

        StorageClient node2 =
                new StorageClient("localhost", 5002);

        StorageClient node3 =
                new StorageClient("localhost", 5003);

        StorageCoordinator coordinator =
                new StorageCoordinator(
                        node1,
                        node2,
                        node3
                );

        String fileName = "failover.txt";

        System.out.println("=== FAILOVER TEST ===");
        System.out.println();

        // Store file on primary and replicate it
        System.out.println("Storing file...");
        System.out.println(
                coordinator.put(
                        fileName,
                        "This file will survive primary node failure."
                )
        );

        System.out.println();

        // Check which node is primary
        int primary =
                coordinator.selectPrimary(fileName);

        System.out.println(
                "Primary Node: " +
                (primary + 1)
        );

        System.out.println();

        // Read while all nodes are available
        System.out.println("Reading before failure:");

        System.out.println(
                coordinator.get(fileName)
        );

        System.out.println();
        System.out.println(
                "Now stop the PRIMARY node manually."
        );
        System.out.println(
                "Then press ENTER here to continue..."
        );

        System.in.read();

        System.out.println();
        System.out.println(
                "Reading after primary failure:"
        );

        System.out.println(
                coordinator.get(fileName)
        );

        System.out.println();
        System.out.println(
                "=== FAILOVER TEST COMPLETED ==="
        );
    }
}