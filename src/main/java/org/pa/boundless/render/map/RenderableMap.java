package org.pa.boundless.render.map;

import static java.lang.System.arraycopy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.pa.boundless.bsp.BspUtil;
import org.pa.boundless.bsp.raw.BspFile;
import org.pa.boundless.bsp.raw.Face;
import org.pa.boundless.bsp.raw.Leaf;
import org.pa.boundless.bsp.raw.Vertex;
import org.pa.boundless.build.Shader;
import org.pa.boundless.build.ShaderLib;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import com.jme3.util.BufferUtils;

public class RenderableMap {

	private final Node rootNode;
	private final MapEntities entities;
	private final float[][] bounds;

	public RenderableMap(AssetManager assetManager, String bspFileRoot,
			BspFile bspFile) {

		// load shaderdef
		String shaderDef;
		try {
			shaderDef =
					FileUtils.readFileToString(new File(
							"data/scripts/q3map2_first_mod.shader"));
		} catch (IOException e1) {
			throw new RuntimeException("io", e1);
		}
		ShaderLib shaderLib = new ShaderLib();
		shaderLib.addShaderDefinitions(shaderDef);

		// load texturenames
		ArrayList<String> texNames = new ArrayList<>();
		for (org.pa.boundless.bsp.raw.Texture bspTex : bspFile.textures) {
			texNames.add(BspUtil.charsToString(bspTex.name));
		}

		// System.out.println(bspFile.toString());
		// load textures
		HashMap<String, Texture> nameToTex = new HashMap<>();
		Texture defaultTexture = null;
		for (String name : shaderLib.getTextures()) {
			Texture tex;
			try {
				tex = assetManager.loadTexture(name);
			} catch (AssetNotFoundException e) {
				System.out.println("ERROR: not found " + e.getMessage());
				if (defaultTexture == null) {
					defaultTexture =
							new Texture2D(new Image(Format.RGB8, 16, 16,
									BufferUtils.createByteBuffer(16 * 16 * 3)));
				}
				tex = defaultTexture;
			}
			tex.setWrap(WrapMode.Repeat);
			nameToTex.put(name, tex);
			System.out.println("LOADED tex " + name);
		}

		// load lightmaps
		HashMap<String, Texture> nameToLm = new HashMap<>();
		for (String name : shaderLib.getLightmaps()) {
			Texture tex;
			try {
				tex = assetManager.loadTexture(name);
			} catch (AssetNotFoundException e) {
				System.out.println("ERROR: not found " + e.getMessage());
				if (defaultTexture == null) {
					defaultTexture =
							new Texture2D(new Image(Format.RGB8, 16, 16,
									BufferUtils.createByteBuffer(16 * 16 * 3)));
				}
				tex = defaultTexture;
			}
			nameToLm.put(name, tex);
			System.out.println("LOADED lm " + name);
		}

		rootNode = new Node("mapNode");

		// load brushes
		for (int iL = 0; iL < bspFile.leafs.length; iL++) {
			Leaf leaf = bspFile.leafs[iL];
			Node brush = new Node("brush" + iL);
			for (int iF = leaf.leafface, iFMax = iF + leaf.n_leaffaces; iF < iFMax; iF++) {
				// load vertices
				Face face = bspFile.faces[bspFile.leaffaces[iF]];
				int nV = face.n_vertexes;
				float[] positionBuf = new float[3 * nV];
				float[] texBuf = new float[2 * nV];
				float[] lmBuf = new float[2 * nV];

				// System.out.println("FACE " + iF);

				for (int iV = 0; iV < nV; iV++) {
					Vertex v = bspFile.vertices[face.vertex + iV];
					// System.out.println(" Vertey " + iV);
					// System.out.println("  pos: "
					// + ArrayUtils.toString(v.position));
					// System.out.println("  tex: "
					// + ArrayUtils.toString(v.texcoord[0]));
					// System.out.println("  lm : "
					// + ArrayUtils.toString(v.texcoord[1]));
					arraycopy(v.position, 0, positionBuf, iV * 3, 3);
					arraycopy(v.texcoord[0], 0, texBuf, iV * 2, 2);
					arraycopy(v.texcoord[1], 0, lmBuf, iV * 2, 2);
					// fix v-coord: flip flip flip
					texBuf[iV * 2 + 1] *= -1;
				}

				// fix position buf: exchange x and y
				for (int pi = 0; pi < positionBuf.length; pi += 3) {
					float tmp = positionBuf[pi + 1];
					positionBuf[pi + 1] = positionBuf[pi + 2];
					positionBuf[pi + 2] = tmp;
				}

				// load triangulation
				int[] indices = new int[face.n_meshverts];
				arraycopy(bspFile.meshverts, face.meshvert, indices, 0,
						indices.length);

				// create mesh
				Mesh faceMesh = new Mesh();
				faceMesh.setBuffer(Type.Position, 3,
						BufferUtils.createFloatBuffer(positionBuf));
				faceMesh.setBuffer(Type.TexCoord, 2,
						BufferUtils.createFloatBuffer(texBuf));
				faceMesh.setBuffer(Type.TexCoord2, 2,
						BufferUtils.createFloatBuffer(lmBuf));
				faceMesh.setBuffer(Type.Index, 3,
						BufferUtils.createIntBuffer(indices));
				faceMesh.updateBound();

				// create geometry: mest + texture + lightmap
				Material mat =
						new Material(assetManager,
								"Common/MatDefs/Misc/Unshaded.j3md");
				String shaderName = texNames.get(face.texture);
				Shader shader = shaderLib.getShader(shaderName);
				// TODO: loading default textures
				mat.setTexture("ColorMap", shader == null ? nameToTex.values()
						.iterator().next() : nameToTex.get(shader.texture));
				mat.setTexture("LightMap", shader == null ? nameToLm.values()
						.iterator().next() : nameToLm.get(shader.lightmap));
				mat.setColor("Color", ColorRGBA.Gray);
				mat.setBoolean("SeparateTexCoord", true);

				Geometry geom = new Geometry("face" + iF, faceMesh);
				geom.setMaterial(mat);

				brush.attachChild(geom);
			}

			rootNode.attachChild(brush);
		}

		// load entities
		entities = new MapEntities(bspFile);

		// find bounds
		String boundsDef =
				entities.getByClassname("worldspawn").get(0).get("bl_bounds");
		if (boundsDef == null) {
			bounds = null;
		} else {
			bounds = new float[2][3];
			String[] coordStrArr = StringUtils.split(boundsDef);
			for (int i = 0; i < 6; i++) {
				bounds[i / 3][i % 3] = Float.parseFloat(coordStrArr[i]);
			}
		}

	}

	public MapEntities getEntities() {
		return entities;
	}

	public float[][] getBounds() {
		return bounds;
	}

	public Node getRootNode() {
		return rootNode;
	}

}
