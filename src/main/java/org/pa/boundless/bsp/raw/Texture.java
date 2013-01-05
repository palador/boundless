package org.pa.boundless.bsp.raw;

/**
 * The textures lump stores information about surfaces and volumes, which are in
 * turn associated with faces, brushes, and brushsides.
 * 
 * @author palador
 */
public class Texture {
	/**
	 * Texture name.
	 */
	char[] name = new char[64];

	/**
	 * Surface flags.
	 */
	int flags;

	/**
	 * Content flags.
	 */
	int contents;
}