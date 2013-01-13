package org.pa.boundless.build;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;

/**
 * Representation of a text-format commonly used by quake for shaders and
 * radiant-maps.
 * 
 * @author palador
 */
public class PropertiesGroup implements Cloneable {

	private PropertiesGroup parent;
	private final ArrayList<PropertiesGroup> children =
			new ArrayList<PropertiesGroup>();
	private final ArrayList<String> entries = new ArrayList<String>();

	public static PropertiesGroup parsePropertiesGroup(BufferedReader reader)
			throws IllegalArgumentException, IOException {
		Validate.notNull(reader);
		PropertiesGroup result = null;
		String line;
		int iLine = 0;
		while ((line = reader.readLine()) != null) {
			iLine++;
			line = line.trim();
			if (line.isEmpty() || line.startsWith("//")) {
				continue;
			}

			if ("{".equals(line)) {
				PropertiesGroup newGrp = new PropertiesGroup();
				if (result != null) {
					result.addChild(newGrp);
				}
				result = newGrp;
			} else if ("}".equals(line)) {
				Validate.notNull(result, "unexpected closing group at line %s",
						iLine);
				if (result.isRoot()) {
					break;
				} else {
					result = result.getParent();
				}
			} else {
				Validate.notNull(result,
						"found entry outside a group at line %s", iLine);
				result.addEntry(line);
			}
		}
		return result;
	}

	public List<String> findAllEntries() {
		ArrayList<String> result = new ArrayList<String>();
		findAllEntries(result);
		return result;
	}

	private void findAllEntries(ArrayList<String> list) {
		list.addAll(entries);
		for (PropertiesGroup child : children) {
			child.findAllEntries(list);
		}
	}

	public PropertiesGroup() {
		this(null);
	}

	private PropertiesGroup(PropertiesGroup parent) {
		this.parent = parent;
	}

	public PropertiesGroup addChild(PropertiesGroup child) {
		Validate.notNull(child);
		child.parent = this;
		children.add(child);
		return this;
	}

	public PropertiesGroup addEntry(String entry) {
		Validate.notNull(entry);
		entry = entry.trim();
		Validate.notEmpty(entry);
		entries.add(entry);
		return this;
	}

	public PropertiesGroup addAllEntries(Collection<String> entries) {
		for (String newE : entries) {
			addEntry(newE);
		}
		return this;
	}

	public PropertiesGroup removeChild(PropertiesGroup child) {
		if (children.remove(child)) {
			child.parent = null;
		}
		return this;
	}

	public PropertiesGroup getParent() {
		return parent;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public List<PropertiesGroup> getChildren() {
		return Collections.unmodifiableList(new ArrayList<>(children));
	}

	public List<String> getEntries() {
		return Collections.unmodifiableList(new ArrayList<>(entries));
	}

	public int getDepth() {
		return parent == null ? 0 : parent.getDepth() + 1;
	}

	@Override
	public PropertiesGroup clone() throws CloneNotSupportedException {
		PropertiesGroup result = new PropertiesGroup();
		for (String entry : entries) {
			result.addEntry(entry);
		}
		for (PropertiesGroup child : children) {
			result.addChild(child.clone());
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this
				|| (obj != null && obj instanceof PropertiesGroup && obj
						.toString().equals(toString()));
	}

	@Override
	public String toString() {
		return toString(null, null).toString();
	}

	public String toIntendedString() {
		return toString(null, "").toString();
	}

	private StrBuilder toString(StrBuilder result, String prefix) {
		if (result == null) {
			result = new StrBuilder().setNewLineText("\n").setNullText("");
		}
		result.append(prefix).append('{').appendNewLine();
		for (String entry : entries) {
			if(prefix != null) {
				result.append("  ");
			}
			result.append(prefix).append(entry).appendNewLine();
		}
		String childPrefix = prefix == null ? null : "    " + prefix;
		for (PropertiesGroup child : children) {
			child.toString(result, childPrefix);
		}
		result.append(prefix).append('}').appendNewLine();
		return result;
	}

}
