package org.pa.boundless.bsp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;
import org.pa.boundless.bsp.raw.Brush;
import org.pa.boundless.bsp.raw.Brushside;
import org.pa.boundless.bsp.raw.BspFile;
import org.pa.boundless.bsp.raw.DirEntry;
import org.pa.boundless.bsp.raw.Effect;
import org.pa.boundless.bsp.raw.Face;
import org.pa.boundless.bsp.raw.Header;
import org.pa.boundless.bsp.raw.Leaf;
import org.pa.boundless.bsp.raw.Lightmap;
import org.pa.boundless.bsp.raw.Lightvol;
import org.pa.boundless.bsp.raw.Lump;
import org.pa.boundless.bsp.raw.Model;
import org.pa.boundless.bsp.raw.Node;
import org.pa.boundless.bsp.raw.Plane;
import org.pa.boundless.bsp.raw.Texture;
import org.pa.boundless.bsp.raw.Vertex;
import org.pa.boundless.bsp.raw.Visdata;

/**
 * Loads raw BSP data from a stream. Produces a {@link BspFile}.
 * 
 * @author palador
 */
public class BspLoader implements Callable<BspFile> {

	/**
	 * Chunkreader used to read direntries.
	 */
	private static final ChunkReader<DirEntry> DIRENTRY = new ChunkReader<>(
			DirEntry.class);

	/**
	 * Maps a lump to a suited chunk-reader. Works for lumps with fixed chunk
	 * length, only.
	 */
	private static final Map<Lump, ChunkReader<?>> LUMP_TO_CHUNKREADER;

	static {
		// init that map
		HashMap<Lump, ChunkReader<?>> lumpToChunkreader = new HashMap<>();
		for (Lump lump : Lump.lumpsWithFixedChunkLength()) {
			lumpToChunkreader.put(lump, new ChunkReader<>(lump.getChunkType()));
		}
		LUMP_TO_CHUNKREADER = Collections.unmodifiableMap(lumpToChunkreader);
	}

	// The inputstream of your choice.
	private InputStream is;

	/**
	 * Sets the stream to read the BSP-data from.
	 * 
	 * @param is
	 *            the input-stream
	 * @return this
	 * @throws IllegalArgumentException
	 *             if <code>is</code> is <code>null</code>
	 */
	public BspLoader setStream(InputStream is) throws IllegalArgumentException {
		this.is = is;
		return this;
	}

	/**
	 * Loads a BSP-file.
	 * 
	 * @param is
	 *            the input-stream to load the BSP from
	 * @return the BSP-file
	 * @throws IOException
	 *             If an I/O-error occured.
	 * @throws IllegalStateException
	 *             If the provided data is illegal or the loader-configuration
	 *             is invalid or insufficient.
	 * @throws IllegalArgumentException
	 *             If the provided data is illegal.
	 */
	public BspFile call() throws IOException, IllegalStateException,
			IllegalArgumentException {
		if (is == null) {
			throw new IllegalStateException("no inputstream set");
		}

		// load whole file and put it into a little endian byte-buffer
		ByteBuffer buf =
				ByteBuffer.wrap(IOUtils.toByteArray(is)).order(
						ByteOrder.LITTLE_ENDIAN);

		// create result-object and load the header
		BspFile bsp = new BspFile();
		bsp.header = new Header();
		bufGetText(buf, bsp.header.magic);
		bsp.header.version = buf.getInt();
		for (Lump lump : Lump.values()) {
			bsp.header.direntries[lump.ordinal()] = DIRENTRY.loadChunk(buf);
		}

		// load fixed chunk-length lumps
		for (Lump lump : Lump.values()) {
			try {
				// prepare buffer
				DirEntry dirEntry = bsp.header.direntries[lump.ordinal()];
				// note: length is specified in multiples of 4
				buf.limit(dirEntry.offset + (dirEntry.length));
				buf.position(dirEntry.offset);

				if (Lump.lumpsWithFixedChunkLength().contains(lump)) {

					ChunkReader<?> chunkReader = LUMP_TO_CHUNKREADER.get(lump);
					int chunkCount =
							buf.remaining() / chunkReader.getChunkLength();
					Object[] chunks = new Object[chunkCount];

					for (int i = 0; i < chunkCount; i++) {
						chunks[i] = chunkReader.loadChunk(buf);
					}

					// find and init target array
					Object targetArray;
					switch (lump) {
					case BRUSHES:
						bsp.brushes = new Brush[chunkCount];
						targetArray = bsp.brushes;
						break;
					case BRUSHSIDES:
						bsp.brushsides = new Brushside[chunkCount];
						targetArray = bsp.brushsides;
						break;
					case EFFECTS:
						bsp.effects = new Effect[chunkCount];
						targetArray = bsp.effects;
						break;
					case FACES:
						bsp.faces = new Face[chunkCount];
						targetArray = bsp.faces;
						break;
					case LEAFS:
						bsp.leafs = new Leaf[chunkCount];
						targetArray = bsp.leafs;
						break;
					case LIGHTMAPS:
						bsp.lightmaps = new Lightmap[chunkCount];
						targetArray = bsp.lightmaps;
						break;
					case LIGHTVOLS:
						bsp.lightvols = new Lightvol[chunkCount];
						targetArray = bsp.lightvols;
						break;
					case MODELS:
						bsp.models = new Model[chunkCount];
						targetArray = bsp.models;
						break;
					case NODES:
						bsp.nodes = new Node[chunkCount];
						targetArray = bsp.nodes;
						break;
					case PLANES:
						bsp.planes = new Plane[chunkCount];
						targetArray = bsp.planes;
						break;
					case TEXTURES:
						bsp.textures = new Texture[chunkCount];
						targetArray = bsp.textures;
						break;
					case VERTEXES:
						bsp.vertices = new Vertex[chunkCount];
						targetArray = bsp.vertices;
						break;
					default:
						throw new RuntimeException(
								"Lump doesn't support automatic reading: "
										+ lump);
					}

					// copy loaded data to target-array
					System.arraycopy(chunks, 0, targetArray, 0, chunkCount);
				} else {
					// Load lump without fixed chunk-length
					switch (lump) {
					case ENTITIES:
						bsp.entities = bufToText(buf);
						break;
					case LEAFBRUSHES:
						bsp.leafbrushes = bufToIntArray(buf);
						break;
					case LEAFFACES:
						bsp.leaffaces = bufToIntArray(buf);
						break;
					case MESHVERTS:
						bsp.meshverts = bufToIntArray(buf);
						break;
					case VISDATA: {
						Visdata visdata = new Visdata();
						visdata.n_vecs = buf.getInt();
						visdata.sz_vecs = buf.getInt();
						visdata.vecs =
								new byte[visdata.n_vecs * visdata.sz_vecs];
						buf.get(visdata.vecs);
						bsp.visdata = visdata;
						break;
					}
					default:
						throw new RuntimeException("unexpected lump: " + lump);
					}
				}
			} catch (Exception e) {
				throw new IllegalArgumentException(
						"Invalid data: could not load lump " + lump, e);
			}
		}

		return bsp;
	}

	private static char[] bufToText(ByteBuffer buf) {
		char[] result = new char[buf.remaining()];
		for (int i = 0; i < result.length; i++) {
			result[i] = (char) buf.get();
		}
		return result;
	}

	private static void bufGetText(ByteBuffer buf, char[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = (char) buf.get();
		}
	}

	private static int[] bufToIntArray(ByteBuffer buf) {
		int[] result = new int[buf.remaining() / 4];
		for (int i = 0; i < result.length; i++) {
			result[i] = buf.getInt();
		}
		return result;
	}
}
