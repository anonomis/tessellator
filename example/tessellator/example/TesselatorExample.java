package tessellator.example;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JApplet;

import tessellator.Tessellator;
import tessellator.Tessellator.TesselationException;
import tessellator.util.Path;
import tessellator.util.Point;
import tessellator.util.Triangle;

/**
 * This {@link JApplet} will make a square over the area, then make a complex
 * polygon from this by subtracting a diamond shaped area, tessellate the
 * result, tessellate the diamond and finally adding it all up in a monolithic
 * mesh.
 * 
 * @author nomis
 * 
 */
public class TesselatorExample extends JApplet {
	private static final long serialVersionUID = 1L;

	public TesselatorExample() {
		int width = 800;
		int height = 600;
		canvas.setSize(800, 600);
		this.add(canvas);

		// Define square shape:

		Path path = new Path(new Point(0, 0));
		path.lineTo(new Point(width, 0));
		path.lineTo(new Point(width, height));
		path.lineTo(new Point(0, height));
		path.lineTo(new Point(0, 0));

		// Define diamond shape that will be the hole:

		Path hole = new Path(new Point(width / 2, height - 60));
		hole.lineTo(new Point(80, 297));
		hole.lineTo(new Point(81, 150));
		hole.lineTo(new Point(width / 2 - 70, 50));
		hole.lineTo(new Point(width / 2 + 70, 50));
		hole.lineTo(new Point(width - 80, 150));
		hole.lineTo(new Point(width - 81, 302));

		try {

			// Tessellate:
			
			List<Triangle> triangles;
			
			triangles = new Tessellator().getTriangles(path, hole);
			triangles.addAll(new Tessellator().getTriangles(hole, null));

			
			// Make a mesh:
			
			Mesh mesh = new Mesh(800, 600, triangles);

			
			// Add it to canvas so it will be drawn:
			
			canvas.add(mesh);

			
		} catch (TesselationException e) {
			throw new RuntimeException(e);
		}
	}

	private Container canvas = new Container() {
		private static final long serialVersionUID = 1L;
		public void paint(Graphics graphics) {
			Graphics2D graphics2d = (Graphics2D) graphics;
			for (Component component : getComponents()) {
				component.paint(graphics2d);
			}
			super.paint(graphics2d);
		};
	};
	

	public static void main(String[] args) {
		new TesselatorExample();
	}

}
