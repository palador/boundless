package org.pa.boundless.bsp.raw;

/**
 * The planes lump stores a generic set of planes that are in turn referenced by
 * nodes and brushsides.
 * 
 * @author palador
 */
public class Plane extends Chunk {
	/**
	 * Plane normal.
	 */
	public final float[] normal = new float[3];

	/**
	 * Distance from origin to plane along normal.
	 */
	public float dist;
}