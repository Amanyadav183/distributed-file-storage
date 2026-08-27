package storage;

public class FailoverPutTest {

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

        String fileName = "put-failover.txt";

        System.out.println("=== PUT FAILOVER TEST ===");
        System.out.println();

        int primary =
                coordinator.selectPrimary(fileName);

        System.out.println(
                "Selected primary Node: " +
                (primary + 1)
        );

        System.out.println();

        System.out.println(
                "Stop the selected primary node (Node " +
                (primary + 1) +
                ") now."
        );

        System.out.println(
                "Then press ENTER to continue..."
        );

        System.in.read();

        System.out.println();
        System.out.println("Attempting PUT after primary failure:");

        try {

            System.out.println(
                    coordinator.put(
                            fileName,
                            "This file was written using PUT failover."
                    )
            );

        } catch (Exception e) {

            System.out.println(
                    "PUT failed: " +
                    e.getMessage()
            );
        }

        System.out.println();
        System.out.println("=== PUT FAILOVER TEST COMPLETED ===");
    }
}