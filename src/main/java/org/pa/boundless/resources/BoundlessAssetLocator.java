package org.pa.boundless.resources;

import java.io.File;
import java.io.IOException;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLocator;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.UrlAssetInfo;

public class BoundlessAssetLocator implements AssetLocator {

	private String rootPath;

	@Override
	public AssetInfo locate(AssetManager manager, AssetKey key) {
		File file = new File(rootPath, key.getName());
		try {
			return file.exists() ? UrlAssetInfo.create(manager, key, file
					.toURI().toURL()) : null;
		} catch (IOException e) {
			// FIXME no difference between error and not exist
			return null;
		}
	}

	@Override
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

}
