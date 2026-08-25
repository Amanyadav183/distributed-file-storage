public class ProcessDemo {
    public static void main(String[] args) throws Exception {
        long pid = ProcessHandle.current().pid();
        System.out.println("The Current PID : " + pid);

        while(true){
            Thread.sleep(1000);
        }
    }
}