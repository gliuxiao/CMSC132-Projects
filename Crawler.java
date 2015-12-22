package student_classes;
import java.net.*;
import java.io.*;

public class Crawler {

	public static void main(String[] args) {

		MyQueue<URL> linkQueue = new MyQueue<URL>();
		MyQueue<URL> picQueue = new MyQueue<URL>();
		MySet<URL> beenThere = new MySet<URL>();
		MySet<URL> doneThat = new MySet<URL>();

		final int MAX_NUM_EXTRACTORS = 5;  // Change this to whatever you want

		ExtractorThread[] extractors = new ExtractorThread[MAX_NUM_EXTRACTORS];

		new SlideShowGUI(picQueue);
		new CrawlerGUI(linkQueue, picQueue, beenThere, doneThat, extractors);

		URL url;

		while (true) {
			// currPos is -1 because once you add one item, it increments to 0, and 
			// refers to the first item in the array
			int currPos = -1;
			// loops through each thread in the extractor array
			for (ExtractorThread currThread : extractors) {
				currPos++;
				// checks if the thread isn't empty, and is alive
				if (currThread != null && currThread.isAlive()) {
					continue;
				}
				synchronized (linkQueue) {
					// if the queue is empty, wait for an item to be added
					if (linkQueue.size() == 0) {
						try {
							linkQueue.wait(200);
						} catch (InterruptedException e) {
							
						}
					}
				}

				synchronized (extractors) {
					while (true) {
						// updates "url" to the next available URL from linkQueue
						url = (URL) linkQueue.dequeue();
						String content = null;
						try {
							content = url.openConnection().getContentType();
						} catch (IOException e) {
							
						}
						if (content != null && (content.startsWith("text/html"))) {
							break;
						}
					}
					// assigns the current position in the queue to the newly created thread to be processed
					ExtractorThread thread = new ExtractorThread(url, linkQueue, picQueue, beenThere, doneThat);
					extractors[currPos] = thread;
					extractors[currPos].start();
				}
			}
			// if no more threads available, the program will wait until it loops
			// through the queue again to input another thread
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				
			}
		}
	}
}
