package org.pa.boundless.bsp.raw;

/**
 * The visdata lump stores bit vectors that provide cluster-to-cluster
 * visibility information.
 * 
 * @author palador
 */
public class Visdata {
	/**
	 * Number of vectors.
	 */
	int n_vecs;
	/**
	 * Size of each vector, in bytes.
	 */
	int sz_vecs;
	/**
	 * Visibility data. One bit per cluster per vector.
	 */
	byte[] vecs;
}