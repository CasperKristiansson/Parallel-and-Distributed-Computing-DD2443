import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceSort implements Sorter {
    public final int threads;
    private final ExecutorService executorService;
	private final Queue<Future<?>> futures = new ConcurrentLinkedQueue<>();

    public ExecutorServiceSort(int threads) {
        this.threads = threads;
        this.executorService = Executors.newFixedThreadPool(threads);
    }

    @Override
    public void sort(int[] arr) {
        try {
            parallelQuickSort(arr, 0, arr.length - 1);

            for (Future<?> future : this.futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
			executorService.shutdown();
        }
    }

    @Override
    public int getThreads() {
        return threads;
    }

    private void parallelQuickSort(int[] arr, int low, int high) {
        if (low < high) {
            if (high - low <= 500) {
                sequentialQuickSort(arr, low, high);
            } else {
                int partitionIndex = partition(arr, low, high);

                Future<?> future1 = executorService.submit(new Worker(arr, low, partitionIndex - 1));
                Future<?> future2 = executorService.submit(new Worker(arr, partitionIndex + 1, high));

                this.futures.add(future1);
                this.futures.add(future2);

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
