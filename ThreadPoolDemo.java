import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolDemo {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 10; i++) {

            int taskId = i;

            executor.submit(() -> {

                String threadName = Thread.currentThread().getName();

                System.out.println(
                    threadName + " started storage operation for file-" + taskId
                );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                }

                System.out.println(
                    threadName + " completed storage operation for file-" + taskId
                );
            });
        }

        executor.shutdown();

        System.out.println("All tasks submitted.");
    }
}