package org.pa.boundless.build;

public class Shader {

	public final String name;
	public final String texture;
	public final String lightmap;

	public Shader(String name, String texture, String lightmap) {
		this.name = name;
		this.texture = texture;
		this.lightmap = lightmap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result =
				prime * result + ((lightmap == null) ? 0 : lightmap.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((texture == null) ? 0 : texture.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shader other = (Shader) obj;
		if (lightmap == null) {
			if (other.lightmap != null)
				return false;
		} else if (!lightmap.equals(other.lightmap))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (texture == null) {
			if (other.texture != null)
				return false;
		} else if (!texture.equals(other.texture))
			return false;
		return true;
	}

}
