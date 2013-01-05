package org.pa.boundless.bsp.raw;

/**
 * The nodes lump stores all of the nodes in the map's BSP tree. The BSP tree is
 * used primarily as a spatial subdivision scheme, dividing the world into
 * convex regions called leafs. The first node in the lump is the tree's root
 * node.
 * 
 * @author palador
 */
public class Node {
	/**
	 * Plane index.
	 */
	public int plane;

	/**
	 * Children indices. Negative numbers are leaf indices: -(leaf+1).
	 */
	public final int[] children = new int[2];

	/**
	 * Integer bounding box min coord.
	 */
	public final int[] mins = new int[3];

	/**
	 * Integer bounding box max coord.
	 */
	public final int[] maxs = new int[3];
}