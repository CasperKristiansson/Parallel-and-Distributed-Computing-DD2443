public class MainA {
    // Create a static integer counter that will be shared between threads
    private static int sharedInt = 0;

    public static class Incrementer implements Runnable {
        public void run() {
            // Increment the sharedInt 1000000 times
            for (int i = 0; i < 1000000; i++) {
                sharedInt++;
            }
        }
    }

    public static class Printer implements Runnable {
        public void run() {
            // Print the value of sharedInt
            System.out.println("Final value of sharedInt: " + sharedInt);
        }
    }

    public static void main(String[] args) {
        // Create threads for incrementing and printing
        Thread incrementingThread = new Thread(new Incrementer());
        Thread printingThread = new Thread(new Printer());

        // Start the threads
        incrementingThread.start();
        printingThread.start();
    }
}
