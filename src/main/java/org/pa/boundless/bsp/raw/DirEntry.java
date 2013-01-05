package org.pa.boundless.bsp.raw;

/**
 * Locates a single lump in the BSP file.
 */
public class DirEntry extends Chunk {
	/**
	 * Offset to start of lump, relative to beginning of file.
	 */
	public int offset;

	/**
	 * Length of lump. Always a multiple of 4.
	 */
	public int length;
}