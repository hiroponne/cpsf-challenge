import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.core.ParseException;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.parsers.FeedParser;

public class RSSReader {

	private final String address = /* "http://rss.dailynews.yahoo.co.jp/fc/rss.xml" */
	/* "http://www.ht.sfc.keio.ac.jp/cpsf/feed/" */
	"http://q.hatena.ne.jp/list?mode=rss";

	private URL url;
	private ChannelIF channel;
	private ItemIF[] items;
	private int no;
	private int selectNo;

	public RSSReader() {

		try {

			/** read RSS */
			url = new URL(address);
			channel = FeedParser.parse(new ChannelBuilder(), url);

			/** print channel information */
			System.out.println(channel.getTitle() + ", "
					+ channel.getDescription() + ", " + channel.getSite());

			/** list news */
			items = (ItemIF[]) channel.getItems().toArray(new ItemIF[0]);

			/** sort items */
			Arrays.sort(items, new ItemComparator());

			no = 0;
			/** print news headline */
			for (ItemIF item : items) {
				System.out.println("(" + no + ")" + getDate(item.getDate())
						+ " " + item.getTitle());
				no++;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("select No");
		try {
			selectNo = Integer.valueOf(br.readLine());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(getDate(items[selectNo].getDate()) + " "
				+ items[selectNo].getTitle() + ","
				+ items[selectNo].getLink().toString());

		/*
		 * JFrame frame = new JFrame(); JEditorPane webPage = new JEditorPane();
		 * webPage.setEditable(false); try {
		 * webPage.setPage(items[selectNo].getLink()); } catch (IOException e) {
		 * e.printStackTrace(); }
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); frame.add(new
		 * JScrollPane(webPage)); frame.setSize(frame.getMaximumSize());
		 * frame.setVisible(true);
		 */}

	public String getDate(java.util.Date date) {
		return "[" + date.getMonth() + "/" + date.getDay() + " "
				+ date.getHours() + ":" + date.getMinutes() + "]";
	}

	class ItemComparator implements Comparator<ItemIF> {
		@Override
		public int compare(ItemIF item1, ItemIF item2) {
			return item2.getDate().compareTo(item1.getDate());
		}
	}

	public static void main(String[] args) {
		RSSReader rss = new RSSReader();
	}

}
