package org.pa.boundless.bsp.raw;

/**
 * The lightmaps lump stores the light map textures used make surface lighting
 * look more realistic.
 * 
 * @author palador
 */
public class Lightmap {
	/**
	 * Lightmap color data. RGB.
	 */
	public final byte[][][] map = new byte[128][128][3];
}