package storage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentWriteTest {

    public static void main(String[] args) throws Exception {

        StorageClient clientA =
                new StorageClient("localhost", 5001);

        StorageClient clientB =
                new StorageClient("localhost", 5001);

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        Future<?> taskA = executor.submit(() -> {

            try {
                for (int i = 1; i <= 10; i++) {

                    clientA.put(
                            "shared.txt",
                            "Client A - write " + i
                    );

                    System.out.println(
                            "Client A completed write " + i
                    );
                }

            } catch (Exception e) {

                System.out.println(
                        "Client A failed: " + e.getMessage()
                );
            }
        });

        Future<?> taskB = executor.submit(() -> {

            try {
                for (int i = 1; i <= 10; i++) {

                    clientB.put(
                            "shared.txt",
                            "Client B - write " + i
                    );

                    System.out.println(
                            "Client B completed write " + i
                    );
                }

            } catch (Exception e) {

                System.out.println(
                        "Client B failed: " + e.getMessage()
                );
            }
        });

        taskA.get();
        taskB.get();

        executor.shutdown();

        System.out.println();
        System.out.println(
                "Final file content:"
        );

        System.out.println(
                clientA.get("shared.txt")
        );
    }
}