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

        replication.put(
                "replicated.txt",
                "This file is replicated across network nodes."
        );

        System.out.println(
        "Node 1: " +
        node1.get("replicated.txt")
);

try {

    System.out.println(
            "Node 2: " +
            node2.get("replicated.txt")
    );

} catch (Exception e) {

    System.out.println(
            "Node 2 is unavailable: " +
            e.getMessage()
    );
}

System.out.println(
        "Node 3: " +
        node3.get("replicated.txt")
);

        replication.shutdown();

    }
}