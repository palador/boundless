package org.pa.boundless.bsp.raw;

/**
 * The leafs lump stores the leaves of the map's BSP tree. Each leaf is a convex
 * region that contains, among other things, a cluster index (for determining
 * the other leafs potentially visible from within the leaf), a list of faces
 * (for rendering), and a list of brushes (for collision detection).
 * 
 * @author palador
 */
public class Leaf {
	/**
	 * Visdata cluster index.
	 * 
	 * If cluster is negative, the leaf is outside the map or otherwise
	 * invalid.
	 */
	int cluster;
	/**
	 * Areaportal area.
	 */
	int area;
	/**
	 * Integer bounding box min coord.
	 */
	int[] mins = new int[3];
	/**
	 * Integer bounding box max coord.
	 */
	int[] maxs = new int[3];
	/**
	 * First leafface for leaf.
	 */
	int leafface;
	/**
	 * Number of leaffaces for leaf.
	 */
	int n_leaffaces;
	/**
	 * First leafbrush for leaf.
	 */
	int leafbrush;
	/**
	 * Number of leafbrushes for leaf.
	 */
	int n_leafbrushes;
}