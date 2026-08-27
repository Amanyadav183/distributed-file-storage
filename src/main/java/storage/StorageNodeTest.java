package storage;

import java.nio.charset.StandardCharsets;

public class StorageNodeTest {

    public static void main(String[] args) throws Exception {

        StorageNode node = new StorageNode("node-data");

        String message = "Hello from Storage Node!";

        node.put(
                "hello.txt",
                message.getBytes(StandardCharsets.UTF_8)
        );

        byte[] data = node.get("hello.txt");

        System.out.println(
                "Retrieved: " +
                new String(data, StandardCharsets.UTF_8)
        );

        node.delete("hello.txt");
    }
}