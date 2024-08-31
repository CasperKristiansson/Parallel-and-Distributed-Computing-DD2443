public class MainB {
    // Create a static integer counter that will be shared between threads
    private static int sharedInt = 0;

	// Create a volatile boolean flag that will be shared between threads
    private static volatile boolean done = false;

    public static class Incrementer implements Runnable {
        public void run() {
            // Increment the sharedInt 1000000 times
            for (int i = 0; i < 1000000; i++) {
                sharedInt++;
            }
            // Set the done flag to true
            done = true;
        }
    }

    public static class Printer implements Runnable {
        public void run() {
            while (!done) {
                // Busy-waiting
            }
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