import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LockFreeSkipListLocal<T extends Comparable<T>> implements LockFreeSet<T> {
	private static final int MAX_LEVEL = 16;
	private static final int MAX_THREADS = 96;

	private final Node<T> head = new Node<T>();
	private final Node<T> tail = new Node<T>();

	private List<List<Log.Entry>> list = new ArrayList<>();

	public LockFreeSkipListLocal() {
		for (int i = 0; i < head.next.length; i++) {
			head.next[i] = new AtomicMarkableReference<LockFreeSkipListLocal.Node<T>>(tail, false);
		}

		list = new ArrayList<>();
        for (int i = 0; i < MAX_THREADS; i++) {
            list.add(new ArrayList<>());
        }
	}

	private static final class Node<T> {
		private final T value;
		private final AtomicMarkableReference<Node<T>>[] next;
		private final int topLevel;

		@SuppressWarnings("unchecked")
		public Node() {
			value = null;
			next = (AtomicMarkableReference<Node<T>>[]) new AtomicMarkableReference[MAX_LEVEL + 1];
			for (int i = 0; i < next.length; i++) {
				next[i] = new AtomicMarkableReference<Node<T>>(null, false);
			}
			topLevel = MAX_LEVEL;
		}

		@SuppressWarnings("unchecked")
		public Node(T x, int height) {
			value = x;
			next = (AtomicMarkableReference<Node<T>>[]) new AtomicMarkableReference[height + 1];
			for (int i = 0; i < next.length; i++) {
				next[i] = new AtomicMarkableReference<Node<T>>(null, false);
			}
			topLevel = height;
		}
	}

	/*
	 * Returns a level between 0 to MAX_LEVEL,
	 * P[randomLevel() = x] = 1/2^(x+1), for x < MAX_LEVEL.
	 */
	private static int randomLevel() {
		int r = ThreadLocalRandom.current().nextInt();
		int level = 0;
		r &= (1 << MAX_LEVEL) - 1;
		while ((r & 1) != 0) {
			r >>>= 1;
			level++;
		}
		return level;
	}

	@SuppressWarnings("unchecked")
	public boolean add(int threadId, T x) {
		int topLevel = randomLevel();
		int bottomLevel = 0;
		Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL + 1];
		Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL + 1];
		long[] timestamp = new long[1];
		while (true) {
			boolean found = find(x, preds, succs, timestamp);
			if (found) {
				logOperation(threadId, Log.Method.ADD, x.hashCode(), false, timestamp[0]);
				return false;
			} else {
				Node<T> newNode = new Node(x, topLevel);
				for (int level = bottomLevel; level <= topLevel; level++) {
					Node<T> succ = succs[level];
					newNode.next[level].set(succ, false);
				}
				Node<T> pred = preds[bottomLevel];
				Node<T> succ = succs[bottomLevel];

				if (!pred.next[bottomLevel].compareAndSet(succ, newNode, false, false)) {
					continue;
				}
				timestamp[0] = System.nanoTime();
				logOperation(threadId, Log.Method.ADD, x.hashCode(), true, timestamp[0]);

				for (int level = bottomLevel + 1; level <= topLevel; level++) {
					while (true) {
						pred = preds[level];
						succ = succs[level];
						if (pred.next[level].compareAndSet(succ, newNode, false, false))
							break;
						find(x, preds, succs, timestamp);
					}
				}
				return true;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public boolean remove(int threadId, T x) {
		int bottomLevel = 0;
		Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL + 1];
		Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL + 1];
		Node<T> succ;
		long[] findTimeStamp = new long[1];
		while (true) {
			boolean found = find(x, preds, succs, findTimeStamp);
			if (!found) {
				logOperation(threadId, Log.Method.REMOVE, x.hashCode(), false, findTimeStamp[0]);
				return false;
			} else {
				Node<T> nodeToRemove = succs[bottomLevel];
				for (int level = nodeToRemove.topLevel; level >= bottomLevel + 1; level--) {
					boolean[] marked = { false };
					succ = nodeToRemove.next[level].get(marked);
					while (!marked[0]) {
						nodeToRemove.next[level].compareAndSet(succ, succ, false, true);
						succ = nodeToRemove.next[level].get(marked);
					}
				}
				boolean[] marked = { false };
				succ = nodeToRemove.next[bottomLevel].get(marked);
				while (true) {
					boolean iMarkedIt = nodeToRemove.next[bottomLevel].compareAndSet(succ, succ, false, true);
					findTimeStamp[0] = System.nanoTime();
					succ = succs[bottomLevel].next[bottomLevel].get(marked);
					if (iMarkedIt) {
						logOperation(threadId, Log.Method.REMOVE, x.hashCode(), true, findTimeStamp[0]);
						find(x, preds, succs, findTimeStamp);
						return true;
					} else if (marked[0]) {
						logOperation(threadId, Log.Method.REMOVE_PLACE_HOLDER, x.hashCode(), false, System.nanoTime());
						return false;
					}
				}
			}
		}
	}

	public boolean contains(int threadId, T x) {
		int bottomLevel = 0;
		boolean[] marked = { false };
		Node<T> pred = head;
		Node<T> curr = null;
		Node<T> succ = null;
		long timestamp = 0;
		for (int level = MAX_LEVEL; level >= bottomLevel; level--) {
			curr = pred.next[level].getReference();
			while (true) {
				succ = curr.next[level].get(marked);
				while (marked[0]) {
					curr = succ;
					succ = curr.next[level].get(marked);
				}
				timestamp = System.nanoTime();
				if (curr.value != null && x.compareTo(curr.value) < 0) {
					pred = curr;
					curr = succ;
				} else {
					break;
				}
			}
		}

		boolean result = curr.value != null && x.compareTo(curr.value) == 0;
		logOperation(threadId, Log.Method.CONTAINS, x.hashCode(), result, timestamp);

		return result;
	}

	private boolean find(T x, Node<T>[] preds, Node<T>[] succs, long[] timestamp) {
		int bottomLevel = 0;
		boolean[] marked = { false };
		boolean snip;
		Node<T> pred = null;
		Node<T> curr = null;
		Node<T> succ = null;

		retry: while (true) {
			pred = head;
			for (int level = MAX_LEVEL; level >= bottomLevel; level--) {
				curr = pred.next[level].getReference();
				while (true) {
					succ = curr.next[level].get(marked);
					while (marked[0]) {
						snip = pred.next[level].compareAndSet(curr, succ, false, false);
						if (!snip)
							continue retry;
						curr = succ;
						succ = curr.next[level].get(marked);
					}
					timestamp[0] = System.nanoTime();
					if (curr.value != null && x.compareTo(curr.value) < 0) {
						pred = curr;
						curr = succ;
					} else {
						break;
					}
				}
				preds[level] = pred;
				succs[level] = curr;
			}
			return curr.value != null && x.compareTo(curr.value) == 0;
		}
	}

	private void logOperation(int threadId, Log.Method method, int arg, boolean ret, long timestamp) {
        Log.Entry entry = new Log.Entry(method, arg, ret, timestamp);

		list.get(threadId).add(entry);
    }

	public Log.Entry[] getLog() {
		List<Log.Entry> log = new LinkedList<>();
		for (List<Log.Entry> entries : list) {
			log.addAll(entries);
		}
		return log.toArray(new Log.Entry[0]);
	}

	public void reset() {
		for (int i = 0; i < head.next.length; i++) {
			head.next[i] = new AtomicMarkableReference<>(tail, false);
		}

		list = new ArrayList<>();
		for (int i = 0; i < MAX_THREADS; i++) {
			list.add(new ArrayList<>());
		}
	}
}
