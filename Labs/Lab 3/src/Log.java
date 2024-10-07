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
		ADD, REMOVE, CONTAINS
	}
}