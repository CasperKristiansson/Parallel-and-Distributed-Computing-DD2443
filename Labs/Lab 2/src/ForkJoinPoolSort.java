import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ForkJoinPoolSort implements Sorter {
    public final int threads;
    private ForkJoinPool forkJoinPool;

    public ForkJoinPoolSort(int threads) {
        this.threads = threads;
    }

    @Override
    public void sort(int[] arr) {
        this.forkJoinPool = new ForkJoinPool(this.threads);

        forkJoinPool.invoke(new Worker(arr, 0, arr.length - 1));
    }

    @Override
    public int getThreads() {
        return threads;
    }

    private static class Worker extends RecursiveAction {
        private final int[] arr;
        private final int low, high;

        Worker(int[] arr, int low, int high) {
            this.arr = arr;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (high - low <= 1000) {
                sequentialQuickSort(arr, low, high);
            } else {
                int partitionIndex = partition(arr, low, high);
                Worker leftTask = new Worker(arr, low, partitionIndex - 1);
                Worker rightTask = new Worker(arr, partitionIndex + 1, high);
                invokeAll(leftTask, rightTask);
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
    }
}
