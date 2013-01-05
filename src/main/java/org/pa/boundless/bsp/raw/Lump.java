package org.pa.boundless.bsp.raw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * You can use {@link Lump#ordinal()} as a index in {@link Header#direntries}.
 * 
 * There are 17 lumps in a Quake 3 BSP file. In the order that they appear in
 * the lump directory, they are:
 */
public enum Lump {
	/**
	 * Game-related object descriptions.
	 */
	ENTITIES(null, false),
	/**
	 * Surface descriptions.
	 */
	TEXTURES(Texture.class, true),
	/**
	 * Planes used by map geometry.
	 */
	PLANES(Plane.class, true),
	/**
	 * BSP tree nodes.
	 */
	NODES(Node.class, true),
	/**
	 * BSP tree leaves.
	 */
	LEAFS(Leaf.class, true),
	/**
	 * List of face indices, one list per leaf.
	 */
	LEAFFACES(null, false),
	/**
	 * Lists of brush indices, one list per leaf.
	 */
	LEAFBRUSHES(null, false),
	/**
	 * Descriptions of rigid world geometry in map.
	 */
	MODELS(Model.class, true),
	/**
	 * Convex polyhedra used to describe solid space.
	 */
	BRUSHES(Brush.class, true),
	/**
	 * Brush surfaces.
	 */
	BRUSHSIDES(Brushside.class, true),
	/**
	 * Vertices used to describe faces.
	 */
	VERTEXES(Vertex.class, true),
	/**
	 * Lists of offsets, one list per mesh.
	 */
	MESHVERTS(null, false),
	/**
	 * List of special map effects.
	 */
	EFFECTS(Effect.class, true),
	/**
	 * Surface geometry.
	 */
	FACES(Face.class, true),
	/**
	 * Packed lightmap data.
	 */
	LIGHTMAPS(Lightmap.class, true),
	/**
	 * Local illumination data.
	 */
	LIGHTVOLS(Lightvol.class, true),
	/**
	 * Cluster-cluster visibility data.
	 */
	VISDATA(Visdata.class, false);

	private static List<Lump> LUMPS_WITH_FIXED_CHUNK_LEN;

	private final Class<?> chunkType;
	private final boolean isChunkLengthFixed;

	private Lump(Class<?> chunkType, boolean isFixed) {
		this.chunkType = chunkType;
		this.isChunkLengthFixed = isFixed;
	}

	public boolean hasChunkType() {
		return chunkType != null;
	}

	public Class<?> getChunkType() {
		return chunkType;
	}

	public boolean isChunkLengthFixed() {
		return isChunkLengthFixed;
	}

	public static List<Lump> lumpsWithFixedChunkLength() {
		if (LUMPS_WITH_FIXED_CHUNK_LEN == null) {
			ArrayList<Lump> lumpList = new ArrayList<>();
			for (Lump lump : Lump.values()) {
				if (lump.isChunkLengthFixed()) {
					lumpList.add(lump);
				}
			}
			LUMPS_WITH_FIXED_CHUNK_LEN = Collections.unmodifiableList(lumpList);
		}
		return LUMPS_WITH_FIXED_CHUNK_LEN;
	}

}