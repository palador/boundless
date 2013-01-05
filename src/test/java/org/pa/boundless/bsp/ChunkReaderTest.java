package org.pa.boundless.bsp;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.pa.boundless.bsp.raw.Brush;
import org.pa.boundless.bsp.raw.Brushside;
import org.pa.boundless.bsp.raw.Effect;
import org.pa.boundless.bsp.raw.Face;
import org.pa.boundless.bsp.raw.Leaf;
import org.pa.boundless.bsp.raw.Lightmap;
import org.pa.boundless.bsp.raw.Lightvol;
import org.pa.boundless.bsp.raw.Model;
import org.pa.boundless.bsp.raw.Node;
import org.pa.boundless.bsp.raw.Plane;
import org.pa.boundless.bsp.raw.Texture;
import org.pa.boundless.bsp.raw.Vertex;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class ChunkReaderTest {

	@DataProvider(name = "possibleChunkTypeProvider")
	public Object[][] possibleChunkTypeProvider() {
		return new Object[][] { { Brush.class }, { Brushside.class },
				{ Effect.class }, { Face.class }, { Leaf.class },
				{ Lightmap.class }, { Lightvol.class }, { Model.class },
				{ Node.class }, { Plane.class }, { Texture.class },
				{ Vertex.class } };
	}

	@Test(dataProvider = "possibleChunkTypeProvider")
	public void creation(Class<?> chunkType) {
		new ChunkReader<>(chunkType);
	}

	@Test(description = "Most simple test with a brush chunk consisting out of 3 integers.")
	public void brush() throws IOException {
		byte[] bytes = new byte[3 * 4];
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		buf.putInt(10);
		buf.putInt(Integer.MIN_VALUE);
		buf.putInt(42);

		ChunkReader<Brush> reader = new ChunkReader<>(Brush.class);
		Brush brush = reader.loadChunk(ByteBuffer.wrap(bytes));
		assertEquals(brush.brushside, 10);
		assertEquals(brush.n_brushsides, Integer.MIN_VALUE);
		assertEquals(brush.texture, 42);
	}

	@Test(description = "Test of huge array-fields. A lightmap contains an single byte[128][128][3] array.")
	public void lighmap() throws IOException {
		byte[][][] lightmapdata = new byte[128][128][3];
		byte[] bytes = new byte[128 * 128 * 3];
		new Random("42".hashCode()).nextBytes(bytes);
		int i = 0;
		for (byte[][] a : lightmapdata) {
			for (byte[] b : a) {
				for (int j = 0; j < 3; j++) {
					b[j] = bytes[i++];
				}
			}
		}

		ChunkReader<Lightmap> reader = new ChunkReader<>(Lightmap.class);
		Lightmap lightmap = reader.loadChunk(ByteBuffer.wrap(bytes));

		assertTrue(Arrays.deepEquals(lightmap.map, lightmapdata));
	}

	@Test(description = "Test node which contains simple fields and arrays")
	public void node() throws IOException {
		byte[] bytes = new byte[4 + 2 * 4 + 3 * 4 + 3 * 4];
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		// int plane
		buf.putInt(42);
		// int[2] children
		buf.putInt(10);
		buf.putInt(11);
		// int[3] mins
		buf.putInt(100);
		buf.putInt(101);
		buf.putInt(102);
		// int[3] maxs
		buf.putInt(1000);
		buf.putInt(1001);
		buf.putInt(1002);

		ChunkReader<Node> reader = new ChunkReader<>(Node.class);
		Node node = reader.loadChunk(ByteBuffer.wrap(bytes));

		assertEquals(node.plane, 42);
		assertEqualsIntArr(node.children, 10, 11);
		assertEqualsIntArr(node.mins, 100, 101, 102);
		assertEqualsIntArr(node.maxs, 1000, 1001, 1002);
	}

	// compare int-arrays with better error message
	private static void assertEqualsIntArr(int[] actual, int... expected) {
		assertEquals(actual, expected, "actual: " + ArrayUtils.toString(actual)
				+ " <-> expected: " + ArrayUtils.toString(expected));
	}

}
