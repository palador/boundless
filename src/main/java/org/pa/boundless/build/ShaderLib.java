package org.pa.boundless.build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;

public class ShaderLib {

	private final HashMap<String, Shader> nameToShader = new HashMap<>();
	private final HashSet<String> textures = new HashSet<>();
	private final HashSet<String> lightmaps = new HashSet<>();

	public void addShaderDefinitions(String shaderDefinitions)
			throws IllegalArgumentException {
		String shaderDef = "{\n" + shaderDefinitions + "\n}";
		PropertiesGroup pg;
		try {
			pg =
					PropertiesGroup.parsePropertiesGroup(new BufferedReader(
							new StringReader(shaderDef)));
		} catch (IOException e) {
			throw new RuntimeException("unlikely");
		}

		for (int i = 0; i < pg.getEntries().size(); i++) {
			String entry = pg.getEntries().get(i);
			PropertiesGroup entryShader = pg.getChildren().get(i);
			String tex = null;
			String lm = null;
			for (PropertiesGroup stage : entryShader.getChildren()) {
				for (String stageEntry : stage.getEntries()) {
					if (stageEntry.startsWith("map ")) {
						String mapPath = stageEntry.substring(4);
						if (mapPath.contains("lm_")) {
							lm = mapPath;
						} else {
							tex = mapPath;
						}
					}
				}
			}

			if (tex != null) {
				Shader shader = new Shader(entry, tex, lm);
				nameToShader.put(entry, shader);
				textures.add(tex);
				if (lm != null) {
					lightmaps.add(lm);
				}
			}
		}
	}

	public HashSet<String> getLightmaps() {
		return lightmaps;
	}

	public HashSet<String> getTextures() {
		return textures;
	}

	public Shader getShader(String name) {
		return nameToShader.get(name);
	}

}
