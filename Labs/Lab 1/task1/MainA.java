public class MainA {
	// Create a volatile integer counter that will be shared between threads using the keyword static volatile
    private static volatile int counter = 0;

    public static class Incrementer implements Runnable {
        public void run() {
			// Increment the counter 1000000 times
            for (int i = 0; i < 1000000; i++) {
                counter++;
            }
        }
    }

    public static void main(String[] args) {
		// Create 4 threads and store them in an array
        Thread[] threads = new Thread[4];

		// Loop over the threads and start them with the Incrementer runnable
        for (int i = 0; i < 4; i++) {
            threads[i] = new Thread(new Incrementer());
            threads[i].start();
        }
		
		// Loop over the threads and join them
        for (int i = 0; i < 4; i++) {
            try {
				threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Counter value: " + counter);
    }
}
