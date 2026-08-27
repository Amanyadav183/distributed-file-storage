package storage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class StorageClient {

    private final String host;
    private final int port;

    public StorageClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String sendRequest(String request) throws Exception {

        try (Socket socket = new Socket(host, port);

             BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     );

             PrintWriter writer =
                     new PrintWriter(
                             socket.getOutputStream(),
                             true
                     )) {

            writer.println(request);

            return reader.readLine();
        }
    }

    public String put(String fileName, String content)
            throws Exception {

        return sendRequest(
                "PUT|" + fileName + "|" + content
        );
    }

    public String get(String fileName)
            throws Exception {

        return sendRequest(
                "GET|" + fileName
        );
    }

    public String delete(String fileName)
            throws Exception {

        return sendRequest(
                "DELETE|" + fileName
        );
    }
}