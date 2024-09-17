
/**
 * Sort using Java's ParallelStreams and Lambda functions.
 *
 * Hints:
 * - Do not take advice from StackOverflow.
 * - Think outside the box.
 * - Stream of threads?
 * - Stream of function invocations?
 *
 * By default, the number of threads in parallel stream is limited by the
 * number of cores in the system. You can limit the number of threads used by
 * parallel streams by wrapping it in a ForkJoinPool.
 * ForkJoinPool myPool = new ForkJoinPool(threads);
 * myPool.submit(() -> "my parallel stream method / function");
 */
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

public class ParallelStreamSort implements Sorter {
    private final int threads;

    public ParallelStreamSort(int threads) {
        this.threads = threads;
    }

    @Override
    public void sort(int[] arr) {
        ForkJoinPool pool = new ForkJoinPool(threads);
        pool.submit(() -> parallelQuickSort(arr, 0, arr.length - 1)).join();
		pool.shutdown(); 
    }

    private void parallelQuickSort(int[] arr, int low, int high) {
        if (low < high) {
			if (high - low <= 1000) {
				sequentialQuickSort(arr, low, high);
			} else {
				int pivotIndex = partition(arr, low, high);

				// Arrays.asList(
				// 	new int[] {low, pivotIndex - 1},
				// 	new int[] {pivotIndex + 1, high}
				// ).parallelStream().forEach(bounds -> parallelQuickSort(arr, bounds[0], bounds[1]));
				Arrays.stream(new int[][]{{low, pivotIndex - 1}, {pivotIndex + 1, high}})
                    .parallel()
                    .forEach(parts -> parallelQuickSort(arr, parts[0], parts[1]));
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

    @Override
    public int getThreads() {
        return threads;
    }
}
