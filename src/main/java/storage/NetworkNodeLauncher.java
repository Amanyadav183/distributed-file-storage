package storage;

public class NetworkNodeLauncher {

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.out.println(
                    "Usage: NetworkNodeLauncher <node-directory> <port>"
            );
            return;
        }

        String directory = args[0];
        int port = Integer.parseInt(args[1]);

        StorageNodeServer server =
                new StorageNodeServer(
                        directory,
                        port,
                        3
                );

        server.start();
    }
}