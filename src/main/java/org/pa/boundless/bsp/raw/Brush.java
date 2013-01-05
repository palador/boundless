package org.pa.boundless.bsp.raw;

/**
 * The brushes lump stores a set of brushes, which are in turn used for
 * collision detection. Each brush describes a convex volume as defined by its
 * surrounding surfaces.
 * 
 * @author palador
 */
public class Brush {
	/**
	 * First brushside for brush.
	 */
	int brushside;
	/**
	 * Number of brushsides for brush.
	 */
	int n_brushsides;
	/**
	 * Texture index.
	 */
	int texture;
}