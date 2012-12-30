package abc.die.katze;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class Map {

	public enum Field {
		WALL, SPACE
	}

	private int width;
	private int height;
	private int startX;
	private int startY;
	private Field[][] fields;

	public void load(String data) {
		String[] rows = data.split("\\s+");
		if (rows.length == 0) {
			throw new IllegalArgumentException("is not map data: is empty");
		}
		height = rows.length;
		width = rows[0].length();
		fields = new Field[width][height];
		startX = startY = -1;
		for (int row = 0; row < height; row++) {
			String rowData = rows[row];
			if (rowData.length() != width) {
				throw new IllegalArgumentException("Illegal map data: row "
						+ row + " has unexpected length");
			}
			for (int col = 0; col < width; col++) {
				Field field;
				switch (rowData.charAt(col)) {
				case 'x':
					field = Field.WALL;
					break;
				case '.':
					field = Field.SPACE;
					if (startX == -1) {
						startX = col;
						startY = row;
					}
					break;
				case 'o':
					field = Field.SPACE;
					startX = col;
					startY = row;
					break;
				default:
					throw new IllegalArgumentException("unexpected field: "
							+ rowData.charAt(col));
				}

				fields[row][col] = field;
			}
		}
	}

	public void load(InputStream is) throws IOException {
		load(IOUtils.toString(is));
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isWall(int x, int y) {
		return fields[x][y] == Field.WALL;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}
}
