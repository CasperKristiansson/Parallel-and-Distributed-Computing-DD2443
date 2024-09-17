import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceSort implements Sorter {
    public final int threads;
    private int step;
    private ExecutorService executorService;

    public ExecutorServiceSort(int threads) {
        this.threads = threads;
        switch (threads) {
            case 2:
                this.step = 1000000;
                break;
            case 4:
                this.step = 600000;
                break;
            case 8:
                this.step = 300000;
                break;
            case 16:
                this.step = 130000;
                break;
            case 32:
                this.step = 63000;
                break;
            case 48:
                this.step = 41666;
                break;
            case 64:
                this.step = 31250;
                break;
            case 96:
                this.step = 20833;
                break;
            default:
                break;
        }
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
			if (high - low <= this.step) {
				sequentialQuickSort(arr, low, high);
			} else {
				int partitionIndex = partition(arr, low, high);

				Future<?> future1 = executorService.submit(new Worker(arr, low, partitionIndex - 1));
				Future<?> future2 = executorService.submit(new Worker(arr, partitionIndex + 1, high));

				try {
                    future1.get();
                    future2.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
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
