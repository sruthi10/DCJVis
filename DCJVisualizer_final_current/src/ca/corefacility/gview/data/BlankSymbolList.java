package ca.corefacility.gview.data;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.biojava.bio.symbol.Alphabet;
import org.biojava.bio.symbol.Edit;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.Symbol;
import org.biojava.bio.symbol.SymbolList;
import org.biojava.utils.AbstractChangeable;
import org.biojava.utils.ChangeVetoException;

/**
 * An implementation of Symbol list which has a length but no symbols.
 * 
 * @author Aaron Petkau
 */
public class BlankSymbolList extends AbstractChangeable implements SymbolList
{
	private int length;
	
	/**
	 * Creates a new BlankSymbol list with the passed length.
	 * @param length  The sequence length this symbol list should have.  Should be >= 0.
	 */
	public BlankSymbolList(int length)
	{
		if (length >= 0)
		{
			this.length = length;
		}
		else
		{
			throw new IllegalArgumentException("length is negative");
		}
	}
	
	public void edit(Edit edit) throws IndexOutOfBoundsException,
			IllegalAlphabetException, ChangeVetoException
	{
	    throw new ChangeVetoException("BlankSymbolList contains no symbols");
	}

	public Alphabet getAlphabet()
	{
		return Alphabet.EMPTY_ALPHABET;
	}

	public Iterator iterator()
	{
        return Collections.EMPTY_LIST.iterator();
	}

	public int length()
	{
		return length;
	}

	
	public String seqString()
	{
		return "";
	}

	public SymbolList subList(int start, int end)
			throws IndexOutOfBoundsException
	{
		return null;
	}
	
	public String subStr(int start, int end) throws IndexOutOfBoundsException
	{
		return null;
	}

	public Symbol symbolAt(int position) throws IndexOutOfBoundsException
	{
		return null;
	}

	public List toList()
	{
		return Collections.EMPTY_LIST;
	}
}
