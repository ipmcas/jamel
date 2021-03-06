package jamel.basic.data.util;

import jamel.util.Circuit;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * An abstract macroeconomic balance sheet matrix.
 */
public abstract class AbstractBalanceSheetMatrix implements BalanceSheetMatrix {

	/**
	 * A convenient class to store String constants.
	 */
	@SuppressWarnings("javadoc")
	private static class KEY {
		public static final String KEY = "key";
		public static final String ROW = "row";
		public static final String SECTOR = "sector";
	}

	/** The number format. */
	final private static NumberFormat nf = NumberFormat.getInstance(Locale.US);

	/** 
	 * A JPanel containing the balance sheet.
	 */
	private final JEditorPane contentPane = new JEditorPane() {
		private static final long serialVersionUID = 1L;
		{
			this.setContentType("text/html");
			this.setText("This is the balance sheet panel.");
			this.setEditable(false);
		}
	};

	/** The rows */
	private  String[] rows;

	/** The sectors */
	private  String[] sectors;

	/** A map that associate a key ("Sector.Row") with the definition of an aggregate data. */
	private final HashMap<String,String> sfcMap = new HashMap<String,String>();

	/**
	 * Creates a new balance sheet matrix.
	 * @param file an XML file that contains the balance sheet config.
	 * @throws FileNotFoundException if the file is not found.
	 */
	public AbstractBalanceSheetMatrix(File file) throws FileNotFoundException {
		// TODO detecter les erreurs du fichier XML (les redondances)
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(file);
			final Element root = document.getDocumentElement();
			final NodeList sectorNodeList = root.getElementsByTagName(KEY.SECTOR);
			this.sectors = new String[sectorNodeList.getLength()];
			for (int i = 0; i<sectorNodeList.getLength(); i++) {
				this.sectors[i]= ((Element) sectorNodeList.item(i)).getAttribute(KEY.KEY);
			}
			final NodeList rowNodeList = root.getElementsByTagName(KEY.ROW);
			this.rows = new String[rowNodeList.getLength()];
			for (int i = 0; i<rowNodeList.getLength(); i++) {
				Element row = (Element) rowNodeList.item(i);
				this.rows[i] = row.getAttribute("key");
				final NodeList childs = row.getChildNodes();
				for(int j = 0; j<childs.getLength(); j++) {
					if (childs.item(j).getNodeType()==Node.ELEMENT_NODE) {
						final Element item = (Element) childs.item(j);
						final String value = item.getAttribute("val");
						final String sector = item.getTagName();
						sfcMap.put(sector+"."+this.rows[i], value);
					}
				}
			}
		}
		catch (final ParserConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("Something went wrong with the file "+file);
		}
		catch (final SAXException e) {
			e.printStackTrace();
			throw new RuntimeException("Something went wrong with the file "+file);
		}
		catch (final IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Something went wrong with the file "+file);
		}
	}

	/**
	 * Returns the value for this key.
	 * @param key the key of the value to return.
	 * @return the value for this key.
	 */
	protected abstract Double getValue(String key);

	@Override
	public Component getPanel() {
		final Component pane = new JScrollPane(this.contentPane);
		pane.setName("Balance sheet");
		return pane;
	}

	@Override
	public String toHtml() {

		final StringBuffer table = new StringBuffer();
		final int period = Circuit.getCurrentPeriod().intValue();
		final int colspan = sectors.length+2;
		table.append("<STYLE TYPE=\"text/css\">.boldtable, .boldtable TD, .boldtable TH" +
				"{font-family:sans-serif;font-size:12pt;}" +
				"</STYLE>");		
		table.append("<BR><BR><BR><BR><TABLE border=0 align=center class=boldtable cellspacing=10>");
		table.append("<CAPTION>Balance sheet matrix (period "+ period +")</CAPTION>");
		table.append("<TR><TD colspan="+colspan+"><HR>");		

		table.append("<TR><TH WIDTH=120>");
		for(String sector:sectors) {
			table.append("<TH WIDTH=110 align=center>" + sector);
		}
		table.append("<TH WIDTH=110 align=right>" + "Sum");
		table.append("<TR><TD colspan="+colspan+"><HR>");

		final HashMap<String,Double> sumSector = new HashMap<String,Double>();
		for (String sector:sectors) {
			sumSector.put(sector, 0.);
		}
		sumSector.put("sum", 0.);

		for(String row:rows) {
			table.append("<TR><TH>" + row);
			double sum = 0l;
			for(String sector:sectors) {
				final String key = this.sfcMap.get(sector+"."+row);
				table.append("<TD align=right>");						
				if (key!=null) {
					final Double value = getValue(key);
					if (value !=null) {
						table.append(nf.format(value));
						sum+=value;
						sumSector.put(sector, sumSector.get(sector)+value);
					}
					else {
						table.append(key+" not found");							
					}
				}
			}
			table.append("<TD align=right>"+nf.format(sum));
			sumSector.put("sum", sumSector.get("sum")+sum);
		}

		table.append("<TR><TD colspan="+colspan+"><HR>");
		table.append("<TR><TH>Sum");
		for (String sector:sectors) {
			table.append("<TD align=right>"+nf.format(sumSector.get(sector)));
		}
		table.append("<TD align=right>"+nf.format(sumSector.get("sum")));
		table.append("<TR><TD colspan="+colspan+"><HR>");
		table.append("</TABLE>");
		return table.toString();
	}

	@Override
	public void update() {
		final String text=this.toHtml();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				contentPane.setText(text);
			}
		});			
	}

}

// ***
