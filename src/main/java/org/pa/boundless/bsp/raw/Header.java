package org.pa.boundless.bsp.raw;


/**
 * Header and directory.
 */
public class Header {

	/**
	 * Magic number. Always "IBSP".
	 */
	public final char[] magic = new char[4];

	/**
	 * Version number. 0x2e for the BSP files distributed with Quake 3.
	 */
	public int version;

	/**
	 * Lump directory, seventeen entries.
	 * 
	 * Each direntry locates a single lump in the BSP file. Goto {@link Lump}
	 * for a list of all entries.
	 */
	public final DirEntry[] direntries = new DirEntry[17];
}