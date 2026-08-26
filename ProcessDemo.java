public class ProcessDemo {
    public static void main(String[] args) throws Exception {
        long pid = ProcessHandle.current().pid();
        System.out.println("The Current PID : " + pid);
        System.out.println("Main Thread : " + Thread.currentThread().getName());

        while(true){
            Thread.sleep(1000);
        }
    }
}