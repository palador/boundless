package org.pa.boundless.bsp.raw;


/**
 * You can use {@link Lump#ordinal()} as a index in
 * {@link Header#direntries}.
 * 
 * There are 17 lumps in a Quake 3 BSP file. In the order that they appear
 * in the lump directory, they are:
 */
public enum Lump {
	/**
	 * Game-related object descriptions.
	 */
	ENTITIES,
	/**
	 * Surface descriptions.
	 */
	TEXTURES,
	/**
	 * Planes used by map geometry.
	 */
	PLANES,
	/**
	 * BSP tree nodes.
	 */
	NODES,
	/**
	 * BSP tree leaves.
	 */
	LEAFS,
	/**
	 * List of face indices, one list per leaf.
	 */
	LEAFFACES,
	/**
	 * Lists of brush indices, one list per leaf.
	 */
	LEAFBRUSHES,
	/**
	 * Descriptions of rigid world geometry in map.
	 */
	MODELS,
	/**
	 * Convex polyhedra used to describe solid space.
	 */
	BRUSHES,
	/**
	 * Brush surfaces.
	 */
	BRUSHSIDES,
	/**
	 * Vertices used to describe faces.
	 */
	VERTEXES,
	/**
	 * Lists of offsets, one list per mesh.
	 */
	MESHVERTS,
	/**
	 * List of special map effects.
	 */
	EFFECTS,
	/**
	 * Surface geometry.
	 */
	FACES,
	/**
	 * Packed lightmap data.
	 */
	LIGHTMAPS,
	/**
	 * Local illumination data.
	 */
	LIGHTVOLS,
	/**
	 * Cluster-cluster visibility data.
	 */
	VISDATA
}