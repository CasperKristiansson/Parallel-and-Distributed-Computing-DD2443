import java.util.concurrent.locks.ReentrantLock;

public class MainA {

    public static class Philosopher implements Runnable {
        private final ReentrantLock leftChopstick;
        private final ReentrantLock rightChopstick;
        private final int philosopherNumber;

        public Philosopher(ReentrantLock leftChopstick, ReentrantLock rightChopstick, int philosopherNumber) {
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
            this.philosopherNumber = philosopherNumber;
        }

        public void run() {
            try {
                while (true) {
                    think();
                    eat();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        private void think() throws InterruptedException {
            System.out.println(philosopherNumber + ": thinking.");
            Thread.sleep((int) (Math.random() * 5));
        }

        private void eat() throws InterruptedException {
            leftChopstick.lock();
            try {
                System.out.println(philosopherNumber + ": picked up left chopstick.");
                rightChopstick.lock();
                try {
                    System.out.println(philosopherNumber + ": picked up right chopstick");
                    Thread.sleep((int) (Math.random() * 20));
                } finally {
                    rightChopstick.unlock();
                }
            } finally {
                leftChopstick.unlock();
            }
        }
    }

    public static void main(String[] args) {
        int numberOfPhilosophers = 15;

        Thread[] threads = new Thread[numberOfPhilosophers];
        ReentrantLock[] chopsticks = new ReentrantLock[numberOfPhilosophers];

        for (int i = 0; i < numberOfPhilosophers; i++) {
            chopsticks[i] = new ReentrantLock();
        }

        for (int i = 0; i < numberOfPhilosophers; i++) {
            ReentrantLock leftChopstick = chopsticks[i];
			// to make sure that we have the correct amount of chopsticks, because the last philosopher should take the first chopstick as the right one
            ReentrantLock rightChopstick = chopsticks[(i + 1) % numberOfPhilosophers];

            threads[i] = new Thread(new Philosopher(leftChopstick, rightChopstick, i));
            threads[i].start();
        }
    }
}
