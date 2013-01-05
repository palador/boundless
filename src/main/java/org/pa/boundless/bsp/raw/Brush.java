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
	public int brushside;
	/**
	 * Number of brushsides for brush.
	 */
	public int n_brushsides;
	/**
	 * Texture index.
	 */
	public int texture;
}