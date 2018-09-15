package ca.corefacility.gview.data.readers.cgview;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

import org.biojava.bio.seq.Sequence;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ca.corefacility.gview.data.GenomeDataFactory;
import ca.corefacility.gview.data.readers.GViewDataParseException;
import ca.corefacility.gview.data.readers.GViewFileData;
import ca.corefacility.gview.style.MapStyle;

/**
 * Main XML handler for cgview xml parsing.
 * @author aaron
 *
 */
class CGViewXMLHandler extends DefaultHandler
{
	private Sequence currentSequence = null;
	private MapStyle currentMapStyle = new MapStyle();
	
	private int startElementCount = 0;
	private CGViewElementHandler cgViewElementHandler;

	private Stack<ElementDetails> context = new Stack<ElementDetails>();

	private boolean ignoreCgviewTag = false;

	private StringBuffer content = new StringBuffer();
	private Locator locator;

	/**
	 * Constructs an object to read in information from the CGView xml format.
	 */
	public CGViewXMLHandler()
	{
		this.currentSequence = null;
		this.currentMapStyle = new MapStyle();

		this.context = new Stack<ElementDetails>();

		// set to true if combining data
		this.ignoreCgviewTag = false;

		this.content = new StringBuffer();
		this.locator = null;
	}

	public GViewFileData read(Reader reader) throws IOException, GViewDataParseException
	{
		if (reader == null)
		{
			throw new IllegalArgumentException("reader is null");
		}
		
		XMLReader xr = null;
		try
		{
			xr = XMLReaderFactory.createXMLReader();

			xr.setContentHandler(this);

			ErrorHandler handler = new ErrorHandler()
			{
				@Override
				public void warning(SAXParseException e) throws SAXException
				{
					System.err.println("[warning] " + e.getMessage());
				}

				@Override
				public void error(SAXParseException e) throws SAXException
				{
					System.err.println("[error] " + e.getMessage());
				}

				@Override
				public void fatalError(SAXParseException e) throws SAXException
				{
					System.err.println("[fatal error] " + e.getMessage());
					throw e;
				}
			};

			xr.setErrorHandler(handler);
			xr.parse(new InputSource(reader));
		}
		catch (SAXException e2)
		{
			throw new GViewDataParseException(e2);
		}

		if (this.currentSequence != null && this.currentMapStyle != null)
		{
			try
			{
				return new GViewFileData(GenomeDataFactory.createGenomeData(this.currentSequence, true), this.currentMapStyle);
			}
			catch (IllegalArgumentException e)
			{
				throw new GViewDataParseException(e);
			}
		} else
		{
			return null;
		}
	}

	/************************
	 * XML parser methods
	 ************************/

	@Override
	public void startDocument()
	{
		System.out.print("Parsing CGView XML input...");
	}

	@Override
	public void endDocument()
	{
		System.out.println("finished");
	}

	@Override
	public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException
	{
		ElementDetails details = new ElementDetails(name, atts);
		this.context.push(details);

		boolean ignoreLegendTag = false;
		boolean ignoreLegendItemTag = false;

		// keeping track of progress
		startElementCount++;
		if (startElementCount % 1000000 == 0)
		{
			System.out.print(".");
		}

		if (name.equalsIgnoreCase("cgview") && !this.ignoreCgviewTag)
		{
			handleCgview();
		}
		else if (name.equalsIgnoreCase("featureSlot"))
		{
			handleFeatureSlot();
		}
		else if (name.equalsIgnoreCase("feature"))
		{
			handleFeature();
		}
		else if (name.equalsIgnoreCase("featureRange"))
		{
			handleFeatureRange();
		}
		else if ((name.equalsIgnoreCase("legend")) && (!(ignoreLegendTag)))
		{
			handleLegend();
		}
		else if ((name.equalsIgnoreCase("legendItem")) && (!(ignoreLegendItemTag)))
		{
			handleLegendItem();
		}

		this.content.setLength(0);
	}
	
	private void handleLegendItem() throws SAXException
	{
		if (this.cgViewElementHandler != null)
		{
			LegendElementHandler currentLegendElement = this.cgViewElementHandler.getLegendElementHandler();

			if (currentLegendElement != null)
			{
				currentLegendElement.handleLegendItem(context, locator);
			}
			else
			{
				throw new SAXException(appendLocation("\'legendItem\' element encountered outside of a \'legend\' element"));
			}
		}
		else
		{
			throw new SAXException(appendLocation("\'legendItem\' element encountered outside of a \'cgview\' element"));
		}
	}

	private void handleLegend() throws SAXException
	{
		if (this.cgViewElementHandler != null)
		{
			LegendElementHandler currentLegendElement = this.cgViewElementHandler.createLegendElementHandler();
			currentLegendElement.handleLegend(this.context, this.locator);
		}
		else
		{
			throw new SAXException(appendLocation("\'legend\' element encountered outside of a \'cgview\' element"));
		}
	}

	private void handleFeatureRange() throws SAXException
	{
		if (this.cgViewElementHandler != null)
		{
			this.cgViewElementHandler.handleFeatureRange(context, locator);
		}
		else
		{
			throw new SAXException(appendLocation("featureRange element encountered outside of a cgview element"));
		}
	}

	private void handleFeature() throws SAXException
	{
		if (this.cgViewElementHandler != null)
		{
			this.cgViewElementHandler.handleFeature(context, locator);
		}
		else
		{
			throw new SAXException(appendLocation("feature element encountered outside of a cgview element"));
		}
	}

	private void handleFeatureSlot() throws SAXException
	{
		if (this.cgViewElementHandler != null)
		{
			this.cgViewElementHandler.handleFeatureSlot(this.context, this.locator);
		}
		else
		{
			throw new SAXException(appendLocation("\'featureSlot\' element encountered outside of a \'cgview\' element"));
		}
	}

	private void handleCgview() throws SAXException
	{
		if (this.cgViewElementHandler == null)
		{
			this.cgViewElementHandler = new CGViewElementHandler();
			this.cgViewElementHandler.handleCgview(this.context, this.locator);
		}
		else
		{
			throw new SAXException(appendLocation("cgview element encountered inside of another cgview element"));
		}
	}

	@Override
	public void endElement(String uri, String name, String qName) throws SAXException
	{
		// System.out.println("End element: " + name);
		this.content.setLength(0);
		this.context.pop();

		if (name.equalsIgnoreCase("cgview"))
		{
			this.currentSequence = cgViewElementHandler.getSequence();
			this.currentMapStyle = cgViewElementHandler.getMapStyle();
			cgViewElementHandler = null;
		}
		else if (name.equalsIgnoreCase("featureSlot"))
		{
			cgViewElementHandler.endSlot();
		}
		else if (name.equalsIgnoreCase("feature"))
		{
			cgViewElementHandler.endFeatureElement();
		}
		else if (name.equalsIgnoreCase("featureRange"))
		{
			cgViewElementHandler.endFeatureRange();
		}
		else if (name.equalsIgnoreCase("legend"))
		{
			cgViewElementHandler.endLegend();
		}
		else if (name.equalsIgnoreCase("legendItem"))
		{
			// do nothing
		}
	}

	/**
	 * Appends the location onto the passed error string.
	 *
	 * @param error
	 *            The string which to append a location on.
	 * @return The error string + the location.
	 */
	private String appendLocation(String error)
	{
		String errorLocation = error;

		if (this.locator != null)
		{
			errorLocation = " in " + this.locator.getSystemId() + " at line " + this.locator.getLineNumber() + " column " + this.locator.getColumnNumber();
			if (error != null)
			{
				errorLocation = error + errorLocation;
			}
		}
		return errorLocation;
	}
}
