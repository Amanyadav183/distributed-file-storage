package storage;

import java.nio.charset.StandardCharsets;

public class ReplicationTest {

    public static void main(String[] args) throws Exception {

        StorageNodeManager manager =
                new StorageNodeManager(3);

        ReplicationManager replication =
                new ReplicationManager(manager);

        replication.replicate(
                "important.txt",
                "This file should survive node failure."
        );

        manager.setNodeAvailability(0, false);
        
        byte[] data =
                replication.retrieve("important.txt");

        System.out.println(
                "Final content: " +
                new String(data, StandardCharsets.UTF_8)
        );
    }
}