import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class MainB {

    public static class Philosopher implements Runnable {
        private final ReentrantLock leftChopstick;
        private final ReentrantLock rightChopstick;
        private final int philosopherNumber;
        private final Semaphore semaphore;

        public Philosopher(ReentrantLock leftChopstick, ReentrantLock rightChopstick, int philosopherNumber, Semaphore semaphore) {
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
            this.philosopherNumber = philosopherNumber;
            this.semaphore = semaphore;
        }

        public void run() {
            try {
                while (true) {
                    think();
                    semaphore.acquire();
                    eat();
                    semaphore.release();
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
                    System.out.println(philosopherNumber + ": picked up right chopstick.");
                    System.out.println(philosopherNumber + ": eating.");
                    Thread.sleep((int) (Math.random() * 20));
                } finally {
                    rightChopstick.unlock();
                    System.out.println(philosopherNumber + ": put down right chopstick.");
                }
            } finally {
                leftChopstick.unlock();
                System.out.println(philosopherNumber + ": put down left chopstick.");
            }
        }
    }

    public static void main(String[] args) {
        int numberOfPhilosophers = 15;
        Semaphore semaphore = new Semaphore(numberOfPhilosophers - 1);
        Thread[] threads = new Thread[numberOfPhilosophers];
        ReentrantLock[] chopsticks = new ReentrantLock[numberOfPhilosophers];

        for (int i = 0; i < numberOfPhilosophers; i++) {
            chopsticks[i] = new ReentrantLock();
        }

        for (int i = 0; i < numberOfPhilosophers; i++) {
            ReentrantLock leftChopstick = chopsticks[i];
            ReentrantLock rightChopstick = chopsticks[(i + 1) % numberOfPhilosophers];
            threads[i] = new Thread(new Philosopher(leftChopstick, rightChopstick, i, semaphore));
            threads[i].start();
        }
    }
}
