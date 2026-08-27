package storage;

public class StorageCoordinatorReplicationTest {

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

        String fileName = "coordinator-replicated.txt";

        System.out.println(
                "Primary index: " +
                        coordinator.selectPrimary(fileName)
        );

        System.out.println();

        System.out.println(
                "Coordinator PUT:"
        );

        System.out.println(
                coordinator.put(
                        fileName,
                        "Hello from coordinator replication!"
                )
        );

        System.out.println();

        System.out.println(
                "Checking all nodes:"
        );

        System.out.println(
                "Node 1: " +
                        node1.get(fileName)
        );

        System.out.println(
                "Node 2: " +
                        node2.get(fileName)
        );

        System.out.println(
                "Node 3: " +
                        node3.get(fileName)
        );

        System.out.println();

        System.out.println(
                "Deleting test file..."
        );

        // Delete from all nodes so the test
        // does not leave files behind.
        try {
            node1.delete(fileName);
        } catch (Exception ignored) {
        }

        try {
            node2.delete(fileName);
        } catch (Exception ignored) {
        }

        try {
            node3.delete(fileName);
        } catch (Exception ignored) {
        }

        System.out.println(
                "Coordinator replication test completed."
        );
    }
}