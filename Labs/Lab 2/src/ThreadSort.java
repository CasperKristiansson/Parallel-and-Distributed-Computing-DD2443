import java.util.concurrent.Semaphore;

public class ThreadSort implements Sorter {
    public final int threads;
    private Semaphore semaphore;

    public ThreadSort(int threads) {
        this.threads = threads;
        this.semaphore = new Semaphore(threads);
    }

    public int getThreads() {
        return threads;
    }

    @Override
    public void sort(int[] arr) {
        parallelQuickSort(arr, 0, arr.length - 1);
    }

    private void parallelQuickSort(int[] arr, int low, int high) {
        if (low < high) {
            if (high - low <= 1000) {
                sequentialQuickSort(arr, low, high);
            } else {
                if (semaphore.tryAcquire()) {
                    try {
                        int partitionIndex = partition(arr, low, high);

                        Thread leftThread = new Thread(new Worker(arr, low, partitionIndex - 1));
                        leftThread.start();
                        parallelQuickSort(arr, partitionIndex + 1, high);
                        
                        leftThread.join();
                        
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                    }
                } else {
                    sequentialQuickSort(arr, low, high);
                }
            }
        }
    }

    private void sequentialQuickSort(int[] arr, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(arr, low, high);
            sequentialQuickSort(arr, low, partitionIndex - 1);
            sequentialQuickSort(arr, partitionIndex + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    private class Worker implements Runnable {
        private final int[] arr;
        private final int low;
        private final int high;

        Worker(int[] arr, int low, int high) {
            this.arr = arr;
            this.low = low;
            this.high = high;
        }

        @Override
        public void run() {
            parallelQuickSort(arr, low, high);
        }
    }
}
