package org.pa.boundless.bsp.raw;

/**
 * The visdata lump stores bit vectors that provide cluster-to-cluster
 * visibility information.
 * 
 * Cluster x is visible from cluster y if the <code>(1 << y % 8) bit of vecs[x *
 * sz_vecs + y / 8]</code> is set.
 * 
 * Note that clusters are associated with leaves.
 * 
 * @author palador
 */
public class Visdata {
	/**
	 * Number of vectors.
	 */
	public int n_vecs;
	/**
	 * Size of each vector, in bytes.
	 */
	public int sz_vecs;
	/**
	 * Visibility data. One bit per cluster per vector.
	 * 
	 * Size if <code>n_vecs * sz_vecs</code>
	 */
	public byte[] vecs;
}