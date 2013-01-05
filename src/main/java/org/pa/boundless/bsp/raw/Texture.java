package org.pa.boundless.bsp.raw;

/**
 * The textures lump stores information about surfaces and volumes, which are in
 * turn associated with faces, brushes, and brushsides.
 * 
 * @author palador
 */
public class Texture extends Chunk {
	/**
	 * Texture name.
	 */
	public final char[] name = new char[64];

	/**
	 * Surface flags.
	 */
	public int flags;

	/**
	 * Content flags.
	 */
	public int contents;
}