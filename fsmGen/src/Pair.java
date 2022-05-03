
public class Pair<T1, T2> {
	private T2 second;
	private T1 first;

	public Pair() {

	}

	public T2 getSecond() {
		return this.second;
	}

	public void setSecond(T2 second) {
		this.second = second;
	}

	public T1 getFirst() {
		return this.first;
	}

	public void setFirst(T1 first) {
		this.first = first;
	}
}