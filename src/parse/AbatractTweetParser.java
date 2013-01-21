package parse;

import java.util.LinkedList;

public abstract class AbatractTweetParser extends Thread {

	protected LinkedList<TweetParseHandler> handlers = new LinkedList<TweetParseHandler>();

	public abstract void process();

	public void addHandler(TweetParseHandler handler) {
		handlers.add(handler);
	}

	public void removeHandler(TweetParseHandler handler) {
		handlers.remove(handler);
	}

	public void run() {
		process();
	}

}
