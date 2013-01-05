package org.pa.boundless.bsp.raw;

/**
 * The faces lump stores information used to render the surfaces of the map.
 * 
 * There are four types of faces: polygons, patches, meshes, and billboards.
 * 
 * Several components have different meanings depending on the face type.
 * <ul>
 * <li>{@link Face#POLYGON}</li>
 * <li>{@link Face#PATCH}</li>
 * <li>{@link Face#MESH}</li>
 * <li>{@link Face#BILLBOARD}</li>
 * </ul>
 * 
 * The lm_ variables are primarily used to deal with lightmap data. A face that
 * has a lightmap has a non-negative lm_index. For such a face, lm_index is the
 * index of the image in the lightmaps lump that contains the lighting data for
 * the face. The data in the lightmap image can be located using the rectangle
 * specified by lm_start and lm_size.
 * 
 * For type 1 faces (polygons) only, lm_origin and lm_vecs can be used to
 * compute the world-space positions corresponding to lightmap samples. These
 * positions can in turn be used to compute dynamic lighting across the face.
 * 
 * None of the lm_ variables are used to compute texture coordinates for
 * indexing into lightmaps. In fact, lightmap coordinates need not be computed.
 * Instead, lightmap coordinates are simply stored with the vertices used to
 * describe each face.
 * 
 * @author palador
 */
public class Face extends Chunk {

	/**
	 * For type 1 faces (polygons), vertex and n_vertexes describe a set of
	 * vertices that form a polygon. The set always contains a loop of vertices,
	 * and sometimes also includes an additional vertex near the center of the
	 * polygon. For these faces, meshvert and n_meshverts describe a valid
	 * polygon triangulation. Every three meshverts describe a triangle. Each
	 * meshvert is an offset from the first vertex of the face, given by vertex.
	 */
	public static final int POLYGON = 1;
	/**
	 * For type 2 faces (patches), vertex and n_vertexes describe a 2D
	 * rectangular grid of control vertices with dimensions given by size.
	 * Within this rectangular grid, regions of 3×3 vertices represent
	 * biquadratic Bezier patches. Adjacent patches share a line of three
	 * vertices. There are a total of (size[0] - 1) / 2 by (size[1] - 1) / 2
	 * patches. Patches in the grid start at (i, j) given by:
	 * 
	 * <pre>
	 * i = 2n, n in [ 0 .. (size[0] - 1) / 2 ), and
	 * j = 2m, m in [ 0 .. * (size[1] - 1) / 2 ).
	 * </pre>
	 */
	public static final int PATCH = 2;
	/**
	 * For type 3 faces (meshes), meshvert and n_meshverts are used to describe
	 * the independent triangles that form the mesh. As with type 1 faces, every
	 * three meshverts describe a triangle, and each meshvert is an offset from
	 * the first vertex of the face, given by vertex.
	 */
	public static final int MESH = 3;
	/**
	 * For type 4 faces (billboards), vertex describes the single vertex that
	 * determines the location of the billboard. Billboards are used for effects
	 * such as flares. Exactly how each billboard vertex is to be interpreted
	 * has not been investigated.
	 */
	public static final int BILLBOARD = 4;

	/**
	 * Texture index.
	 */
	public int texture;
	/**
	 * Index into lump 12 (Effects), or -1.
	 */
	public int effect;
	/**
	 * Face type. 1=polygon, 2=patch, 3=mesh, 4=billboard
	 */
	public int type;
	/**
	 * Index of first vertex.
	 */
	public int vertex;
	/**
	 * Number of vertices.
	 */
	public int n_vertexes;
	/**
	 * Index of first meshvert.
	 */
	public int meshvert;
	/**
	 * Number of meshverts.
	 */
	public int n_meshverts;
	/**
	 * Lightmap index.
	 */
	public int lm_index;
	/**
	 * Corner of this face's lightmap image in lightmap.
	 */
	public final int[] lm_start = new int[2];
	/**
	 * Size of this face's lightmap image in lightmap.
	 */
	public final int[] lm_size = new int[2];
	/**
	 * World space origin of lightmap.
	 */
	public final float[] lm_origin = new float[3];
	/**
	 * World space lightmap s and t unit vectors.
	 */
	public final float[][] lm_vecs = new float[2][3];
	/**
	 * Surface normal.
	 */
	public final float[] normal = new float[3];
	/**
	 * Patch dimensions.
	 */
	public final int[] size = new int[2];

}