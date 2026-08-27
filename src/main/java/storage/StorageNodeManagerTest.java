package storage;

import java.nio.charset.StandardCharsets;

public class StorageNodeManagerTest {

    public static void main(String[] args) throws Exception {

        StorageNodeManager manager =
                new StorageNodeManager(3);

        System.out.println(
                "Total nodes: " + manager.getNodeCount()
        );

        StorageNode node1 = manager.getNode(0);

        node1.put(
                "test.txt",
                "Hello from Node 1!"
                        .getBytes(StandardCharsets.UTF_8)
        );

        byte[] data = node1.get("test.txt");

        System.out.println(
                "Retrieved: "
                        + new String(data, StandardCharsets.UTF_8)
        );

        node1.delete("test.txt");
    }
}