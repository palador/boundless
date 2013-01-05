package org.pa.boundless.bsp.raw;

/**
 * The lightvols lump stores a uniform grid of lighting information used to
 * illuminate non-map objects.
 * 
 * Lightvols make up a 3D grid whose dimensions are:
 * <pre>
 * nx = floor(models[0].maxs[0] / 64) - ceil(models[0].mins[0] / 64) + 1
 * ny = floor(models[0].maxs[1] / 64) - ceil(models[0].mins[1] / 64) + 1
 * nz = floor(models[0].maxs[2] / 128) - ceil(models[0].mins[2] / 128) + 1
 * </pre>
 * 
 * @author palador
 */
public class Lightvol extends Chunk {
	/**
	 * Index of phi for dir.
	 */
	public static final int PHI = 0;
	/**
	 * Index of theta for dir.
	 */
	public static final int THETA = 1;
	/**
	 * Ambient color component. RGB.
	 */
	public final byte[] ambient = new byte[3];
	/**
	 * Directional color component. RGB.
	 */
	public final byte[] directional = new byte[3];
	/**
	 * Direction to light. 0=phi, 1=theta.
	 */
	public final byte[] dir = new byte[2];
}