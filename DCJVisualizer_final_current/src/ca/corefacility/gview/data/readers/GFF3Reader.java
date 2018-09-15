package ca.corefacility.gview.data.readers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.biojava.bio.Annotation;
import org.biojava.bio.BioException;
import org.biojava.bio.SimpleAnnotation;
import org.biojava.bio.program.gff3.GFF3DocumentHandler;
import org.biojava.bio.program.gff3.GFF3Parser;
import org.biojava.bio.program.gff3.GFF3Record;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.StrandedFeature;
import org.biojava.ontology.Term;
import org.biojava.utils.ChangeVetoException;
import org.biojava.utils.ParserException;
import org.biojavax.bio.seq.RichLocation;
import org.biojavax.bio.seq.SimplePosition;
import org.biojavax.bio.seq.SimpleRichLocation;
import org.biojavax.ontology.SimpleComparableOntology;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.data.GenomeDataFactory;
import ca.corefacility.gview.utils.FileLocationHandler;
import ca.corefacility.gview.utils.Util;

public class GFF3Reader extends AbstractFileFormatReader
{

	@Override
	public GViewFileData read(File file) throws IOException,
			GViewDataParseException
	{
		if (file == null)
		{
			throw new IllegalArgumentException("File cannot be null");
		}
		else
		{
			FileReader fileReader = new FileReader(file);
			GViewFileData data = read(fileReader);
			
			fileReader.close();
			
			return data;
		}
	}

	@Override
	public GViewFileData read(Reader reader) throws IOException,
			GViewDataParseException
	{
		GFF3Parser parser = new GFF3Parser();
		GViewFileData fileData = null;
		
		BufferedReader br = new BufferedReader(reader);
		
		try
		{			
			GenomeData data = null;
			DocHandler h = new DocHandler();
			
			parser.parse(br, h, new SimpleComparableOntology("the ontology"));
			data = buildGenomeData(h.getSequenceLength());
			injectGFFFeatures(data, h.getFeaturesList());
			
			fileData = new GViewFileData(data, null);
			
			br.close();
		}
		catch (BioException e)
		{
			br.close();
			throw new GViewDataParseException(e);
		}
		catch (ParserException e)
		{
			br.close();
			throw new GViewDataParseException(e);
		}
		
		return fileData;
	}
	
	private void injectGFFFeatures(GenomeData data, List<Feature.Template> featuresList)
	{
		Sequence seq = data.getSequence();
		
		for (Feature.Template f : featuresList)
		{
			try
			{
				seq.createFeature(f);
			}
			catch (ChangeVetoException e)
			{
				System.err.println("[warning] - could not insert feature " + f);
			}
			catch (BioException e)
			{
				System.err.println("[warning] - could not insert feature " + f);
			}
		}
	}

	private GenomeData buildGenomeData(int sequenceLength)
	{
		return GenomeDataFactory.createBlankGenomeData(sequenceLength);
	}
	
	public void injectGFFFeatures(GenomeData genomeData, String fileLocation) throws GViewDataParseException, IOException
	{
		if (genomeData == null)
		{
			throw new IllegalArgumentException("genomeData is null");
		}
		
		if (fileLocation == null)
		{
			throw new IllegalArgumentException("fileLocation is null");
		}
		
		Reader reader = FileLocationHandler.getReader(fileLocation);
		BufferedReader br = new BufferedReader(reader);
		
		GFF3Parser parser = new GFF3Parser();
	
		try
		{
			DocHandler h = new DocHandler();
			
			parser.parse(br, h, new SimpleComparableOntology("the ontology"));
			injectGFFFeatures(genomeData, h.getFeaturesList());
			
			br.close();
		}
		catch (BioException e)
		{
			br.close();
			throw new GViewDataParseException(e);
		}
		catch (ParserException e)
		{
			br.close();
			throw new GViewDataParseException(e);
		}
	}

	@Override
	public boolean canRead(URI uri)
	{
		if (uri == null)
		{
			throw new IllegalArgumentException("uri is null");
		}
		
		String extension = Util.extractExtension(uri.getPath().toString());
		
		return extension != null && extension.equals("gff");
	}
	
	private class DocHandler implements GFF3DocumentHandler
	{
		private int maxEnd = 0;
		private List<Feature.Template> featuresList = new LinkedList<Feature.Template>();
		

		@Override
		public void commentLine(String arg0)
		{
			// TODO Auto-generated method stub
			
		}

		public List<Feature.Template> getFeaturesList()
		{
			return featuresList;
		}

		public int getSequenceLength()
		{
			return maxEnd;
		}

		@Override
		public void endDocument()
		{

		}
		
		// converts the passed annotation to an annotation which maps only strings to strings
		private Annotation toStringAnnotation(Annotation a)
		{
			Annotation stringAnnotation = new SimpleAnnotation();
			
			Set keys = a.keys();
			
			for (Object key : keys)
			{
				Object value = a.getProperty(key);
				String stringKey = key.toString();
				
				if (key instanceof List && ((List)key).size() > 0)
				{
					List l = (List)key;
					
					stringKey = l.get(0).toString();
				}
				else if (key instanceof Term)
				{
					Term t = (Term)key;
					
					stringKey = t.getName();
				}
				
				if (value instanceof List && ((List)value).size() > 0)
				{
					List l = (List)value;
					
					stringAnnotation.setProperty(stringKey, l.get(0));
				}
				else
				{
					stringAnnotation.setProperty(stringKey, value);
				}
			}
			
			return stringAnnotation;
		}

		@Override
		public void recordLine(GFF3Record record)
		{
			int start = record.getStart();
			int end = record.getEnd();
			
			if (maxEnd < end)
			{
				maxEnd = end;
			}
			
			// pull out keys that map to lists, so we only map to the first element of the list
			Annotation stringAnnotation = toStringAnnotation(record.getAnnotation());
			
			// we cannot just use a StrandedFeature template, as biojava does not handle the strand correctly
			// must set strand in the location instead
			RichLocation.Strand strand;
			if (record.getStrand().equals(StrandedFeature.POSITIVE))
			{
				strand = RichLocation.Strand.POSITIVE_STRAND;
			}
			else if (record.getStrand().equals(StrandedFeature.NEGATIVE))
			{
				strand = RichLocation.Strand.NEGATIVE_STRAND;
			}
			else
			{
				strand = RichLocation.Strand.UNKNOWN_STRAND;
			}
			
			Feature.Template ft = new Feature.Template();
			ft.typeTerm = record.getType();
			ft.location = new SimpleRichLocation( new SimplePosition( start ), new SimplePosition( end ), 0, strand );
			ft.sourceTerm = record.getSource();
			ft.annotation = stringAnnotation;
			ft.annotation.setProperty( "SequenceName", record.getSequenceID() );
			ft.annotation.setProperty( "Score", Double.toString( record.getScore() ) );
			
			featuresList.add(ft);
		}

		@Override
		public void startDocument(String arg0)
		{
			
		}
	}

	@Override
	public GViewFileData read(InputStream inputStream) throws IOException,
			GViewDataParseException
	{
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		GViewFileData data = read(inputStreamReader);
		
		inputStreamReader.close();
		
		return data;
	}

	@Override
	public boolean canRead(BufferedInputStream inputStream)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
