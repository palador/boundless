package org.pa.boundless.bsp.raw;

/**
 * Plain Java representation of the quake 3 map file format. Most comments are
 * quotes from Kekoa Proudfoot who created the nice
 * "Unofficial Quake 3 Map Specs", which can be found at
 * <i>http://www.mralligator.com/q3/</i>.
 * 
 * @author palador
 */
public class BspFile {

	/**
	 * The header of the BspFile.
	 */
	public Header header;

	/**
	 * The entities lump stores game-related map information, including
	 * information about the map name, weapons, health, armor, triggers, spawn
	 * points, lights, and .md3 models to be placed in the map. The lump
	 * contains only one record, a string that describes all of the entities.
	 * 
	 * The length of the entity string is given by the size of the lump itself,
	 * as specified in the lump directory.
	 * 
	 * The meanings, formats, and parameters of the various entity descriptions
	 * are currently outside the scope of this document. For more information
	 * about entity descriptions, see the documentation to Q3Radiant, the Quake
	 * 3 level editor.
	 */
	public char[] entities;

	/**
	 * The textures lump stores information about surfaces and volumes, which
	 * are in turn associated with faces, brushes, and brushsides. There are a
	 * total of length / sizeof(texture) records in the lump, where length is
	 * the size of the lump itself, as specified in the lump directory.
	 */
	public Texture[] textures;

	/**
	 * The planes lump stores a generic set of planes that are in turn
	 * referenced by nodes and brushsides. There are a total of length /
	 * sizeof(plane) records in the lump, where length is the size of the lump
	 * itself, as specified in the lump directory.
	 * 
	 * Note that planes are paired. The pair of planes with indices i and i ^ 1
	 * are coincident planes with opposing normals.
	 */
	public Plane[] planes;

	/**
	 * The nodes lump stores all of the nodes in the map's BSP tree. The BSP
	 * tree is used primarily as a spatial subdivision scheme, dividing the
	 * world into convex regions called leafs. The first node in the lump is the
	 * tree's root node. There are a total of length / sizeof(node) records in
	 * the lump, where length is the size of the lump itself, as specified in
	 * the lump directory.
	 */
	public Node[] nodes;

	/**
	 * The leafs lump stores the leaves of the map's BSP tree. Each leaf is a
	 * convex region that contains, among other things, a cluster index (for
	 * determining the other leafs potentially visible from within the leaf), a
	 * list of faces (for rendering), and a list of brushes (for collision
	 * detection). There are a total of length / sizeof(leaf) records in the
	 * lump, where length is the size of the lump itself, as specified in the
	 * lump directory.
	 */
	public Leaf[] leafs;

	/**
	 * The leaffaces lump stores lists of face indices, with one list per leaf.
	 * There are a total of length / sizeof(leafface) records in the lump, where
	 * length is the size of the lump itself, as specified in the lump
	 * directory.
	 */
	public int[] leaffaces;

	/**
	 * The leafbrushes lump stores lists of brush indices, with one list per
	 * leaf. There are a total of length / sizeof(leafbrush) records in the
	 * lump, where length is the size of the lump itself, as specified in the
	 * lump directory.
	 */
	public int[] leafbrushes;

	/**
	 * The models lump describes rigid groups of world geometry. The first model
	 * correponds to the base portion of the map while the remaining models
	 * correspond to movable portions of the map, such as the map's doors,
	 * platforms, and buttons. Each model has a list of faces and list of
	 * brushes; these are especially important for the movable parts of the map,
	 * which (unlike the base portion of the map) do not have BSP trees
	 * associated with them. There are a total of length / sizeof(models)
	 * records in the lump, where length is the size of the lump itself, as
	 * specified in the lump directory.
	 */
	public Model[] models;

	/**
	 * The brushes lump stores a set of brushes, which are in turn used for
	 * collision detection. Each brush describes a convex volume as defined by
	 * its surrounding surfaces. There are a total of length / sizeof(brushes)
	 * records in the lump, where length is the size of the lump itself, as
	 * specified in the lump directory.
	 */
	public Brush[] brushes;

	/**
	 * The brushsides lump stores descriptions of brush bounding surfaces. There
	 * are a total of length / sizeof(brushsides) records in the lump, where
	 * length is the size of the lump itself, as specified in the lump
	 * directory.
	 */
	public Brushside[] brushsides;

	/**
	 * The vertexes lump stores lists of vertices used to describe faces. There
	 * are a total of length / sizeof(vertex) records in the lump, where length
	 * is the size of the lump itself, as specified in the lump directory.
	 */
	public Vertex[] vertices;

	/**
	 * The effects lump stores references to volumetric shaders (typically fog)
	 * which affect the rendering of a particular group of faces. There are a
	 * total of length / sizeof(effect) records in the lump, where length is the
	 * size of the lump itself, as specified in the lump directory.
	 */
	public Effect[] effects;

	/**
	 * The faces lump stores information used to render the surfaces of the map.
	 * There are a total of length / sizeof(faces) records in the lump, where
	 * length is the size of the lump itself, as specified in the lump
	 * directory.
	 */
	public Face[] faces;

	/**
	 * The lightmaps lump stores the light map textures used make surface
	 * lighting look more realistic. There are a total of length /
	 * sizeof(lightmap) records in the lump, where length is the size of the
	 * lump itself, as specified in the lump directory.
	 */
	public Lightmap[] lightmaps;

	/**
	 * The lightvols lump stores a uniform grid of lighting information used to
	 * illuminate non-map objects. There are a total of length /
	 * sizeof(lightvol) records in the lump, where length is the size of the
	 * lump itself, as specified in the lump directory.
	 */
	public Lightvol[] lightvols;

	/**
	 * The visdata lump stores bit vectors that provide cluster-to-cluster
	 * visibility information. There is exactly one visdata record, with a
	 * length equal to that specified in the lump directory.
	 */
	public Visdata visdata;

}
