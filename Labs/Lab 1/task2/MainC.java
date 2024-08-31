public class MainC {
    // Create a static integer counter that will be shared between threads
    private static int sharedInt = 0;
    private static boolean done = false;

    public static class Incrementer implements Runnable {
        public void run() {
            // Increment the sharedInt 1000000 times
            for (int i = 0; i < 1000000; i++) {
                sharedInt++;
            }

            // Notify the printingThread of completion
            synchronized (MainC.class) {
                done = true;
                MainC.class.notify();
            }
        }
    }

    public static class Printer implements Runnable {
        public void run() {
            synchronized (MainC.class) {
                while (!done) {
					try {
						MainC.class.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
                System.out.println("sharedInt: " + sharedInt);
            }
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
