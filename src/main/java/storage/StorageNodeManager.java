package storage;

import java.util.ArrayList;
import java.util.List;

public class StorageNodeManager {

    private final List<StorageNode> nodes = new ArrayList<>();
    private final List<Boolean> nodeAvailability = new ArrayList<>();

    public StorageNodeManager(int numberOfNodes) throws Exception {

        for (int i = 1; i <= numberOfNodes; i++) {

            String directory = "node-data/node" + i;

            StorageNode node = new StorageNode(directory);

            nodes.add(node);
            nodeAvailability.add(true);
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

    public boolean isNodeAvailable(int index) {
        return nodeAvailability.get(index);
    }

    public void setNodeAvailability(int index, boolean available) {

        if (index < 0 || index >= nodes.size()) {
            throw new IllegalArgumentException(
                    "Invalid node index: " + index
            );
        }

        nodeAvailability.set(index, available);

        System.out.println(
                "Node " + (index + 1) +
                " availability: " + available
        );
    }
}