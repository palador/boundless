package org.pa.boundless.util;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealVector;

public abstract class Math3D {

	private Math3D() {
	}

	/**
	 * 
	 * @param planepoints
	 *            [plane][point][axis]
	 * @return
	 */
	public static double[] intersectionOfPlanes(double[][][] allplanepoints) {
		double[][] planeABCs = new double[3][];
		double[] planeDs = new double[3];
		for (int ip = 0; ip < 3; ip++) {
			Vector3D[] planepoints = new Vector3D[3];
			for (int ipp = 0; ipp < 3; ipp++) {
				planepoints[ipp] = new Vector3D(allplanepoints[ip][ipp]);
			}
			Vector3D cross =
					planepoints[1].subtract(planepoints[0]).crossProduct(
							planepoints[2].subtract(planepoints[0]));
			planeABCs[ip] = cross.toArray();
			planeDs[ip] =
					cross.getX() * planepoints[0].getX() + cross.getY()
							* planepoints[0].getY() + cross.getZ()
							* planepoints[0].getZ();
		}

		Array2DRowRealMatrix matrix =
				new Array2DRowRealMatrix(planeABCs, false);
		DecompositionSolver solver = new LUDecomposition(matrix).getSolver();
		RealVector constans = new ArrayRealVector(planeDs);
		return solver.solve(constans).toArray();
	}

}
