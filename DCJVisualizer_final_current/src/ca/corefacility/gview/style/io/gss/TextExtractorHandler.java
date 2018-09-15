package ca.corefacility.gview.style.io.gss;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;

import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.textextractor.AnnotationExtractor;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;
import ca.corefacility.gview.textextractor.GeneTextExtractor;
import ca.corefacility.gview.textextractor.LocationExtractor;
import ca.corefacility.gview.textextractor.StringBuilder;
import ca.corefacility.gview.textextractor.SymbolsExtractor;

public class TextExtractorHandler
{
	private static final String TEXT_EXTRACTOR_FUNCTION = "text-extractor";

	private static final String ANNOTATION_NAME = "annotation";
	private static final String GENE_NAME = "gene";
	private static final String LOCATION_NAME = "location";
	private static final String SYMBOLS_NAME = "symbols";
	private static final String STRINGBUILDER_NAME = "stringbuilder";
	private static final String BLANK_NAME = "blank";
	

	public static String encode(FeatureTextExtractor textExtractor)
	{
		return TEXT_EXTRACTOR_FUNCTION + "(" + encodeTextExtractor(textExtractor) + ")";
	}

	private static String encodeTextExtractor(FeatureTextExtractor textExtractor)
	{
		String extractorString = null;

		if (textExtractor == null)
		{
			extractorString = "\"null\"";
		}
		else if (textExtractor.getClass().equals(AnnotationExtractor.class))
		{
			AnnotationExtractor annExt = (AnnotationExtractor)textExtractor;
			String annotation = annExt.getAnnotation();

			extractorString = ANNOTATION_NAME + "(\"" + annotation + "\")";
		}
		else if (textExtractor.getClass().equals(LocationExtractor.class))
		{
			extractorString = "\"" + LOCATION_NAME + "\"";
		}
		else if (textExtractor.getClass().equals(GeneTextExtractor.class))
		{
			extractorString = "\"" + GENE_NAME + "\"";
		}
		else if (textExtractor.getClass().equals(SymbolsExtractor.class))
		{
			extractorString = "\"" + SYMBOLS_NAME + "\"";
		}
		else if (textExtractor.getClass().equals(StringBuilder.class))
		{
			extractorString = encodeStringBuilder((StringBuilder)textExtractor);
		}
		else if (textExtractor.equals(FeatureTextExtractor.BLANK))
		{
			extractorString = "\"" + BLANK_NAME + "\"";
		}

		return extractorString;
	}
	
	private static String encodeStringBuilder(StringBuilder stringBuilder)
	{
		Iterator<FeatureTextExtractor> extractors = stringBuilder.getFeatureTextExtractors();
		String extractorString = STRINGBUILDER_NAME + "(";		
		
		extractorString += "\"" + stringBuilder.getFormat().replace("\n", "\\A ") + "\"";
		
		while(extractors.hasNext())
		{
			extractorString += "," + encodeTextExtractor(extractors.next());
		}
		
		extractorString += ")";
		
		return extractorString;
	}
	
	private static FeatureTextExtractor decodeParameters(LexicalUnit textExtractorParameters) throws ParseException
	{
		FeatureTextExtractor textExtractorObj = null;
		
		LexicalUnit parameters = textExtractorParameters;

		if (parameters == null)
		{
			throw new ParseException("error in " + textExtractorParameters);
		}
		else
		{
			if (parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
			{
				if (LOCATION_NAME.equals(parameters.getStringValue()))
				{
					textExtractorObj = new LocationExtractor();
				}
				else if (GENE_NAME.equals(parameters.getStringValue()))
				{
					textExtractorObj = new GeneTextExtractor();
				}
				else if (SYMBOLS_NAME.equals(parameters.getStringValue()))
				{
					textExtractorObj = new SymbolsExtractor();
				}
				else
				{
					throw new ParseException("error in " + textExtractorParameters);
				}
			}
			else if (parameters.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION) // sub-function below top function
			{
				LexicalUnit function = parameters;
				parameters = function.getParameters();

				if (parameters == null)
				{
					throw new ParseException("error in " + textExtractorParameters);
				}
				else
				{
					// annotation extractor
					if (ANNOTATION_NAME.equals(function.getFunctionName()))
					{
						if (parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
						{
							String parameter = parameters.getStringValue();

							textExtractorObj = new AnnotationExtractor(parameter);
						}
						else
						{
							throw new ParseException("error in " + textExtractorParameters + " parameter not string");
						}
					}
					else if (STRINGBUILDER_NAME.equals(function.getFunctionName()))
					{
						if (parameters.getLexicalUnitType() != LexicalUnit.SAC_STRING_VALUE)
						{
							throw new ParseException("error in " + textExtractorParameters + " missing format string");
						}
						else
						{
							String formatString = parameters.getStringValue();
							
							parameters = parameters.getNextLexicalUnit();
							
							// only format string
							if (parameters == null)
							{
								textExtractorObj = new StringBuilder(formatString);
							}
							else if (parameters.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_COMMA)
							{
								throw new ParseException("error in " + textExtractorParameters + "  missing \",\" in function " + function);
							}
							else
							{
								parameters = parameters.getNextLexicalUnit();
							
								List<FeatureTextExtractor> printfUnits = new ArrayList<FeatureTextExtractor>();
								
								// pull out all feature text extractors
								while (parameters != null && (parameters.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION ||
										parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE))
								{
									FeatureTextExtractor currExtractor = decodeParameters(parameters);
									
									printfUnits.add(currExtractor); // push current printf unit
									
									parameters = parameters.getNextLexicalUnit();
									
									if (parameters != null)
									{
										if (parameters.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA) // if comma, skip
										{
											parameters = parameters.getNextLexicalUnit();
										}
										else // TODO, throw exception
										{
											throw new ParseException("error in " + textExtractorParameters + "  missing \",\" in function " + function);
										}
									}
								}
								
								try
								{
									textExtractorObj = new StringBuilder(formatString, printfUnits);
								}
								catch (IllegalFormatException e)
								{
									throw new ParseException("Illegal format \"" + e + "\" in " + function);
								}
							}
						}
					}
				}
			}
		}
		
		return textExtractorObj;
	}

	public static FeatureTextExtractor decode(LexicalUnit textExtractor) throws ParseException
	{
		if (textExtractor == null)
			throw new NullPointerException("textExtractor is null");

		FeatureTextExtractor textExtractorObj = null;

		if (TEXT_EXTRACTOR_FUNCTION.equals(textExtractor.getFunctionName()))
		{
			textExtractorObj = decodeParameters(textExtractor.getParameters());
		}
		else
		{
			throw new ParseException("error in " + textExtractor + "  invalid function name");
		}

		return textExtractorObj;
	}
}
