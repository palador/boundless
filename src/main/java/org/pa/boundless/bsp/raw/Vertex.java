package org.pa.boundless.bsp.raw;

/**
 * The vertexes lump stores lists of vertices used to describe faces.
 * 
 * @author palador
 */
public class Vertex {
	/**
	 * Vertex position.
	 */
	float[] position = new float[3];
	/**
	 * Vertex texture coordinates. 0=surface, 1=lightmap.
	 */
	float[][] texcoord = new float[2][2];
	/**
	 * Vertex normal.
	 */
	float[] normal = new float[3];
	/**
	 * Vertex color. RGBA.
	 */
	byte[] color = new byte[4];
}