package storage;

public class StorageServerTest {

    public static void main(String[] args) throws Exception {

        StorageServer server =
                new StorageServer("server-data", 3);

        for (int i = 1; i <= 10; i++) {

            server.put(
                    "file-" + i + ".txt",
                    "Data for file " + i
            );
        }

        Thread.sleep(3000);

        server.shutdown();
    }
}