package storage;

import java.nio.charset.StandardCharsets;

public class ReplicationManager {

    private final StorageNodeManager nodeManager;

    public ReplicationManager(StorageNodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    public void replicate(String fileName, String content)
            throws Exception {

        byte[] data = content.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < nodeManager.getNodeCount(); i++) {

            StorageNode node = nodeManager.getNode(i);

            node.put(fileName, data);

            System.out.println(
                    "Replicated " + fileName +
                    " to Node " + (i + 1)
            );
        }
    }

    public byte[] retrieve(String fileName) throws Exception {

    for (int i = 0; i < nodeManager.getNodeCount(); i++) {

        if (!nodeManager.isNodeAvailable(i)) {

            System.out.println(
                    "Node " + (i + 1) +
                    " is unavailable. Skipping..."
            );

            continue;
        }

        try {

            StorageNode node = nodeManager.getNode(i);

            byte[] data = node.get(fileName);

            System.out.println(
                    "Retrieved " + fileName +
                    " from Node " + (i + 1)
            );

            return data;

        } catch (Exception e) {

            System.out.println(
                    "Node " + (i + 1) +
                    " does not have " + fileName
            );
        }
    }

    throw new Exception(
            "File unavailable on all available storage nodes: "
                    + fileName
    );
}
}