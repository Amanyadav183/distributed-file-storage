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

            System.out.println(
                    "Received request: " + request
            );

            writer.println("ACK: " + request);

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