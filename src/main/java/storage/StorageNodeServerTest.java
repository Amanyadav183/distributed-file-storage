package storage;

public class StorageNodeServerTest {

    public static void main(String[] args) throws Exception {

        StorageNodeServer server =
                new StorageNodeServer(
                        "network-node-data",
                        5001,
                        3
                );

        server.start();
    }
}