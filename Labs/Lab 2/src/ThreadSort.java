/**
 * Sort using Java's Thread, Runnable, start(), and join().
 */
public class ThreadSort implements Sorter {
    public final int threads;

    public ThreadSort(int threads) {
        this.threads = threads;
        int availableCores = Runtime.getRuntime().availableProcessors();
        if (threads > availableCores) {
            System.err.printf("Warning: Requested threads (%d) exceed available cores (%d).\n", threads, availableCores);
        }
    }

    @Override
    public void sort(int[] arr) {
        if (threads <= 1) {
            // Fallback to sequential quicksort if only one thread is requested
            sequentialQuickSort(arr, 0, arr.length - 1);
            return;
        }

        int chunkSize = (int) Math.ceil((double) arr.length / threads);
        Thread[] threadArray = new Thread[threads];
        int start = 0;

        // Create and start each thread
        for (int i = 0; i < threads; i++) {
            int end = Math.min(start + chunkSize, arr.length);
            threadArray[i] = new Thread(new Worker(arr, start, end - 1));
            threadArray[i].start();
            start = end;
        }

        // Wait for all threads to finish
        for (Thread t : threadArray) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Merge the sorted subarrays
        mergeAll(arr, chunkSize);
    }

    @Override
    public int getThreads() {
        return threads;
    }

    private static class Worker implements Runnable {
        private final int[] arr;
        private final int low, high;

        Worker(int[] arr, int low, int high) {
            this.arr = arr;
            this.low = low;
            this.high = high;
        }

        @Override
        public void run() {
            // Sort each chunk sequentially
            sequentialQuickSort(arr, low, high);
        }
    }

    // Sequential quicksort for individual chunks
    private static void sequentialQuickSort(int[] arr, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(arr, low, high);
            sequentialQuickSort(arr, low, partitionIndex - 1);
            sequentialQuickSort(arr, partitionIndex + 1, high);
        }
    }

    // Partition function for quicksort
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    // Swap function for array elements
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Merge function to combine sorted chunks
    private static void mergeAll(int[] arr, int chunkSize) {
        int n = arr.length;
        int[] temp = new int[n];

        // Merge sorted chunks one by one
        for (int size = chunkSize; size < n; size *= 2) {
            for (int left = 0; left < n; left += 2 * size) {
                int mid = Math.min(left + size - 1, n - 1);
                int rightEnd = Math.min(left + 2 * size - 1, n - 1);

                if (mid < rightEnd) {
                    merge(arr, temp, left, mid, rightEnd);
                }
            }
        }
    }

    // Merge function for two sorted subarrays
    private static void merge(int[] arr, int[] temp, int left, int mid, int right) {
        int i = left, j = mid + 1, k = left;

        // Merge two sorted halves into temp
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }

        // Copy remaining elements from left half
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        // Copy remaining elements from right half
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        // Copy merged subarray back to original array
        for (i = left; i <= right; i++) {
            arr[i] = temp[i];
        }
    }
}
