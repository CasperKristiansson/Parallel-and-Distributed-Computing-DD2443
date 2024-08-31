import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final int N;					// Buffer Size
    private final Queue<Integer> buffer;
    private final Lock lock;				// Lock for synchronization
    private final Condition notEmpty;		// Condition for not empty buffer
    private final Condition notFull;		// Condition for not full buffer
    private boolean isClosed;

    public Buffer(int N) {
        this.N = N;
        this.buffer = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
        this.isClosed = false;
    }

	// Add an element to the buffer
    public void add(int i) {
		// Aquire the lock
        lock.lock();
        try {
			// Wait until the buffer is not full
            while (buffer.size() == N) {
				// If the buffer is closed, throw an exception
                if (isClosed) {
                    throw new IllegalStateException("Closed");
                }
				// Wait until the buffer is not full
                notFull.await();
            }
			// Add the element to the buffer
            buffer.add(i);
			// Signal that the buffer is not empty
            notEmpty.signal();
        } catch (InterruptedException e) {
			return;
        } finally {
			// Release the lock
            lock.unlock();
        }
    }

    public int remove() {
		// Aquire the lock
        lock.lock();
        try {
			// Wait until the buffer is not empty
            while (buffer.isEmpty()) {
                if (isClosed) {
                    throw new IllegalStateException("Buffer is closed and empty");
                }
				// Wait until the buffer is not empty
                notEmpty.await();
            }
			// Remove the element from the buffer
            int value = buffer.remove();
            notFull.signal();
            return value;
        } catch (InterruptedException e) {
            return -1;
        } finally {
			// Release the lock
            lock.unlock();
        }
    }

    public void close() {
		// Aquire the lock
        lock.lock();
        try {
            if (isClosed) {
                throw new IllegalStateException("Buffer is already closed");
            }
			// Set the buffer as closed
            isClosed = true;
            notEmpty.signalAll();
            notFull.signalAll();
        } finally {
			// Release the lock
            lock.unlock();
        }
    }
}
