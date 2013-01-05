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
public class Model {
	/**
	 * Bounding box min coord.
	 */
	float[] mins = new float[3];
	/**
	 * Bounding box max coord.
	 */
	float[] maxs = new float[3];
	/**
	 * First face for model.
	 */
	int face;
	/**
	 * Number of faces for model.
	 */
	int n_faces;
	/**
	 * First brush for model.
	 */
	int brush;
	/**
	 * Number of brushes for model.
	 */
	int n_brushes;
}