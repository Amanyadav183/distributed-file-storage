package storage;

public class StorageCoordinatorTest {

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

        String fileName = "coordinator.txt";

        System.out.println(
                "Primary index: " +
                        coordinator.selectPrimary(fileName)
        );

        System.out.println();

        System.out.println(
                "PUT: " +
                        coordinator.put(
                                fileName,
                                "Hello from the coordinator!"
                        )
        );

        System.out.println();

        System.out.println(
                "GET: " +
                        coordinator.get(fileName)
        );

        System.out.println();

        System.out.println(
                "DELETE: " +
                        coordinator.delete(fileName)
        );
    }
}