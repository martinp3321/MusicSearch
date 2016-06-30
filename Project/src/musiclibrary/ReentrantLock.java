package musiclibrary;
import java.util.HashMap;
import java.util.Map;

//Martin
/**
 * A read/write lock that allows multiple readers, disallows multiple writers, and allows a writer to 
 * acquire a read lock while holding the write lock. 
 * 
 */
public class ReentrantLock {

	private final Map<Long, Integer> reader; 
	private final Map<Long, Integer> writer; 

	//one thread
	//the thread has write, can have read and other writes
	//the thread has read, can have read, cannot have write unless nobody has write
	//multiple threads
	//only one thread can have write
	//many threads can have reads


	/**
	 * Construct a new ReentrantLock.
	 */
	public ReentrantLock() {

		reader = new HashMap<>();
		writer = new HashMap<>();

	}

	/**
	 * Returns true if the invoking thread holds a read lock.
	 * @return
	 */
	public synchronized boolean hasRead() {
		return reader.containsKey(Thread.currentThread().getId());

	}

	/**
	 * Returns true if the invoking thread holds a write lock.
	 * @return
	 */
	public synchronized boolean hasWrite() {

		return writer.containsKey(Thread.currentThread().getId());
	}

	/**
	 * Non-blocking method that attempts to acquire the read lock.
	 * Returns true if successful.
	 * @return
	 */
	public synchronized boolean tryLockRead() {

		if (!writer.isEmpty() && !writer.containsKey(Thread.currentThread().getId())) {
			return false;
		}

		if (!reader.containsKey(Thread.currentThread().getId())) {
			reader.put(Thread.currentThread().getId(), 1);
		}
		else {
			reader.put(Thread.currentThread().getId(), reader.get(Thread.currentThread().getId())+1);
		}
		return true;
	}

	/**
	 * Non-blocking method that attempts to acquire the write lock.
	 * Returns true if successful.
	 * @return
	 */	
	public synchronized boolean tryLockWrite() {
		if (!reader.isEmpty() || (!writer.isEmpty() && !hasWrite())) {
			return false;
		}
		if (!writer.containsKey(Thread.currentThread().getId())) {
			writer.put(Thread.currentThread().getId(), 1);
		}
		else {
			writer.put(Thread.currentThread().getId(), writer.get(Thread.currentThread().getId())+1);
		}
		return true;
	}

	/**
	 * Blocking method that will return only when the read lock has been 
	 * acquired.
	 */	 
	public synchronized void lockRead() {
		while (!tryLockRead()) {
			try {
				wait();
			}
			catch (InterruptedException ex) {
			}
		}
	}

	/**
	 * Releases the read lock held by the calling thread. Other threads may continue
	 * to hold a read lock.
	 */
	public synchronized void unlockRead() {
		if (hasRead()) {
			if (reader.get(Thread.currentThread().getId()) > 1) {
				reader.replace(Thread.currentThread().getId(), reader.get(Thread.currentThread().getId())-1);
			}
			else {
				reader.remove(Thread.currentThread().getId());
			}	
		}
		notifyAll();
	}

	/**
	 * Blocking method that will return only when the write lock has been 
	 * acquired.
	 */
	public synchronized void lockWrite() {
		while (!tryLockWrite()) {
			try {
				wait();
			}
			catch (InterruptedException ex) {
			}
		}
	}

	/**
	 * Releases the write lock held by the calling thread. The calling thread may continue to hold
	 * a read lock.
	 */
	public synchronized void unlockWrite() {
		if (hasWrite()) {
			if (writer.get(Thread.currentThread().getId()) > 1) {
				writer.replace(Thread.currentThread().getId(), writer.get(Thread.currentThread().getId())-1);
			}
			else {
				writer.remove(Thread.currentThread().getId());
			}	
		}
		notifyAll();
	}
}

