package org.pa.boundless.render.map;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.pa.boundless.bsp.raw.BspFile;

public class MapEntities {

	public static final String NO_CLASSNAME = "NO_CLASSNAME";

	private final ArrayList<Map<String, String>> entities = new ArrayList<>();
	private final HashMap<String, ArrayList<Map<String, String>>> classnameToEntity =
			new HashMap<>();

	public MapEntities(BspFile bsp) {
		String entitiesDef = new String(bsp.entities);
		int start;
		int end = 0;
		while ((start = entitiesDef.indexOf('{', end)) != -1) {
			end = entitiesDef.indexOf('}', start);
			String entityDef = entitiesDef.substring(start + 1, end);

			TreeMap<String, String> entity = new TreeMap<>();
			Map<String, String> unmodifiableEntity = unmodifiableMap(entity);
			entities.add(unmodifiableEntity);

			int quoteStart;
			int quoteEnd = -1;
			String key = null;
			while ((quoteStart = entityDef.indexOf('\"', quoteEnd + 1)) != -1) {
				quoteEnd = entityDef.indexOf('\"', quoteStart + 1);
				String val = entityDef.substring(quoteStart + 1, quoteEnd);
				if (key == null) {
					key = val;
				} else {
					entity.put(key, val);
					key = null;
				}
			}

			String classname = entity.get("classname");
			classname = classname != null ? classname : NO_CLASSNAME;
			ArrayList<Map<String, String>> cnEntities =
					classnameToEntity.get(classname);
			if (cnEntities == null) {
				cnEntities = new ArrayList<>();
				classnameToEntity.put(classname, cnEntities);
			}
			cnEntities.add(unmodifiableEntity);
		}
	}

	public List<Map<String, String>> getByClassname(String classname) {
		List<Map<String, String>> result = classnameToEntity.get(classname);
		return (List<Map<String, String>>) (result == null ? emptyList()
				: result);
	}

	@Override
	public String toString() {
		return entities.toString();
	}
}
