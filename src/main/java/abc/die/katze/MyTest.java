package abc.die.katze;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class MyTest extends SimpleApplication {

	public static void main(String[] args) {
		new MyTest().start();
	}

	private Spatial ninja;
	private Spatial teaPot;

	@Override
	public void simpleInitApp() {
		Geometry wall = new Geometry("wall", new Box(2.5f, 2.5f, 1f));
		Material wallMat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		wall.setMaterial(wallMat);

		rootNode.attachChild(wall);

		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText helloTxt = new BitmapText(guiFont, false);
		helloTxt.setSize(guiFont.getCharSet().getRenderedSize());
		helloTxt.setText("Hello World");
		helloTxt.setLocalTranslation(300, helloTxt.getLineHeight(), 0);
		guiNode.attachChild(helloTxt);

		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-0.1f, -0.7f, -1));
		rootNode.addLight(sun);
	}

	@Override
	public void simpleUpdate(float tpf) {
	}

}