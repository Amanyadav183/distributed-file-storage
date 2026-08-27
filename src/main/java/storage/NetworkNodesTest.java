package storage;

public class NetworkNodesTest {

    public static void main(String[] args) throws Exception {

        StorageClient node1 =
                new StorageClient("localhost", 5001);

        StorageClient node2 =
                new StorageClient("localhost", 5002);

        StorageClient node3 =
                new StorageClient("localhost", 5003);

        System.out.println(
                "Node 1: " +
                node1.put(
                        "distributed.txt",
                        "Hello from the distributed storage system!"
                )
        );

        System.out.println(
                "Node 1 GET: " +
                node1.get("distributed.txt")
        );

        System.out.println(
                "Node 2 GET: " +
                node2.get("distributed.txt")
        );

        System.out.println(
                "Node 3 GET: " +
                node3.get("distributed.txt")
        );
    }
}