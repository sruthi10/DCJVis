package ca.corefacility.gview.map.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.biojava.bio.Annotation;
import org.biojava.bio.seq.Feature;
import org.biojava.ontology.Term;

import ca.corefacility.gview.data.GenomeData;

/**
 * This class controls access to the GenomeData object and provides methods for operating on that data.
 * 
 * It is expected that the GenomeDataObject DOES NOT change during this controller's lifetime.
 * 
 * @author Eric Marinier
 *
 */
public class GenomeDataController
{
	private final GenomeData genomeData;
	
	private ArrayList<Object> annotations;
	private ArrayList<String> annotationsAsStrings;
	private ArrayList<String> types;
	
	/**
	 * 
	 * @param genomeData The associated GenomeData.
	 */
	public GenomeDataController(GenomeData genomeData)
	{
		this.genomeData = genomeData;
	}
	
	@SuppressWarnings("rawtypes")
	/**
	 * 
	 * @return A list of the annotations in the GenomeData. Will not contain duplicates.
	 */
	public ArrayList<Object> getAnnotations()
	{
		if(this.annotations == null)
		{
			this.annotations = new ArrayList<Object>();
	
			for(Iterator<Feature> features = this.genomeData.getSequence().features(); features.hasNext();)
			{	
				Feature currentFeature = features.next();
				Annotation currentAnnotation = currentFeature.getAnnotation();
				
				for(Iterator keys = currentAnnotation.keys().iterator(); keys.hasNext();)
				{
					Object key = keys.next();
					
					if(!this.annotations.contains(key))
					{
						this.annotations.add(key);
					}
				}
			}
		}
		
		return this.annotations;
	}
	
	@SuppressWarnings("rawtypes")
	/**
	 * 
	 * @return A list of the annotations in the GenomeData as Strings. Will not contain duplicates.
	 */
	public ArrayList<String> getAnnotationsAsStrings()
	{
		if(this.annotationsAsStrings == null)
		{
			ArrayList<Object> objectAnnotations = getAnnotations();
			this.annotationsAsStrings = new ArrayList<String>();
			
			String text;
			
			for(Object annotation : objectAnnotations)
			{
				if (annotation instanceof List && ((List)annotation).size() > 0)
				{
					List l = (List)annotation;
					
					text = l.get(0).toString();
					this.annotationsAsStrings.add(text);
				}
				else if (annotation instanceof Term)
				{
					Term term = (Term)annotation;
					
					text = term.getName();
					this.annotationsAsStrings.add(text);
				}
				else
				{
					text = annotation.toString();
					this.annotationsAsStrings.add(text);
				}
			}
		}
		
		return this.annotationsAsStrings;
	}
	
	/**
	 * 
	 * @return A list of the features' types. Will not contain duplicates.
	 */
	public ArrayList<String> getTypes()
	{
		if(this.types == null)
		{
			this.types = new ArrayList<String>();
			
			for(Iterator<Feature> features = this.genomeData.getSequence().features(); features.hasNext();)
			{	
				Feature currentFeature = features.next();
				
				if(!this.types.contains(currentFeature.getType()) && currentFeature.getType() != null)
				{
					this.types.add(currentFeature.getType());
				}
			}
		}
		
		return this.types;
	}
}
