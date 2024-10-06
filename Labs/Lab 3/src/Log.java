import java.util.HashSet;
import java.util.Set;

public class Log {
	private Log() {
	}

	public static int validate(Log.Entry[] log) {
		Set<Integer> set = new HashSet<Integer>();
		int discrepancies = 0;
		for (Log.Entry entry : log) {
			switch (entry.method) {
				case ADD:
					if (set.contains(entry.arg)) {
						discrepancies++;
					} else {
						set.add(entry.arg);
					}
					break;
				case REMOVE:
					if (!set.contains(entry.arg)) {
						discrepancies++;
					} else {
						set.remove(entry.arg);
					}
					break;
				case CONTAINS:
					boolean expected = set.contains(entry.arg);
					if (expected != entry.ret) {
						discrepancies++;
					}
					break;
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
