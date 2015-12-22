//The following class was pre-written by the UMD CS Department
package student_classes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class ExtractorThread extends Thread {

	private URL url;
	private MyQueue<URL> linkQueue, picQueue;
	private MySet<URL> beenThere, doneThat;

	public ExtractorThread(URL url, MyQueue<URL> linkQueue, MyQueue<URL> picQueue, MySet<URL> beenThere, MySet<URL> doneThat) {
		this.url = url;
		this.linkQueue = linkQueue;
		this.picQueue = picQueue;
		this.beenThere = beenThere;
		this.doneThat = doneThat;
	}

	public String getCurrentURL() {
		return url.toString();
	}

	private static Pattern LINK_PATTERN = Pattern.compile("href *= *\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
	private static Pattern IMAGE_PATTERN = Pattern.compile("<( )*(img|IMG)( )+([^<>])*(src|SRC)( )*=( )*\"([^\"]+)\"[^>]*>");

	private static Set<URL> extractLinks(Pattern toMatch, String s, URL currentURL, int group) {
		Matcher m = toMatch.matcher(s);
		Set<URL> links = new HashSet<URL>();
		while ( m != null && s!= null && m.find()) {
			String found = m.group(group);
			try {
				links.add(new URL(currentURL, found));
			} catch (MalformedURLException e) {
				// just ignore
			}
		}
		return links;
	}

	private static Set<URL> getLinks(String s, URL currentURL) {
		return extractLinks(LINK_PATTERN, s, currentURL, 1);
	}

	private static Set<URL> getPicURLs(String s, URL currentURL) {
		return extractLinks(IMAGE_PATTERN, s, currentURL, 8);
	}

	public void run() {
		try {
			Reader read = new InputStreamReader(url.openStream());
			BufferedReader bufferRead = new BufferedReader(read);
			String line;
			// checks if there are any more lines left to process
			while ((line = bufferRead.readLine()) != null) {
				// sets the link
				Set<URL> linkSet = getLinks(line, url);
				for (URL link : linkSet) {
					// checks if the link is not null
					if (link != null) {
						String protocol = link.getProtocol();
						if (protocol != null && (protocol.equals("http") || protocol.equals("file"))) {
							 // if the to-process queue does not contain a link, one from the link 
							 // queue is added and that link is removed from the waiting queue
							if (!beenThere.contains(link)) {
								linkQueue.enqueue(link);
								beenThere.add(link);
							}
						}
					}
				}		
			}
			bufferRead.close();
		} catch (IOException e) {

		} try {
			Reader read = new InputStreamReader(url.openStream());
			BufferedReader bufferRead = new BufferedReader(read);
			String lineURL;
			// null means that the end of the page has been reached
			while ((lineURL = bufferRead.readLine()) != null) {
				// sets the pictures 
				Set<URL> pictures = getPicURLs(lineURL, url);
				for (URL currURL : pictures) {
					 // if the to-process queue does not contain a URL, one from the URL 
					 // queue is added and that link is removed from the waiting queue
					if (!doneThat.contains(currURL)) {
						picQueue.enqueue(currURL);
						doneThat.add(currURL);
					}
				}
			}
			// closes the reader
			bufferRead.close();
		} catch (IOException e) {

		}
	}
}
