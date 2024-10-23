import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class Log {
	private Log() {
		// Do not implement
	}

	public static int validate(Log.Entry[] log) {
        HashSet<Integer> simulatedSet = new HashSet<>();
        int discrepancies = 0;

        Arrays.sort(log, Comparator.comparingLong(e -> e.timestamp));

        long lastRemoveTimestamp = -1;
        for (int i = log.length - 1; i >= 0; i--) {
            Log.Entry entry = log[i];
            if (entry.method == Log.Method.REMOVE) {
                lastRemoveTimestamp = entry.timestamp;
            } else if (entry.method == Log.Method.REMOVE_PLACE_HOLDER) {
                if (lastRemoveTimestamp != -1) {
                    entry.timestamp = lastRemoveTimestamp;
                    entry.method = Log.Method.REMOVE;
                }
            }
        }

		Arrays.sort(log, Comparator.comparingLong(e -> e.timestamp));

        for (Log.Entry entry : log) {
            boolean expected = false;
            switch (entry.method) {
                case ADD:
                    expected = simulatedSet.add(entry.arg);
                    break;
                case REMOVE:
                    expected = simulatedSet.remove(entry.arg);
                    break;
                case CONTAINS:
                    expected = simulatedSet.contains(entry.arg);
                    break;
                default:
                    break;
            }

            if (expected != entry.ret) {
                discrepancies++;
            }
        }

        return discrepancies;
    }

	// Log entry for linearization point.
	public static class Entry {
		public Method method;
		public int arg;
		public boolean ret;
		public long timestamp;

		public Entry(Method method, int arg, boolean ret, long timestamp) {
			this.method = method;
			this.arg = arg;
			this.ret = ret;
			this.timestamp = timestamp;
		}
	}

	public static enum Method {
		ADD, REMOVE, CONTAINS, REMOVE_PLACE_HOLDER
	}
}