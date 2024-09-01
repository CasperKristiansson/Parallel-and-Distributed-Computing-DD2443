public class Main {

    public static class Runner implements Runnable {
        private final CountingSemaphore semaphore;

        public Runner(CountingSemaphore semaphore) {
            this.semaphore = semaphore;
        }

        public void run() {
            semaphore.s_wait();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            semaphore.signal();
            System.out.println(Thread.currentThread().getId() + ": releasing the semaphore, count: " + semaphore.getCount());
        }
    }

    public static void main(String[] args) {
        CountingSemaphore semaphore = new CountingSemaphore(3);

        for (int i = 0; i < 5; i++) {
            new Thread(new Runner(semaphore)).start();
        }
    }
}
