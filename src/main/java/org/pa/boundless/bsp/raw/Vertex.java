package org.pa.boundless.bsp.raw;

/**
 * The vertexes lump stores lists of vertices used to describe faces.
 * 
 * @author palador
 */
public class Vertex extends Chunk {
	/**
	 * Vertex position.
	 */
	public final float[] position = new float[3];
	/**
	 * Vertex texture coordinates. 0=surface, 1=lightmap.
	 */
	public final float[][] texcoord = new float[2][2];
	/**
	 * Vertex normal.
	 */
	public final float[] normal = new float[3];
	/**
	 * Vertex color. RGBA.
	 */
	public final byte[] color = new byte[4];
}