package test;


public class WordsCount implements Comparable{

	private String word;
	private long count;
	
	public WordsCount() {
		super();
	}

	public WordsCount(String word, long count) {
		super();
		this.word = word;
		this.count = count;
	}
	public int compareTo(Object o) {
		WordsCount wc  = (WordsCount) o ;
		long co = wc.count;
		if(this.count>co)
			return -1;
		else
			return 1;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}





}
