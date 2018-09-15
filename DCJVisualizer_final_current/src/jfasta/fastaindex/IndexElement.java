package jfasta.fastaindex;

/**
 * Interface describing IndexElements
 * that represent the position of
 * an object in a file.

 *
 */
public interface IndexElement {
	/**
	 * Returns the starting position (in bytes) of the
	 * indexed object.
	 * @return Byte offset of the indexed offset.
	 */
	public long getStart();

	/**
	 * Returns the size of the indexed object
	 * in the file.
	 *
	 * @return Size of the indexed object in bytes.
	 */
	public int getSize();
}