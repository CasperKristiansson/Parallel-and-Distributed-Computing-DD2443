public class Main {

    public static class Producer implements Runnable {
        private final Buffer buffer;

        public Producer(Buffer buffer) {
            this.buffer = buffer;
        }

		// Add elements to the buffer
        public void run() {
            try {
                for (int i = 0; i < 1000000; i++) {
                    buffer.add(i);
                }
            } catch (IllegalStateException e) {
                System.out.println("Producer stopped: " + e.getMessage());
            } finally {
                buffer.close();
            }
        }
    }

    public static class Consumer implements Runnable {
        private final Buffer buffer;

        public Consumer(Buffer buffer) {
            this.buffer = buffer;
        }

		// Remove elements from the buffer
        public void run() {
            try {
                while (true) {
                    int value = buffer.remove();
                    System.out.println("Consumed: " + value);
                }
            } catch (IllegalStateException e) {
                System.out.println("Consumer stopped: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Buffer buffer = new Buffer(100);

        Thread producerThread = new Thread(new Producer(buffer));
        Thread consumerThread = new Thread(new Consumer(buffer));

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Main thread finished.");
    }
}
