package org.pa.boundless.bsp.raw;

/**
 * The leafs lump stores the leaves of the map's BSP tree. Each leaf is a convex
 * region that contains, among other things, a cluster index (for determining
 * the other leafs potentially visible from within the leaf), a list of faces
 * (for rendering), and a list of brushes (for collision detection).
 * 
 * @author palador
 */
public class Leaf extends Chunk {
	/**
	 * Visdata cluster index.
	 * 
	 * If cluster is negative, the leaf is outside the map or otherwise
	 * invalid.
	 */
	public int cluster;
	/**
	 * Areaportal area.
	 */
	public int area;
	/**
	 * Integer bounding box min coord.
	 */
	public final int[] mins = new int[3];
	/**
	 * Integer bounding box max coord.
	 */
	public final int[] maxs = new int[3];
	/**
	 * First leafface for leaf.
	 */
	public int leafface;
	/**
	 * Number of leaffaces for leaf.
	 */
	public int n_leaffaces;
	/**
	 * First leafbrush for leaf.
	 */
	public int leafbrush;
	/**
	 * Number of leafbrushes for leaf.
	 */
	public int n_leafbrushes;
}