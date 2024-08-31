public class MainC {
    // Create a static integer counter that will be shared between threads
    @SuppressWarnings("unused")
    private static int counter = 0;

    // Create a synchronized method that increments the counter
    private static synchronized void incrementCounter() {
        counter++;
    }

    public static class Incrementer implements Runnable {
        public void run() {
            // Increment the counter 1000000 times
            for (int i = 0; i < 1000000; i++) {
                incrementCounter();
            }
        }
    }

    long run_experiments(int n) {
        long startTime = System.nanoTime();
        counter = 0;

        // Create n threads and store them in an array
        Thread[] threads = new Thread[n];

        // Loop over the threads and start them with the Incrementer runnable
        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(new Incrementer());
            threads[i].start();
        }

        // Loop over the threads and join them
        for (int i = 0; i < n; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public static void main(String[] args) {
        MainC mainC = new MainC();

        // Number of warmups
        int X = 10;
        // Number of experiments
        int Y = 20;

        // Array of thread counts
        int[] threads = {1, 2, 4, 8, 16, 32, 64};

        // Warmup
        for (int i : threads) {
            for (int y = 0; y < X; y++) {
                mainC.run_experiments(i);
            }
        }

        // Experiment
        for (int x : threads) {
            long totalTime = 0;
            long[] times = new long[Y];

            for (int y = 0; y < Y; y++) {
                long time = mainC.run_experiments(x);
                times[y] = time;  // Store each time for standard deviation calculation
                totalTime += time;
            }

            // Calculate average time
            double averageTime = totalTime / (double) Y;

            // Calculate variance
            double variance = 0;
            for (int y = 0; y < Y; y++) {
                variance += Math.pow(times[y] - averageTime, 2);
            }
            variance /= Y;

            // Calculate standard deviation
            double stdDeviation = Math.sqrt(variance);

            System.out.println("threads = " + x);
            System.out.println("Average time: " + averageTime + " ns");
            System.out.println("Standard deviation: " + stdDeviation + " ns\n");
        }
    }
}
