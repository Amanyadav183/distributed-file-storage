package storage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LostUpdateTest {

    public static void main(String[] args) throws Exception {

        StorageClient clientA =
                new StorageClient("localhost", 5001);

        StorageClient clientB =
                new StorageClient("localhost", 5001);

        // Reset counter
        clientA.put("counter.txt", "0");

        System.out.println("Initial value: 0");

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        Future<?> taskA = executor.submit(() -> {

            try {

                String result =
                        clientA.increment("counter.txt");

                System.out.println(
                        "Client A increment result: " +
                        result
                );

            } catch (Exception e) {

                System.out.println(
                        "Client A failed: " +
                        e.getMessage()
                );
            }
        });

        Future<?> taskB = executor.submit(() -> {

            try {

                String result =
                        clientB.increment("counter.txt");

                System.out.println(
                        "Client B increment result: " +
                        result
                );

            } catch (Exception e) {

                System.out.println(
                        "Client B failed: " +
                        e.getMessage()
                );
            }
        });

        taskA.get();
        taskB.get();

        executor.shutdown();

        String finalValue =
                clientA.get("counter.txt");

        System.out.println();
        System.out.println(
                "Final value: " + finalValue
        );

        System.out.println(
                "Expected value: 2"
        );
    }
}