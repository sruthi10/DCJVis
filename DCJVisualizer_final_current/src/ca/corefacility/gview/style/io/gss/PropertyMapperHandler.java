package ca.corefacility.gview.style.io.gss;

import java.awt.Paint;
import java.util.ArrayList;

import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.datastyle.mapper.COGMapper;
import ca.corefacility.gview.style.datastyle.mapper.ContinuousPropertyMapper;
import ca.corefacility.gview.style.datastyle.mapper.ContinuousStyleMapper;
import ca.corefacility.gview.style.datastyle.mapper.DiscretePaintMapper;
import ca.corefacility.gview.style.datastyle.mapper.DiscretePropertyMapper;
import ca.corefacility.gview.style.datastyle.mapper.DiscreteStyleMapper;
import ca.corefacility.gview.style.datastyle.mapper.OpacityFeatureStyleMapper;
import ca.corefacility.gview.style.datastyle.mapper.PropertyMapperRandom;
import ca.corefacility.gview.style.datastyle.mapper.PropertyMapperScore;
import ca.corefacility.gview.style.datastyle.mapper.PropertyStyleContinuous;
import ca.corefacility.gview.style.datastyle.mapper.PropertyStyleDiscrete;
import ca.corefacility.gview.style.datastyle.mapper.PropertyStyleMapper;
import ca.corefacility.gview.style.io.gss.PaintHandler.UnknownPaintException;
import ca.corefacility.gview.style.io.gss.exceptions.MalformedDeclarationException;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;

public class PropertyMapperHandler
{
	private static final String CONTINUOUS_MAPPER_NAME = "continuous-map";
	private static final String DISCRETE_MAPPER_NAME = "discrete-map";
	
	private static final String OPACITY_STYLE_NAME = "opacity";
	private static final String SCORE_NAME = "score";
	
	private static final String COG_NAME = "cog";
	private static final String COLORS_NAME = "colors";
	
	private static ContinuousStyleMapper handleDecodeStyleMapper(LexicalUnit currUnit) throws MalformedDeclarationException
	{
		String value;
		ContinuousStyleMapper styleMapper = null;

		if (currUnit == null || (currUnit.getLexicalUnitType() != LexicalUnit.SAC_FUNCTION && 
				currUnit.getLexicalUnitType() != LexicalUnit.SAC_IDENT))
		{
			throw new MalformedDeclarationException("invalid parameter " + currUnit);
		}
		else
		{
			if (currUnit.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION)
			{
				value = currUnit.getFunctionName();
			}
			else
			{
				value = currUnit.getStringValue();
			}
			
			if (OPACITY_STYLE_NAME.equals(value))
			{
				styleMapper = new OpacityFeatureStyleMapper();
			}
			else
			{
				throw new MalformedDeclarationException("invalid parameter " + value);
			}
			
			return styleMapper;
		}
	}
	
	private static ContinuousPropertyMapper handleScore(LexicalUnit currUnit) throws MalformedDeclarationException
	{
		ContinuousPropertyMapper propertyMapper = null;
		
		if (currUnit == null)
		{
			throw new MalformedDeclarationException("invalid function " + currUnit);
		}
		else
		{
			LexicalUnit parameters = currUnit.getParameters();
			
			if (parameters == null || (parameters.getLexicalUnitType() != LexicalUnit.SAC_REAL && 
					parameters.getLexicalUnitType() != LexicalUnit.SAC_INTEGER))
			{
				throw new MalformedDeclarationException("invalid number " + parameters);
			}
			else
			{
				float minScore = parameters.getFloatValue();
				
				parameters = parameters.getNextLexicalUnit();
				
				if (parameters == null || parameters.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_COMMA)
				{
					throw new MalformedDeclarationException("missing maxScore parameter");
				}
				else
				{
					parameters = parameters.getNextLexicalUnit();

					if (parameters == null || (parameters.getLexicalUnitType() != LexicalUnit.SAC_REAL && 
							parameters.getLexicalUnitType() != LexicalUnit.SAC_INTEGER))
					{
						throw new MalformedDeclarationException("invalid number " + parameters);
					}
					else
					{
						float maxScore = parameters.getFloatValue();
						
						propertyMapper = new PropertyMapperScore(minScore, maxScore);
					}
				}
			}
		}
		
		return propertyMapper;
	}

	private static PropertyStyleMapper handleDiscreteMapper(LexicalUnit parameters) throws ParseException
	{
		PropertyStyleMapper propertyStyle = null;
		
		DiscretePropertyMapper propertyMapper = null;
		DiscreteStyleMapper styleMapper = null;
		
		boolean firstParameter = false;
		String value;
		
		if (parameters == null || (parameters.getLexicalUnitType() != LexicalUnit.SAC_FUNCTION && 
				parameters.getLexicalUnitType() != LexicalUnit.SAC_IDENT))
		{
			throw new MalformedDeclarationException("invalid parameter " + parameters);
		}
		else
		{
			if (parameters.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION)
			{
				value = parameters.getFunctionName();
			}
			else
			{
				value = parameters.getStringValue();
			}
			
			if (COG_NAME.equals(value))
			{
				propertyMapper = handleCOG(parameters);
				
				firstParameter = true;
			}
			else
			{
				throw new MalformedDeclarationException("invalid parameter " + value);
			}
			
			if (firstParameter)
			{
				parameters = parameters.getNextLexicalUnit();
				
				if (parameters == null || parameters.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_COMMA)
				{
					throw new MalformedDeclarationException("no comma found");
				}
				else
				{
					parameters = parameters.getNextLexicalUnit();
					
					styleMapper = handleDiscreteStyleMapper(parameters);
					
					if (propertyMapper != null && styleMapper != null)
					{
						if (propertyMapper.getNumberOfCategories() != styleMapper.getNumberOfCategories())
						{
							throw new MalformedDeclarationException("number of property parameters = "
									+ propertyMapper.getNumberOfCategories() + " is not equal to number " +
									" of style parameters = " +
									styleMapper.getNumberOfCategories());

						}
						else
						{
							propertyStyle = new PropertyStyleDiscrete(propertyMapper, styleMapper);
						}
					}
					else if (propertyMapper == null)
					{
						throw new MalformedDeclarationException("property mapper was not properly parsed for " + parameters);
					}
					else
					{
						throw new MalformedDeclarationException("style mapper was not properly parsed for " + parameters);
					}
				}
			}
		}
		
		return propertyStyle;
	}
	
	private static DiscreteStyleMapper handleDiscreteStyleMapper(
			LexicalUnit currUnit) throws ParseException
	{
		DiscreteStyleMapper styleMapper = null;
		
		if (currUnit == null || currUnit.getLexicalUnitType() != LexicalUnit.SAC_FUNCTION)
		{
			throw new MalformedDeclarationException("invalid parameter " + currUnit);
		}
		else
		{
			String value;
			
			value = currUnit.getFunctionName();
			
			if (COLORS_NAME.equals(value))
			{
				if (PaintsHandler.canProcess(value))
				{
					Paint[] paints = PaintsHandler.decode(currUnit);
					styleMapper = new DiscretePaintMapper(paints);
				}
			}
			else
			{
				throw new MalformedDeclarationException("invalid parameter " + currUnit);
			}
		}
		
		return styleMapper;
	}

	private static DiscretePropertyMapper handleCOG(LexicalUnit currUnit) throws MalformedDeclarationException
	{
		DiscretePropertyMapper propertyMapper = null;
		
		if (currUnit == null)
		{
			throw new MalformedDeclarationException("invalid function " + currUnit);
		}
		else
		{
			LexicalUnit parameters = currUnit.getParameters();
			ArrayList<String> categories = new ArrayList<String>(20);
			
			while (parameters != null && parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
			{
				categories.add(parameters.getStringValue());
				
				parameters = parameters.getNextLexicalUnit();
				
				if (parameters != null && parameters.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA)
				{
					parameters = parameters.getNextLexicalUnit();
				}
				else if (parameters != null)
				{
					throw new MalformedDeclarationException("missing comma for " + currUnit);
				}
				// else, parameters == null
			}
			
			if (parameters != null)
			{
				throw new MalformedDeclarationException("invalid parameter for " + currUnit);
			}
			else
			{
				String[] cogStrings = new String[categories.size()];
				cogStrings = categories.toArray(cogStrings);
				propertyMapper = new COGMapper(cogStrings);
			}
		}
		
		return propertyMapper;
	}

	private static PropertyStyleMapper handleContinuousMapper(LexicalUnit parameters) throws MalformedDeclarationException
	{
		PropertyStyleMapper propertyStyle = null;
		
		ContinuousPropertyMapper propertyMapper = null;
		ContinuousStyleMapper styleMapper = null;
		
		boolean firstParameter = false;
		String value;
		
		if (parameters == null || (parameters.getLexicalUnitType() != LexicalUnit.SAC_FUNCTION && 
				parameters.getLexicalUnitType() != LexicalUnit.SAC_IDENT))
		{
			throw new MalformedDeclarationException("invalid parameter " + parameters);
		}
		else
		{
			if (parameters.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION)
			{
				value = parameters.getFunctionName();
			}
			else
			{
				value = parameters.getStringValue();
			}
			
			if (SCORE_NAME.equals(value))
			{
				propertyMapper = handleScore(parameters);
				
				firstParameter = true;
			}
			else if ("random".equals(value)) // remove this later, only for testing
			{
				propertyMapper = new PropertyMapperRandom();
				
				firstParameter = true;
			}
			else
			{
				throw new MalformedDeclarationException("invalid parameter " + value);
			}
			
			if (firstParameter)
			{
				parameters = parameters.getNextLexicalUnit();
				
				if (parameters == null || parameters.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_COMMA)
				{
					throw new MalformedDeclarationException("no comma found");
				}
				else
				{
					parameters = parameters.getNextLexicalUnit();
					
					styleMapper = handleDecodeStyleMapper(parameters);
					
					if (propertyMapper != null && styleMapper != null)
					{
						propertyStyle = new PropertyStyleContinuous(propertyMapper, styleMapper);
					}
				}
			}
		}
		
		return propertyStyle;
	}
	
	public static PropertyStyleMapper  decode(LexicalUnit currUnit) throws ParseException
	{
		PropertyStyleMapper propertyStyle = null;

		if (currUnit != null)
		{
			if (currUnit.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION)
			{
				String value = currUnit.getFunctionName();

				if ( CONTINUOUS_MAPPER_NAME.equals( value ) )
				{
					propertyStyle = handleContinuousMapper(currUnit.getParameters());
				}
				else if ( DISCRETE_MAPPER_NAME.equals(value))
				{
					propertyStyle = handleDiscreteMapper(currUnit.getParameters());
				}
				else
				{
					throw new MalformedDeclarationException("invalid function " + value);
				}
			}
			else
			{
				throw new MalformedDeclarationException("invalid type " + currUnit);
			}
		}
		else
		{
			throw new MalformedDeclarationException("invalid function " + currUnit);
		}
		
		return propertyStyle;
	}
	
	public static String encode(PropertyStyleMapper propertyStyleMapper)
	{
		String result = null;
		
		PropertyStyleContinuous continuousMapper;
		PropertyMapperScore propertyScoreMapper;
		
		PropertyStyleDiscrete discreteMapper;
		COGMapper cogMapper;		
		String[] cogCategories;
		DiscretePaintMapper paintMapper;
		Paint[] colors;
		
		if(propertyStyleMapper instanceof PropertyStyleContinuous)
		{
			continuousMapper = (PropertyStyleContinuous)propertyStyleMapper;
			
			if(continuousMapper.getPropertyMapper() instanceof PropertyMapperScore)
			{
				propertyScoreMapper = (PropertyMapperScore)continuousMapper.getPropertyMapper();
				
				result = CONTINUOUS_MAPPER_NAME + "(" + SCORE_NAME + "(" + propertyScoreMapper.getMinScore() 
					+ "," + propertyScoreMapper.getMaxScore() + "), ";
				
				if(continuousMapper.getStyleMapper() instanceof OpacityFeatureStyleMapper)
				{					
					result += OPACITY_STYLE_NAME + ")";
				}
			}
		}
		else if (propertyStyleMapper instanceof PropertyStyleDiscrete)
		{
			discreteMapper = (PropertyStyleDiscrete)propertyStyleMapper;
			
			if(discreteMapper.getPropertyMapper() instanceof COGMapper)
			{
				cogMapper = (COGMapper)discreteMapper.getPropertyMapper();
				
				result = DISCRETE_MAPPER_NAME + "(" + COG_NAME + "(";
				
				cogCategories = cogMapper.getCategories();
				
				for(int i = 0; i < cogCategories.length; i++)
				{
					result += "'" + cogCategories[i] + "'";
					
					if(i < cogCategories.length - 1)
						result += ",";
				}
				
				result += "), ";
				
				if(discreteMapper.getStyleMapper() instanceof DiscretePaintMapper)
				{
					paintMapper = (DiscretePaintMapper)discreteMapper.getStyleMapper();
					colors = paintMapper.getColors();
					
					result += COLORS_NAME + "(";
					
					for(int i = 0; i < colors.length; i++)
					{
						try
						{
							result += PaintHandler.encode(colors[i]);
						}
						catch (UnknownPaintException e)
						{
							e.printStackTrace();
						}
						
						if(i < colors.length - 1)
							result += ",";
					}
					
					result += ")";
				}
				
				result += ")";				
			}
		}
		
		return result;
	}
}
