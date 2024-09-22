import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;

public class ExecutorServiceSort implements Sorter {
    public final int threads;
    private ExecutorService executorService;
    private Semaphore semaphore;

    public ExecutorServiceSort(int threads) {
        this.threads = threads;
        this.semaphore = new Semaphore(threads);
    }

    public int getThreads() {
        return threads;
    }

    @Override
    public void sort(int[] arr) {
        this.executorService = Executors.newFixedThreadPool(threads);
        parallelQuickSort(arr, 0, arr.length - 1);

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    private void parallelQuickSort(int[] arr, int low, int high) {
        if (low < high) {
            if (high - low <= 1000) {
                sequentialQuickSort(arr, low, high);
            } else {
                if (semaphore.tryAcquire()) {
                    try {
                        int partitionIndex = partition(arr, low, high);

                        Future<?> future1 = executorService.submit(new Worker(arr, low, partitionIndex - 1));
                        parallelQuickSort(arr, partitionIndex + 1, high);

                        future1.get();

                    } catch (InterruptedException | ExecutionException e) {
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
