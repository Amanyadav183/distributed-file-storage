package storage;

public class StorageClientTest {

    public static void main(String[] args) throws Exception {

        StorageClient client =
                new StorageClient("localhost", 5001);

        System.out.println(
                "PUT response: " +
                client.put(
                        "hello.txt",
                        "Hello from the network!"
                )
        );

        System.out.println(
                "GET response: " +
                client.get("hello.txt")
        );

        System.out.println(
                "DELETE response: " +
                client.delete("hello.txt")
        );
    }
}