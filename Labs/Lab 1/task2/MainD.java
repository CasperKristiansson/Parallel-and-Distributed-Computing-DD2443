public class MainD {
    // Create a static integer counter that will be shared between threads
    private static int sharedInt = 0;
    private static volatile boolean done = false; 
    private static boolean doneGuarded = false;

    public static class Incrementer implements Runnable {
        public void run() {
            // Increment the sharedInt 1000000 times
            for (int i = 0; i < 1000000; i++) {
                sharedInt++;
            }

            done = true;

            // Notify the printingThread of completion
            synchronized (MainD.class) {
                doneGuarded = true;
                MainD.class.notify();
            }
        }
    }

    public static class PrinterBusyWaiting implements Runnable {
        public void run() {
            while (!done) {
            }
        }
    }

    public static class PrinterGuardedBlock implements Runnable {
        public void run() {
            synchronized (MainD.class) {
                while (!doneGuarded) {
                    try {
                        MainD.class.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    void runExperimentsBusyWaiting() {
        sharedInt = 0;
        done = false;

        // Create threads for incrementing and printing
        Thread incrementingThread = new Thread(new Incrementer());
        Thread printingThread = new Thread(new PrinterBusyWaiting());

        incrementingThread.start();
        printingThread.start();

        try {
            incrementingThread.join();
            printingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void runExperimentsGuardedBlock() {
        sharedInt = 0;
        doneGuarded = false;

        // Create threads for incrementing and printing
        Thread incrementingThread = new Thread(new Incrementer());
        Thread printingThread = new Thread(new PrinterGuardedBlock());

        incrementingThread.start();
        printingThread.start();

        try {
            incrementingThread.join();
            printingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MainD mainD = new MainD();

        // Warmup X times, Experiment Y times
        int X = 10;
        int Y = 1000;

        for (int i = 0; i < X; i++) {
            mainD.runExperimentsBusyWaiting();
        }

        System.out.println("Busy-Waiting");
        long totalBusyWaitingTime = 0;

        for (int i = 0; i < Y; i++) {
            long startTime = System.nanoTime();
            mainD.runExperimentsBusyWaiting();
            long endTime = System.nanoTime();
            long delay = endTime - startTime;
            totalBusyWaitingTime += delay;
            System.out.println("Tot: (Busy-Waiting) for run " + (i + 1) + " in nanoseconds: " + delay);
        }

        for (int i = 0; i < X; i++) {
            mainD.runExperimentsGuardedBlock();
        }

        System.out.println("\nGuarded Block");
        long totalGuardedBlockTime = 0;

        for (int i = 0; i < Y; i++) {
            long startTime = System.nanoTime();
            mainD.runExperimentsGuardedBlock();
            long endTime = System.nanoTime();
            long delay = endTime - startTime;
            totalGuardedBlockTime += delay;
            System.out.println("Tot: (Guarded Block) for run " + (i + 1) + " in nanoseconds: " + delay);
        }

        double averageBusyWaitingTime = totalBusyWaitingTime / (double) Y;
        double averageGuardedBlockTime = totalGuardedBlockTime / (double) Y;

        System.out.println("Avg: (Busy-Waiting)  in nanoseconds: " + averageBusyWaitingTime);
        System.out.println("Avg: (Guarded Block) in nanoseconds: " + averageGuardedBlockTime);
    }
}
