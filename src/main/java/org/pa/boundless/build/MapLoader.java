package org.pa.boundless.build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.pa.boundless.util.Math3D;

public class MapLoader implements Callable<PropertiesGroup> {

	private static final NumberFormat US_NUM_FORMAT = NumberFormat
			.getNumberInstance(Locale.US);

	static {
		US_NUM_FORMAT.setGroupingUsed(false);
	}

	private InputStream is;

	@Override
	public PropertiesGroup call() throws Exception {
		if (is == null) {
			throw new IllegalStateException("Inputstream not set");
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		// read entities
		PropertiesGroup entities = new PropertiesGroup();
		{
			PropertiesGroup entity;
			while ((entity = PropertiesGroup.parsePropertiesGroup(reader)) != null) {
				entities.addChild(entity);
			}
		}

		// get bounds
		float[][] bounds =
				new float[][] {
						{ Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE },
						{ -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE } }; // 0=min,
																					// 1=max

		for (PropertiesGroup entity : entities.getChildren()) {
			for (PropertiesGroup brush : entity.getChildren()) {

				ArrayList<double[][]> planesByPoints =
						new ArrayList<double[][]>();
				for (String entry : brush.getEntries()) {
					if (isPlane(entry)) {
						Plane plane = new Plane(entry);
						double[][] planePoints = new double[3][3];
						for (int iaxis = 0; iaxis < 3; iaxis++) {
							for (int ip = 0; ip < 3; ip++) {
								planePoints[ip][iaxis] =
										plane.points[ip][iaxis];
							}
						}
						planesByPoints.add(planePoints);
					}

				}

				// combine all planes with each other (cascadestyle, for the
				// intersection points)
				int nPlanes = planesByPoints.size();
				for (int ipa = 0; ipa < nPlanes; ipa++) {
					for (int ipb = ipa + 1; ipb < nPlanes; ipb++) {
						for (int ipc = ipb + 1; ipc < nPlanes; ipc++) {
							try {
								double[] intersectPnt =
										Math3D.intersectionOfPlanes(new double[][][] {
												planesByPoints.get(ipa),
												planesByPoints.get(ipb),
												planesByPoints.get(ipc) });
								for (int iaxis = 0; iaxis < 3; iaxis++) {
									bounds[0][iaxis] =
											Math.min(bounds[0][iaxis],
													(float) intersectPnt[iaxis]);
									bounds[1][iaxis] =
											Math.max(bounds[1][iaxis],
													(float) intersectPnt[iaxis]);
								}
							} catch (SingularMatrixException e) {
								// cant intersect because they are parallel
							}
						}
					}
				}
			}
		}

		PropertiesGroup result = entities.clone();

		// add original stuff 2 times per axis
		for (int iaxis = 0; iaxis < 3; iaxis++) {
			for (int direction = -1; direction <= 1; direction += 2) {
				float[] offset = new float[3];
				offset[iaxis] =
						direction * (bounds[1][iaxis] - bounds[0][iaxis]);

				List<PropertiesGroup> orgEntities = entities.getChildren();
				for (int ie = 0; ie < orgEntities.size(); ie++) {
					PropertiesGroup orgEntity = orgEntities.get(ie);
					PropertiesGroup targetEntity;
					if (ie == 0) {
						// worldspawn!
						targetEntity = result.getChildren().get(0);
					} else {
						PropertiesGroup newEntity = new PropertiesGroup();
						for (String orgEntry : orgEntity.getEntries()) {
							newEntity.addEntry(offsetOrigin(orgEntry, offset));
						}
						result.addChild(newEntity);
						targetEntity = newEntity;
					}

					for (PropertiesGroup brush : orgEntity.getChildren()) {
						targetEntity.addChild(offsetBrush(brush, offset));
					}
				}
			}
		}

		return result;
	}

	private static PropertiesGroup offsetBrush(PropertiesGroup brush,
			float[] offset) {
		PropertiesGroup result = new PropertiesGroup();
		for (String entry : brush.getEntries()) {
			Plane entryPlane = new Plane(entry);
			for (float[] point : entryPlane.points) {
				for (int iaxis = 0; iaxis < 3; iaxis++) {
					point[iaxis] += offset[iaxis];
				}
			}
			result.addEntry(entryPlane.toString());
		}
		return result;
	}

	private static String offsetOrigin(String entry, float[] offset) {
		if (entry.startsWith("\"origin\"")) {
			Matcher matcher = Pattern.compile("\\d+").matcher(entry);
			float[] newPos = new float[3];
			for (int iaxis = 0; iaxis < 3; iaxis++) {
				matcher.find();
				newPos[iaxis] =
						Float.parseFloat(matcher.group()) + offset[iaxis];
			}
			entry = "\"origin\" \"";
			for (int iaxis = 0; iaxis < 3; iaxis++) {
				if (iaxis > 0) {
					entry += " ";
				}
				entry += US_NUM_FORMAT.format(newPos[iaxis]);

			}
			entry += "\"";
		}
		return entry;
	}

	private static PropertiesGroup createWhiteBrush(float[][] bounds) {
		float[] tmp = bounds[1];
		bounds[1] = bounds[0];
		bounds[0] = tmp;
		PropertiesGroup result = new PropertiesGroup();
		System.out.println("CREATE FOR : " + ArrayUtils.toString(bounds));

		for (int iaxis = 0; iaxis < 3; iaxis++) {
			for (int side = 0; side <= 1; side++) {

				Plane plane =
						new Plane(
								"( 0 0 0 ) ( 0 0 0 ) ( 0 0 0 ) common/white 0 0 0 0.500000 0.500000 0 0 0");

				for (int ip = 0; ip < 3; ip++) {
					plane.points[ip][(iaxis + 0) % 3] = bounds[side][iaxis];
					plane.points[ip][(iaxis + 1) % 3] =
							bounds[ip == 0 ? flipSide(side) : side][iaxis];
					plane.points[ip][(iaxis + 2) % 3] =
							bounds[ip == 2 ? flipSide(side) : side][iaxis];
				}

				result.addEntry(plane.toString());
			}
		}
		return result;
	}

	private static int flipSide(int side) {
		return side == 0 ? 1 : 0;
	}

	private static boolean isPlane(String entry) {
		return entry.startsWith("(");
	}

	public MapLoader setInputStream(InputStream is) {
		this.is = is;
		return this;
	}

	public static void main(String[] args) throws Exception {
		// TODO KILLME
		PropertiesGroup result =
				new MapLoader().setInputStream(
						new FileInputStream(new File("data/maps/cube.map")))
						.call();

		System.out.println(result.toIntendedString());
		FileWriter out = new FileWriter("data/maps/first_mod.map");
		for (PropertiesGroup group : result.getChildren()) {
			out.write(group.toString());
		}
		PropertiesGroup test = new PropertiesGroup();
		test.addEntry("\"classname\" \"func_group\"");
		test.addChild(createWhiteBrush(new float[][] { { 128, 196, 256 },
				{ 128 + 1024, 196 + 1024, 256 + 1024 } }));
		out.write(test.toString());
		System.out.println(test);
		out.close();
	}

	private static class Plane {
		final float[][] points = new float[3][3];
		private final String suffix;

		public Plane(String entry) {
			for (int ip = 0; ip < 3; ip++) {
				int icb = entry.indexOf(')');
				String pointText = entry.substring(entry.indexOf('(') + 1, icb);
				String[] coords = StringUtils.split(pointText);
				for (int ic = 0; ic < 3; ic++) {
					points[ip][ic] = Float.parseFloat(coords[ic]);
				}
				entry = entry.substring(icb + 2);
			}
			suffix = entry;
		}

		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			for (int ip = 0; ip < 3; ip++) {
				result.append("( ");
				for (int ic = 0; ic < 3; ic++) {
					result.append(US_NUM_FORMAT.format(points[ip][ic])).append(
							" ");
				}
				result.append(") ");
			}
			result.append(suffix);
			return result.toString();
		}
	}

}
