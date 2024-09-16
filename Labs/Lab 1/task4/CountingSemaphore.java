public class CountingSemaphore {
    private int count; // Semaphore counter

    public CountingSemaphore(int n) {
        this.count = n;
    }

	public synchronized int getCount() {
		return count;
	}

    public synchronized void signal() {
		// Increment the semaphore counter
        count++;
		System.out.println(Thread.currentThread().getId() + ": signal count: " + count);
		// If the counter is greater than 0, notify one thread
        if (count > 0) {
			System.out.println(Thread.currentThread().getId() + ": notifying");
            notify();
        }
    }

    public synchronized void s_wait() {
		// Decrement the semaphore counter
		System.out.println(Thread.currentThread().getId() + ": wait count: " + count);
		// If the counter is less than 0, wait
        while (count <= 0) {
            System.out.println(Thread.currentThread().getId() + ": checking " + count);
            try {
				// Wait until the semaphore counter is greater than 0
				System.out.println(Thread.currentThread().getId() + ": waiting");
                wait();
				System.out.println(Thread.currentThread().getId() + ": woke up");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        count--;
    }
}
