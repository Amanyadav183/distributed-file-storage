package storage;

import java.util.ArrayList;
import java.util.List;

public class StorageNodeManager {

    private final List<StorageNode> nodes = new ArrayList<>();

    public StorageNodeManager(int numberOfNodes) throws Exception {

        for (int i = 1; i <= numberOfNodes; i++) {

            String directory = "node-data/node" + i;

            StorageNode node = new StorageNode(directory);

            nodes.add(node);
        }

        System.out.println(
                "Initialized " + numberOfNodes + " storage nodes."
        );
    }

    public StorageNode getNode(int index) {

        if (index < 0 || index >= nodes.size()) {
            throw new IllegalArgumentException(
                    "Invalid node index: " + index
            );
        }

        return nodes.get(index);
    }

    public int getNodeCount() {
        return nodes.size();
    }
}