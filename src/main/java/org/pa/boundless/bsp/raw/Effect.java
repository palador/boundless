package org.pa.boundless.bsp.raw;

/**
 * The effects lump stores references to volumetric shaders (typically fog)
 * which affect the rendering of a particular group of faces.
 * 
 * @author palador
 */
public class Effect {
	/**
	 * Effect shader.
	 */
	char[] name = new char[64];
	/**
	 * Brush that generated this effect.
	 */
	int brush;
	/**
	 * Always 5, except in q3dm8, which has one effect with -1.
	 */
	int unknown;
}