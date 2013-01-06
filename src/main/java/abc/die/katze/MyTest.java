package abc.die.katze;

import java.io.FileInputStream;
import java.io.IOException;

import org.pa.boundless.bsp.BspLoader;
import org.pa.boundless.bsp.raw.BspFile;
import org.pa.boundless.render.map.RenderableMap;
import org.pa.boundless.resources.BoundlessAssetLocator;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class MyTest extends SimpleApplication implements ActionListener {

	public static void main(String[] args) {
		new MyTest().start();
	}

	float boxSize = 10f;
	float boxExtend = boxSize / 2;

	private CharacterControl player;
	private boolean up, down, left, right;
	private Vector3f walkDirection = new Vector3f();
	private BulletAppState bulletAppState = new BulletAppState();
	private Material wallMat;
	private float mapWidth, mapHeight;
	private BitmapText helloTxt;

	@Override
	public void simpleInitApp() {
		try {
			assetManager.registerLocator("data", BoundlessAssetLocator.class);
			BspLoader bspLoader =
					new BspLoader().setStream(new FileInputStream(
							"data/maps/first.bsp"));
			BspFile bsp = bspLoader.call();
			RenderableMap map = new RenderableMap(assetManager, bsp);
			rootNode.attachChild(map.getRootNode());

			Box b = new Box(Vector3f.ZERO, 1, 1, 1); // create cube shape at the
														// origin
			Geometry geom = new Geometry("Box", b); // create cube geometry from
													// the shape
			Material mat =
					new Material(assetManager,
							"Common/MatDefs/Misc/Unshaded.j3md"); // create a
																	// simple
																	// material
			mat.setColor("Color", ColorRGBA.Blue); // set color of material to
													// blue
			geom.setMaterial(mat); // set the cube's material
			rootNode.attachChild(geom);
		} catch (IOException e1) {
			throw new RuntimeException("ERR: ", e1);
		}
		flyCam.setMoveSpeed(300);
		flyCam.setEnabled(true);
		// settings.setVSync(true);
		// setUpKeys();

		// Map map = new Map();
		// try {
		// map.load(MyTest.class.getResourceAsStream("test.map"));
		// } catch (IOException e) {
		// throw new RuntimeException("failed to load map", e);
		// }
		// mapWidth = map.getWidth() * boxSize;
		// mapHeight = map.getHeight() * boxSize;
		//
		// wallMat = new Material(assetManager,
		// "Common/MatDefs/Light/Lighting.j3md");
		//
		// // physic
		// stateManager.attach(bulletAppState);
		//
		// // MAP
		// //
		// final float boxSize = 1f;
		// final float boxExtend = boxSize / 2f;
		// // build floor
		// addBox(-map.getWidth(), -1, -map.getHeight(), 3 * map.getWidth(), 1,
		// 3 * map.getHeight());
		//
		// // build walls
		// addWalls(0, 0, map);
		// for (int x = -1; x <= 1; x += 2) {
		// addWalls(x, 0, map);
		// }
		// for (int y = -1; y <= 1; y += 2) {
		// addWalls(0, y, map);
		// }
		//
		// // player
		// CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(2f,
		// 6f,
		// 1);
		// player = new CharacterControl(capsuleShape, 0.01f);
		// player.setJumpSpeed(0.4f);
		// player.setFallSpeed(20f);
		// player.setGravity(10f);
		// player.setPhysicsLocation(new Vector3f(map.getStartX() * boxSize
		// + boxExtend, 10, map.getStartY() * boxSize + boxExtend));
		// bulletAppState.getPhysicsSpace().add(player);
		//
		// guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		// helloTxt = new BitmapText(guiFont, false);
		// helloTxt.setSize(guiFont.getCharSet().getRenderedSize());
		// helloTxt.setText("Hello World");
		// helloTxt.setLocalTranslation(300, helloTxt.getLineHeight(), 0);
		// guiNode.attachChild(helloTxt);
		//
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-0.6f, -0.4f, -1));
		sun.setColor(ColorRGBA.White);
		rootNode.addLight(sun);
		DirectionalLight sun2 = new DirectionalLight();
		sun2.setDirection(new Vector3f(0.6f, 0.4f, 1));
		sun2.setColor(new ColorRGBA(0f, 0f, 0.5f, 1f));
		rootNode.addLight(sun2);
	}

	private void addBox(int x, int y, int z, int width, int height, int depth) {

		Vector3f pos = new Vector3f(boxSize * x + width * boxExtend, boxSize
				* y + height * boxExtend, boxSize * z + depth * boxExtend);
		Box box = new Box(pos, boxExtend * width, boxExtend * height, boxExtend
				* depth);
		Geometry geom = new Geometry("box" + x + "," + y + "," + z + ","
				+ width + "," + height + "," + depth, box);
		geom.setMaterial(wallMat);
		rootNode.attachChild(geom);

		BoxCollisionShape colShape = new BoxCollisionShape(new Vector3f(
				boxExtend * width, boxExtend * height, boxExtend * depth));
		RigidBodyControl colCtrl = new RigidBodyControl(colShape, 0);
		colCtrl.setPhysicsLocation(pos);
		bulletAppState.getPhysicsSpace().add(colCtrl);
	}

	private void addWalls(int xOff, int yOff, Map map) {
		xOff *= map.getWidth();
		yOff *= map.getHeight();
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				if (map.isWall(x, y)) {
					addBox(xOff + x, 0, yOff + y, 1, 1, 1);
				}
			}
		}
	}

	private void setUpKeys() {
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Left");
		inputManager.addListener(this, "Right");
		inputManager.addListener(this, "Up");
		inputManager.addListener(this, "Down");
		inputManager.addListener(this, "Jump");
	}

	/**
	 * These are our custom actions triggered by key presses. We do not walk
	 * yet, we just keep track of the direction the user pressed.
	 */
	public void onAction(String binding, boolean value, float tpf) {
		if (binding.equals("Left")) {
			left = value;
		} else if (binding.equals("Right")) {
			right = value;
		} else if (binding.equals("Up")) {
			up = value;
		} else if (binding.equals("Down")) {
			down = value;
		} else if (binding.equals("Jump")) {
			player.jump();
		}
	}

	@Override
	public void simpleUpdate(float tpf) {
		// Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
		// Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
		// walkDirection.set(0, 0, 0);
		// if (left) {
		// walkDirection.addLocal(camLeft);
		// }
		// if (right) {
		// walkDirection.addLocal(camLeft.negate());
		// }
		// if (up) {
		// walkDirection.addLocal(camDir);
		// }
		// if (down) {
		// walkDirection.addLocal(camDir.negate());
		// }
		// player.setWalkDirection(walkDirection);
		// Vector3f location = new Vector3f(player.getPhysicsLocation());
		// location.x = fixPos(location.x, mapWidth);
		// location.z = fixPos(location.z, mapHeight);
		// player.setPhysicsLocation(location);
		// helloTxt.setText(String.format("%.1f %.1f%.1f", location.x,
		// location.y,
		// location.z));
		// cam.setLocation(location);
	}

	private float fixPos(float pos, float max) {
		if (pos > max) {
			return pos - max;
		}
		if (pos < 0) {
			pos = max + pos;
		}
		return pos;
	}

}