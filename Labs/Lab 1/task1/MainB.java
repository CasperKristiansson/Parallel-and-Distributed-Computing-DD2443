public class MainB {
	// Create a static integer counter that will be shared between threads
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
        MainB mainB = new MainB();

        long timeTaken = mainB.run_experiments(4);

        System.out.println("Counter Value: " + counter);
        System.out.println("Time: " + timeTaken + " ns");
    }
}