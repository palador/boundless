package org.pa.boundless.bsp.raw;

/**
 * The models lump describes rigid groups of world geometry. The first model
 * correponds to the base portion of the map while the remaining models
 * correspond to movable portions of the map, such as the map's doors,
 * platforms, and buttons. Each model has a list of faces and list of brushes;
 * these are especially important for the movable parts of the map, which
 * (unlike the base portion of the map) do not have BSP trees associated with
 * them.
 * 
 * @author palador
 */
public class Model extends Chunk {
	/**
	 * Bounding box min coord.
	 */
	public final float[] mins = new float[3];
	/**
	 * Bounding box max coord.
	 */
	public final float[] maxs = new float[3];
	/**
	 * First face for model.
	 */
	public int face;
	/**
	 * Number of faces for model.
	 */
	public int n_faces;
	/**
	 * First brush for model.
	 */
	public int brush;
	/**
	 * Number of brushes for model.
	 */
	public int n_brushes;
}