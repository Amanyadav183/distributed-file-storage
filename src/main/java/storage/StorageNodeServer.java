package storage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StorageNodeServer {

    private final StorageNode storageNode;
    private final ExecutorService executor;
    private final int port;

    public StorageNodeServer(String storageDirectory, int port, int numberOfThreads)
            throws Exception {

        storageNode = new StorageNode(storageDirectory);
        executor = Executors.newFixedThreadPool(numberOfThreads);
        this.port = port;
    }

    public void start() throws Exception {

        ServerSocket serverSocket = new ServerSocket(port);

        System.out.println(
                "Storage node server listening on port " + port
        );

        while (true) {

            Socket clientSocket = serverSocket.accept();

            executor.submit(() -> handleClient(clientSocket));
        }
    }

    private void handleClient(Socket socket) {

    try (
            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream())
                    );

            PrintWriter writer =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true
                    )
    ) {

        String request = reader.readLine();

        if (request == null || request.isBlank()) {
            writer.println("ERROR|Empty request");
            return;
        }

        System.out.println("Received request: " + request);

        String[] parts = request.split("\\|", 3);

        String command = parts[0];

        switch (command) {

            case "PUT":

                if (parts.length < 3) {
                    writer.println("ERROR|Invalid PUT request");
                    return;
                }

                String putFileName = parts[1];
                String content = parts[2];

                storageNode.put(
                        putFileName,
                        content.getBytes(java.nio.charset.StandardCharsets.UTF_8)
                );

                writer.println("OK|PUT");

                break;

            case "GET":

                if (parts.length < 2) {
                    writer.println("ERROR|Invalid GET request");
                    return;
                }

                String getFileName = parts[1];

                byte[] data = storageNode.get(getFileName);

                String fileContent =
                        new String(
                                data,
                                java.nio.charset.StandardCharsets.UTF_8
                        );

                writer.println("OK|" + fileContent);

                break;

            case "DELETE":

                if (parts.length < 2) {
                    writer.println("ERROR|Invalid DELETE request");
                    return;
                }

                String deleteFileName = parts[1];

                storageNode.delete(deleteFileName);

                writer.println("OK|DELETE");

                break;

            default:

                writer.println(
                        "ERROR|Unknown command: " + command
                );
        }

    } catch (Exception e) {

        System.err.println(
                "Client handling failed: " + e.getMessage()
        );

    } finally {

        try {
            socket.close();
        } catch (Exception ignored) {
        }
    }
}
}